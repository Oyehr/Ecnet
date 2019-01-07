package com.ecm.util;

import com.ecm.model.LogicNode;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by deng on 2018/4/7.
 */
public class LogicExcelGenerator {
    private List<LogicNode> nodes;
    private Map<Integer, LogicNode> nodeMap;
    private List<Integer> roots;

    private String[] types = {"证据", "事实", "法条", "结论"};
    private final String FILE_PATH;

    private final static String SHEET_NAME = "说理逻辑表";

    public LogicExcelGenerator(String path, List<LogicNode> nodes) {
        FILE_PATH = path;
        this.nodes = nodes;

        nodeMap = new HashMap<>();
        for (LogicNode node : nodes) {
            nodeMap.put(node.getNodeID(), node);
        }

        roots = new ArrayList<>();
        for (LogicNode node : nodes) {
            int parentNodeID = node.getParentNodeID();
            if (parentNodeID == -1) {
                roots.add(node.getNodeID());
            }
        }
    }

    public void generateExcelFile() {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(SHEET_NAME);
        HSSFRow firstRow = sheet.createRow(0);
        for (short i = 0; i < types.length; i++) {
            HSSFCell cell = firstRow.createCell(i);
            cell.setCellValue(types[i]);
        }

        for (int i = 0; i < roots.size(); i++) {
            String evidenceStr = "";
            String factStr = "";
            String lawStr = "";
            String conclusionStr = "";

            for (LogicNode node : nodes) {
                if (isChildOrSelf(roots.get(i), node.getNodeID())) {
                    switch (node.getType()) {
                        case 0:
                            evidenceStr += node.getTopic() + "、";
                            break;
                        case 1:
                            factStr += node.getTopic() + "、";
                            break;
                        case 2:
                            lawStr += node.getTopic() + "、";
                            break;
                        case 3:
                            conclusionStr += node.getTopic() + "、";
                            break;
                    }
                }
            }

            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(getColumnStr(evidenceStr));
            row.createCell(1).setCellValue(getColumnStr(factStr));
            row.createCell(2).setCellValue(getColumnStr(lawStr));
            row.createCell(3).setCellValue(getColumnStr(conclusionStr));
        }


        FileOutputStream out = null;
        try {
            out = new FileOutputStream(FILE_PATH);
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getColumnStr(String str) {
        if (str.length() > 0) {
            return str.substring(0, str.length() - 1);
        } else {
            return "无";
        }
    }

    private boolean isChildOrSelf(int pID, int id) {
        if (nodeMap.get(id).getNodeID() == pID) return true;
        else if (nodeMap.get(id).getParentNodeID() == -1) return false;
        else if (nodeMap.get(id).getParentNodeID() == pID) return true;
        else return isChildOrSelf(pID, nodeMap.get(id).getParentNodeID());
    }
}

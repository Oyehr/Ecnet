package com.ecm.service.impl;

import com.ecm.service.EvaluateService;
import com.ecm.vo.BundleDocumentEvaluateResult;
import com.ecm.vo.SingleDocumentBasicInfo;
import com.ecm.vo.SingleDocumentEvaluateResult;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EvaluateServiceImpl implements EvaluateService {

    /*private ArrayList<String> pythonOutputResultList; //python程序输出结果列表，用于保存python程序结果
    private String fact; //文书的事实部分
    private String law; //文书的法条部分
    private String judgement; //文书的判决部分*/

    /**获取单个文书中的案件基本信息
     *
     * @param fileName 要解析的文书对应的文件名
     * @return 文书对应的案件基本信息
     */
    @Override
    public SingleDocumentBasicInfo getSingleCaseBasicInfo(String fileName) {
        return executePythonToGetDocumentInfo(fileName);
    }

    /**评估单个文书中某二者之间的相关性
     *
     * @param fileName 要解析的文书对应的文件名
     * @param evaluateType 评估类型：事实到法条；法条到事实；法条到结论；结论到法条；
     * @return 文书中此二者相关性评估结果列表
     */
    @Override
    public List<SingleDocumentEvaluateResult> singleEvaluate(String fileName, String evaluateType) {
        //第一种情况：某二者之间的相关性全部输出。比如：一篇文书中有5个事实，引用了5个法条，那么输出结果共有5*5=25种。
        //return executePythonToCalculateRelevancy(fileName, evaluateType);

        //第二种情况：仅输出有关联性的二者。比如：一篇文书中有5个事实，引用了5个法条，那么只输出相关性为1的事实-法条对，输出结果应不超过25种
        List<SingleDocumentEvaluateResult> allResultList = executePythonToCalculateRelevancy(fileName, evaluateType); //获取全部的二者关联性
        List<SingleDocumentEvaluateResult> selectedResultList = new ArrayList<SingleDocumentEvaluateResult>(); //保存仅有关联性的结果
        if (evaluateType.startsWith("l")) { //计算“法条到事实”或者“法条到结论”的相关性
            boolean lastLawReferenceMatch = false; //与某法条相关的事实或者结论是否匹配上（之一满足即可）。比如：一篇文书中有5个事实，引用了5个法条，那么输出结果的前5项对应第一个法条，如果其中任意一项和第一个法条的相关性为1，那么就记为匹配上。如果这5项和第一个法条的相关性都为0，那么就没有匹配上。
            String lastLawName = ""; //上一条结果的法条名称
            if (allResultList.size() > 0) {
                lastLawName = allResultList.get(0).getLawName();
            }
            for (int i = 0; i < allResultList.size() - 1; i++) {
                SingleDocumentEvaluateResult tempSingleDocumentEvaluateResult = allResultList.get(i);
                if (!tempSingleDocumentEvaluateResult.getLawName().equals(lastLawName)) { //这条结果的法条名称和上一条结果的法条名称不同，那么和上一条结果的法条相关的事实或者结论已经全部结束。
                    if (lastLawReferenceMatch == false) { //如果上一条结果的法条没有匹配上，那么增加一个特殊记录：事实或结论的序号和内容都是空的，法条名称和内容正常，相关性为“无对应内容”
                        SingleDocumentEvaluateResult specialSingleDocumentEvaluateResult = new SingleDocumentEvaluateResult("", "", tempSingleDocumentEvaluateResult.getLawName(), tempSingleDocumentEvaluateResult.getLawContent(), "无对应内容");
                        selectedResultList.add(specialSingleDocumentEvaluateResult); //把特殊记录加入到返回结果列表中
                    }
                    //重置lastLawName、lastLawReferenceMatch变量的值
                    lastLawName = tempSingleDocumentEvaluateResult.getLawName();
                    lastLawReferenceMatch = false;

                    if (tempSingleDocumentEvaluateResult.getRelevancy().equals("1")) { //如果结果是相关的，那么更改lastLawReferenceMatch的值，并把这条记录加入到返回结果列表中
                        lastLawReferenceMatch = true;
                        selectedResultList.add(tempSingleDocumentEvaluateResult);
                    }
                } else { //这条结果的法条名称和上一条结果的法条名称相同
                    if (tempSingleDocumentEvaluateResult.getRelevancy().equals("1")) { //如果结果是相关的，那么更改lastLawReferenceMatch的值，并把这条记录加入到返回结果列表中
                        lastLawReferenceMatch = true;
                        selectedResultList.add(tempSingleDocumentEvaluateResult);
                    }
                }
            }
            if (allResultList.size() > 0) {
                //所有记录的最后一条，要单独拿出来考虑（因为循环的思路是根据某条记录来判断前一条，而最后一条记录之后再无记录，所以无法判断，只能单独拿出来判断）
                if (allResultList.get(allResultList.size() - 1).getRelevancy().equals("0") && lastLawReferenceMatch == false) {//如果最后一条记录的结果是不相关的，且本个法条没有匹配上，那么增加一个特殊记录：事实或结论的序号和内容都是空的，法条名称和内容正常，相关性为“无对应内容”
                    SingleDocumentEvaluateResult specialSingleDocumentEvaluateResult = new SingleDocumentEvaluateResult("", "", allResultList.get(allResultList.size() - 1).getLawName(), allResultList.get(allResultList.size() - 1).getLawContent(), "无对应内容");
                    selectedResultList.add(specialSingleDocumentEvaluateResult); //把特殊记录加入到返回结果列表中
                } else if (allResultList.get(allResultList.size() - 1).getRelevancy().equals("1")) { //如果最后一条记录的结果是相关的，那么把这条记录加入到返回结果列表中
                    selectedResultList.add(allResultList.get(allResultList.size() - 1));
                }
            }
        } else { //计算“事实到法条”或者“结论到法条”的相关性
            boolean lastFojReferenceMatch = false; //与某事实或者结论相关的法条是否匹配上（之一满足即可）。比如：一篇文书中有5个事实，引用了5个法条，那么输出结果的前5项对应第一个事实，如果其中任意一项和第一个事实的相关性为1，那么就记为匹配上。如果这5项和第一个事实的相关性都为0，那么就没有匹配上。
            String lastFojNo = ""; //上一条结果的事实或者结论的序号
            if (allResultList.size() > 0) {
                lastFojNo = allResultList.get(0).getFojNo();
            }
            for (int i = 0; i < allResultList.size(); i++) {
                SingleDocumentEvaluateResult tempSingleDocumentEvaluateResult = allResultList.get(i);
                if (!tempSingleDocumentEvaluateResult.getFojNo().equals(lastFojNo)) { //这条结果的事实或结论的序号和上一条结果的事实或结论序号不同，那么和上一条结果的事实或结论相关的法条已经全部结束。
                    if (lastFojReferenceMatch == false) { //如果上一条结果的法条没有匹配上，那么增加一个特殊记录：事实或结论的序号和内容正常，法条名称和内容都是空的，相关性为“无对应内容”
                        SingleDocumentEvaluateResult specialSingleDocumentEvaluateResult = new SingleDocumentEvaluateResult(tempSingleDocumentEvaluateResult.getFojNo(), tempSingleDocumentEvaluateResult.getFojContent(), "", "", "无对应内容");
                        selectedResultList.add(specialSingleDocumentEvaluateResult); //把特殊记录加入到返回结果列表中
                    }
                    //重置lastFojName、lastFojReferenceMatch变量的值
                    lastFojNo = tempSingleDocumentEvaluateResult.getFojNo();
                    lastFojReferenceMatch = false;

                    if (tempSingleDocumentEvaluateResult.getRelevancy().equals("1")) { //如果结果是相关的，那么更改lastFojReferenceMatch的值，并把这条记录加入到返回结果列表中
                        lastFojReferenceMatch = true;
                        selectedResultList.add(tempSingleDocumentEvaluateResult);
                    }
                } else { //这条结果的事实或结论的序号和上一条结果的事实或结论序号相同
                    if (tempSingleDocumentEvaluateResult.getRelevancy().equals("1")) { //如果结果是相关的，那么更改lastFojReferenceMatch的值，并把这条记录加入到返回结果列表中
                        lastFojReferenceMatch = true;
                        selectedResultList.add(tempSingleDocumentEvaluateResult);
                    }
                }
            }
            if (allResultList.size() > 0) {
                //所有记录的最后一条，要单独拿出来考虑（因为循环的思路是根据某条记录来判断前一条，而最后一条记录之后再无记录，所以无法判断，只能单独拿出来判断）
                if (allResultList.get(allResultList.size() - 1).getRelevancy().equals("0") && lastFojReferenceMatch == false) {//如果最后一条记录的结果是不相关的，且本个事实或结论没有匹配上，那么增加一个特殊记录：事实或结论的序号和内容正常，法条名称和内容都是空的，相关性为“无对应内容”
                    SingleDocumentEvaluateResult specialSingleDocumentEvaluateResult = new SingleDocumentEvaluateResult(allResultList.get(allResultList.size() - 1).getFojNo(), allResultList.get(allResultList.size() - 1).getFojContent(), "", "", "无对应内容");
                    selectedResultList.add(specialSingleDocumentEvaluateResult); //把特殊记录加入到返回结果列表中
                } else if (allResultList.get(allResultList.size() - 1).getRelevancy().equals("1")) { //如果最后一条记录的结果是相关的，那么把这条记录加入到返回结果列表中
                    selectedResultList.add(allResultList.get(allResultList.size() - 1));
                }
            }
        }
        return selectedResultList;
    }

    /**评估批量文书中某二者之间的相关性
     *
     * @param directoryPath 批量文书所在文件夹的路径
     * @param evaluateType 评估类型：事实到法条；法条到事实；法条到结论；结论到法条；
     * @return 批量文书中此二者相关性评估结果列表
     */
    @Override
    public List<BundleDocumentEvaluateResult> bundleEvaluate(String directoryPath, String evaluateType) {
        List<BundleDocumentEvaluateResult> resultList = new ArrayList<BundleDocumentEvaluateResult>();
        File directory = new File(directoryPath);
        try { //p.s.这里只对该文件夹中的每个文件做处理，不考虑该文件夹中还有子文件夹的情况
            File[] files = directory.listFiles(); //获取该文件夹下的所有子文件
            for(int i=0; i<files.length; i++){
                if(!files[i].isDirectory()){ //如果是文件（不是文件夹）
                    SingleDocumentBasicInfo basicInfo = getSingleCaseBasicInfo(files[i].getAbsolutePath());
                    List<SingleDocumentEvaluateResult> thisDocuResultList = singleEvaluate(files[i].getAbsolutePath(), evaluateType.substring(1, evaluateType.length())); //调用singleEvaluate方法时，要把评估类型最前面的b去掉
                    for(int j=0; j<thisDocuResultList.size(); j++){
                        SingleDocumentEvaluateResult tempSingleDocumentEvaluateResult = thisDocuResultList.get(j);
                        if(tempSingleDocumentEvaluateResult.getRelevancy().equals("无对应内容")){
                            BundleDocumentEvaluateResult bundleDocumentEvaluateResult = new BundleDocumentEvaluateResult(basicInfo.getCaseId(), basicInfo.getCaseName(), basicInfo.getCaseReason(), basicInfo.getJudgeName(), "");
                            if(evaluateType.startsWith("bl")){ //计算“法条到事实”或者“法条到结论”的相关性
                                bundleDocumentEvaluateResult.setNoReferenceSource(tempSingleDocumentEvaluateResult.getLawName()); //没有引用的来源是法条名称
                            }
                            else{//计算“事实到法条”或者“结论到法条”的相关性
                                bundleDocumentEvaluateResult.setNoReferenceSource(tempSingleDocumentEvaluateResult.getFojContent()); //没有引用的来源是事实或结论内容
                            }
                            resultList.add(bundleDocumentEvaluateResult);
                        }
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }


    /**导出文书评估的结果到Excel文件中去
     *
     * @param filePath 导出文件的路径
     * @param evaluateType 文书的评估类型
     * @param basicInfo 文书的基本信息（如果是单篇文书的评估，则此参数有意义；如果是批量文书的评估，则此参数无意义）
     * @param evaluateResult 文书的评估结果
     * @return 导出Excel是否成功：若成功，返回导出的Excel的文件路径和文件名；若不成功，返回fail
     */
    @Override
    public String exportToExcel(String filePath, String evaluateType, String basicInfo, String evaluateResult) {

        //创建workbook
        HSSFWorkbook workbook = new HSSFWorkbook();

        //标题字体对象
        HSSFFont titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setFontName("宋体");

        //列名字体
        HSSFFont colHeadingFont = workbook.createFont();
        colHeadingFont.setBold(true);
        colHeadingFont.setFontHeightInPoints((short) 10);
        colHeadingFont.setFontName("宋体");

        //单元格字体
        HSSFFont cellFont = workbook.createFont();
        cellFont.setBold(false);
        cellFont.setFontHeightInPoints((short) 10);
        cellFont.setFontName("宋体");

        //标题样式
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setFont(titleFont);
        titleStyle.setWrapText(true);//自动换行

        //列名样式
        HSSFCellStyle colHeadingStyle = workbook.createCellStyle();
        colHeadingStyle.setAlignment(HorizontalAlignment.CENTER);
        colHeadingStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        colHeadingStyle.setFont(colHeadingFont);
        colHeadingStyle.setWrapText(true);
        //设置边框样式
        colHeadingStyle.setBorderTop(BorderStyle.THIN);
        colHeadingStyle.setBorderBottom(BorderStyle.THIN);
        colHeadingStyle.setBorderLeft(BorderStyle.THIN);
        colHeadingStyle.setBorderRight(BorderStyle.THIN);
        //设置边框颜色
        colHeadingStyle.setTopBorderColor(HSSFColor.BLACK.index);
        colHeadingStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        colHeadingStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        colHeadingStyle.setRightBorderColor(HSSFColor.BLACK.index);

        //单元格样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFont(cellFont);
        cellStyle.setWrapText(true);
        //设置边框样式
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        String fileName = null; //要导出的文件的名称
        String sheetName = null; //要创建的sheet的名字
        String titleName = null; //标题的名字
        String[] itemHeadings = null; //评估列的标题名称

        if(!evaluateType.startsWith("b")) { //如果是单个评估文书

            JSONObject basicInfoJsonObj = JSONObject.fromObject(basicInfo);
            JSONArray evaluateResultJsonArr = JSONArray.fromObject(evaluateResult);

            if(evaluateType.equals("f2l")){ //事实到法条
                fileName = "事实到法条评估结果.xls";
                sheetName = "事实到法条评估";
                titleName = "事实到法条的评估表";
                itemHeadings = new String[]{"序号", "事实序号", "事实文本", "相关法条名称", "相关法条正文", "备注"};
            }
            else if(evaluateType.equals("l2f")){ //法条到事实
                fileName = "法条到事实评估结果.xls";
                sheetName = "法条到事实评估";
                titleName = "法条到事实的评估表";
                itemHeadings = new String[]{"序号", "法条名称", "法条正文", "相关事实序号", "相关事实文本", "备注"};
            }
            else if(evaluateType.equals("l2j")){ //法条到结论
                fileName = "法条到结论评估结果.xls";
                sheetName = "法条到结论评估";
                titleName = "法条到结论的评估表";
                itemHeadings = new String[]{"序号", "法条名称", "法条正文", "相关结论序号", "相关结论文本", "备注"};
            }
            else{ //结论到法条
                fileName = "结论到法条评估结果.xls";
                sheetName = "结论到法条评估";
                titleName = "结论到法条的评估表";
                itemHeadings = new String[]{"序号", "结论序号", "结论文本", "相关法条名称", "相关法条正文", "备注"};
            }

            HSSFSheet sheet = workbook.createSheet(sheetName);
            //sheet1.autoSizeColumn(1, true);

            CellRangeAddress callRangeAddress0016 = new CellRangeAddress(0, 0, 1, 6);//起始行,结束行,起始列,结束列
            sheet.addMergedRegion(callRangeAddress0016);
            HSSFRow row0 = sheet.createRow(0);
            HSSFCell cell01 = row0.createCell(1);
            cell01.setCellStyle(titleStyle);
            cell01.setCellValue(titleName);

            HSSFRow row1 = sheet.createRow(1);
            String[] row1Contents = {"案号", basicInfoJsonObj.getString("caseId"), "承办法官", basicInfoJsonObj.getString("judgeName"), "判决日期", basicInfoJsonObj.getString("judgeTime")};
            for (int i=1; i<=row1Contents.length; i++) {
                HSSFCell cell1i = row1.createCell(i);
                cell1i.setCellStyle(cellStyle);
                cell1i.setCellValue(row1Contents[i-1]);
            }

            CellRangeAddress callRangeAddress2212 = new CellRangeAddress(2, 2, 1, 2);//起始行,结束行,起始列,结束列
            sheet.addMergedRegion(callRangeAddress2212);
            CellRangeAddress callRangeAddress2236 = new CellRangeAddress(2, 2, 3, 6);//起始行,结束行,起始列,结束列
            sheet.addMergedRegion(callRangeAddress2236);
            HSSFRow row2 = sheet.createRow(2);
            HSSFCell cell21 = row2.createCell(1);
            cell21.setCellStyle(cellStyle);
            cell21.setCellValue("案件名称");
            HSSFCell cell23 = row2.createCell(3);
            cell23.setCellStyle(cellStyle);
            cell23.setCellValue(basicInfoJsonObj.getString("caseName"));

            CellRangeAddress callRangeAddress3312 = new CellRangeAddress(3, 3, 1, 2);//起始行,结束行,起始列,结束列
            sheet.addMergedRegion(callRangeAddress3312);
            CellRangeAddress callRangeAddress3336 = new CellRangeAddress(3, 3, 3, 6);//起始行,结束行,起始列,结束列
            sheet.addMergedRegion(callRangeAddress3336);
            HSSFRow row3 = sheet.createRow(3);
            HSSFCell cell31 = row3.createCell(1);
            cell31.setCellStyle(cellStyle);
            cell31.setCellValue("案由");
            HSSFCell cell33 = row3.createCell(3);
            cell33.setCellStyle(cellStyle);
            cell33.setCellValue(basicInfoJsonObj.getString("caseReason"));

            HSSFRow row4 = sheet.createRow(4);
            String[] row4Contents = itemHeadings;
            for (int i=1; i<=row4Contents.length; i++) {
                HSSFCell cell4i = row4.createCell(i);
                cell4i.setCellStyle(cellStyle);
                cell4i.setCellValue(row4Contents[i-1]);
            }

            for (int i=0; i<evaluateResultJsonArr.size(); i++) {
                JSONObject tempSDER = evaluateResultJsonArr.getJSONObject(i);

                HSSFRow erRowi = sheet.createRow(i+5);
                for(int j=1; j<=6; j++){
                    HSSFCell erCellij = erRowi.createCell(j);
                    erCellij.setCellStyle(cellStyle);
                    erCellij.setCellValue(tempSDER.getString("col"+j));
                }
            }

            sheet.autoSizeColumn((short)1); //调整第1列宽度
            sheet.autoSizeColumn((short)2); //调整第2列宽度
            sheet.autoSizeColumn((short)3); //调整第3列宽度
            sheet.autoSizeColumn((short)4); //调整第4列宽度
            sheet.autoSizeColumn((short)5); //调整第5列宽度
            sheet.autoSizeColumn((short)6); //调整第6列宽度
        }

        else { //如果是批量评估文书

            JSONArray evaluateResultJsonArr = JSONArray.fromObject(evaluateResult);

            itemHeadings = new String[]{"序号", "案号", "案件名称", "案由", "承办人", "", "备注"};

            if(evaluateType.equals("bf2l")){ //批量事实到法条
                fileName = "事实到法条的批量评估结果.xls";
                sheetName = "事实到法条的批量评估";
                titleName = "事实到法条的批量评估表";
                itemHeadings[5] = "没有法条的事实";
            }
            else if(evaluateType.equals("bl2f")){ //批量法条到事实
                fileName = "法条到事实的批量评估结果.xls";
                sheetName = "法条到事实的批量评估";
                titleName = "法条到事实的批量评估表";
                itemHeadings[5] = "没有事实的法条";
            }
            else if(evaluateType.equals("bl2j")){ //批量法条到结论
                fileName = "法条到结论的批量评估结果.xls";
                sheetName = "法条到结论的批量评估";
                titleName = "法条到结论的批量评估表";
                itemHeadings[5] = "没有结论的法条";
            }
            else{ //批量结论到法条
                fileName = "结论到法条的批量评估结果.xls";
                sheetName = "结论到法条的批量评估";
                titleName = "结论到法条的批量评估表";
                itemHeadings[5] = "没有法条的结论";
            }

            HSSFSheet sheet = workbook.createSheet(sheetName);
            //sheet1.autoSizeColumn(1, true);

            CellRangeAddress callRangeAddress0016 = new CellRangeAddress(0, 0, 1, 7);//起始行,结束行,起始列,结束列
            sheet.addMergedRegion(callRangeAddress0016);
            HSSFRow row0 = sheet.createRow(0);
            HSSFCell cell01 = row0.createCell(1);
            cell01.setCellStyle(titleStyle);
            cell01.setCellValue(titleName);

            HSSFRow row1 = sheet.createRow(1);
            String[] row1Contents = itemHeadings;
            for (int i=1; i<=row1Contents.length; i++) {
                HSSFCell cell1i = row1.createCell(i);
                cell1i.setCellStyle(cellStyle);
                cell1i.setCellValue(row1Contents[i-1]);
            }

            for (int i=0; i<evaluateResultJsonArr.size(); i++) {
                JSONObject tempBDER = evaluateResultJsonArr.getJSONObject(i);

                HSSFRow erRowi = sheet.createRow(i+2);
                for(int j=1; j<=7; j++){
                    HSSFCell erCellij = erRowi.createCell(j);
                    erCellij.setCellStyle(cellStyle);
                    erCellij.setCellValue(tempBDER.getString("col"+j));
                }
            }

            sheet.autoSizeColumn((short)1); //调整第1列宽度
            sheet.autoSizeColumn((short)2); //调整第2列宽度
            sheet.autoSizeColumn((short)3); //调整第3列宽度
            sheet.autoSizeColumn((short)4); //调整第4列宽度
            sheet.autoSizeColumn((short)5); //调整第5列宽度
            sheet.autoSizeColumn((short)6); //调整第6列宽度
            sheet.autoSizeColumn((short)7); //调整第7列宽度
        }

        File file = new File(filePath + File.separator + fileName);
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            workbook.close();
            fileOut.close();
            return file.getAbsolutePath();
        }
        catch (IOException e) {
            e.printStackTrace();
            return "fail";
        }
    }

    /**执行python程序来解析文书中的元素
     *
     * @param fileName 要解析的文件名
     */
    /*
    private void executePythonToGetDocumentElements(String fileName) {
        pythonOutputResultList = new ArrayList<String>(); //初始化python程序输出结果列表

        //通过模拟命令行直接执行java程序来运行，然后获取控制台输出来得到python的结果，这种要比通过Jython调用快得多，因此使用这种方式
        String sepa = File.separator;
        String pythonFileFullName = System.getProperty("user.dir") + sepa + "pythonSrc" + sepa + "wsfx" + sepa + "util"+ sepa + "wsfun.py";  //python代码的路径、文件名
        //由于是通过获取控制台输出，所以需要修改上面这个python的文件内容，把需要返回的内容输出，才能够被调用它的Java程序获取
        String[] arguments = new String[] { "python", pythonFileFullName, fileName};
        try {
            Process process = Runtime.getRuntime().exec(arguments);
            final InputStream isin = process.getInputStream(); //标准输入流
            final InputStream iserror = process.getErrorStream(); //标准错误流

            //启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流，否则waitFor方法会被阻塞，导致程序不能正常运行
            new Thread() {
                public void run() {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(isin));
                    try {
                        String line1 = null;
                        while ((line1 = br1.readLine()) != null) {
                            if (line1 != null){
                                pythonOutputResultList.add(line1); //正常来说，python程序输出结果是三段话，分别是事实、法条和结论，将它们保存到List中
                            }
                        }
                    } catch (IOException e) { e.printStackTrace(); }
                    finally{
                        try { isin.close();} catch (IOException e) { e.printStackTrace(); }
                    }
                }
            }.start();

            new Thread() {
                public void  run() {
                    BufferedReader br2 = new  BufferedReader(new  InputStreamReader(iserror));
                    try {
                        String line2 = null ;
                        while ((line2 = br2.readLine()) !=  null ) {
                            if (line2 != null){
                                System.out.println("Error Info: "+line2);
                            }
                        }
                    } catch (IOException e) { e.printStackTrace(); }
                    finally{
                        try { iserror.close(); } catch (IOException e) { e.printStackTrace(); }
                    }
                }
            }.start();

            int procResult = process.waitFor();
            if(procResult > 0) {
                System.out.println("执行出错。错误编码：" + procResult);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //python程序有正常输出结果，将python程序抽取文书中关键部分的结果分派到事实、法条、判决字符串中
        System.out.println(pythonOutputResultList.size());
        if(pythonOutputResultList.size() >= 3) {
            fact = pythonOutputResultList.get(0);
            law = pythonOutputResultList.get(1);
            judgement = pythonOutputResultList.get(2);
        }
        else { //python程序没有正常输出结果，将事实、法条、判决字符串初始化为null
            fact = null; law = null; judgement = null;
        }
        //System.out.println(fact);
        //System.out.println(law);
        //System.out.println(judgement);
    }*/

    /**执行python程序来获取文书的基本信息
     *
     * @param fileName 要解析的文件名
     */
    private SingleDocumentBasicInfo executePythonToGetDocumentInfo(String fileName) {
        final List<SingleDocumentBasicInfo> basicInfoList = new ArrayList<SingleDocumentBasicInfo>();

        //通过模拟命令行直接执行java程序来运行，然后获取控制台输出来得到python的结果，这种要比通过Jython调用快得多，因此使用这种方式
        String sepa = File.separator;
        String pythonFileFullName = System.getProperty("user.dir") + sepa + "pythonSrc" + sepa + "wsfx" + sepa + "NN" + sepa + "wsfun.py";  //python代码的路径、文件名
        //由于是通过获取控制台输出，所以需要修改上面这个python的文件内容，把需要返回的内容输出，才能够被调用它的Java程序获取
        String[] arguments = new String[] { "python", pythonFileFullName, fileName};
        try {
            Process process = Runtime.getRuntime().exec(arguments);
            final InputStream isin = process.getInputStream(); //标准输入流
            final InputStream iserror = process.getErrorStream(); //标准错误流

            //启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流，否则waitFor方法会被阻塞，导致程序不能正常运行
            new Thread() {
                public void run() {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(isin));
                    try {
                        String line1 = null;
                        int line1Count = 0;
                        String[] temp = new String[5]; //保存中间结果
                        while ((line1 = br1.readLine()) != null) { //每五行输出为一组
                            if (line1 != null) {
                                temp[line1Count] = line1;
                                line1Count++;
                            }
                        }
                        SingleDocumentBasicInfo basicInfo = new SingleDocumentBasicInfo(temp[0], temp[4], temp[3], temp[1], temp[2]);
                        basicInfoList.add(basicInfo);
                    } catch (IOException e) { e.printStackTrace(); }
                    finally{
                        try { isin.close();} catch (IOException e) { e.printStackTrace(); }
                    }
                }
            }.start();

            new Thread() {
                public void  run() {
                    BufferedReader br2 = new  BufferedReader(new  InputStreamReader(iserror));
                    try {
                        String line2 = null ;
                        while ((line2 = br2.readLine()) !=  null ) {
                            if (line2 != null){
                                System.out.println("Run Info: "+line2);
                            }
                        }
                    } catch (IOException e) { e.printStackTrace(); }
                    finally{
                        try { iserror.close(); } catch (IOException e) { e.printStackTrace(); }
                    }
                }
            }.start();

            int procResult = process.waitFor();
            if(procResult > 0) {
                System.out.println("执行出错。错误编码：" + procResult);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return basicInfoList.get(0);
    }

    /**执行python程序来计算相关性
     *
     * @param fileName 要解析的文件名
     * @param evaluateType 评估类型：事实到法条；法条到事实；法条到结论；结论到法条；
     */
    private List<SingleDocumentEvaluateResult> executePythonToCalculateRelevancy(String fileName, final String evaluateType) {
        final ArrayList<SingleDocumentEvaluateResult> resultList = new ArrayList<SingleDocumentEvaluateResult>();

        //通过模拟命令行直接执行java程序来运行，然后获取控制台输出来得到python的结果，这种要比通过Jython调用快得多，因此使用这种方式
        String sepa = File.separator;
        String pythonFileFullName = System.getProperty("user.dir") + sepa + "pythonSrc" + sepa + "wsfx" + sepa + "NN" + sepa + "predictws.py";  //python代码的路径、文件名
        //由于是通过获取控制台输出，所以需要修改上面这个python的文件内容，把需要返回的内容输出，才能够被调用它的Java程序获取
        String[] arguments = new String[] { "python", pythonFileFullName, fileName, evaluateType};
        try {
            Process process = Runtime.getRuntime().exec(arguments);
            final InputStream isin = process.getInputStream(); //标准输入流
            final InputStream iserror = process.getErrorStream(); //标准错误流

            //启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流，否则waitFor方法会被阻塞，导致程序不能正常运行
            new Thread() {
                public void run() {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(isin));
                    try {
                        String line1 = null;
                        int line1Count = 0;
                        String[] temp = new String[5]; //保存中间结果;
                        while ((line1 = br1.readLine()) != null) {
                            if (line1 != null) { //每五行输出为一组
                                if(line1Count % 5 == 0) { temp[0] = line1;}  //保存中间结果
                                else if(line1Count % 5 == 1) { temp[1] = line1; }  //保存中间结果
                                else if(line1Count % 5 == 2) { temp[2] = line1;}  //保存中间结果
                                else if(line1Count % 5 == 3) { temp[3] = line1; }  //保存中间结果
                                else {
                                    temp[4] = line1;
                                    SingleDocumentEvaluateResult singleDocumentEvaluateResult = new SingleDocumentEvaluateResult(temp[0], temp[1], temp[2], temp[3], temp[4]);
                                    resultList.add(singleDocumentEvaluateResult);
                                }
                                line1Count++;
                            }
                        }
                    } catch (IOException e) { e.printStackTrace(); }
                    finally{
                        try { isin.close();} catch (IOException e) { e.printStackTrace(); }
                    }
                }
            }.start();

            new Thread() {
                public void  run() {
                    BufferedReader br2 = new  BufferedReader(new  InputStreamReader(iserror));
                    try {
                        String line2 = null ;
                        while ((line2 = br2.readLine()) !=  null ) {
                            if (line2 != null){
                                System.out.println("Run Info: "+line2);
                            }
                        }
                    } catch (IOException e) { e.printStackTrace(); }
                    finally{
                        try { iserror.close(); } catch (IOException e) { e.printStackTrace(); }
                    }
                }
            }.start();

            int procResult = process.waitFor();
            if(procResult > 0) {
                System.out.println("执行出错。错误编码：" + procResult);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
        for(int i=0; i<resultList.size(); i++) {
            System.out.println(resultList.get(i).getSource()+"  "+resultList.get(i).getLawName()+"  "+resultList.get(i).getRelevancy());
        }*/
        return resultList;
    }

//    public static void main(String[] args) {
//        String sepa = File.separator;
//        String xmlFileFullName = System.getProperty("user.dir") + sepa + "src" + sepa + "main" + sepa + "resources" + sepa + "upload" + sepa + "uploadedWS.xml";  //xml文件的文件全名
//        String rarFileFullName = System.getProperty("user.dir") + sepa + "src" + sepa + "main" + sepa + "resources" + sepa + "upload" + sepa + "uploadedWSList.rar";  //rar文件的文件全名
//        String zipFileFullName = System.getProperty("user.dir") + sepa + "src" + sepa + "main" + sepa + "resources" + sepa + "upload" + sepa + "uploadedWSList.zip";  //zip文件的文件全名
//        String unrarDirectoryPath = System.getProperty("user.dir") + sepa + "src" + sepa + "main" + sepa + "resources" + sepa + "upload" + sepa + "rarDir" + sepa;  //rar文件解压后的目录名
//        String unzipDirectoryPath = System.getProperty("user.dir") + sepa + "src" + sepa + "main" + sepa + "resources" + sepa + "upload" + sepa + "zipDir" + sepa;  //zip文件解压后的目录名
//        String exportExcelName = System.getProperty("user.dir") + sepa + "src" + sepa + "main" + sepa + "resources" + sepa + "download" + sepa + "事实到法条评估结果.xls";  //导出的评估结果Excel文件的文件全名
//        EvaluateServiceImpl esi = new EvaluateServiceImpl();
//        SingleDocumentBasicInfo singleDocumentBasicInfo = esi.getSingleCaseBasicInfo(xmlFileFullName);
//        //System.out.println(singleDocumentBasicInfo.getCaseId()+"  "+singleDocumentBasicInfo.getCaseName()+"  "+singleDocumentBasicInfo.getCaseReason()+"  "+singleDocumentBasicInfo.getJudgeName()+"  "+singleDocumentBasicInfo.getJudgeTime());
//        List<SingleDocumentEvaluateResult> singleDocumentEvaluateResultList = esi.singleEvaluate(xmlFileFullName, "f2l");
//        /*for(int i=0; i<singleDocumentEvaluateResultList.size(); i++){
//            SingleDocumentEvaluateResult singleDocumentEvaluateResult = singleDocumentEvaluateResultList.get(i);
//            System.out.println(singleDocumentEvaluateResult.getFojNo()+"  "+singleDocumentEvaluateResult.getFojContent()+"  "+singleDocumentEvaluateResult.getLawName()+"  "+singleDocumentEvaluateResult.getLawContent()+"   "+singleDocumentEvaluateResult.getRelevancy());
//        }*/
//        //boolean exportSingleResult = esi.exportToExcel(exportExcelName, "f2l");
//        //System.out.println(exportSingleResult);
//        /*
//        List<BundleDocumentEvaluateResult> bundleDocumentEvaluateResultList = esi.bundleEvaluate(unzipDirectoryPath, "bl2f");
//        for(int j=0; j<bundleDocumentEvaluateResultList.size(); j++){
//            BundleDocumentEvaluateResult bundleDocumentEvaluateResult = bundleDocumentEvaluateResultList.get(j);
//            System.out.println(bundleDocumentEvaluateResult.getCaseId()+"  "+bundleDocumentEvaluateResult.getCaseName()+"  "+bundleDocumentEvaluateResult.getCaseReason()+"  "+bundleDocumentEvaluateResult.getJudgeName()+"   "+bundleDocumentEvaluateResult.getNoReferenceSource());
//        }*/
//    }

}

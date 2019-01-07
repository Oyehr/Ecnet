package com.ecm.service.impl;

import com.ecm.dao.*;
import com.ecm.keyword.manager.KeyWordCalculator;
import com.ecm.keyword.manager.RelationCreator;
import com.ecm.model.*;
import com.ecm.model.xsd_evidence.*;
import com.ecm.service.LogicService;
import com.ecm.service.ModelManageService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModelManageServiceImpl implements ModelManageService {

    @Autowired
    private EvidenceBodyDao evidenceBodyDao;
    @Autowired
    private EvidenceHeadDao evidenceHeadDao;
    @Autowired
    private MOD_JointDao jointDao;
    @Autowired
    private MOD_ArrowDao arrowDao;
    @Autowired
    private MOD_FactDao factDao;
    @Autowired
    private MOD_FactDocDao factDocDao;
    @Autowired
    private LogicService logicService;
//    @Autowired
//    private LogicNodeDao logicNodeDao;

    @Override
    public JSONObject getEvidences(int cid) {
        JSONObject res = new JSONObject();
        JSONArray trusts = new JSONArray();
        JSONArray untrusts = new JSONArray();

        List<Evidence_Body> bodies = evidenceBodyDao.findAllByCaseID(cid);
        for(int i = 0;i<bodies.size();i++){
            Evidence_Body body = bodies.get(i);
            int bid = body.getId();

            JSONObject jo = new JSONObject();
            jo.put("body",body);

            List<Evidence_Head> headers = evidenceHeadDao.findAllByBodyid(bid);
            jo.put("headers",headers);

            if(body.getTrust()==1){
                trusts.add(jo);
            }else{
                untrusts.add(jo);
            }
        }

        List<Evidence_Head> freeHeaders = evidenceHeadDao.findAllByCaseIDAndBodyid(cid,-1);
        res.put("trusts",trusts);
        res.put("untrusts",untrusts);
        res.put("freeHeaders",freeHeaders);

        JSONArray factArr = new JSONArray();
        List<MOD_Fact> facts = factDao.findAllByCaseID(cid);
        for(int i = 0;i<facts.size();i++){
            MOD_Fact fact = facts.get(i);
            JSONObject jo = new JSONObject();
            jo.put("fact",fact);
            List<MOD_Joint> joints = jointDao.findAllByFactID(fact.getId());
            jo.put("joints",joints);
            factArr.add(jo);
        }
        res.put("facts",factArr);
        res.put("freeJoints",jointDao.findAllByFactIDAndCaseID(-1,cid));
        res.put("arrows",arrowDao.findAllByCaseID(cid));
        res.put("factDoc",factDocDao.findByCaseID(cid));

        return res;
    }

    @Override
    public JSONObject getHeadersByBodyID(int bid) {
        JSONObject obj = new JSONObject();
        obj.put("body",evidenceBodyDao.findById(bid));
        obj.put("headers",evidenceHeadDao.findAllByBodyid(bid));
        return obj;
    }

    //    @Async
    @Override
    public Evidence_Head saveHeader(Evidence_Head header) {
        return evidenceHeadDao.save(header);
    }

    @Override
    @Async
    public void saveHeaders(List<Evidence_Head> headers) {
        for(int i = 0;i<headers.size();i++){
            evidenceHeadDao.save(headers.get(i));
        }
    }

    @Override
//    @Async
    @Transactional
    public void deleteHeaderById(int id) {
        evidenceHeadDao.deleteById(id);
    }

    @Override
//    @Async
    public Evidence_Body saveBody(Evidence_Body body) {
        return evidenceBodyDao.save(body);
    }

    @Override
    @Async
    public void saveBodies(List<Evidence_Body> bodies) {
        for(int i = 0;i<bodies.size();i++){
            Evidence_Body body = bodies.get(i);
            if(body.getLogicNodeID()>=0){
                logicService.modEvidenceOrFactNode(body.getLogicNodeID(),body.getBody());
            }else{
                int lid = logicService.addEvidenceOrFactNode(body.getCaseID(),body.getBody(),0);
                body.setLogicNodeID(lid);
            }
            evidenceBodyDao.save(body);
        }
    }

    @Override
    public int getLogicNodeIDofBody(int bid) {
        return evidenceBodyDao.findLogicId(bid);
    }

    @Override
//    @Async
    @Transactional
    public void deleteBodyById(int id) {
        evidenceBodyDao.deleteById(id);
    }

    @Override
//    @Async
    public MOD_Joint saveJoint(MOD_Joint joint) {
        return jointDao.save(joint);
    }

    @Override
    @Async
    public void saveJoints(List<MOD_Joint> joints) {
        for(int i = 0;i<joints.size();i++){
            jointDao.save(joints.get(i));
        }
    }

    @Override
    @Transactional
//    @Async
    public void deleteJointById(int id) {
        jointDao.deleteById(id);
    }

    @Override
//    @Async
    public MOD_Fact saveFact(MOD_Fact fact) {
        return factDao.save(fact);
    }

    @Override
    @Async
    public void saveFacts(List<MOD_Fact> facts) {
        for(int i = 0;i<facts.size();i++){
            MOD_Fact fact = facts.get(i);
            if(fact.getLogicNodeID()>=0){
                logicService.modEvidenceOrFactNode(fact.getLogicNodeID(),fact.getContent());
            }else{
                int lid = logicService.addEvidenceOrFactNode(fact.getCaseID(),fact.getContent(),1);
                fact.setLogicNodeID(lid);
            }
            factDao.save(fact);
        }
    }

    @Override
    public int getLogicNodeIDofFact(int fid) {
        return factDao.getLogicNodeIDByID(fid);
    }

    @Override
    public MOD_Fact getFactByID(int id) {
        return factDao.findById(id);
    }

    @Override
    @Transactional
//    @Async
    public void deleteFactById(int id) {
        factDao.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteFactByCid(int cid) {
        factDao.deleteAllByCaseID(cid);
    }

    @Override
    @Transactional
    public void deleteJointsByCid(int cid) {
        jointDao.deleteAllByCaseID(cid);
    }

    @Override
//    @Async
    public MOD_Arrow saveArrow(MOD_Arrow arrow) {
        return arrowDao.save(arrow);
    }

    @Override
//    @Async
    public void saveArrows(List<MOD_Arrow> arrows) {
        for(int i = 0;i<arrows.size();i++){
            arrowDao.save(arrows.get(i));
        }
    }

    @Override
    @Transactional
    public void deleteArrowsByCid(int cid) {
        arrowDao.deleteAllByCaseID(cid);
    }

    @Override
    @Transactional
    public void deleteAll(int cid) {
        evidenceBodyDao.deleteAllByCaseID(cid);
        evidenceHeadDao.deleteAllByCaseID(cid);
        arrowDao.deleteAllByCaseID(cid);
        jointDao.deleteAllByCaseID(cid);
        factDao.deleteAllByCaseID(cid);
    }

    @Transactional
    @Override
    public void deleteByDocumentID(int did) {
        List<Evidence_Head> heads = evidenceHeadDao.getEvidenceHead(did);
        for(int i = 0;i<heads.size();i++){
            int hid = heads.get(i).getId();
            List<MOD_Arrow> arrows = arrowDao.findAllByHeaderID(hid);
            for(int j = 0;j<arrows.size();j++){
                MOD_Arrow arrow = arrows.get(j);
                int jid = arrow.getNodeTo_jid();
                MOD_Joint joint = jointDao.findById(jid);
                if(joint!=null){
                    int fid = joint.getFactID();
                    List<MOD_Joint> js = jointDao.findAllByFactID(fid);
                    if(js.size()>1){
                        jointDao.updateFactID(fid);
                    }else{
                        factDao.deleteById(fid);
                    }
                    jointDao.deleteById(jid);
                }
                arrowDao.deleteAllByJointID(jid);
            }
        }
    }

    @Override
    @Async
    public void saveLogicLinks(HashMap<Integer, List<Integer>> list, int cid) {
        for(int bid : list.keySet()){
            int eid = getLogicNodeIDofBody(bid);
            List<Integer> arr = list.get(bid);
            for(int i = 0;i<arr.size();i++){
                int fid = arr.get(i);
                int factID = getFactByID(fid).getLogicNodeID();
                logicService.addLinkForEvidenceAndFactNode(cid,eid,factID);
            }
        }
    }

    @Override
    public MOD_Fact_Doc saveFactDoc(MOD_Fact_Doc factDoc) {
        MOD_Fact_Doc fd = factDocDao.findByCaseID(factDoc.getCaseID());
        if(fd!=null&&fd.getId()>=0){
            factDoc.setId(fd.getId());
        }
        return factDocDao.save(factDoc);
    }

    @Override
    @Transactional
    public JSONObject getFactLinkpoints(int cid,JSONArray facts,JSONArray bodies) {
        deleteArrowsByCid(cid);
        deleteJointsByCid(cid);
        deleteFactByCid(cid);

        JSONObject jsonObject = new JSONObject();
        JSONArray unconfirmArr = new JSONArray();

        JSONObject data = new JSONObject();
        HashMap<Integer,Integer> factIndexArr = new HashMap<>();
        JSONArray farr = new JSONArray();
        for(Object factobj:facts){
            JSONObject factjobj = (JSONObject) factobj;
            MOD_Fact fact = new MOD_Fact();
            fact.setTextID(factjobj.getInt("textID"));
            fact.setContent(factjobj.getString("content"));
            fact.setCaseID(factjobj.getInt("caseID"));
            fact.setConfirm(factjobj.getInt("confirm"));

            if(fact.getConfirm()==1){
                int lid = logicService.addEvidenceOrFactNode(fact.getCaseID(),fact.getContent(),1);
                fact.setLogicNodeID(lid);
                fact = factDao.save(fact);

                KeyWordCalculator keyWordCalculator = new KeyWordCalculator();
                HashMap<String, List<String>> res = keyWordCalculator.calcKeyWord(fact.getContent());
                JSONObject fobj = new JSONObject();
                fobj.put("id", fact.getId());
                fobj.put("content", fact.getContent());
                fobj.put("keyWordMap", res);
                farr.add(fobj);
            }else{
                fact = factDao.save(fact);
                JSONObject unconfirmjo = new JSONObject();
                unconfirmjo.put("originID",factjobj.getInt("id"));
                unconfirmjo.put("newID",fact.getId());
                unconfirmArr.add(unconfirmjo);
            }
            factIndexArr.put(fact.getId(),factjobj.getInt("id"));
        }
        data.put("factList",farr);
        jsonObject.put("unconfirm",unconfirmArr);

        String[] typeStr = {"证人证言","被告人供述和辩解","书证","鉴定结论","勘验","检查笔录","其他"};
        int[] bodyIndexArr = new int[bodies.size()];

        JSONArray earr = new JSONArray();
        int index = 0;
        for (Object bodyobj : bodies) {
            JSONObject bodyjobj = (JSONObject) bodyobj;
            JSONObject bobj = new JSONObject();
            int bid = bodyjobj.getInt("id");
            bobj.put("id", bid);
            bobj.put("content", bodyjobj.getString("body"));
            bobj.put("type", typeStr[bodyjobj.getInt("type")]);

            JSONArray headArr = bodyjobj.getJSONArray("headList");
            JSONArray headList = new JSONArray();
            for(Object headobj:headArr){
                JSONObject headjo = (JSONObject) headobj;
                int hid = headjo.getInt("id");
                String head = headjo.getString("head");
                evidenceHeadDao.updateBodyIdById(hid,bid);
                headList.add(head);
            }
            bobj.put("headList", headList);
            earr.add(bobj);
            bodyIndexArr[index++] = bid;
        }
        data.put("evidenceList",earr);

        JSONObject res = new JSONObject();
        try {
            res = RelationCreator.getJoints(data);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("网络传输错误");
            return null;
        }

        if (res == null) {
            System.out.println("提取联结点失败");
            return null;
        } else {
            System.out.println("res:"+res.toString());
            JSONArray confirmArr = new JSONArray();

            JSONArray factList = (JSONArray) res.get("factList");
            for (Object fobject : factList) {
                JSONObject factobj = new JSONObject();

                JSONObject fjo = (JSONObject) fobject;
                int factID = fjo.getInt("id");
                MOD_Fact fact = factDao.findById(factID);

                factobj.put("originID",factIndexArr.get(factID));
                factobj.put("newID",factID);
                factobj.put("content",fjo.getString("content"));
                factobj.put("logicNodeID",fact.getLogicNodeID());
                factobj.put("textID",fact.getTextID());
                JSONArray relArr = new JSONArray();

                JSONArray linksArray = fjo.getJSONArray("factLinkPointList");

                for (Object relation : linksArray) {
                    JSONObject jointjo = new JSONObject();

                    JSONObject relationjo = (JSONObject) relation;
                    String jointContent = relationjo.getString("factValue");
                    MOD_Joint joint = new MOD_Joint();
                    joint.setCaseID(cid);
                    joint.setContent(jointContent);
                    joint.setFactID(factID);
                    int jointID = jointDao.save(joint).getId();

                    jointjo.put("id",jointID);
                    jointjo.put("content",jointContent);
                    JSONArray headArr = new JSONArray();

                    JSONArray arrowArr = relationjo.getJSONArray("evidenceList");
                    for(Object arrowobj:arrowArr){
                        JSONObject arrowjo = (JSONObject) arrowobj;
                        int bodyID = bodyIndexArr[arrowjo.getInt("evidenceIndex")];
                        int headID = evidenceHeadDao.findIdByBodyidAndHead(bodyID,arrowjo.getString("value"));
                        MOD_Arrow arrow = new MOD_Arrow();
                        arrow.setCaseID(cid);
                        arrow.setNodeFrom_hid(headID);
                        arrow.setNodeTo_jid(jointID);
                        arrow = arrowDao.save(arrow);

                        JSONObject ajson = new JSONObject();
                        ajson.put("headID",headID);
                        ajson.put("arrowID",arrow.getId());
                        headArr.add(ajson);
                    }
                    jointjo.put("headList",headArr);
                    relArr.add(jointjo);
                }
                factobj.put("linkpoints",relArr);
                confirmArr.add(factobj);
            }
            jsonObject.put("confirm",confirmArr);
            return jsonObject;
        }
    }

    @Override
    @Transactional
    public void updateBodyTrustById(int bid) {
        evidenceBodyDao.updateTrustById(1,bid);
    }

    @Override
    public void writeToExcel(int cid,String filePath) {
        //创建workbook
        HSSFWorkbook workbook = new HSSFWorkbook();
        Map number = new HashMap();//链体id与证据清单序号

        // 标题字体对象
        HSSFFont titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);
//        font.setFontName("新宋体");
        titleFont.setColor(HSSFColor.BLUE.index);

        //列名字体
        HSSFFont cNameFont = workbook.createFont();
        cNameFont.setBold(true);
        cNameFont.setFontHeightInPoints((short) 10);
//        font.setFontName("新宋体");
        cNameFont.setColor(HSSFColor.BLUE.index);

        //单元格字体
        HSSFFont font = workbook.createFont();
        font.setBold(false);
        font.setFontHeightInPoints((short) 10);
//        font.setFontName("新宋体");
        cNameFont.setColor(HSSFColor.BLACK.index);

        //标题样式
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setFont(titleFont);
        titleStyle.setWrapText(true);//自动换行

        //列名样式
        HSSFCellStyle cStyle = workbook.createCellStyle();
        cStyle.setAlignment(HorizontalAlignment.CENTER);
        cStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cStyle.setFont(cNameFont);
        cStyle.setWrapText(true);
        //设置边框样式
//        cStyle.setBorderTop(BorderStyle.THIN);
//        cStyle.setBorderBottom(BorderStyle.THIN);
//        cStyle.setBorderLeft(BorderStyle.THIN);
//        cStyle.setBorderRight(BorderStyle.THIN);
        //设置边框颜色
//        cStyle.setTopBorderColor(HSSFColor.BLACK.index);
//        cStyle.setBottomBorderColor(HSSFColor.BLACK.index);
//        cStyle.setLeftBorderColor(HSSFColor.BLACK.index);
//        cStyle.setRightBorderColor(HSSFColor.BLACK.index);

        //列名样式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(font);
        style.setWrapText(true);
        //设置边框样式
//        style.setBorderTop(BorderStyle.THIN);
//        style.setBorderBottom(BorderStyle.THIN);
//        style.setBorderLeft(BorderStyle.THIN);
//        style.setBorderRight(BorderStyle.THIN);

        //创建证据清单sheet
        HSSFSheet sheet1 = workbook.createSheet("证据清单");
//        sheet1.autoSizeColumn(1, true);
        CellRangeAddress callRangeAddress = new CellRangeAddress(0,0,1,9);//起始行,结束行,起始列,结束列
        sheet1.addMergedRegion(callRangeAddress);
        HSSFRow row = sheet1.createRow(0);
        HSSFCell cell = row.createCell(1);
        cell.setCellValue("证据清单");
        cell.setCellStyle(titleStyle);

        HSSFRow row2 = sheet1.createRow(1);
        String[] titles = {"序号","证据名称","证据明细","证据种类（下拉）","提交人","质证理由","质证结论（下拉）","链头信息","该链头在证据中的关键文本（短句）"};
        for(int i=1;i<=titles.length;i++)
        {
            HSSFCell cell2 = row2.createCell(i);
            //加载单元格样式
            cell2.setCellStyle(cStyle);
            cell2.setCellValue(titles[i-1]);
        }

        List<Evidence_Body> bodies = evidenceBodyDao.findAllByCaseID(cid);
        int rowNum = 2;
        for(int i = 0;i<bodies.size();i++){
            HSSFRow hrow = sheet1.createRow(rowNum);
            Evidence_Body body = bodies.get(i);
            int bid = body.getId();
            List<Evidence_Head> headers = evidenceHeadDao.findAllByCaseIDAndBodyid(cid,bid);
            int hNum = headers.size();

            if(hNum>1)
            for(int j = 1;j<=7;j++){
                CellRangeAddress cra = new CellRangeAddress(rowNum,rowNum+hNum-1,j,j);
                sheet1.addMergedRegion(cra);
//                RegionUtil.setBorderBottom(1,cra, sheet1);
//                RegionUtil.setBorderLeft(1,cra, sheet1);
//                RegionUtil.setBorderRight(1,cra, sheet1);
//                RegionUtil.setBorderTop(1,cra, sheet1);
//                setRegionStyle(sheet1,cra,style);
            }
            HSSFCell ctemp1 = hrow.getCell(1);
            if(ctemp1==null){
                ctemp1 = hrow.createCell(1);
                ctemp1.setCellStyle(style);
            }
            ctemp1.setCellValue(i+1);
            number.put(bid,i+1);

            HSSFCell ctemp2 = hrow.getCell(2);
            if(ctemp2==null){
                ctemp2 = hrow.createCell(2);
                ctemp2.setCellStyle(style);
            }
            ctemp2.setCellValue(body.getName());

            HSSFCell ctemp3 = hrow.getCell(3);
            if(ctemp3==null){
                ctemp3 = hrow.createCell(3);
                ctemp3.setCellStyle(style);
            }
            ctemp3.setCellValue(body.getBody());

            HSSFCell ctemp4 = hrow.getCell(4);
            if(ctemp4==null){
                ctemp4 = hrow.createCell(4);
                ctemp4.setCellStyle(style);
            }
            ctemp4.setCellValue(body.getTypeToString());

            HSSFCell ctemp5 = hrow.getCell(5);
            if(ctemp5==null){
                ctemp5 = hrow.createCell(5);
                ctemp5.setCellStyle(style);
            }
            ctemp5.setCellValue(body.getCommitter());

            HSSFCell ctemp6 = hrow.getCell(6);
            if(ctemp6==null){
                ctemp6 = hrow.createCell(6);
                ctemp6.setCellStyle(style);
            }
            ctemp6.setCellValue(body.getReason());

            HSSFCell ctemp7 = hrow.getCell(7);
            if(ctemp7==null){
                ctemp7 = hrow.createCell(7);
                ctemp7.setCellStyle(style);
            }
            ctemp7.setCellValue(body.getTrustToString());

            for(int k = 0;k<hNum;k++){
                Evidence_Head h = headers.get(k);
                HSSFRow rowtemp;
                if(k==0){
                    rowtemp = hrow;
                }else{
                    rowtemp = sheet1.createRow(rowNum);
                }
                HSSFCell ctempk_8 = rowtemp.createCell(8);
                ctempk_8.setCellValue(h.getHead());
                ctempk_8.setCellStyle(style);
                HSSFCell ctempk_9 = rowtemp.createCell(9);
                ctempk_9.setCellValue(h.getKeyText());
                ctempk_9.setCellStyle(style);
                rowNum++;
            }
            if(hNum==0){
                rowNum++;
            }
        }


        //创建事实清单sheet
        HSSFSheet sheet2 = workbook.createSheet("事实清单");
//        sheet2.autoSizeColumn(1, true);
        CellRangeAddress callRangeAddress2 = new CellRangeAddress(0,0,1,7);//起始行,结束行,起始列,结束列
        sheet2.addMergedRegion(callRangeAddress2);
        HSSFRow r1 = sheet2.createRow(0);
        HSSFCell c1 = r1.createCell(1);
        c1.setCellValue("事实清单");
        c1.setCellStyle(titleStyle);

        HSSFRow r2 = sheet2.createRow(1);
        String[] titles2 = {"序号","事实名称","事实明细(较长文本)","来自事实的链头（联结点）","来自证据的链头","证据序号(引用证据清单的序号)","与链头相关的证据中的关键文本(短句)"};
        for(int i=1;i<=titles2.length;i++)
        {
            HSSFCell cell2 = r2.createCell(i);
            //加载单元格样式
            cell2.setCellStyle(cStyle);
            cell2.setCellValue(titles2[i-1]);
        }

        int startRow = 2;
        int endRow = 2;
        List<MOD_Fact> facts = factDao.findAllByCaseID(cid);
        for(int i = 0;i<facts.size();i++){
            startRow = endRow;
            HSSFRow hrow = sheet2.createRow(endRow);
            MOD_Fact fact = facts.get(i);
            int fid = fact.getId();
            List<MOD_Joint> joints = jointDao.findAllByFactIDAndCaseID(fid,cid);
            int jNum = joints.size();

            for(int j = 0;j<jNum;j++){
                MOD_Joint joint = joints.get(j);
                List<Integer> hids = arrowDao.getHeaderIdByJointIdAndCaseID(joint.getId(),cid);
                int hNum = hids.size();
                HSSFRow rowtemp;
                if(j==0){
                    rowtemp = hrow;
                }else{
                    rowtemp = sheet2.createRow(endRow);
                }

                if(hNum>1){
                    CellRangeAddress cra = new CellRangeAddress(endRow,endRow+hNum-1,4,4);
                    sheet2.addMergedRegion(cra);
//                    RegionUtil.setBorderBottom(1,cra, sheet1);
//                    RegionUtil.setBorderLeft(1,cra, sheet1);
//                    RegionUtil.setBorderRight(1,cra, sheet1);
//                    RegionUtil.setBorderTop(1,cra, sheet1);
//                   setRegionStyle(sheet2,cra,style);
                }
                HSSFCell ctemp4 = rowtemp.getCell(4);
                if(ctemp4==null){
                    ctemp4 = rowtemp.createCell(4);
                    ctemp4.setCellStyle(style);
                }
                ctemp4.setCellValue(joint.getContent());

                for(int k = 0;k<hNum;k++){
                    int hid = hids.get(k);
                    Evidence_Head head = evidenceHeadDao.findById(hid);
                    HSSFRow rtmp;
                    if(k==0){
                        rtmp = rowtemp;
                    }else{
                        rtmp = sheet2.createRow(endRow);
                    }
                    HSSFCell ctemp5 = rtmp.createCell(5);
                    ctemp5.setCellValue(head.getHead());
                    ctemp5.setCellStyle(style);
                    HSSFCell ctemp6 = rtmp.createCell(6);
                    ctemp6.setCellValue(number.get(head.getBodyid()).toString());
                    ctemp6.setCellStyle(style);
                    HSSFCell ctemp7 = rtmp.createCell(7);
                    ctemp7.setCellValue(head.getKeyText());
                    ctemp7.setCellStyle(style);
                    endRow++;
                }
                if(hNum==0){
                    HSSFCell ctemp5 = rowtemp.createCell(5);
                    ctemp5.setCellStyle(style);
                    HSSFCell ctemp6 = rowtemp.createCell(6);
                    ctemp6.setCellStyle(style);
                    HSSFCell ctemp7 = rowtemp.createCell(7);
                    ctemp7.setCellStyle(style);
                    endRow++;
                }
            }
            if(jNum==0){
                HSSFCell ctemp4 = hrow.createCell(4);
                ctemp4.setCellStyle(style);
                endRow++;
            }

            if(endRow-1>startRow){
                for(int m = 1;m<=3;m++){
                    CellRangeAddress crat = new CellRangeAddress(startRow,endRow-1,m,m);
                    sheet2.addMergedRegion(crat);
//                    RegionUtil.setBorderBottom(1,crat, sheet1);
//                    RegionUtil.setBorderLeft(1,crat, sheet1);
//                    RegionUtil.setBorderRight(1,crat, sheet1);
//                    RegionUtil.setBorderTop(1,crat, sheet1);
//                    setRegionStyle(sheet2,crat,style);
                }
            }
            HSSFCell ctemp1 = hrow.getCell(1);
            if(ctemp1==null){
                ctemp1 = hrow.createCell(1);
                ctemp1.setCellStyle(style);
            }
            ctemp1.setCellValue(i+1);

            HSSFCell ctemp2 = hrow.getCell(2);
            if(ctemp2==null){
                ctemp2 = hrow.createCell(2);
                ctemp2.setCellStyle(style);
            }
            ctemp2.setCellValue(fact.getName());

            HSSFCell ctemp3 = hrow.getCell(3);
            if(ctemp3==null){
                ctemp3 = hrow.createCell(3);
                ctemp3.setCellStyle(style);
            }
            ctemp3.setCellValue(fact.getContent());
        }


        File file = new File(filePath);
        try {
            if(!file.exists()){
                file.createNewFile();
            }

            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            workbook.close();
            fileOut.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeToXML(int cid, String filePath) {
        //构造空的Document
        Document doc = DocumentHelper.createDocument();
        // 构造根元素
        Element rootElmt = doc.addElement("evidenceList");
        Element evidencesElmt = rootElmt.addElement("evidences");
        List<Evidence_Body> bodies = evidenceBodyDao.findAllByCaseID(cid);

        for(int i = 0;i<bodies.size();i++){
            Evidence_Body body = bodies.get(i);
            Element element = evidencesElmt.addElement("evidence");
            element.addAttribute("id", body.getId()+"");
            Element ename = element.addElement("name");
            ename.setText(body.getName());
            Element econtent = element.addElement("content");
            econtent.setText(body.getBody());
            Element etype = element.addElement("type");
            etype.setText(body.getTypeToString());
            Element ecommitter = element.addElement("committer");
            ecommitter.setText(body.getCommitter());
            Element ereason = element.addElement("reason");
            ereason.setText(body.getReason());
            Element etrust = element.addElement("trust");
            etrust.setText(body.getTrustToString());

            List<Evidence_Head> heads = evidenceHeadDao.findAllByBodyid(body.getId());
            if(heads.size()>=1){
                Element headElement = element.addElement("heads");
                for(int j = 0;j<heads.size();j++){
                    Evidence_Head head = heads.get(j);
                    Element hel = headElement.addElement("head");
                    hel.addAttribute("id",head.getId()+"");
                    Element hname = hel.addElement("name");
                    hname.setText(head.getName());
                    Element hcontent = hel.addElement("content");
                    hcontent.setText(head.getHead());
                }
            }
        }

        Element factsElmt = rootElmt.addElement("facts");
        List<MOD_Fact> facts = factDao.findAllByCaseID(cid);
        for(int i = 0;i<facts.size();i++){
            MOD_Fact fact = facts.get(i);
            Element element = factsElmt.addElement("fact");
            element.addAttribute("id", fact.getId()+"");
            Element fname = element.addElement("name");
            fname.setText(fact.getName());
            Element fcontent = element.addElement("content");
            fcontent.setText(fact.getContent());

            List<MOD_Joint> joints = jointDao.findAllByFactIDAndCaseID(fact.getId(),cid);
            if(joints.size()>=1){
                Element jointElement = element.addElement("joints");
                for(int j = 0;j<joints.size();j++){
                    MOD_Joint joint = joints.get(j);
                    Element jel = jointElement.addElement("joint");
                    jel.addAttribute("id",joint.getId()+"");
                    Element jname = jel.addElement("name");
                    jname.setText(joint.getName());
                    Element jcontent = jel.addElement("content");
                    jcontent.setText(joint.getContent());
                }
            }
        }

        List<MOD_Joint> joints = jointDao.findAllByCaseID(cid);
        Element relationsElmt = rootElmt.addElement("relations");
        for(int i = 0;i<joints.size();i++){
            MOD_Joint joint = joints.get(i);
            List<MOD_Arrow> arrows = arrowDao.findAllByCaseIDAndJointID(cid,joint.getId());

            if(arrows.size()>0){
                Element arrowsElmt = relationsElmt.addElement("relation");
                Element rel = arrowsElmt.addElement("arrows");
                for(int j = 0;j<arrows.size();j++){
                    MOD_Arrow arrow = arrows.get(j);
                    Evidence_Head head = evidenceHeadDao.findById(arrow.getNodeFrom_hid());

                    Element arrowElmt = rel.addElement("arrow");
                    arrowElmt.addAttribute("id",arrow.getId()+"");
                    Element aname = arrowElmt.addElement("name");
                    aname.setText(arrow.getName());
                    Element acontent = arrowElmt.addElement("content");
                    acontent.setText(arrow.getContent());
                    Element headElmt = arrowElmt.addElement("head");
                    headElmt.addAttribute("id",head.getId()+"");
                    Element hname = headElmt.addElement("name");
                    hname.setText(head.getName());
                    Element hcontent = headElmt.addElement("content");
                    hcontent.setText(head.getHead());
                    Element bodyID = headElmt.addElement("bodyID");
                    bodyID.setText(head.getBodyid()+"");
                }

                Element ato = arrowsElmt.addElement("joint");
                ato.addAttribute("id",joint.getId()+"");
                Element jname = ato.addElement("name");
                jname.setText(joint.getName());
                Element jcontent = ato.addElement("content");
                jcontent.setText(joint.getContent());
            }
        }
//        List<MOD_Arrow> arrows = arrowDao.findAllByCaseID(cid);
//        if(arrows.size()>=1){
//            Element arrowsElmt = rootElmt.addElement("relations");
//            for(int i = 0;i<arrows.size();i++){
//                MOD_Arrow arrow = arrows.get(i);
//                Evidence_Head head = evidenceHeadDao.findById(arrow.getNodeFrom_hid());
//                MOD_Joint joint = jointDao.findByIdAndCaseID(arrow.getNodeTo_jid(),cid);
//                Element rel = arrowsElmt.addElement("relation");
//                rel.addAttribute("id",joint.getId()+"");
//
//                Element aname = rel.addElement("name");
//                aname.setText(arrow.getName());
//                Element acontent = rel.addElement("content");
//                acontent.setText(arrow.getContent());
//                Element afrom = rel.addElement("from");
//                afrom.addAttribute("type","head");
//                afrom.addAttribute("id",arrow.getNodeFrom_hid()+"");
//                afrom.addAttribute("name",head.getName());
//                afrom.setText(head.getHead());
//                Element ato = rel.addElement("to");
//                ato.addAttribute("type","joint");
//                ato.addAttribute("id",arrow.getNodeTo_jid()+"");
//                ato.addAttribute("name",joint.getName());
//                ato.setText(joint.getContent());
//            }
//        }

        FileWriter fw = null;
        try {
            fw = new FileWriter(filePath);
            //定义输出格式 和 字符集
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            //定义用于输出xml文件的XMLWriter对象
            XMLWriter xmlWriter = new XMLWriter(fw,format);
            xmlWriter.write(doc);
            xmlWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeToXMLBySchema(int cid, String filePath) {
        try {
            JAXBContext ctx = JAXBContext.newInstance("com.ecm.model.xsd_evidence");
            ObjectFactory of = new ObjectFactory();
            Ecm ecm = of.createEcm();

            List<Evidence_Body> bodies = evidenceBodyDao.findAllByCaseID(cid);
            Ecm.Evidences evidences = of.createEcmEvidences();
            for(int i = 0;i<bodies.size();i++){
                Evidence_Body body = bodies.get(i);
                Evidence e = of.createEvidence();
//                e.setId(new BigInteger(body.getId()+""));
                e.setX(new BigInteger(body.getX()+""));
                e.setY(new BigInteger(body.getY()+""));
//                e.setDocumentId(new BigInteger(body.getDocumentid()+""));
                e.setType(new BigInteger(body.getType()+""));
                e.setTrust(new BigInteger(body.getTrust()+""));
                e.setIsDefendant(new BigInteger(body.getIsDefendant()+""));
//                e.setLogicNodeId(new BigInteger(body.getLogicNodeID()+""));
                e.getContent().add(of.createEvidenceName(transNull(body.getName())));
                e.getContent().add(of.createEvidenceContent(transNull(body.getBody())));
                e.getContent().add(of.createEvidenceType(body.getTypeToString()));
                e.getContent().add(of.createEvidenceCommitter(transNull(body.getCommitter())));
                e.getContent().add(of.createEvidenceReason(transNull(body.getReason())));
                e.getContent().add(of.createEvidenceTrust(body.getTrustToString()));

                Evidence.Heads headsXml = of.createEvidenceHeads();
                List<Evidence_Head> heads = evidenceHeadDao.findAllByBodyid(body.getId());
                for(int j = 0;j<heads.size();j++){
                    Evidence_Head head = heads.get(j);
                    Head h = of.createHead();
//                    h.setId(new BigInteger(head.getId()+""));
                    h.setX(new BigInteger(head.getX()+""));
                    h.setY(new BigInteger(head.getY()+""));
                    h.getContent().add(of.createHeadName(transNull(head.getName())));
                    h.getContent().add(of.createHeadContent(transNull(head.getHead())));
                    headsXml.getContent().add(of.createEvidenceHeadsHead(h));
                }
                e.getContent().add(of.createEvidenceHeads(headsXml));
                evidences.getContent().add(of.createEcmEvidencesEvidence(e));
            }
            ecm.getContent().add(of.createEcmEvidences(evidences));

            Ecm.Facts factsXml = of.createEcmFacts();
            List<MOD_Fact> facts = factDao.findAllByCaseID(cid);
            for(int i = 0;i<facts.size();i++){
                MOD_Fact fact = facts.get(i);
                Fact f = of.createFact();
//                f.setId(new BigInteger(fact.getId()+""));
                f.setX(new BigInteger(fact.getX()+""));
                f.setY(new BigInteger(fact.getY()+""));
//                f.setLogicNodeId(new BigInteger(fact.getLogicNodeID()+""));
                f.getContent().add(of.createFactName(transNull(fact.getName())));
                f.getContent().add(of.createFactContent(transNull(fact.getContent())));

                List<MOD_Joint> joints = jointDao.findAllByFactIDAndCaseID(fact.getId(),cid);
                Fact.Joints fjs = of.createFactJoints();
                for(int j = 0;j<joints.size();j++){
                    MOD_Joint joint = joints.get(j);
                    Joint jx = of.createJoint();
//                    jx.setId(new BigInteger(joint.getId()+""));
                    jx.setX(new BigInteger(joint.getX()+""));
                    jx.setY(new BigInteger(joint.getY()+""));
                    jx.getContent().add(of.createJointName(transNull(joint.getName())));
                    jx.getContent().add(of.createJointContent(transNull(joint.getContent())));
                    fjs.getContent().add(of.createFactJointsJoint(jx));
                }
                f.getContent().add(of.createFactJoints(fjs));
                factsXml.getContent().add(of.createEcmFactsFact(f));
            }
            ecm.getContent().add(of.createEcmFacts(factsXml));

            Ecm.Relations relations = of.createEcmRelations();
            List<MOD_Joint> joints = jointDao.findAllByCaseID(cid);
            for(int i = 0;i<joints.size();i++){
                MOD_Joint joint = joints.get(i);
                List<MOD_Arrow> arrows = arrowDao.findAllByCaseIDAndJointID(cid,joint.getId());

                if(arrows.size()>0){
                    Ecm.Relations.Relation relation = of.createEcmRelationsRelation();
                    Ecm.Relations.Relation.Arrows arrowsXml = of.createEcmRelationsRelationArrows();

                    for(int j = 0;j<arrows.size();j++){
                        MOD_Arrow arrow = arrows.get(j);
                        Evidence_Head head = evidenceHeadDao.findById(arrow.getNodeFrom_hid());
                        Arrow arrowXml = of.createArrow();
//                        arrowXml.setId(new BigInteger(arrow.getId()+""));
                        arrowXml.getContent().add(of.createArrowName(transNull(arrow.getName())));
                        arrowXml.getContent().add(of.createArrowContent(transNull(arrow.getContent())));
                        Arrow.Head hXml = of.createArrowHead();
                        int hid = -1;
                        int hx = -1;
                        int hy = -1;
                        String hname = "";
                        String hcontent = "";
                        int bodyId = -1;
                        String bodyContent = "";
                        if(head!=null){
                            hid = head.getId();
                            hx = head.getX();
                            hy = head.getY();
                            hname = head.getName();
                            hcontent = head.getHead();
                            bodyId = head.getBodyid();
                            if(bodyId>=0){
                                bodyContent = evidenceBodyDao.getContentById(bodyId);
                            }
                        }
//                        hXml.setId(new BigInteger(hid+""));
                        hXml.setX(new BigInteger(hx+""));
                        hXml.setY(new BigInteger(hy+""));
                        hXml.setName(transNull(hname));
                        hXml.setContent(transNull(hcontent));
                        hXml.setBodyContent(transNull(bodyContent));
//                        hXml.setBodyID(new BigInteger(bodyId+""));
                        arrowXml.getContent().add(of.createArrowHead(hXml));
                        arrowsXml.getContent().add(of.createEcmRelationsRelationArrowsArrow(arrowXml));
                    }
                    relation.getContent().add(of.createEcmRelationsRelationArrows(arrowsXml));

                    String factContent = "";
                    int fid = joint.getFactID();
                    if(fid>=0){
                        factContent = factDao.findById(fid).getContent();
                    }
                    Ecm.Relations.Relation.Joint jXml = of.createEcmRelationsRelationJoint();
//                    jXml.setId(new BigInteger(joint.getId()+""));
                    jXml.setX(new BigInteger(joint.getX()+""));
                    jXml.setY(new BigInteger(joint.getY()+""));
                    jXml.getContent().add(of.createEcmRelationsRelationJointName(transNull(joint.getName())));
                    jXml.getContent().add(of.createEcmRelationsRelationJointContent(transNull(joint.getContent())));
                    jXml.getContent().add(of.createEcmRelationsRelationJointFactContent(transNull(factContent)));
                    relation.getContent().add(of.createEcmRelationsRelationJoint(jXml));
                    relations.getContent().add(of.createEcmRelationsRelation(relation));
                }
            }
            ecm.getContent().add(of.createEcmRelations(relations));

            File file = new File(filePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            Marshaller marshaller = ctx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// //编码格式
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);// 是否省略xm头声明信息
            // 将XML文件格式化输出
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ecm, fileOutputStream);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String transNull(String str){
        if(str!=null){
            return str;
        }else{
            return "";
        }
    }

    private void setRegionStyle(HSSFSheet sheet, CellRangeAddress region, HSSFCellStyle cs) {
        for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
            HSSFRow row = sheet.getRow(i);
            if(row==null){
                row = sheet.createRow(i);
            }
            for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
                HSSFCell cell = row.getCell(j);
                if(cell==null){
                    cell = row.createCell(j);
                    System.out.println(i+";"+j);
                }
                cell.setCellStyle(cs);
            }
        }
    }
}

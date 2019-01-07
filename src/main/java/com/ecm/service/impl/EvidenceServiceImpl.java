package com.ecm.service.impl;

import com.ecm.dao.EvidenceBodyDao;
import com.ecm.dao.EvidenceDocuDao;
import com.ecm.dao.EvidenceHeadDao;
import com.ecm.dao.MOD_ArrowDao;
import com.ecm.keyword.manager.HeadCreator;
import com.ecm.keyword.manager.KeyWordCalculator;
import com.ecm.keyword.reader.ExcelUtil;
import com.ecm.model.*;
import com.ecm.service.EvidenceService;
import com.ecm.service.LogicService;
import com.ecm.service.ModelManageService;
import com.ecm.util.ImportXMLUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ecm.keyword.reader.ExcelUtil.getExcelWorkbook;
import static com.ecm.keyword.reader.ExcelUtil.getSheetByNum;

@Service
public class EvidenceServiceImpl implements EvidenceService {


    @Autowired
    public EvidenceDocuDao evidenceDocuDao;
    @Autowired
    public EvidenceBodyDao evidenceBodyDao;
    @Autowired
    public EvidenceHeadDao evidenceHeadDao;
    @Autowired
    public LogicService logicService;
    @Autowired
    public MOD_ArrowDao arrowDao;
    @Autowired
    public ModelManageService modelManageService;

    @Override
    public Evidence_Document saveOrUpdate(Evidence_Document evidence_document) {
        int id = findDocuIdByAjxhAndType(evidence_document.getCaseID(), evidence_document.getType());
        if (id != -1) {
            evidence_document.setId(id);
        }
        return evidenceDocuDao.save(evidence_document);
    }

    @Override
    public int findDocuIdByAjxhAndType(int ajxh, int type) {
        Evidence_Document evidence_document = evidenceDocuDao.getEvidenceDocument(ajxh, type);
        if (evidence_document == null) {
            return -1;
        } else {
            return evidence_document.getId();
        }
    }

    @Override
    public Evidence_Document findDocuByAjxhAndType(int ajxh, int type) {
        return evidenceDocuDao.getEvidenceDocument(ajxh, type);
    }

    @Override
    public List<Evidence_Body> findBodyByDocu(int documentid) {
        return evidenceBodyDao.getAllByDocumentid(documentid);
    }

    @Override
    public Evidence_Body saveBody(Evidence_Body evidence_body) {
        return evidenceBodyDao.save(evidence_body);
    }

    @Transactional
    @Override
    public Evidence_Head saveHead(Evidence_Head evidence_head) {
        return evidenceHeadDao.save(evidence_head);
    }

    @Transactional
    @Override
    public void deleteBodyById(int id) {
        evidenceBodyDao.deleteById(id);
        return;
    }

    @Transactional
    @Override
    public void deleteBodyAllByDocuId(int document_id) {
        List<Integer> bodyIdList = evidenceBodyDao.findAllByDocumentid(document_id);


        for (Integer i : bodyIdList
                ) {

            logicService.deleteNode(evidenceBodyDao.findLogicId(i));
            evidenceHeadDao.deleteAllByBodyid(i);

        }
        evidenceBodyDao.deleteAllByDocumentid(document_id);
    }

    @Transactional
    @Override
    public void deleteBodyAllByCaseId(int caseId) {


        evidenceBodyDao.deleteAllByCaseID(caseId);

    }

    @Transactional
    @Override
    public void updateBodyById(String body, int id) {
        evidenceBodyDao.updateBodyById(body, id);
    }

    @Transactional
    @Override
    public void updateTypeById(int type, int id) {
        evidenceBodyDao.updateTypeById(type, id);
    }

    @Transactional
    @Override
    public void updateTrustById(int trust, int id) {
        evidenceBodyDao.updateTrustById(trust, id);
    }

    @Transactional
    @Override
    public List<Evidence_Body> createHead(int documentid) {
        List<Evidence_Body> bodies = evidenceBodyDao.getAllByDocumentid(documentid);
        JSONArray jsonArray = new JSONArray();
        for (Evidence_Body body : bodies) {
            KeyWordCalculator keyWordCalculator = new KeyWordCalculator();
            HashMap<String, List<String>> res = keyWordCalculator.calcKeyWord(body.getBody());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", body.getId());
            jsonObject.put("content", body.getBody());
            jsonObject.put("type", body.getTypeToString());
            jsonObject.put("keyWordMap", res);
            jsonObject.put("headList", new JSONArray());
            jsonArray.add(jsonObject);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("evidenceList", jsonArray);

        try {
            jsonObject = HeadCreator.getHead(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("网络传输错误");
            return null;
        }

        if (jsonObject == null) {
            System.out.println("提取链头失败");
            return null;
        } else {
            JSONArray evidenceList = (JSONArray) jsonObject.get("evidenceList");
            List<Evidence_Body> evidenceBodyList = new ArrayList<>();
            for (Object object : evidenceList) {
                JSONObject evidence = (JSONObject) object;
                Evidence_Body evidence_body = new Evidence_Body();
//                evidence_body.setCaseID(bodies.get(0).getCaseID());
//                evidence_body.setBody(evidence.getString("content"));
//                evidence_body.setDocumentid(documentid);
//                evidence_body.setType(EvidenceType.getTypeByName(evidence.getString("type")).getIndex());
                evidence_body.setId(evidence.getInt("id"));

                JSONArray headArray = evidence.getJSONArray("headList");
                evidenceHeadDao.deleteAllByBodyid(evidence.getInt("id"));
                //List<String> headNameList=(ArrayList<String>)evidence.get("headList");
                for (Object headname : headArray) {
                    Evidence_Head head = new Evidence_Head();
                    head.setBodyid(evidence.getInt("id"));
                    head.setCaseID(bodies.get(0).getCaseID());
                    head.setDocumentid(documentid);
                    head.setHead(headname.toString());
                    head = evidenceHeadDao.save(head);
                    evidence_body.addHead(head);
                }
                evidenceBodyList.add(evidence_body);
            }

            return evidenceBodyList;
        }


    }

    @Transactional
    @Override
    public void updateHeadById(String head, int id) {
        evidenceHeadDao.updateHeadById(head, id);
    }

    @Transactional
    @Override
    public void deleteHeadById(int id) {
        evidenceHeadDao.deleteById(id);
        arrowDao.deleteAllByNodeFrom_hid(id);
    }

    @Transactional
    @Override
    public void deleteHeadAllByCaseId(int caseId) {
        evidenceHeadDao.deleteAllByCaseID(caseId);
    }

    @Transactional
    @Override
    public void deleteHeadAllByBody(int body_id) {
        List<Evidence_Head> heads = evidenceHeadDao.findAllByBodyid(body_id);
        for (int i = 0; i < heads.size(); i++) {
            Evidence_Head h = heads.get(i);
            evidenceHeadDao.deleteById(h.getId());
            arrowDao.deleteAllByNodeFrom_hid(h.getId());
        }
//        evidenceHeadDao.deleteAllByBodyid(body_id);
    }

    @Override
    public List<Evidence_Head> findHeadByBody(int bodyid) {
        return evidenceHeadDao.findAllByBodyid(bodyid);
    }

    @Override
    public int findLogicId(int bodyid) {
        return evidenceBodyDao.findLogicId(bodyid);
    }


    @Override
    public List<Evidence_Document> importDocumentByExcel(String filepath, int caseId) {
        List<Evidence_Document> list = new ArrayList<>();
        Evidence_Document evidence_document1 = new Evidence_Document();
        Evidence_Document evidence_document2 = new Evidence_Document();
        Workbook book = null;
        try {
            book = ExcelUtil.getExcelWorkbook(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet = ExcelUtil.getSheetByNum(book, 0);
        int lastRowNum = sheet.getLastRowNum();
        System.out.println("last number is " + lastRowNum);
        String text1 = "";
        String text2 = "";
        int xh = 1;
        for (int i = 2; i <= lastRowNum; i++) {
            Row row = null;
            row = sheet.getRow(i);
            if (row.getCell(3).getStringCellValue() != null && row.getCell(3).getStringCellValue() != "") {
                 System.out.println("reading line is " + i);
                if (row.getCell(5).getStringCellValue().contains("被告")) {
                    text2 += xh + "、" + row.getCell(3).getStringCellValue();
                } else {
                    text1 += xh + "、" + row.getCell(3).getStringCellValue();
                }
                xh++;
            }
        }
        evidence_document1.setType(0);//??????????????无法区分原告被告
        evidence_document1.setText(text1);
        evidence_document1.setCaseID(caseId);
        saveOrUpdate(evidence_document1);
        evidence_document2.setType(1);//??????????????无法区分原告被告
        evidence_document2.setText(text2);
        evidence_document2.setCaseID(caseId);
        saveOrUpdate(evidence_document2);

        list.add(evidence_document1);
        list.add(evidence_document2);
        return list;
    }


    @Transactional
    @Override
    public List<Evidence_Body> importEviByExcel(String filepath, int caseId, List<Evidence_Document> doculist) {
        deleteBodyAllByCaseId(caseId);
        deleteHeadAllByCaseId(caseId);
        logicService.deleteAllNodesByCaseID(caseId);

        List<Evidence_Body> bodylist = new ArrayList<>();

        Workbook book = null;
        try {
            book = getExcelWorkbook(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet = getSheetByNum(book, 0);
        int lastRowNum = sheet.getLastRowNum();
        System.out.println("last number is " + lastRowNum);
        String text = "";
        int xh = 1;
        Evidence_Body evidenceBody = new Evidence_Body();
        for (int i = 2; i <= lastRowNum; i++) {
            Row row = null;
            row = sheet.getRow(i);
            if (row != null) {
                if (row.getCell(3).getStringCellValue() != null && row.getCell(3).getStringCellValue() != "") {
                    System.out.println("reading line is " + i);
                    text = row.getCell(3).getStringCellValue();
                    //System.out.println(evidenceBody.toString());
                    evidenceBody = new Evidence_Body();

                   // int logicNodeId = logicService.addEvidenceOrFactNode(caseId, text, 0);
                    evidenceBody.setCaseID(caseId);
                    evidenceBody.setBody(text);
                    evidenceBody.setTypeByString(row.getCell(4).getStringCellValue());
                    evidenceBody.setTrustByString(row.getCell(8).getStringCellValue());
                    evidenceBody.setDocumentid(getDocuIdByDocuList(row.getCell(5).getStringCellValue(), doculist));
                    evidenceBody.setLogicNodeID(-1);
                    evidenceBody = saveBody(evidenceBody);
                    bodylist.add(evidenceBody);
                    //  deleteHeadAllByBody(evidenceBody.getId());
                }

                // 将区域编号的cell中的内容当做字符串处理
                row.getCell(9).setCellType(HSSFCell.CELL_TYPE_STRING);
                String headText = row.getCell(9).getStringCellValue();
                if (!evidenceBody.isHeadContained(headText)) {
                    Evidence_Head evidence_head = new Evidence_Head();
                    evidence_head.setCaseID(caseId);
                    evidence_head.setHead(row.getCell(9).getStringCellValue());
                    evidence_head.setBodyid(evidenceBody.getId());
                    evidence_head.setDocumentid(evidenceBody.getDocumentid());
                    System.out.println(evidence_head.toString());
                    evidence_head = saveHead(evidence_head);
                    evidenceBody.addHead(evidence_head);
                }

            }


        }
        return bodylist;


    }

    private int getDocuIdByDocuList(String stringCellValue, List<Evidence_Document> list) {
        if (stringCellValue.contains("原告")) {
            return list.get(0).getId();
        } else {
            return list.get(1).getId();
        }
    }

    @Override
    @Async
    public void importFactByExcel(String filepath, int caseId, List<Evidence_Body> bodylist) {

        modelManageService.deleteArrowsByCid(caseId);
        modelManageService.deleteJointsByCid(caseId);
        modelManageService.deleteFactByCid(caseId);
        Workbook book = null;
        try {
            book = ExcelUtil.getExcelWorkbook(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet = ExcelUtil.getSheetByNum(book, 1);
        int lastRowNum = sheet.getLastRowNum();
        String text = "";
        List<HashMap<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> hashMap = new HashMap<>();
        List<HashMap<String, Object>> headlist = new ArrayList<>();
        List<MOD_Joint> jointList = new ArrayList<>();

        MOD_Fact mod_fact = new MOD_Fact();
        for (int i = 2; i <= lastRowNum; i++) {
            Row row = null;
            row = sheet.getRow(i);
            if (row != null) {
                if (row.getCell(3).getStringCellValue() != null && row.getCell(3).getStringCellValue() != "") {
                    System.out.println("reading line is " + i);
                    System.out.println(hashMap.toString());
                    hashMap = new HashMap<>();
                    hashMap.put("id", row.getCell(1).getNumericCellValue());
                    hashMap.put("name", row.getCell(2).getStringCellValue());
                    hashMap.put("text", row.getCell(3).getStringCellValue());
                    headlist = new ArrayList<>();
                    hashMap.put("headList", headlist);
                    list.add(hashMap);


                    mod_fact = new MOD_Fact();
                    mod_fact.setCaseID(caseId);
                    mod_fact.setId((int) row.getCell(1).getNumericCellValue());
                    mod_fact.setContent(row.getCell(3).getStringCellValue());
                    mod_fact.setName(row.getCell(2).getStringCellValue());
                    mod_fact = modelManageService.saveFact(mod_fact);
                    jointList = new ArrayList<>();

                }

                HashMap<String, Object> headMap = new HashMap<>();


                row.getCell(4).setCellType(HSSFCell.CELL_TYPE_STRING);
                row.getCell(6).setCellType(HSSFCell.CELL_TYPE_STRING);
                headMap.put("link", row.getCell(4).getStringCellValue());
                headMap.put("nodeId", row.getCell(5).getNumericCellValue());
                headMap.put("nodeFromEvi", row.getCell(6).getStringCellValue());
                headMap.put("keyText", row.getCell(7).getStringCellValue());


                MOD_Joint mod_joint = isJointContained(jointList, row.getCell(4).getStringCellValue());
                if (mod_joint == null) {
                    mod_joint = new MOD_Joint();
                    mod_joint.setCaseID(caseId);
                    mod_joint.setContent(row.getCell(4).getStringCellValue());
                    mod_joint.setFactID(mod_fact.getId());
                    mod_joint = modelManageService.saveJoint(mod_joint);
                    jointList.add(mod_joint);
                }

                MOD_Arrow mod_arrow = new MOD_Arrow();
                mod_arrow.setCaseID(caseId);
                mod_arrow.setNodeTo_jid(mod_joint.getId());
                mod_arrow.setNodeFrom_hid(getHeadIdByEvi(bodylist.get((int) row.getCell(5).getNumericCellValue() - 1), row.getCell(6).getStringCellValue()));
                mod_arrow = modelManageService.saveArrow(mod_arrow);
                headlist.add(headMap);
            }


        }

    }

    private MOD_Joint isJointContained(List<MOD_Joint> jointList, String stringCellValue) {
        for (MOD_Joint joint : jointList) {
            if (joint.getContent().equals(stringCellValue)) {
                return joint;
            }
        }
        return null;
    }

    @Override
    @Async
    public void importLogicByExcel(String filepath, int caseId, List<Evidence_Body> bodyList) {


        Workbook book = null;
        try {
            book = ExcelUtil.getExcelWorkbook(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet = ExcelUtil.getSheetByNum(book, 2);
        int lastRowNum = sheet.getLastRowNum();
        String text = "";

        List<HashMap<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> hashMap = new HashMap<>();
        List<HashMap<String, Object>> headlist = new ArrayList<>();
        List<HashMap<String, Object>> factList = new ArrayList<>();
        int resultId = -1;
        for (int i = 2; i <= lastRowNum; i++) {
            Row row = null;
            row = sheet.getRow(i);
            if (row != null) {
                if (row.getCell(4).getStringCellValue() != null && row.getCell(4).getStringCellValue() != "") {
                    System.out.println("reading line is " + i);
                    text = row.getCell(4).getStringCellValue();
                    text = text.substring(4);

                    System.out.println(hashMap.toString());
                    hashMap = new HashMap<>();
                    hashMap.put("result", text);//idList是string数组

                    resultId = logicService.addNode(caseId, -1, text, 3);//结论
                    List<String> lawList = saveLawList(row.getCell(3).getStringCellValue(), resultId, caseId);
                    hashMap.put("law", lawList);
                    factList = new ArrayList<>();
                    hashMap.put("factList", factList);
                    list.add(hashMap);
                }
                HashMap<String, Object> factMap = new HashMap<>();
                int id = (int) row.getCell(2).getStringCellValue().charAt(2) - '0';
                factMap.put("id", id);
                factMap.put("fact", row.getCell(2).getStringCellValue().substring(4));

                String detail = row.getCell(2).getStringCellValue().substring(4);
                int factId = logicService.addNode(caseId, resultId, detail, 1);//事实

                List<Integer> eviList = saveEviList(row.getCell(1).getStringCellValue(), factId, caseId, bodyList);
                factMap.put("evi", eviList);
                factList.add(factMap);

            }


        }
        //    System.out.println(list.toString());


    }


    @Transactional
    @Override
    public void deleteAllTable(int caseId) {
        modelManageService.deleteAll(caseId);
        logicService.deleteAllNodesByCaseID(caseId);
    }


    private int getHeadIdByEvi(Evidence_Body body, String head) {
        List<Evidence_Head> headList = body.getHeadList();


        for (Evidence_Head headtemp : headList) {

            if (headtemp.getHead().equals(head)) {
                return headtemp.getId();
            }
        }
        System.out.println("error");
        return -1;
    }

    private List<Integer> saveEviList(String stringCellValue, int factId, int caseId, List<Evidence_Body> bodyList) {


        List<Integer> list = new ArrayList<>();

        stringCellValue = stringCellValue.substring(2);
        // System.out.println(stringCellValue);
        String[] strs = stringCellValue.split("、");
        for (String str : strs) {
            list.add(Integer.valueOf(str));
            // System.out.println(str);
            int i = Integer.valueOf(str);
            Evidence_Body evidence_body=bodyList.get(i - 1);
           int logic= logicService.addNode(caseId, factId,evidence_body.getBody(), 0);
            evidence_body.setLogicNodeID(logic);
           evidenceBodyDao.save(evidence_body);
        }
        return list;
    }

    /**
     * 为了解决书名号中的顿号问题我也是煞费苦心
     *
     * @param stringCellValue
     * @return
     */
    private List<String> saveLawList(String stringCellValue, int resultId, int caseId) {

//        String[] strs=stringCellValue.split("\\(\\?<=《\\)\\[^》]\\+\\(\\?=》\\)");
        List<String> result = new ArrayList<>();
        String lawName = "";
//        lawName=lawName.substring(0,lawName.indexOf('》')+1);
//
//        for(String str:strs){
//            if(!str.contains("《")){
//                result.add(lawName+str);
//                System.out.println(lawName+str);
//            }else{
//                result.add(str);
//                System.out.println(str);
//                lawName=str;
//                lawName=lawName.substring(0,lawName.indexOf('》')+1);
//
//            }
//        }

        int begin = 0;
        int end = 0;
        String law = "";
        for (int i = 0; i < stringCellValue.length(); i++) {
            if (stringCellValue.charAt(i) == '《') {
                begin = i;
                int j = i;
                while (stringCellValue.charAt(j) != '》') {
                    j++;
                }
                end = j;
                lawName = stringCellValue.substring(begin, end + 1);
                stringCellValue = stringCellValue.substring(end + 1);
                // System.out.println("String"+stringCellValue);
                i = 0;
            }
            if (stringCellValue.charAt(i) == '、') {
                law = lawName + stringCellValue.substring(0, i);
                stringCellValue = stringCellValue.substring(i + 1);

                i = -1;
                //System.out.println(law);

                result.add(law);
                logicService.addNode(caseId, resultId, law, 2);
            }


        }

        result.add(lawName + stringCellValue);
        logicService.addNode(caseId, resultId, lawName + stringCellValue, 2);

        return result;

    }


    @Override
    public List<Evidence_Document> importDocumentByXML(ImportXMLUtil xmlUtil) {
        //按文档顺序返回包含在文档中且具有给定标记名称的所有 Element 的 NodeList
        Node root = xmlUtil.getDocument().getElementsByTagName("documents").item(0);
        ArrayList<Evidence_Document> documentList = new ArrayList<>();
        //遍历
        NodeList list = root.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {

            //获取第i个book结点
            Node node = list.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element evi = (Element) node;
                // System.out.println(evi.getTagName());
                Evidence_Document evidence_document = new Evidence_Document();

                //获取第i个book的所有属性
                NamedNodeMap namedNodeMap = evi.getAttributes();
                //获取已知名为id的属性值
                //System.out.println(namedNodeMap.getLength());
                int id = Integer.parseInt(namedNodeMap.getNamedItem("id").getTextContent());
                int type = Integer.parseInt(namedNodeMap.getNamedItem("type").getTextContent());
                evidence_document.setId(id);
                evidence_document.setCaseID(xmlUtil.getCaseId());
                evidence_document.setType(type);

                //获取book结点的子节点,包含了Test类型的换行
                String text = evi.getElementsByTagName("text").item(0).getTextContent();
                evidence_document.setText(text);
                String committer = evi.getElementsByTagName("committer").item(0).getTextContent();
                evidence_document.setCommitter(committer);

                evidence_document = saveOrUpdate(evidence_document);

                xmlUtil.addDocument(evidence_document);
                documentList.add(evidence_document);
            }
        }

        return documentList;
    }

    @Transactional
    @Override
    public void importEviByXML(ImportXMLUtil xmlUtil) {

        deleteBodyAllByCaseId(xmlUtil.getCaseId());
        deleteHeadAllByCaseId(xmlUtil.getCaseId());
        //按文档顺序返回包含在文档中且具有给定标记名称的所有 Element 的 NodeList
        Node root = xmlUtil.getDocument().getElementsByTagName("evidences").item(0);
        ArrayList<Evidence_Body> bodyList = new ArrayList<>();
        //遍历
        NodeList list = root.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            //获取第i个book结点
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element evi = (Element) node;

                int x = Integer.valueOf(evi.getAttribute("x"));
                int y = Integer.valueOf(evi.getAttribute("y"));

                int type = Integer.valueOf(evi.getAttribute("type"));
                int trust = Integer.valueOf(evi.getAttribute("trust"));
                int isDefendant = Integer.valueOf(evi.getAttribute("isDefendant"));


                Evidence_Body evidence_body = new Evidence_Body();

                String name = evi.getElementsByTagName("name").item(0).getTextContent();
                String content = evi.getElementsByTagName("content").item(0).getTextContent();
                // String typeString=evi.getElementsByTagName("type").item(0).getTextContent();
                String committer = evi.getElementsByTagName("committer").item(0).getTextContent();
                //  String trustString=evi.getElementsByTagName("trust").item(0).getTextContent();
                String reason = evi.getElementsByTagName("reason").item(0).getTextContent();
                int logicNodeId=xmlUtil.getLogicNodeId(content);
                int documentId =xmlUtil.getDocumentId(isDefendant);
                evidence_body.setDocumentid(documentId);
                evidence_body.setLogicNodeID(logicNodeId);
                evidence_body.setTrust(trust);
                evidence_body.setType(type);
                evidence_body.setX(x);
                evidence_body.setY(y);
                evidence_body.setName(name);
                evidence_body.setBody(content);
                evidence_body.setCommitter(committer);
                evidence_body.setReason(reason);
                evidence_body.setCaseID(xmlUtil.getCaseId());
                //saveBody body
                evidence_body= saveBody(evidence_body);


                NodeList heads = evi.getElementsByTagName("heads").item(0).getChildNodes();

                for (int j = 0; j < heads.getLength(); j++) {
                    if (heads.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        Element headNode = (Element) heads.item(j);
                        int headx = Integer.valueOf(evi.getAttribute("x"));
                        int heady = Integer.valueOf(evi.getAttribute("y"));
                        String headname = evi.getElementsByTagName("name").item(0).getTextContent();
                        String headcontent = evi.getElementsByTagName("content").item(0).getTextContent();

                        Evidence_Head head = new Evidence_Head();

                        head.setDocumentid(documentId);
                        head.setHead(headcontent);
                        head.setBodyid(evidence_body.getId());
                        head.setCaseID(xmlUtil.getCaseId());
                        head.setName(headname);
                        head.setX(headx);
                        head.setY(heady);
                        System.out.println(head.toString());
                        head= saveHead(head);
                        evidence_body.addHead(head);
                    }
                }


                xmlUtil.addBody(evidence_body);

            }
        }


    }


    @Async
    @Transactional
    @Override
    public void importFactByXML(ImportXMLUtil xmlUtil) {

        modelManageService.deleteJointsByCid(xmlUtil.getCaseId());
        modelManageService.deleteFactByCid(xmlUtil.getCaseId());
        List<MOD_Fact> factList = new ArrayList<>();
        //按文档顺序返回包含在文档中且具有给定标记名称的所有 Element 的 NodeList
        Node root = xmlUtil.getDocument().getElementsByTagName("facts").item(0);
        //遍历
        NodeList list = root.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            //获取第i个book结点
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element fact = (Element) node;

                int x = Integer.valueOf(fact.getAttribute("x"));
                int y = Integer.valueOf(fact.getAttribute("y"));

                String name = fact.getElementsByTagName("name").item(0).getTextContent();
                String content = fact.getElementsByTagName("content").item(0).getTextContent();
                String type = fact.getElementsByTagName("type").item(0).getTextContent();
                int logicNodeId = xmlUtil.getLogicNodeId(content);

                MOD_Fact mod_fact = new MOD_Fact();
                mod_fact.setCaseID(xmlUtil.getCaseId());
                mod_fact.setName(name);
                mod_fact.setContent(content);
                mod_fact.setLogicNodeID(logicNodeId);
               // mod_fact.setType(type);
                mod_fact.setX(x);
                mod_fact.setY(y);

                mod_fact=modelManageService.saveFact(mod_fact);

                NodeList joints = fact.getElementsByTagName("joints").item(0).getChildNodes();

                for (int j = 0; j < joints.getLength(); j++) {
                    if (joints.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        Element headNode = (Element) joints.item(j);

                        int jointsx = Integer.valueOf(headNode.getAttribute("x"));
                        int jointsy = Integer.valueOf(headNode.getAttribute("y"));
                        String jointname = headNode.getElementsByTagName("name").item(0).getTextContent();
                        String jointcontent = headNode.getElementsByTagName("content").item(0).getTextContent();

                        MOD_Joint mod_joint = new MOD_Joint();

                        mod_joint.setFactID(mod_fact.getId());
                        mod_joint.setContent(jointcontent);
                        mod_joint.setCaseID(xmlUtil.getCaseId());
                        mod_joint.setName(jointname);
                        //      mod_joint.setType(type);

                        mod_joint.setX(jointsx);
                        mod_joint.setY(jointsy);

                        mod_joint=modelManageService.saveJoint(mod_joint);
                        mod_fact.addJoint(mod_joint);
                        System.out.println(mod_joint.toString());
                    }
                }

                xmlUtil.addFact(mod_fact);

            }
        }
    }

    @Transactional
    @Async
    @Override
    public void importArrowByXML(ImportXMLUtil xmlUtil) {

        modelManageService.deleteArrowsByCid(xmlUtil.getCaseId());

        //按文档顺序返回包含在文档中且具有给定标记名称的所有 Element 的 NodeList
        Node root = xmlUtil.getDocument().getElementsByTagName("relations").item(0);
        //遍历
        NodeList list = root.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            //获取第i个book结点
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element relation = (Element) node;

                Element joint = (Element) relation.getElementsByTagName("joint").item(0);
                String factContent = joint.getElementsByTagName("factContent").item(0).getTextContent();
                String jointContent = joint.getElementsByTagName("content").item(0).getTextContent();
                int jointId =xmlUtil.getJointId(factContent,jointContent);

                NodeList arrows = relation.getElementsByTagName("arrows").item(0).getChildNodes();

                for (int j = 0; j < arrows.getLength(); j++) {
                    if (arrows.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        Element arrow = (Element) arrows.item(j);

                        String arrowname = arrow.getElementsByTagName("name").item(0).getTextContent();
                        String arrowcontent = arrow.getElementsByTagName("content").item(0).getTextContent();
                        Element head = (Element) arrow.getElementsByTagName("head").item(0);
                        String bodyContent = head.getElementsByTagName("bodyContent").item(0).getTextContent();
                        String headContent = head.getElementsByTagName("content").item(0).getTextContent();

                        int headId =xmlUtil.getHeadId(bodyContent,headContent);
                        MOD_Arrow mod_arrow = new MOD_Arrow();

                        mod_arrow.setCaseID(xmlUtil.getCaseId());
                        mod_arrow.setName(arrowname);
                        mod_arrow.setContent(arrowcontent);
                        mod_arrow.setNodeTo_jid(jointId);
                        mod_arrow.setNodeFrom_hid(headId);
                        System.out.println(mod_arrow.toString());
                        modelManageService.saveArrow(mod_arrow);
                    }
                }


            }
        }
    }

    @Transactional
    @Override
    public void importLogicByXML(ImportXMLUtil xmlUtil) {

        logicService.deleteAllNodesByCaseID(xmlUtil.getCaseId());
        //按文档顺序返回包含在文档中且具有给定标记名称的所有 Element 的 NodeList
        Node root = xmlUtil.getDocument().getElementsByTagName("graph").item(0);

        //递归遍历
        NodeList list = root.getChildNodes();
        saveAndgetChildren(-1,list,xmlUtil);

    }

    private int getTypeByString(String typeString) {
        if (typeString.equals("证据")) {
            return 0;
        }

        if (typeString.equals("事实")) {
            return 1;
        }

        if (typeString.equals("法条")) {
            return 2;
        }

        if (typeString.equals("结论")) {
            return 3;
        }

        return -1;
    }



    /**
     * 递归遍历graph节点
     * @param nodelist
     * @param xmlUtil
     * @return
     */
    private NodeList saveAndgetChildren(int parentNodeId,NodeList nodelist,ImportXMLUtil xmlUtil) {
        NodeList list=null;
        for(int i=0;i<nodelist.getLength();i++) {
            Node node = nodelist.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                //  Element element = (Element) node;
                NamedNodeMap nodeMap = node.getAttributes();
                String topic = nodeMap.getNamedItem("topic").getTextContent();
                int nodeId = Integer.valueOf(nodeMap.getNamedItem("nodeId").getTextContent());
                String detail = nodeMap.getNamedItem("detail").getTextContent();
                String typeString = nodeMap.getNamedItem("type").getTextContent();
                int type = getTypeByString(typeString);
                int x = Integer.valueOf(nodeMap.getNamedItem("x").getTextContent());
                int y = Integer.valueOf(nodeMap.getNamedItem("y").getTextContent());
                LogicNode logicNode = new LogicNode();
                logicNode.setDetail(detail);
                logicNode.setTopic(topic);
                logicNode.setType(type);
                logicNode.setNodeID(nodeId);
                logicNode.setX(x);
                logicNode.setY(y);
                logicNode.setCaseID(xmlUtil.getCaseId());
                logicNode.setParentNodeID(parentNodeId);
                System.out.println(logicNode.toString());
                logicNode=logicService.saveNode(logicNode);
                xmlUtil.addLogicNode(logicNode);
                list = saveAndgetChildren(nodeId, node.getChildNodes(),xmlUtil);
            }
        }
        return  list;

    }

}
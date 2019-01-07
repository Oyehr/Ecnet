package com.ecm.controller;

import com.ecm.keyword.manager.TypeCalculator;
import com.ecm.keyword.model.SplitType;
import com.ecm.keyword.reader.FileUtil;
import com.ecm.model.Evidence_Body;
import com.ecm.model.Evidence_Document;

import com.ecm.model.Evidence_Head;
import com.ecm.service.EvidenceService;
import com.ecm.service.LogicService;
import com.ecm.service.ModelManageService;
import com.ecm.util.ImportXMLUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping(value="/evidence")
public class EvidenceController {

    @Autowired
    private EvidenceService evidenceService;
    @Autowired
    private ModelManageService modelManageService;
    @Autowired
    private LogicService logicService;

    @PostMapping(value = "/document")
    public JSONArray save(@RequestParam("ajxh") int ajxh, @RequestParam("type") int type, @RequestParam("text") String text){
        JSONArray res = new JSONArray();
        TypeCalculator typeCalculator=new TypeCalculator();
      Evidence_Document evidence_document=new Evidence_Document();
      evidence_document.setCaseID(ajxh);
      evidence_document.setText(text);
      evidence_document.setType(type);

      evidence_document=evidenceService.saveOrUpdate(evidence_document);

      modelManageService.deleteByDocumentID(evidence_document.getId());
      evidenceService.deleteBodyAllByDocuId(evidence_document.getId());


     // String test="1、test1。2、test2。3、test3";
      String[] tests=text.split(SplitType.getType(text).getRegex());
      for(String str:tests){
            if(!str.isEmpty()){
                int logicNodeId=logicService.addEvidenceOrFactNode(ajxh,str,0);
                Evidence_Body evidence_body=new Evidence_Body();
                evidence_body.setCaseID(ajxh);
                evidence_body.setDocumentid(evidence_document.getId());
                evidence_body.setBody(str);
                evidence_body.setType(typeCalculator.calType(str));
                evidence_body.setLogicNodeID(logicNodeId);
                evidence_body=evidenceService.saveBody(evidence_body);


                JSONObject jsonObject = new JSONObject();

                jsonObject.put("id",evidence_body.getId());
                jsonObject.put("document_id",evidence_body.getDocumentid());
                jsonObject.put("body",evidence_body.getBody());
                jsonObject.put("type",evidence_body.getType());

                res.add(jsonObject);
            }

        }


        return res;

    }

    @PostMapping(value = "/deleteBody")
    public int deleteBody(@RequestParam("id") int id){
       // System.out.println(id);

        logicService.deleteNode(evidenceService.findLogicId(id));
        evidenceService.deleteBodyById(id);
        evidenceService.deleteHeadAllByBody(id);
        return 0;
    }


    @PostMapping(value = "/addBody")
    public Evidence_Body addBody(@RequestParam("ajxh") int ajxh, @RequestParam("type") int type, @RequestParam("body") String body,@RequestParam("document_id") int document_id){

        int logicNodeId=logicService.addEvidenceOrFactNode(ajxh,body,0);
        Evidence_Body evidence_body=new Evidence_Body();
        evidence_body.setCaseID(ajxh);
        evidence_body.setDocumentid(document_id);
        evidence_body.setType(type);
        evidence_body.setBody(body);
        evidence_body.setLogicNodeID(logicNodeId);
        evidenceService.saveBody(evidence_body);

        return evidence_body;
    }
    @PostMapping(value = "/updateBodyById")
    public void updateBodyById(@RequestParam("id") int id,@RequestParam("body") String body){

        logicService.modEvidenceOrFactNode(evidenceService.findLogicId(id),body);
         evidenceService.updateBodyById(body,id);

    }
    @PostMapping(value = "/updateTypeById")
    public void updateTypeById(@RequestParam("id") int id,@RequestParam("type") int type){
        evidenceService.updateTypeById(type,id);

    }

    @PostMapping(value = "/updateTrustById")
    public void updateTrustById(@RequestParam("id") int id,@RequestParam("trust") int trust){
        evidenceService.updateTrustById(trust,id);

    }


    @RequestMapping(value="/getContent")
    public JSONArray getAllCases(@RequestParam("ajxh") int ajxh){
        JSONArray res=new JSONArray();
        for(int i=0;i<=1;i++) {
            Evidence_Document evidence_document = evidenceService.findDocuByAjxhAndType(ajxh, i);
            if (evidence_document != null) {
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("id", evidence_document.getId());
                jsonObject.put("text", evidence_document.getText());
                jsonObject.put("type", evidence_document.getType());
                List<Evidence_Body> evidence_bodies = evidenceService.findBodyByDocu(evidence_document.getId());
                JSONArray bodylist = new JSONArray();
                for (Evidence_Body evidence_body : evidence_bodies) {
                    evidence_body.setHeadList(evidenceService.findHeadByBody(evidence_body.getId()));
                    JSONObject temp = new JSONObject();
                    temp.put("id", evidence_body.getId());
                    temp.put("body", evidence_body.getBody());
                    temp.put("type", evidence_body.getType());
                    temp.put("trust", evidence_body.getTrust());
                    temp.put("headList", evidence_body.getHeadList());
                    bodylist.add(temp);
                }
                jsonObject.put("bodylist", bodylist);
                res.add(jsonObject);
            }
        }
            return res;

    }


    @PostMapping(value = "/createHead")
    public List<Evidence_Body> createHead(@RequestParam("document_id") int document_id){
        List<Evidence_Body> headList=evidenceService.createHead(document_id);
        return headList;
    }

    @PostMapping(value = "/deleteHead")
    public void deleteHead(@RequestParam("id") int id){
            evidenceService.deleteHeadById(id);
    }

    @PostMapping(value = "/addHead")
    public Evidence_Head addHead(@RequestParam("head") String head,@RequestParam("document_id") int document_id,@RequestParam("ajxh") int ajxh,@RequestParam("body_id") int bodyid){

        Evidence_Head evidence_head=new Evidence_Head();
        evidence_head.setHead(head);
        evidence_head.setDocumentid(document_id);
        evidence_head.setCaseID(ajxh);
        evidence_head.setBodyid(bodyid);
        evidence_head=evidenceService.saveHead(evidence_head);
        return evidence_head;
    }
    @PostMapping(value = "/updateHead")
    public void updateHeadById(@RequestParam("id") int id,@RequestParam("head") String head){
        evidenceService.updateHeadById(head,id);
    }


    @PostMapping(value = "/importExcel")
    public void importExcel(@RequestParam("file") MultipartFile file,@RequestParam("ajxh") int ajxh,HttpServletResponse response) throws IOException {
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        /*System.out.println("fileName-->" + fileName);
        System.out.println("getContentType-->" + contentType);*/
         String sepa = java.io.File.separator;
        String filePath = System.getProperty("user.dir")+sepa+"src"+sepa+"main"+sepa+"resources"+sepa+"upload"+sepa;
        try {
            FileUtil.uploadFile(file.getBytes(), filePath, fileName);
        } catch (Exception e) {
            // TODO: handle exception
        }

        if(fileName.contains("xml")){

            ImportXMLUtil xmlUtil=new ImportXMLUtil(filePath+sepa+fileName,ajxh);

            evidenceService.importDocumentByXML(xmlUtil);
            evidenceService.importLogicByXML(xmlUtil);
            evidenceService.importEviByXML(xmlUtil);
            evidenceService.importFactByXML(xmlUtil);
            evidenceService.importArrowByXML(xmlUtil);

        }
        if(fileName.contains("xls")){

            List<Evidence_Document> documentList=evidenceService.importDocumentByExcel(filePath+sepa+fileName,ajxh);
            List<Evidence_Body> bodylist=evidenceService.importEviByExcel(filePath+sepa+fileName,ajxh,documentList);
            evidenceService.importFactByExcel(filePath+sepa+fileName,ajxh,bodylist);
            evidenceService.importLogicByExcel(filePath+sepa+fileName,ajxh,bodylist);
        }
        response.sendRedirect("/upload");

    }


}

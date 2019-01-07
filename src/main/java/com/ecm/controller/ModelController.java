package com.ecm.controller;

import com.ecm.keyword.model.SplitType_Fact;
import com.ecm.model.*;
import com.ecm.service.LogicService;
import com.ecm.service.ModelManageService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value="/model")
public class ModelController {
    @Autowired
    private ModelManageService modelManageService;
    @Autowired
    private LogicService logicService;

    @RequestMapping(value="/getEvidences")
    public JSONObject getEvidences(@RequestParam("cid") int cid){

        return modelManageService.getEvidences(cid);
    }

    @RequestMapping(value="/getHeaders")
    public JSONObject getHeaders(@RequestParam("bid") int bid){

        return modelManageService.getHeadersByBodyID(bid);
    }

    @RequestMapping(value="/saveHead")
    public int saveHead(@RequestBody Evidence_Head head){

        return modelManageService.saveHeader(head).getId();
    }

    @RequestMapping(value="/deleteHead")
    public void deleteHead(@RequestParam("id") int id){

        modelManageService.deleteHeaderById(id);
    }

    @RequestMapping(value="/saveHeaders")
    public void saveHeaders(@RequestBody List<Evidence_Head> heads){

        modelManageService.saveHeaders(heads);
    }

    @RequestMapping(value="/saveBody")
    public Evidence_Body saveBody(@RequestBody Evidence_Body body){

        if(body.getLogicNodeID()>=0){
            logicService.modEvidenceOrFactNode(body.getLogicNodeID(),body.getBody());
        }else{
            int lid = logicService.addEvidenceOrFactNode(body.getCaseID(),body.getBody(),0);
            body.setLogicNodeID(lid);
        }

        return modelManageService.saveBody(body);
    }

    @RequestMapping(value="/deleteBody")
    public void deleteBody(@RequestParam("id") int id){

        int lid = modelManageService.getLogicNodeIDofBody(id);
        if(lid>=0)
            logicService.deleteNode(lid);
        modelManageService.deleteBodyById(id);
    }

    @RequestMapping(value="/updateBodyTrust")
    public void updateBodyTrust(@RequestParam("bid") int bid){

       modelManageService.updateBodyTrustById(bid);
    }

    @RequestMapping(value="/saveJoint")
    public int saveJoint(@RequestBody MOD_Joint joint){
        return modelManageService.saveJoint(joint).getId();
    }

    @RequestMapping(value="/deleteJoint")
    public void deleteJoint(@RequestParam("id") int id){

        modelManageService.deleteJointById(id);
    }

    @RequestMapping(value="/saveFact")
    public MOD_Fact saveFact(@RequestBody MOD_Fact fact){

        if(fact.getLogicNodeID()>=0){
            logicService.modEvidenceOrFactNode(fact.getLogicNodeID(),fact.getContent());
        }else{
            int lid = logicService.addEvidenceOrFactNode(fact.getCaseID(),fact.getContent(),1);
            fact.setLogicNodeID(lid);
        }
        return modelManageService.saveFact(fact);
    }

    @RequestMapping(value="/deleteFact")
    public void deleteFact(@RequestParam("id") int id){

        MOD_Fact fact = modelManageService.getFactByID(id);
        if(fact!=null){
            if(fact.getLogicNodeID()>=0){
                logicService.deleteNode(fact.getLogicNodeID());
            }
        }
        modelManageService.deleteFactById(id);

    }

    @RequestMapping(value="/saveAll")
    public void saveAll(@RequestBody Evidence_Data all){

        modelManageService.saveHeaders(all.getHeaders());
        modelManageService.saveBodies(all.getBodies());
        modelManageService.saveJoints(all.getJoints());
        modelManageService.saveFacts(all.getFacts());
        modelManageService.deleteArrowsByCid(all.getCaseID());
        modelManageService.saveArrows(all.getArrows());
        saveInLogic(all.getLinks(),all.getCaseID());
    }

    public void saveInLogic(HashMap<Integer,List<Integer>> list, int cid){
        logicService.deleteAllLinksBetweenEvidenceAndFactNode(cid);
        modelManageService.saveLogicLinks(list,cid);
    }

    @RequestMapping(value="/exportExcel")
    public ResponseEntity<InputStreamResource> exportExcel(HttpServletRequest request)
            throws IOException {
        String filePath = System.getProperty("user.dir")+"\\src\\main\\resources\\download\\证据链.xls";
        int cid = Integer.parseInt(request.getParameter("cid"));
        modelManageService.writeToExcel(cid,filePath);

        return exportFile(filePath);
    }

    @RequestMapping(value="/exportXML")
    public ResponseEntity<InputStreamResource> exportXML(HttpServletRequest request)
            throws IOException {
        String filePath = System.getProperty("user.dir")+"\\src\\main\\resources\\download\\证据链.xml";
        int cid = Integer.parseInt(request.getParameter("cid"));
        modelManageService.writeToXMLBySchema(cid,filePath);
        return exportFile(filePath);
    }

    private ResponseEntity<InputStreamResource> exportFile(String filePath)
            throws IOException{
        FileSystemResource fileSystemResource = new FileSystemResource(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", new String( fileSystemResource.getFilename().getBytes("utf-8"), "ISO8859-1" )));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(fileSystemResource.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(fileSystemResource.getInputStream()));
    }

    @RequestMapping(value="/splitFact")
    public List<MOD_Fact> splitFact(@RequestParam("caseID") int caseID,@RequestParam("text") String text){
        MOD_Fact_Doc factDoc = new MOD_Fact_Doc();
        factDoc.setCaseID(caseID);
        factDoc.setText(text);
        factDoc = modelManageService.saveFactDoc(factDoc);
        int factDocID = factDoc.getId();

        List<MOD_Fact> facts = new ArrayList<MOD_Fact>();

        int index = 0;
        String[] tests=text.split(SplitType_Fact.getType(text).getRegex());
        for(String str:tests) {
            if (!str.isEmpty()) {
                MOD_Fact fact = new MOD_Fact();
                fact.setId(index);
                fact.setCaseID(caseID);
                fact.setContent(str);
                fact.setTextID(factDocID);
                facts.add(fact);
                index++;
            }
        }
        return facts;
    }

    @RequestMapping(value="/extractJoints")
    public JSONObject extractJoints(@RequestBody JSONObject data){

        return modelManageService.getFactLinkpoints(data.getInt("caseID"),data.getJSONArray("facts"),data.getJSONArray("bodies"));
    }
}

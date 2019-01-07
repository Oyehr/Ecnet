package com.ecm.controller;

import com.ecm.service.CaseManageService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/case")
public class CaseController {

    @Autowired
    private CaseManageService caseManageService;

    @RequestMapping(value="/getAll")
    public JSONArray getAllCases(@RequestParam("username") String username){
        return caseManageService.getAllCases(username);
    }
}

package com.ecm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

    @RequestMapping(value = "/login")
    public String getLoginView() {
        return "login";
    }

    @RequestMapping(value = "/manage")
    public String getManageView() {
        return "manage";
    }

    @RequestMapping(value = "/index")
    public String getIndexView() {
        return "index";
    }

    @RequestMapping(value = "/logic")
    public String getLogicView() {
        return "logic";
    }

    @RequestMapping(value = "/logic-new")
    public String getLogicNewView() {
        return "logic-new";
    }

    @RequestMapping(value = "/new")
    public String getNewView() {
        return "new";
    }

    @RequestMapping(value = "/model")
    public String getModelView() {
        return "model";
    }
    @RequestMapping(value = "/upload")

    public String getUploadView() {
        return "upload";
    }

    @RequestMapping(value = "/reason-evaluate")
    public String getCaseDecideView() {
        return "reason-evaluate";
    }

}

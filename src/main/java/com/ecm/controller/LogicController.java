package com.ecm.controller;

import com.ecm.model.LogicNode;
import com.ecm.service.LogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * Created by deng on 2018/3/31.
 */
@RestController
@RequestMapping(value = "/logic")
public class LogicController {
    @Autowired
    private LogicService logicService;

    @RequestMapping(value = "getAll")
    public List<LogicNode> getAll(@RequestParam("caseID") int caseID) {
        return logicService.getAllNodesByCaseID(caseID);
    }

    @PostMapping(value = "saveAll")
    public void saveAll(@RequestParam("caseID") int caseID, @RequestBody List<LogicNode> logicNodes) {
        logicService.saveAllNodesInSameCase(caseID, logicNodes);
    }

    @RequestMapping(value = "generateXML")
    @ResponseBody
    public String generateXML(HttpServletResponse response, @RequestParam("caseID") int caseID) {
        String completePath = logicService.generateXMLFile(caseID);
        File file = new File(completePath);
        if (file.exists()) {
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            response.addHeader("Content-Disposition",
                    "attachment;fileName=" + file.getName());// 设置文件名
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                os.flush();
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            } finally {
                try {
                    if (bis != null) {
                        bis.close();
                    }
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {
                    return "failed:" + e.getMessage();
                }
            }
        }
        return "";
    }

    @RequestMapping(value = "generateExcel")
    @ResponseBody
    public String generateExcel(HttpServletResponse response, @RequestParam("caseID") int caseID) {
        String completePath = logicService.generateExcelFile(caseID);
        File file = new File(completePath);
        if (file.exists()) {
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            response.addHeader("Content-Disposition",
                    "attachment;fileName=" + file.getName());// 设置文件名
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                os.flush();
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            } finally {
                try {
                    if (bis != null) {
                        bis.close();
                    }
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {
                    return "failed:" + e.getMessage();
                }
            }
        }
        return "";
    }
}

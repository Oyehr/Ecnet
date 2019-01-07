package com.ecm.controller;

import com.ecm.service.EvaluateService;
import com.ecm.util.FileUtil;
import com.ecm.vo.BundleDocumentEvaluateResult;
import com.ecm.vo.SingleDocumentBasicInfo;
import com.ecm.vo.SingleDocumentEvaluateResult;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/evaluate")
public class EvaluateController {

    @Autowired
    private EvaluateService evaluateService;

    /**上传XML文件
     *
     * @param request HTTP请求
     * @param wsFile 上传的文件
     * @return 上传结果
     */
    @RequestMapping(value="/uploadXML",method= RequestMethod.POST)
    public String uploadXML(HttpServletRequest request, @RequestParam("ws-file") MultipartFile wsFile) {
        if(!wsFile.isEmpty()) {  //文件不为空
            if(FileUtil.checkFileIsCertainType(wsFile.getOriginalFilename(), "xml")) {
                //文件是XML文件，写入到上传路径
                String sepa = File.separator;
                String filePath = System.getProperty("user.dir")+sepa+"src"+sepa+"main"+sepa+"resources"+sepa+"upload"+sepa; //上传文件路径
                String fileName = "uploadedWS.xml";  //上传文件名

                if(FileUtil.uploadFile(filePath, fileName, wsFile) == true) {
                    return "success";
                }
                else{
                    return "fail";
                }
            }
            else{ //文件非XML文件，返回错误信息
                return "type error";
            }
        }
        else { //文件为空，返回错误信息
            return "empty file";
        }
    }

    /**上传RAR文件
     *
     * @param request HTTP请求
     * @param wsFile 上传的文件
     * @return 上传结果
     */
    @RequestMapping(value="/uploadRAR",method= RequestMethod.POST)
    public String uploadRAR(HttpServletRequest request, @RequestParam("ws-file") MultipartFile wsFile) {
        if(!wsFile.isEmpty()) {  //文件不为空
            if(FileUtil.checkFileIsCertainType(wsFile.getOriginalFilename(), "rar")) {
                //文件是RAR文件，写入到上传路径
                String sepa = File.separator;
                String filePath = System.getProperty("user.dir")+sepa+"src"+sepa+"main"+sepa+"resources"+sepa+"upload"+sepa; //上传文件路径
                String fileName = "uploadedWSList.rar";  //上传文件名

                if(FileUtil.uploadFile(filePath, fileName, wsFile) == true) {
                    return "success";
                }
                else{
                    return "fail";
                }
            }
            else{ //文件非XML文件，返回错误信息
                return "type error";
            }
        }
        else { //文件为空，返回错误信息
            return "empty file";
        }
    }

    /**上传ZIP文件
     *
     * @param request HTTP请求
     * @param wsFile 上传的文件
     * @return 上传结果
     */
    @RequestMapping(value="/uploadZIP",method= RequestMethod.POST)
    public String uploadZIP(HttpServletRequest request, @RequestParam("ws-file") MultipartFile wsFile) {
        if(!wsFile.isEmpty()) {  //文件不为空
            if(FileUtil.checkFileIsCertainType(wsFile.getOriginalFilename(), "zip")) {
                //文件是ZIP文件，写入到上传路径
                String sepa = File.separator;
                String filePath = System.getProperty("user.dir")+sepa+"src"+sepa+"main"+sepa+"resources"+sepa+"upload"+sepa; //上传文件路径
                String fileName = "uploadedWSList.zip";  //上传文件名

                if(FileUtil.uploadFile(filePath, fileName, wsFile) == true) {
                    return "success";
                }
                else{
                    return "fail";
                }
            }
            else{ //文件非XML文件，返回错误信息
                return "type error";
            }
        }
        else { //文件为空，返回错误信息
            return "empty file";
        }
    }

    /**导出单个文书评估的结果到Excel文件中
     *
     * @param request HTTP请求
     * @param evaluateType 评估类型
     * @param basicInfo 单个文书的基本信息
     * @param evaluateResult 单个文书的评估信息
     * @return 导出结果
     */
    @RequestMapping(value="/singleExport",method= RequestMethod.POST)
    public String exportSingleResultToExcel(HttpServletRequest request, @RequestParam("evaluate-type") String evaluateType, @RequestParam("basic-info") String basicInfo, @RequestParam("evaluate-result") String evaluateResult) {
        System.out.println(evaluateType);
        System.out.println(basicInfo);
        System.out.println(evaluateResult);
        String sepa = File.separator;
        String exportExcelPath = System.getProperty("user.dir") + sepa + "src" + sepa + "main" + sepa + "resources" + sepa + "download";  //导出的评估结果Excel文件所在的目录路径
        return evaluateService.exportToExcel(exportExcelPath, evaluateType, basicInfo, evaluateResult);
    }

    /**导出批量文书评估的结果到Excel文件中
     *
     * @param request HTTP请求
     * @param evaluateType 评估类型
     * @param evaluateResult 批量文书的评估信息
     * @return 导出结果
     */
    @RequestMapping(value="/bundleExport",method= RequestMethod.POST)
    public String exportBundleResultToExcel(HttpServletRequest request, @RequestParam("evaluate-type") String evaluateType, @RequestParam("evaluate-result") String evaluateResult) {
        System.out.println(evaluateType);
        System.out.println(evaluateResult);
        String sepa = File.separator;
        String exportExcelPath = System.getProperty("user.dir") + sepa + "src" + sepa + "main" + sepa + "resources" + sepa + "download";  //导出的评估结果Excel文件所在的目录路径
        return evaluateService.exportToExcel(exportExcelPath, evaluateType, "", evaluateResult);
    }

    /**获取单个文书中的基本信息
     *
     * @param request HTTP请求
     * @return 单个文书中的基本信息
     */
    @RequestMapping(value="/getBasicInfo",method= RequestMethod.GET)
    public JSONObject getBasicInfo(HttpServletRequest request) {
        String sepa = File.separator;
        String filePath = System.getProperty("user.dir") + sepa + "src" + sepa + "main" + sepa + "resources" + sepa + "upload" + sepa; //要解析的文件路径
        String fileName = "uploadedWS.xml";  //要解析的文件名
        String fileFullName = filePath + fileName;

        SingleDocumentBasicInfo basicInfo = evaluateService.getSingleCaseBasicInfo(fileFullName);
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("caseId", basicInfo.getCaseId());
        jsonObj.put("judgeName", basicInfo.getJudgeName());
        jsonObj.put("judgeTime", basicInfo.getJudgeTime());
        jsonObj.put("caseName", basicInfo.getCaseName());
        jsonObj.put("caseReason", basicInfo.getCaseReason());
        return jsonObj;
    }

    /**评估单个文书
     *
     * @param request HTTP请求
     * @param evaluateType 评估类型：f2l表示factToLaw，事实到法条; l2f表示lawToFact，法条到事实; l2j表示lawToJudgement，法条到结论；j2l表示judgementToLaw，结论到法条
     * @return 单个文书中的评估信息
     */
    @RequestMapping(value="/singleEvaluate",method= RequestMethod.GET)
    public JSONArray evaluate(HttpServletRequest request, @RequestParam("evaluate-type") String evaluateType) {
        String sepa = File.separator;
        String filePath = System.getProperty("user.dir") + sepa + "src" + sepa + "main" + sepa + "resources" + sepa + "upload" + sepa; //要解析的文件路径
        String fileName = "uploadedWS.xml";  //要解析的文件名
        String fileFullName = filePath + fileName; //要解析的文件全名（路径+文件名）

        JSONArray jsonArr = new JSONArray();
        List<SingleDocumentEvaluateResult> resultList = evaluateService.singleEvaluate(fileFullName, evaluateType);
        for(int i=0; i<resultList.size(); i++) {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("fojNo", resultList.get(i).getFojNo());
            jsonObj.put("fojContent", resultList.get(i).getFojContent());
            jsonObj.put("lawName", resultList.get(i).getLawName());
            jsonObj.put("lawContent", resultList.get(i).getLawContent());
            //jsonObj.put("relevancy", resultList.get(i).getRelevancy());
            if (!resultList.get(i).getRelevancy().equals("无对应内容")){
                jsonObj.put("relevancy", "");
            }
            else{
                jsonObj.put("relevancy", resultList.get(i).getRelevancy());
            }
            jsonArr.add(jsonObj);
        }
        return jsonArr;
    }

    /**评估批量文书
     *
     * @param request HTTP请求
     * @param evaluateType 评估类型：bf2l表示bundleFactToLaw，批量的事实到法条; bl2f表示bundleLawToFact，批量的法条到事实; bl2j表示bundleLawToJudgement，批量的法条到结论；bj2l表示bundleJudgementToLaw，批量的结论到法条
     * @param lastUploadFileType 上次上传的文件类型（因为批量文书的类型为RAR或ZIP格式）
     * @return 单个文书中的评估信息
     */
    @RequestMapping(value="/bundleEvaluate",method= RequestMethod.GET)
    public JSONArray bundleEvaluate(HttpServletRequest request, @RequestParam("evaluate-type") String evaluateType, @RequestParam("last-upload-file-type") String lastUploadFileType) {
        String sepa = File.separator;
        String filePath = System.getProperty("user.dir") + sepa + "src" + sepa + "main" + sepa + "resources" + sepa + "upload" + sepa; //要解析的文件路径
        String fileName = "uploadedWSList."+lastUploadFileType.toLowerCase();  //要解析的文件名，根据上次上传文书的文件类型得出
        String fileFullName = filePath + fileName; //要解析的文件全名（路径+文件名）
        String unZRDirectoryPath = System.getProperty("user.dir") + sepa + "src" + sepa + "main" + sepa + "resources" + sepa + "upload" + sepa + lastUploadFileType.toLowerCase() + "Dir" + sepa;  //zip或者rar文件解压后的目录名，根据上次上传文书的文件类型得出

        boolean clearDirectoryResult = FileUtil.deletefile(unZRDirectoryPath); //先清空目标文件夹中的内容
        if(clearDirectoryResult) { //清空成功，解压文件至该文件夹中
            boolean unZRResult = FileUtil.unzipFile(fileFullName, unZRDirectoryPath); //p.s.这里无论ZIP文件还是RAR文件都统一用unzip方法解压缩，因为unrar方法会抛出异常
            if (unZRResult) { //解压成功，对该文件夹中的每一个文件，解析评估（这里只对该文件夹中的每个文件做处理，不考虑该文件夹中子文件夹的情况）
                System.out.println("UNZIP success");
                JSONArray jsonArr = new JSONArray();
                List<BundleDocumentEvaluateResult> resultList = evaluateService.bundleEvaluate(unZRDirectoryPath, evaluateType);
                System.out.println(resultList.size());
                for(int i=0; i<resultList.size(); i++){
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("caseId", resultList.get(i).getCaseId());
                    jsonObj.put("caseName", resultList.get(i).getCaseName());
                    jsonObj.put("caseReason", resultList.get(i).getCaseReason());
                    jsonObj.put("judgeName", resultList.get(i).getJudgeName());
                    jsonObj.put("noReferenceSource", resultList.get(i).getNoReferenceSource());
                    jsonArr.add(jsonObj);
                }
                return jsonArr;
            }
        }
        return null;
    }

}

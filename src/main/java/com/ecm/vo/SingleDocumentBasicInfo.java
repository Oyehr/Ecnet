package com.ecm.vo;

public class SingleDocumentBasicInfo {

    private String caseId;    //案号
    private String judgeName; //审判法官
    private String judgeTime; //判决日期
    private String caseName;  //案件名称
    private String caseReason;//案由

    public SingleDocumentBasicInfo(){
        caseId = null;
        judgeName = null;
        judgeTime = null;
        caseName = null;
        caseReason = null;
    }

    public SingleDocumentBasicInfo(String caseId, String judgeName, String judgeTime, String caseName, String caseReason){
        this.caseId = caseId;
        this.judgeName = judgeName;
        this.judgeTime = judgeTime;
        this.caseName = caseName;
        this.caseReason = caseReason;
    }

    public String getCaseId() { return caseId; }
    public void setCaseId(String caseId) { this.caseId = caseId; }

    public String getJudgeName() { return judgeName; }
    public void setJudgeName(String judgeName) { this.judgeName = judgeName; }

    public String getJudgeTime() { return judgeTime; }
    public void setJudgeTime(String judgeTime) { this.judgeTime = judgeTime; }

    public String getCaseName() { return caseName; }
    public void setCaseName(String caseName) { this.caseName = caseName; }

    public String getCaseReason() { return caseReason; }
    public void setCaseReason(String caseReason) { this.caseReason = caseReason; }

}

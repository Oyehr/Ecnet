package com.ecm.vo;

public class BundleDocumentEvaluateResult {

    private String caseId;    //案号
    private String caseName;  //案件名称
    private String caseReason;//案由
    private String judgeName; //承办人
    private String noReferenceSource; //没有引用的来源。如：没有法条的事实，那么法条就是引用（Reference），事实就是（Source）

    public BundleDocumentEvaluateResult(){
        caseId = null;
        caseName = null;
        caseReason = null;
        judgeName = null;
        noReferenceSource = null;
    }

    public BundleDocumentEvaluateResult(String caseId, String caseName, String caseReason, String judgeName, String noReferenceSource){
        this.caseId = caseId;
        this.caseName = caseName;
        this.caseReason = caseReason;
        this.judgeName = judgeName;
        this.noReferenceSource = noReferenceSource;
    }

    public String getCaseId() { return caseId; }
    public void setCaseId(String caseId) { this.caseId = caseId; }

    public String getCaseName() { return caseName; }
    public void setCaseName(String caseName) { this.caseName = caseName; }

    public String getCaseReason() { return caseReason; }
    public void setCaseReason(String caseReason) { this.caseReason = caseReason; }

    public String getJudgeName() { return judgeName; }
    public void setJudgeName(String judgeName) { this.judgeName = judgeName; }

    public String getNoReferenceSource() { return noReferenceSource; }
    public void setNoReferenceSource(String noReferenceSource) { this.noReferenceSource = noReferenceSource; }

}

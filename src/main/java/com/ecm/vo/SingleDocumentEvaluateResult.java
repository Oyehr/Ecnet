package com.ecm.vo;

public class SingleDocumentEvaluateResult {

    private String fojNo;      //事实或结论序号
    private String fojContent; //事实或结论内容
    private String lawName;    //法条名称
    private String lawContent; //法条内容
    private String relevancy;  //来源和参考的相关性

    public SingleDocumentEvaluateResult() {
        fojNo = null;
        fojContent = null;
        lawName = null;
        lawContent = null;
        relevancy = "-1";
    }
    public SingleDocumentEvaluateResult(String fojNo, String fojContent, String lawName, String lawContent, String relevancy) {
        this.fojNo = fojNo;
        this.fojContent = fojContent;
        this.lawName = lawName;
        this.lawContent = lawContent;
        this.relevancy = relevancy;
    }

    public String getFojNo() { return fojNo; }
    public void setFojNo(String fojNo) { this.fojNo = fojNo; }

    public String getFojContent() { return fojContent; }
    public void setFojContent(String fojContent) { this.fojContent = fojContent; }

    public String getLawName() {
        return lawName;
    }
    public void setLawName(String lawName) {
        this.lawName = lawName;
    }

    public String getLawContent() { return lawContent; }
    public void setLawContent(String lawContent) { this.lawContent = lawContent; }

    public String getRelevancy() {
        return relevancy;
    }
    public void setRelevancy(String relevancy) {
        this.relevancy = relevancy;
    }

}

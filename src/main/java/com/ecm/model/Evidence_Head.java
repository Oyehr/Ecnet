package com.ecm.model;

import javax.persistence.*;

@Entity
@Table(name="EVIDENCE_HEAD")
public class Evidence_Head {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ajxh")
    private int caseID;
    @Column(name = "document_id")
    private int documentid;
    @Column(name = "body_id")
    private int bodyid = -1;
    @Column(name = "head")
    private String head;
    private String name;
    private String keyText;
    private int x = -1;
    private int y = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCaseID() {
        return caseID;
    }

    public void setCaseID(int caseID) {
        this.caseID = caseID;
    }

    public int getDocumentid() {
        return documentid;
    }

    public void setDocumentid(int documentid) {
        this.documentid = documentid;
    }

    public int getBodyid() {
        return bodyid;
    }

    public void setBodyid(int bodyid) {
        this.bodyid = bodyid;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyText() {
        return keyText;
    }

    public void setKeyText(String keyText) {
        this.keyText = keyText;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Evidence_Head{" +
                "id=" + id +
                ", caseID=" + caseID +
                ", documentid=" + documentid +
                ", bodyid=" + bodyid +
                ", head='" + head + '\'' +
                ", name='" + name + '\'' +
                ", keyText='" + keyText + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}

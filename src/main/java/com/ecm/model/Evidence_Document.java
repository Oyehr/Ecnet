package com.ecm.model;

import javax.persistence.*;
@Entity
@Table(name="EVIDENCE_DOCUMENT")
public class Evidence_Document {

        @Id
        @GeneratedValue(strategy=GenerationType.IDENTITY)
        @Column(name = "id")
        private int id;
        @Column(name = "ajxh")
        private int caseID;
        @Column(name = "committer")
        private String committer;
        @Column(name = "text")
        private String text;
        @Column(name = "type")
        private int type;//0-原告证据   1-被告证据

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

    public String getCommitter() {
        return committer;
    }

    public void setCommitter(String committer) {
        this.committer = committer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Evidence_Document{" +
                "id=" + id +
                ", caseID=" + caseID +
                ", committer='" + committer + '\'' +
                ", text='" + text + '\'' +
                ", type=" + type +
                '}';
    }
}

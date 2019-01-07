package com.ecm.model;

import javax.persistence.*;

@Entity
@Table(name="MOD_FACT_DOC")
public class MOD_Fact_Doc {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private int caseID;
    private String text;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

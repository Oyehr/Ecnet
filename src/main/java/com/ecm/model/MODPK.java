package com.ecm.model;

import java.io.Serializable;

public class MODPK implements Serializable {

    private int id;
    private int caseID;

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
}

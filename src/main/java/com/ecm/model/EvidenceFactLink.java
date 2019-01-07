package com.ecm.model;

import javax.persistence.*;

/**
 * Created by deng on 2018/4/3.
 */
@Entity
@Table(name = "evidence_fact_link")
public class EvidenceFactLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int caseID;
    private int initEviID; // 原始证据节点id
    private int copyEviID; // 复制的证据节点id
    private int factID; // 证据节点对应的事实节点id

    public EvidenceFactLink() {
    }

    public EvidenceFactLink(int caseID, int initEviID, int copyEviID, int factID) {
        this.caseID = caseID;
        this.initEviID = initEviID;
        this.copyEviID = copyEviID;
        this.factID = factID;
    }

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

    public int getInitEviID() {
        return initEviID;
    }

    public void setInitEviID(int initEviID) {
        this.initEviID = initEviID;
    }

    public int getCopyEviID() {
        return copyEviID;
    }

    public void setCopyEviID(int copyEviID) {
        this.copyEviID = copyEviID;
    }

    public int getFactID() {
        return factID;
    }

    public void setFactID(int factID) {
        this.factID = factID;
    }
}

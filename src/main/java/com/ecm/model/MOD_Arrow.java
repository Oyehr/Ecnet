package com.ecm.model;

import javax.persistence.*;

@Entity
@Table(name="MOD_ARROW")
//@IdClass(MODPK.class)
public class MOD_Arrow {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
//    @Id
    private int caseID;
    @Column(name = "headerId")
    private int nodeFrom_hid;
    @Column(name = "jointId")
    private int nodeTo_jid;
    private String name;
    private String content;

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

    public int getNodeFrom_hid() {
        return nodeFrom_hid;
    }

    public void setNodeFrom_hid(int nodeFrom_hid) {
        this.nodeFrom_hid = nodeFrom_hid;
    }

    public int getNodeTo_jid() {
        return nodeTo_jid;
    }

    public void setNodeTo_jid(int nodeTo_jid) {
        this.nodeTo_jid = nodeTo_jid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MOD_Arrow{" +
                "id=" + id +
                ", caseID=" + caseID +
                ", nodeFrom_hid=" + nodeFrom_hid +
                ", nodeTo_jid=" + nodeTo_jid +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

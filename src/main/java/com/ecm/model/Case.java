package com.ecm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name="PUB_AJ_JB")
public class Case {

    @Id
    @Column(name = "AJXH")
    private int id;
    @Column(name = "AH")
    private String caseNum;
    @Column(name = "AJMC")
    private String name;
    @Column(name = "AJXZ")
    private String type;
    @Column(name = "LARQ")
    private Timestamp fillingDate;
    @Column(name = "JARQ")
    private Timestamp closingDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCaseNum() {
        return caseNum;
    }

    public void setCaseNum(String caseNum) {
        this.caseNum = caseNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getFillingDate() {
        return fillingDate;
    }

    public void setFillingDate(Timestamp fillingDate) {
        this.fillingDate = fillingDate;
    }

    public Timestamp getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(Timestamp closingDate) {
        this.closingDate = closingDate;
    }
}

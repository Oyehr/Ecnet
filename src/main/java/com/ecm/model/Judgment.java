package com.ecm.model;

import javax.persistence.*;

@Entity
@Table(name="PUB_SPRY")
@IdClass(JudgmentPK.class)
public class Judgment {

    @Id
    @Column(name = "AJXH")
    private String cid;
    @Id
    @Column(name = "SPRYBH")
    private String jid;
    @Column(name = "FG")
    private String isJudge;
    @Column(name = "SFCBR")
    private String isUndertaker;
    @Column(name = "XM")
    private String realName;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getIsJudge() {
        return isJudge;
    }

    public void setIsJudge(String isJudge) {
        this.isJudge = isJudge;
    }

    public String getIsUndertaker() {
        return isUndertaker;
    }

    public void setIsUndertaker(String isUndertaker) {
        this.isUndertaker = isUndertaker;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}

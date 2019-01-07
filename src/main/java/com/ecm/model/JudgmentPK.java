package com.ecm.model;

import java.io.Serializable;

public class JudgmentPK implements Serializable {

    private String cid;
    private String jid;

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
}

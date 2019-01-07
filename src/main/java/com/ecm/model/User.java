package com.ecm.model;

import javax.persistence.*;

@Entity
@Table(name="PUB_XTGL_YHB")
public class User {

    @Id
    @Column(name = "YHBH")
    private String id;
    @Column(name = "YHDM")
    private String name;
    @Column(name = "YHKL")
    private String password;
    @Column(name = "YHMC")
    private String realName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}

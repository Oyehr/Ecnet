package com.ecm.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="MOD_FACT")
//@IdClass(MODPK.class)
public class MOD_Fact {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
//    @Id
    private int caseID;
    private String name;
    private String content;
    private int x = -1;
    private int y = -1;

    private int logicNodeID = -1; // 关联LogicNode中的id
    private int textID = -1;
    private int confirm = 1;//0-不认定 1-认定

    @Transient
    private List<MOD_Joint> jointList=new ArrayList<>();//持有的joint


    public List<MOD_Joint> getJointList() {
        return jointList;
    }

    public void setJointList(List<MOD_Joint> jointList) {
        this.jointList = jointList;
    }

    public void addJoint(MOD_Joint joint){
        this.jointList.add(joint);
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

    public int getLogicNodeID() {
        return logicNodeID;
    }

    public void setLogicNodeID(int logicNodeID) {
        this.logicNodeID = logicNodeID;
    }

    public int getTextID() {
        return textID;
    }

    public void setTextID(int textID) {
        this.textID = textID;
    }

    public int getConfirm() {
        return confirm;
    }

    public void setConfirm(int confirm) {
        this.confirm = confirm;
    }

    @Override
    public String toString() {
        return "MOD_Fact{" +
                "id=" + id +
                ", caseID=" + caseID +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", logicNodeID=" + logicNodeID +
                '}';
    }
}

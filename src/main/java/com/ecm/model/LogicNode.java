package com.ecm.model;

import javax.persistence.*;

/**
 * Created by deng on 2018/3/28.
 */
@Entity
@Table(name = "logic_node")
public class LogicNode {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private int caseID;

    private int nodeID;
    private int parentNodeID; // 若不存在，则设置为-1
    private String topic;
    private String detail;
    private int type; // 0-证据,1-事实,2-法条,3-结论
    private int x; // 画布上的x坐标
    private int y;

    public LogicNode() {
    }

    public LogicNode(int caseID, int nodeID, int parentNodeID, String topic, String detail, int type, int x, int y) {
        this.caseID = caseID;
        this.nodeID = nodeID;
        this.parentNodeID = parentNodeID;
        this.topic = topic;
        this.detail = detail;
        this.type = type;
        this.x = x;
        this.y = y;
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

    public int getNodeID() {
        return nodeID;
    }

    public void setNodeID(int nodeID) {
        this.nodeID = nodeID;
    }

    public int getParentNodeID() {
        return parentNodeID;
    }

    public void setParentNodeID(int parentNodeID) {
        this.parentNodeID = parentNodeID;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "LogicNode{" +
                "id=" + id +
                ", caseID=" + caseID +
                ", nodeID=" + nodeID +
                ", parentNodeID=" + parentNodeID +
                ", topic='" + topic + '\'' +
                ", detail='" + detail + '\'' +
                ", type=" + type +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}

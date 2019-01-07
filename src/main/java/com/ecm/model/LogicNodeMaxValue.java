package com.ecm.model;

import java.io.Serializable;

/**
 * Created by deng on 2018/4/2.
 */
public class LogicNodeMaxValue implements Serializable{
    private int maxY;
    private int maxNodeID;

    public LogicNodeMaxValue(int maxY, int maxNodeID) {
        this.maxY = maxY;
        this.maxNodeID = maxNodeID;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public int getMaxNodeID() {
        return maxNodeID;
    }

    public void setMaxNodeID(int maxNodeID) {
        this.maxNodeID = maxNodeID;
    }
}

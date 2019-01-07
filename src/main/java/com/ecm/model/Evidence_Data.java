package com.ecm.model;

import java.util.HashMap;
import java.util.List;

public class Evidence_Data {

    private int caseID;
    private List<Evidence_Head> headers;
    private List<Evidence_Body> bodies;
    private List<MOD_Joint> joints;
    private List<MOD_Fact> facts;
    private List<MOD_Arrow> arrows;
    private HashMap<Integer,List<Integer>> links;

    public int getCaseID() {
        return caseID;
    }

    public void setCaseID(int caseID) {
        this.caseID = caseID;
    }

    public List<Evidence_Head> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Evidence_Head> headers) {
        this.headers = headers;
    }

    public List<Evidence_Body> getBodies() {
        return bodies;
    }

    public void setBodies(List<Evidence_Body> bodies) {
        this.bodies = bodies;
    }

    public List<MOD_Joint> getJoints() {
        return joints;
    }

    public void setJoints(List<MOD_Joint> joints) {
        this.joints = joints;
    }

    public List<MOD_Fact> getFacts() {
        return facts;
    }

    public void setFacts(List<MOD_Fact> facts) {
        this.facts = facts;
    }

    public List<MOD_Arrow> getArrows() {
        return arrows;
    }

    public void setArrows(List<MOD_Arrow> arrows) {
        this.arrows = arrows;
    }

    public HashMap<Integer, List<Integer>> getLinks() {
        return links;
    }

    public void setLinks(HashMap<Integer, List<Integer>> links) {
        this.links = links;
    }
}

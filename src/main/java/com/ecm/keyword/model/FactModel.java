package com.ecm.keyword.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FactModel {

	private String content;
	private HashMap<String, List<String>> keyWordMap = new HashMap<String, List<String>>();
	private ArrayList<EvidenceModel> evidenceList;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public HashMap<String, List<String>> getKeyWordMap() {
		return keyWordMap;
	}
	public void setKeyWordMap(HashMap<String, List<String>> keyWordMap) {
		this.keyWordMap = keyWordMap;
	}

	public ArrayList<EvidenceModel> getEvidenceList() {
		return evidenceList;
	}
	public void setEvidenceList(ArrayList<EvidenceModel> evidenceList) {
		this.evidenceList = evidenceList;
	}
	public void addEvidence(EvidenceModel evi){
		evidenceList.add(evi);
	}
}

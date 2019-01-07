package com.ecm.service;
import net.sf.json.JSONArray;

public interface CaseManageService {

    public JSONArray getAllCases(String username);

    public JSONArray getFinishedCases(String username);

    public JSONArray getProcessingCases(String username);

    public JSONArray getRawCases(String username);
}

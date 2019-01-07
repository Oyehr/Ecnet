package com.ecm.service.impl;

import com.ecm.dao.CaseDao;
import com.ecm.dao.JudgmentDao;
import com.ecm.model.Case;
import com.ecm.model.Judgment;
import com.ecm.service.CaseManageService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaseManageServiceImpl implements CaseManageService {

    @Autowired
    private CaseDao caseDao;
    @Autowired
    private JudgmentDao judgmentDao;

    @Override
    public JSONArray getAllCases(String username) {
        JSONArray res = new JSONArray();
        List<Judgment> judges = judgmentDao.getAllByName(username);

        for(int i = 0;i<judges.size();i++){
            JSONObject jsonObject = new JSONObject();

            Judgment j = judges.get(i);
            Case c = caseDao.findById(Integer.parseInt(j.getCid()));

            jsonObject.put("cid",j.getCid());
            jsonObject.put("caseNum",c.getCaseNum());
            jsonObject.put("cname",c.getName());
            jsonObject.put("type",c.getType());
            jsonObject.put("fillingDate",c.getFillingDate().toString());

//            if(j.getHasJudge().equals('0')){
//                jsonObject.put("courtClerk",j.getRealName());
//            }else
//            if (j.getIsJudge().equals('0')||j.getIsUndertaker().equals('Y')){
                jsonObject.put("manageJudge",j.getRealName());
//            }

            res.add(jsonObject);
        }
        return res;
    }

    @Override
    public JSONArray getFinishedCases(String username) {
        return null;
    }

    @Override
    public JSONArray getProcessingCases(String username) {
        return null;
    }

    @Override
    public JSONArray getRawCases(String username) {
        return null;
    }
}

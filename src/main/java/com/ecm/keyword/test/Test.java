package com.ecm.keyword.test;

import com.ecm.keyword.model.EvidenceType;
import com.ecm.keyword.model.SplitType;
import com.ecm.keyword.reader.ExcelUtil;
import com.ecm.model.Evidence_Body;
import com.google.gson.JsonArray;
import jdk.nashorn.internal.parser.JSONParser;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONString;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by dongyixuan on 2017/12/5.
 */
public class Test {
    public static void main(String[] args)  {
//       String type="一.接受刑事案件登记表、破案及到案经过，证明2014年12月27日，公安机关接到王某某报警称，当日13时许，殷万友在张乙家因琐事持刀将陈某某扎伤致死。次日15时许，公安机关在公主岭市农田中将殷万友抓获。十二.现场勘验、检查笔录及照片，证明现场位于公主岭市张乙宅院。宅院大门北侧地面有一具男性尸体，尸体下方有血迹。宅院大门及院内有滴落血迹。现场血迹已提取。";
//
//        System.out.println(SplitType.getType(type).toString());
//
//        String[] res=type.split(SplitType.getType(type).getRegex());
//        for(String str:res){
//            if(!str.isEmpty()){
//                System.out.println(str);
//            }
//        }

        try {
            List<Evidence_Body> test=ExcelUtil.evidenceToList("证据清单.xls");
            for (Evidence_Body body:test
                 ) {
                System.out.println(body.toString());
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



}
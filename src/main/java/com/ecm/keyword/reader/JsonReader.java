package com.ecm.keyword.reader;

import com.ecm.keyword.model.FactModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by dongyixuan on 2017/12/28.
 */
public class JsonReader {
    public ArrayList<FactModel> readFactModelListFromFile(String filePath) throws IOException{
        Gson gson = new GsonBuilder().create();
        FileInputStream fis = new FileInputStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
        String content = "";
        String temp;
        if ((temp = reader.readLine())!=null){
            content += temp;
        }
        reader.close();
        FactModel[] arr = gson.fromJson(content, FactModel[].class);
        ArrayList<FactModel> result = new ArrayList<>();
        for (FactModel fact: arr){
            result.add(fact);
        }
        return result;
    }
}

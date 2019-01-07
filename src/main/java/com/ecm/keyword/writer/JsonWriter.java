package com.ecm.keyword.writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Created by dongyixuan on 2017/12/25.
 */
public class JsonWriter<T> {
    public void writeListToJson(ArrayList<T> list, String fileUrl) throws IOException{
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fileUrl), StandardCharsets.UTF_8);
        Gson gson = new GsonBuilder().create();
        gson.toJson(list,writer);
        writer.close();
    }
}

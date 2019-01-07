package com.ecm.keyword.writer;

import com.ecm.keyword.manager.ChainCreator;
import com.ecm.keyword.model.EvidenceModel;
import com.ecm.keyword.model.FactModel;
import com.ecm.keyword.reader.JsonReader;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;





public class Main {

    public static void main(String[] args) throws IOException{
        //遍历文件夹下所有的文件
//        String path = "/Users/sweets/Documents/故意杀人罪/2015";
//        File folder = new File(path);
//        if (folder.exists()) {
//            File[] files = folder.listFiles();
//            for (File file2: files) {
//                if (file2.isFile() && file2.getAbsolutePath().endsWith("444.xml")) {
//                    System.out.println("processing:"+file2.getName());
//                    generateIntermediateResult(file2.getName());
//                }
//            }
//        }

        //遍历中间结果文件夹下的所有文件，生成Excel
//        String jsonPath = "/Users/dongyixuan/workspace/证据链/裁判文书/JsonResult";
//        File jsonFolder = new File(jsonPath);
//        if (jsonFolder.exists()) {
//            File[] files = jsonFolder.listFiles();
//            for (File file: files) {
//                if (file.isFile() && file.getAbsolutePath().endsWith("xmlfact.json")) {
//                    String name = file.getName();
//                    String fileName = name.split("\\.")[0];
//                    writeIntermediateResultToExcel(file.getPath(),fileName);
//                }
//            }
//        }



    }

    public static void generateIntermediateResult(String fileName) {
        ChainCreator chainCreator = new ChainCreator();
        ArrayList<FactModel> fList = new ArrayList<FactModel>();
        ArrayList<EvidenceModel> eList = new ArrayList<EvidenceModel>();
        String folder = "/Users/sweets/Documents/故意杀人罪/2015";
        chainCreator.creatChain(folder,fileName,fList,eList);
    }

    public static void writeIntermediateResultToExcel(String jsonFilePath, String fileName) throws IOException{
        ExcelWriter excelWriter = new ExcelWriter();
        JsonReader jsonReader = new JsonReader();

        ArrayList<FactModel> fList = new ArrayList<FactModel>();

        fList = jsonReader.readFactModelListFromFile(jsonFilePath);
        excelWriter.writeExcel(fileName, fList);
    }

}
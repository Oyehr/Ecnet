package com.ecm.keyword.manager;

public class TypeCalculator {
    //根据文本内容查找关键词的方式判断证据类型
    //分类包括0-证人证言，1-被告人供述和辩解，2-书证，3-鉴定结论，4-勘验、检查笔录，5-其他
    public int calType(String evidence){
        if(evidence.contains("证人")){
            return 0;
        }
        if(evidence.contains("被告人")){
            return 1;
        }
        if(evidence.contains("书证")||evidence.contains("凭证")){
            return 2;
        }
        if(evidence.contains("鉴定")){
            return 3;
        }
        if(evidence.contains("勘验")){
            return 4;
        }else{
            return 5;
        }
    }
}

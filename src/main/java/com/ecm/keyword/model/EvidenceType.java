package com.ecm.keyword.model;

public enum EvidenceType {
    证人证言("证人证言",0),被告人供述和辩解("被告人供述和辩解",1),书证("书证",2),鉴定结论("鉴定结论",3),勘验检查笔录("勘验、检查笔录",4),其他("其他",5);

    String name;
    int index;
    EvidenceType(String name,int index){
        this.name=name;
        this.index=index;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    public static EvidenceType getTypeByName(String name){
        for(EvidenceType type:EvidenceType.values()){
            if(type.getName().endsWith(name)){
            return type;
        }
        }
        return EvidenceType.其他;
    }




}

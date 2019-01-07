package com.ecm.keyword.model;

public enum SplitType_Fact {
    //设定优先级可以解决1、（1）（2）2、的问题或判断组合（待解决）

    //注意转义符
    TEXT_NUM_COMMA_FULL("事实1，","事实[0-9]+，",0),
    TEXT_NUM_COMMA_HALF("事实1,","事实[0-9]+,",1),
    TEXT_NUM_MARK("事实1、","事实[0-9]+、",2),
    TEXT_NUM_BRACKETS_FULL("（事实1）","（事实[0-9]+）",3),
    TEXT_NUM_BRACKETS_HALF("(事实1)","\\(事实[0-9]+\\)",4),
    TEXT_NUM_LEFT_BRACKETS_FULL("（事实1","（事实[0-9]+",3),
    TEXT_NUM_RIGHT_BRACKETS_FULL("事实1）","事实[0-9]+）",3),
    TEXT_NUM_LEFT_BRACKETS_HALF("(事实1","\\(事实[0-9]+",4),
    TEXT_NUM_RIGHT_BRACKETS_HALF("事实1)","事实[0-9]+\\)",4),
    TEXT_NUM_DOT("事实1.","事实[0-9]+\\.",0),
    TEXT_NUM_COLON_FULL("事实1:","事实[0-9]+\\:",0),
    TEXT_NUM_COLON_HALF("事实1：","事实[0-9]+\\：",0),

    TEXT_CHINESE_COMMA_FULL("事实，","事实[一二三四五六七八九十]+，",0),
    TEXT_CHINESE_COMMA_HALF("事实,","事实[一二三四五六七八九十]+,",1),
    TEXT_CHINESE_MARK("事实、","事实[一二三四五六七八九十]+、",2),
    TEXT_CHINESE_BRACKETS_FULL("（事实）","（事实[一二三四五六七八九十]+）",3),
    TEXT_CHINESE_BRACKETS_HALF("(事实)","\\(事实[一二三四五六七八九十]+\\)",4),
    TEXT_CHINESE_LEFT_BRACKETS_FULL("（事实","（事实[一二三四五六七八九十]+",3),
    TEXT_CHINESE_RIGHT_BRACKETS_FULL("事实）","事实[一二三四五六七八九十]+）",3),
    TEXT_CHINESE_LEFT_BRACKETS_HALF("(事实一","\\(事实[一二三四五六七八九十]+",4),
    TEXT_CHINESE_RIGHT_BRACKETS_HALF("事实一)","事实[一二三四五六七八九十]+\\)",4),
    TEXT_CHINESE_DOT("事实一.","事实[一二三四五六七八九十]+\\.",0),
    TEXT_CHINESE_COLON_FULL("事实一：","事实[一二三四五六七八九十]+\\.",0),
    TEXT_CHINESE_COLON_HALF("事实一:","事实[一二三四五六七八九十]+\\.",0),

    TEXT_CHINESE("事实","事实[一二三四五六七八九十]+",0),
    TEXT_NUM("事实","事实[0-9]+",0),

    NUM_COMMA_FULL("1，","[0-9]+，",0),
    NUM_COMMA_HALF("1,","[0-9]+,",1),
    NUM_MARK("1、","[0-9]+、",2),
    NUM_BRACKETS_FULL("（1）","（[0-9]+）",3),
    NUM_BRACKETS_HALF("(1)","\\([0-9]+\\)",4),
    NUM_LEFT_BRACKETS_FULL("（1","（[0-9]+",3),
    NUM_RIGHT_BRACKETS_FULL("1）","[0-9]+）",3),
    NUM_LEFT_BRACKETS_HALF("(1","\\([0-9]+",4),
    NUM_RIGHT_BRACKETS_HALF("1)","[0-9]+\\)",4),
    NUM_DOT("1.","[0-9]+\\.",0),

    CHINESE_COMMA_FULL("一，","[一二三四五六七八九十]+，",0),
    CHINESE_COMMA_HALF("一,","[一二三四五六七八九十]+,",1),
    CHINESE_MARK("一、","[一二三四五六七八九十]+、",2),
    CHINESE_BRACKETS_FULL("（一）","（[一二三四五六七八九十]+）",3),
    CHINESE_BRACKETS_HALF("(一)","\\([一二三四五六七八九十]+\\)",4),
    CHINESE_LEFT_BRACKETS_FULL("（一","（[一二三四五六七八九十]+",3),
    CHINESE_RIGHT_BRACKETS_FULL("一）","[一二三四五六七八九十]+）",3),
    CHINESE_LEFT_BRACKETS_HALF("(一","\\([一二三四五六七八九十]+",4),
    CHINESE_RIGHT_BRACKETS_HALF("一)","[一二三四五六七八九十]+\\)",4),
    CHINESE_DOT("一.","[一二三四五六七八九十]+\\.",0),

    DEFAULT("不分割","若无匹配分割符，则不进行分割",5);


    String name;
    String regex;
    int index;

    SplitType_Fact(String name, String regex, int index) {
        this.name = name;
        this.regex = regex;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public String getRegex() {
        return regex;
    }

    public int getIndex() {
        return index;
    }

    public static SplitType_Fact getType(String content){
        for(SplitType_Fact type:SplitType_Fact.values()){
            if(content.contains(type.name))
                return type;
        }
        return SplitType_Fact.DEFAULT;
    }

    @Override
    public String toString() {
        return "SplitType{" +
                "name='" + name + '\'' +
                ", regex='" + regex + '\'' +
                ", index=" + index +
                '}';
    }
}

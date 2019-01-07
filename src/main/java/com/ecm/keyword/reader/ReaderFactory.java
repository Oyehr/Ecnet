package com.ecm.keyword.reader;

public class ReaderFactory {


    public XMLReader createXMLReader(String type) {
        switch (type) {

            // 刑事
            case "刑事案件":
                return new XMLReaderCri();

            // 民事
            case "民事案件":
                return new XMLReaderCiAndAd();

            // 行政
            case "行政案件":
                return new XMLReaderCiAndAd();

            default:
                break;
        }
        return null;
    }
}
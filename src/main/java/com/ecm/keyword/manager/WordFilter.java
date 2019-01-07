package com.ecm.keyword.manager;

import org.ansj.domain.Result;
import org.ansj.domain.Term;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordFilter {

    ArrayList<String> stopWords;
    //final String stopWordUrl="C:\\Users\\nju\\Downloads\\stopWords.txt";
   // final String stopWordUrl="/Users/sweets/Documents/毕业设计/提取链头/stopWords.txt";
    final String stopWordUrl="/root/ecm/stopWords.txt";
    public WordFilter(){
        stopWords = getStopWords();
    }

    public void filterStopWords(Result result){
        List<Term> terms = new ArrayList<Term>();

        for(Term term : result){
            if(!stopWords.contains(term.getName())){
                terms.add(term);
            }
        }
        result.setTerms(terms);
    }

    public List<String> filterStopWords(List<String> list){
        List<String> resultList = new ArrayList<String>();

        for(String str : list){
            if (str.length() == 1){
                continue;
            }
            if(!stopWords.contains(str)){
                resultList.add(str);
            }
        }

        return resultList;
    }


    public List<String> filterDateFromWhat(List<String> list){
        List<String> resultList = new ArrayList<String>();

        for(String str : list){
            Pattern pattern = Pattern.compile(
                    "\\d+年\\d+月\\d+日\\d+时\\d+分|"
                            + "\\d+年\\d+月\\d+日\\d+时|"
                            + "\\d+年\\d+月\\d+日|"
                            + "\\d+年\\d+月|"
                            + "\\d+月\\d+日|"
                            +"\\d+日|"
                            + "\\d+时\\d+分|"
                            + "\\d+时");
            Matcher matcher = pattern.matcher(str);
            if (!matcher.find()){
                resultList.add(str);
            }
        }

        return resultList;
    }

    public void filterSingleWords(Result result){
        List<Term> terms = new ArrayList<Term>();

        for(Term term : result){
            if(term.getName().length()>1){
                terms.add(term);
            }
        }
        result.setTerms(terms);
    }
    public ArrayList<String> getStopWords(){
        ArrayList<String> stopWords = new ArrayList<String>();

        try {
            FileInputStream fis = new FileInputStream(stopWordUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
            String tempString = null;
            while ((tempString = reader.readLine())!=null) {
                String[] str = tempString.split(",");
                stopWords.add(str[0]);
            }
            reader.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return stopWords;
    }
}
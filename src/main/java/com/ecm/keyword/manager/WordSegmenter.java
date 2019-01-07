package com.ecm.keyword.manager;

import org.ansj.domain.Result;
import org.ansj.splitWord.analysis.NlpAnalysis;

public class WordSegmenter {

	public Result segment(String text){
		Result result = NlpAnalysis.parse(text);
		return result;
	}
	
}

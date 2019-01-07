package com.ecm.keyword.manager;

import com.ecm.keyword.model.EvidenceModel;
import com.ecm.keyword.model.FactModel;
import com.ecm.keyword.reader.ReaderFactory;
import com.ecm.keyword.reader.XMLReader;
import com.ecm.keyword.writer.JsonWriter;
import org.xml.sax.SAXException;


import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ChainCreator {
	
	private String[] keyList = {"what","where","when","who","how much"};
	
	public void creatChain(String folderName, String fileName, ArrayList<FactModel> fList, ArrayList<EvidenceModel> eList){
		ReaderFactory fac = new ReaderFactory();
		XMLReader xmlReader = new XMLReader();
		String filePath = folderName + "/" + fileName;
		String type = xmlReader.getType(filePath);
		xmlReader = fac.createXMLReader(type);
		
		try {
			//读取事实集及事实相关证据集
			fList = xmlReader.getFactlist(filePath);
			//读取无关联点证据集
			eList = xmlReader.getEvidencelist(filePath);
			
			//提取关键要素
			KeyWordCalculator calculator = new KeyWordCalculator();
			calculator.calcKeyWord(fList,eList);

			JsonWriter jsonWriter = new JsonWriter();
			//构建证据事实关系
			calcLink(fList, eList);
			jsonWriter.writeListToJson(fList,folderName+"/result/fact/"+fileName+"fact.json");
			jsonWriter.writeListToJson(eList,folderName+"/result/evidence/"+fileName+"evidence.json");
			//计算证据链头
			calcHead(fList);
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	// 构建证据事实关系
	private void calcLink(ArrayList<FactModel> fList, ArrayList<EvidenceModel> eList){
		for(EvidenceModel evi : eList){
			int linkFactIndex = calcMaxRelatedFact(fList,evi);
			linkToFact(fList,evi,linkFactIndex);
		}
	}

	// 计算证据链头
	private void calcHead(ArrayList<FactModel> fList){
		for(FactModel fact : fList){
			ArrayList<EvidenceModel> eList = fact.getEvidenceList();
			
			for(String key : keyList){
				calcHeadBetweenEvis(eList,key); //计算证据间的共同关键词作为链头
				calcHeadBetweenFactAndEvis(fact,eList,key); //计算证据与事实间的共同关键词作为链头
			}
		}
	}
	
	private int calcMaxRelatedFact(ArrayList<FactModel> fList, EvidenceModel evi){
		int index = -1; //记录最大关联的事实的位置
		
		int maxValue = 0; //记录最大关联度
		for(int i=0;i<fList.size();i++){
			FactModel fact = fList.get(i);
			int relateValue = calcRelateValue(fact,evi); //计算证据与事实的关联度
			if(relateValue>maxValue){
				maxValue = relateValue;
				index = i;
			}
		}
		
		return index;
	}
	
	private int calcRelateValue(FactModel fact, EvidenceModel evi){
		int value = 0;
		
		HashMap<String, List<String>> factKeyWordMap = fact.getKeyWordMap();
		HashMap<String, List<String>> eviKeyWordMap = evi.getKeyWordMap();
		
		for(String key : keyList){
			List<String> factValueList = factKeyWordMap.get(key);
			List<String> eviValueList = eviKeyWordMap.get(key);
			
			if(factValueList!=null&&eviValueList!=null){
				for(String factValue : factValueList){
					for(String eviValue : eviValueList){
						if(factValue.equals(eviValue)){
							value++;
						}
					}
				}
			}
		}
		
		return value;
	}
	
	private void linkToFact(ArrayList<FactModel> fList, EvidenceModel evi, int index){
		if(index>-1){
			fList.get(index).addEvidence(evi);
		}
	}
	// 证据见链头的计算：只有两个或以上的证据都包含该要素，才会作为链头
	// TODO 链头计算可以调整，不需要要素完全一致，使用相似度计算，在多少以上可以作为链头 例如 匕首和刀
	private void calcHeadBetweenEvis(ArrayList<EvidenceModel> eList, String key){
		HashMap<String, List<Integer>> wordMap = new HashMap<String, List<Integer>>();
		
		for(int i=0;i<eList.size();i++){
			List<String> valueList = eList.get(i).getKeyWordMap().get(key);
			if(valueList!=null){
				for(String value : valueList){
					if(wordMap.containsKey(value)){
						List<Integer> indexs = wordMap.get(value);
						if(!indexs.contains(i))
							indexs.add(i);
						wordMap.replace(value, indexs);
					}else{
						List<Integer> indexs = new ArrayList<Integer>();
						indexs.add(i);
						wordMap.put(value,indexs);
					}
				}
			}
		}
		
		Set<String> keySet = wordMap.keySet();
		for(String value : keySet){
			List<Integer> indexs = wordMap.get(value);
			if(indexs.size()>1){
				for(int index : indexs){
					eList.get(index).addHead(value);
				}
			}
		}
	}
	
	private void calcHeadBetweenFactAndEvis(FactModel fact, ArrayList<EvidenceModel> eList, String key){
		List<String> factValueList = fact.getKeyWordMap().get(key);
		
		for(EvidenceModel evi : eList){
			List<String> eviValueList = evi.getKeyWordMap().get(key);
			if(eviValueList!=null){
				for(String factValue : factValueList){
					for(String eviValue : eviValueList){
						if(factValue.equals(eviValue)&&!evi.getHeadList().contains(eviValue)){
							evi.addHead(eviValue);
						}
					}
				}
			}
		}
	}
}

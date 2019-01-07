package com.ecm.keyword.reader;

import com.ecm.keyword.model.EvidenceModel;
import com.ecm.keyword.model.FactModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/*民事Civil 和 行政Administrative 文书读取器*/
public class XMLReaderCiAndAd extends XMLReader {

	/*获得事实集，事实中没有关联的证据集*/
	public ArrayList<FactModel> getFactlist(String path) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{
		ArrayList<FactModel> factList = new ArrayList<FactModel>();
		
		DocumentBuilderFactory dbf =DocumentBuilderFactory.newInstance();			  
		DocumentBuilder builder = dbf.newDocumentBuilder(); 
		File f = new File(path);
		Document doc = builder.parse(f); //编译xml
		  
		NodeList nodes = doc.getElementsByTagName("RDSS"); //获得认定事实节点
		
		if(nodes!=null && nodes.getLength()>0){
			for(int i=0;i<nodes.getLength();i++){
				FactModel fact = new FactModel();
				Element currentElement = (Element) nodes.item(i);
				fact.setContent(currentElement.getAttribute("value")); //获得事实内容
			}
		}

		return factList;
	}
	
	/*获得没有关联事实的证据集*/
	public ArrayList<EvidenceModel> getEvidencelist(String path) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{
		ArrayList<EvidenceModel> evidenceList = new ArrayList<EvidenceModel>();
		
		DocumentBuilderFactory dbf =DocumentBuilderFactory.newInstance();			  
		DocumentBuilder builder = dbf.newDocumentBuilder(); 
		File f = new File(path);
		Document doc = builder.parse(f); //编译xml
		
		NodeList nodes = doc.getElementsByTagName("ZJJL"); //获得证据记录节点
		
		if(nodes!=null && nodes.getLength()>0){
			for(int i=0;i<nodes.getLength();i++){
				EvidenceModel evidence = new EvidenceModel();
				Node node = nodes.item(i);
				evidence.setEvidence(node);
			}
		}

		return evidenceList;
	}
}

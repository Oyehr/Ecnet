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

/*刑事Criminal 文书读取器*/
public class XMLReaderCri extends XMLReader {

	/*获得事实集，每个事实包含关联的证据集*/
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
				ArrayList<EvidenceModel> evidenceList = new ArrayList<EvidenceModel>();
				
				Node currentNode = nodes.item(i);
				Element currentElement = (Element) currentNode;
				fact.setContent(currentElement.getAttribute("value")); //获得事实内容
				
				while(currentNode.getNextSibling()!=null){
					currentNode = currentNode.getNextSibling(); //获得下一个节点
					EvidenceModel evidence = new EvidenceModel();
					evidence.setEvidence(currentNode);
					evidenceList.add(evidence);
				}
				
				fact.setEvidenceList(evidenceList);
				factList.add(fact);
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
		  
		NodeList nodes = doc.getElementsByTagName("ZJFZ"); //获得证据分组节点
		
		if(nodes!=null && nodes.getLength()>0){
			for(int i=0;i<nodes.getLength();i++){
				Node currentNode = nodes.item(i);
				
				// 如果证据分组中没有认定事实节点，则说明证据无关联事实
				if(!hasChildNode(currentNode, "RDSS")){
					NodeList childNodeList = currentNode.getChildNodes();

					if(childNodeList!=null && childNodeList.getLength()>0){
						for(int j=0;j<childNodeList.getLength();j++){
							Node childNode = childNodeList.item(j);
							EvidenceModel evidence = new EvidenceModel();
							evidence.setEvidence(childNode);
							evidenceList.add(evidence);
						}
					}
				}
			}
		}

		return evidenceList;
	}
}

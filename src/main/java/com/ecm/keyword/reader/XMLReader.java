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

public class XMLReader {

	/*判断文书所属案件类别*/
	public String getType(String path){
		String type = "";
		
		try {
			DocumentBuilderFactory dbf =DocumentBuilderFactory.newInstance();			  
			DocumentBuilder builder;
			builder = dbf.newDocumentBuilder();
			File file = new File(path);
			Document doc = builder.parse(file); //编译xml
			NodeList nodes = doc.getElementsByTagName("AJLB"); //获得案件类别节点
			Element element = (Element) nodes.item(0);
			type = element.getAttribute("value");
			
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
		
		return type;
	}
	
	/*判断当前节点下是否有某个名称的子节点*/
	protected boolean hasChildNode(Node pNode, String childNodeName){
		NodeList cNodeList = pNode.getChildNodes(); //获得子节点
		if(cNodeList!=null && cNodeList.getLength()>0){
			for(int i=0;i<cNodeList.getLength();i++){
				Node currentNode = cNodeList.item(i);
				// 如果存在节点名称与所寻找名称一致的节点，则表示存在所寻找子节点
				if(childNodeName.equals(currentNode.getNodeName())){
					return true;
				}
			}
		}
		
		return false;
	}
	
	/*判断证据是否采信*/
	protected boolean isAdopted(){
		return false;
	}
	
	public ArrayList<FactModel> getFactlist(String path) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{
		return null;
	}
	
	public ArrayList<EvidenceModel> getEvidencelist(String path) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{
		return null;
	}
}

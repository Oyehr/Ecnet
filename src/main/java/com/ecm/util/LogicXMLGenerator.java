package com.ecm.util;

import com.ecm.model.LogicNode;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by deng on 2018/4/7.
 */
public class LogicXMLGenerator {
    private Map<Integer, LogicNode> nodeMap;
    private List<Integer> roots;
    private Map<Integer, List<Integer>> parentAndItsChildren;

    private String[] types = {"证据", "事实", "法条", "结论"};
    private final String FILE_PATH;
    private Document document;

    public LogicXMLGenerator(String path, List<LogicNode> nodes) {
        FILE_PATH = path;

        nodeMap = new HashMap<>();
        for (LogicNode node : nodes) {
            nodeMap.put(node.getNodeID(), node);
        }
        roots = new ArrayList<>();
        parentAndItsChildren = new HashMap<>();
        for (LogicNode node : nodes) {
            int parentNodeID = node.getParentNodeID();
            if (parentNodeID != -1) {
                if (parentAndItsChildren.containsKey(parentNodeID)) {
                    parentAndItsChildren.get(parentNodeID).add(node.getNodeID());
                } else {
                    List<Integer> children = new ArrayList<>();
                    children.add(node.getNodeID());
                    parentAndItsChildren.put(parentNodeID, children);
                }
            } else {
                roots.add(node.getNodeID());
            }
        }

        document = DocumentHelper.createDocument();
    }

    public void generateXMLFile() throws IOException {
        Element rootEle = document.addElement("graph").addElement("nodes");
        for (Integer root : roots) {
            Element rootNodeEle = generateNodeElement(rootEle, root);
            recursive(rootNodeEle.addElement("children"), parentAndItsChildren.get(root));
        }

        File file = new File(FILE_PATH);
        if (!file.exists()) file.createNewFile();
        XMLWriter writer = new XMLWriter(new FileOutputStream(file), OutputFormat.createPrettyPrint());
        writer.setEscapeText(false);//字符是否转义,默认true
        writer.write(document);
        writer.close();
    }

    private void recursive(Element rootElement, List<Integer> children) {
        if (children == null) return;

        for (Integer child : children) {
            Element childEle = generateNodeElement(rootElement, child);
            recursive(childEle.addElement("children"), parentAndItsChildren.get(child));
        }
    }

    private Element generateNodeElement(Element parentEle, int nodeID) {
        LogicNode node = nodeMap.get(nodeID);
        Element nodeEle = parentEle.addElement("node");
        nodeEle.addElement("topic").setText(node.getTopic());
        nodeEle.addAttribute("nodeId", "" + node.getNodeID());
        nodeEle.addElement("type").setText(types[node.getType()]);
        nodeEle.addElement("detail").setText(node.getDetail());
        nodeEle.addAttribute("x", "" + node.getX());
        nodeEle.addAttribute("y", "" + node.getY());

        return nodeEle;
    }
}

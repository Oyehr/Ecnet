package com.ecm.service.impl;

import com.ecm.dao.EvidenceFactLinkDao;
import com.ecm.dao.LogicNodeDao;
import com.ecm.model.EvidenceFactLink;
import com.ecm.model.LogicNode;
import com.ecm.model.LogicNodeMaxValue;
import com.ecm.service.LogicService;
import com.ecm.util.LogicExcelGenerator;
import com.ecm.util.LogicXMLGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * Created by deng on 2018/3/28.
 */
@Service
@PropertySource(value = {"classpath:application.properties"}, encoding = "utf-8")
public class LogicServiceImpl implements LogicService {
    @Autowired
    private LogicNodeDao logicNodeDao;
    @Autowired
    private EvidenceFactLinkDao evidenceFactLinkDao;

    @Value("${download.logicnode.xml}")
    private String downloadXMLPath;

    @Value("${download.logicnode.excel}")
    private String downloadExcelPath;

    private String[] types = {"证据", "事实", "法条", "结论"};

    @Override
    public List<LogicNode> getAllNodesByCaseID(int caseID) {
        List<LogicNode> logicNodes = logicNodeDao.findByCaseID(caseID);
        return logicNodes;
    }

    public int addEvidenceOrFactNode(int caseID, String detail, int type) {
        if (type != 0 && type != 1) {
            return -1;
        }
        LogicNode node = generateNode(caseID, -1, detail, type);
        return logicNodeDao.save(node).getId();
    }

    @Override
    public int addNode(int caseID, int parentID, String detail, int type) {
        //   int parentID = -1nt parentID = -1;

        if (logicNodeDao.findById(parentID) != null) {
            parentID = logicNodeDao.findById(parentID).getNodeID();
        }
        LogicNode node = generateNode(caseID, parentID, detail, type);
        return logicNodeDao.save(node).getId();
    }

    @Override
    public void modEvidenceOrFactNode(int id, String detail) {
        LogicNode node = getNode(id);
        node.setDetail(detail);
        logicNodeDao.save(node);
    }

    @Override
    public void addLinkForEvidenceAndFactNode(int caseID, int evidenceID, int factID) {
        LogicNode eviNode = getNode(evidenceID);
        if (eviNode.getParentNodeID() == -1) {
            // 证据节点不存在父事实节点，修改该节点的parentNodeID信息
            eviNode.setParentNodeID(logicNodeDao.findById(factID).getNodeID());
            logicNodeDao.save(eviNode);

            EvidenceFactLink eviFactLink = new EvidenceFactLink(caseID, evidenceID, evidenceID, factID);
            evidenceFactLinkDao.save(eviFactLink);
        } else {
            // 证据节点存在父事实节点，需要copy新的证据节点，并将新的证据节点的parentNodeID设置为factNodeID
            LogicNodeMaxValue maxValue = getLogicNodeMaxValue(eviNode.getCaseID());
            LogicNode copyEviNode = new LogicNode(eviNode.getCaseID(), maxValue.getMaxNodeID() + 1, logicNodeDao.findById(factID).getNodeID(), eviNode.getTopic(), eviNode.getDetail(), eviNode.getType(), 80, maxValue.getMaxY() + 50);
            copyEviNode = logicNodeDao.save(copyEviNode);

            EvidenceFactLink eviFactLink = new EvidenceFactLink(caseID, evidenceID, copyEviNode.getId(), factID);
            evidenceFactLinkDao.save(eviFactLink);
        }
    }

    @Override
    @Transactional
    public void deleteAllLinksBetweenEvidenceAndFactNode(int caseID) {
        List<EvidenceFactLink> links = evidenceFactLinkDao.findByCaseID(caseID);

        for (EvidenceFactLink link : links) {
            if (link.getInitEviID() == link.getCopyEviID()) {
                // 保留原节点，但是修改掉父ID
                LogicNode eviNode = logicNodeDao.findById(link.getCopyEviID());
                eviNode.setParentNodeID(-1);
                logicNodeDao.save(eviNode);
            } else {
                // 直接删除复制的证据节点
                logicNodeDao.delete(link.getCopyEviID());
            }
        }

        evidenceFactLinkDao.deleteByCaseID(caseID);
    }

    @Override
    public LogicNode getNode(int id) {
        return logicNodeDao.findById(id);
    }

    @Override
    public LogicNode saveNode(LogicNode logicNode) {
       return logicNodeDao.save(logicNode);
    }

    @Override
    @Transactional
    public void saveAllNodesInSameCase(int caseID, List<LogicNode> logicNodes) {
        for (LogicNode node : logicNodes) {
            LogicNode originNode = logicNodeDao.findByCaseIDAndNodeID(node.getCaseID(), node.getNodeID());
            if (originNode == null) {
                logicNodeDao.save(node);
            } else {
                originNode.setDetail(node.getDetail());
                originNode.setTopic(node.getTopic());
                originNode.setParentNodeID(node.getParentNodeID());
                originNode.setType(node.getType());
                originNode.setX(node.getX());
                originNode.setY(node.getY());
                logicNodeDao.save(originNode);
            }
        }
    }

    @Override
    @Transactional
    public void deleteNode(int id) {
        logicNodeDao.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAllNodesByCaseID(int caseID) {
        logicNodeDao.deleteByCaseID(caseID);
        evidenceFactLinkDao.deleteByCaseID(caseID);
    }

    @Override
    public String generateXMLFile(int caseID) {
        List<LogicNode> nodes = logicNodeDao.findByCaseID(caseID);
        try {
            new LogicXMLGenerator(downloadXMLPath, nodes).generateXMLFile();
            return downloadXMLPath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String generateExcelFile(int caseID) {
        List<LogicNode> nodes = logicNodeDao.findByCaseID(caseID);
        new LogicExcelGenerator(downloadExcelPath, nodes).generateExcelFile();
        return downloadExcelPath;
    }

    private LogicNode generateNode(int caseID, int parentNodeID, String detail, int type) {
        LogicNodeMaxValue maxValue = getLogicNodeMaxValue(caseID);

        int nodeID = maxValue.getMaxNodeID() + 1;
        String topic = types[type] + nodeID;
        int x = 80;
        int y = maxValue.getMaxY() + 50;

        LogicNode node = new LogicNode(caseID, nodeID, parentNodeID, topic, detail, type, x, y);
        return node;
    }

    private LogicNodeMaxValue getLogicNodeMaxValue(int caseID) {
        LogicNodeMaxValue maxValue;
        try {
            maxValue = logicNodeDao.getLogicNodeMaxValueByCaseID(caseID);
        } catch (org.springframework.dao.InvalidDataAccessApiUsageException e) {
            // 对应案件不存在节点时
            maxValue = new LogicNodeMaxValue(0, 0);
        }
        return maxValue;
    }
}

package com.ecm.service;

import com.ecm.model.LogicNode;

import java.util.List;

/**
 * Created by deng on 2018/3/28.
 */
public interface LogicService {
    List<LogicNode> getAllNodesByCaseID(int caseID);

    int addEvidenceOrFactNode(int caseID, String detail, int type);

    int addNode(int caseID, int parentID, String detail, int type);

    /**
     * 修改节点的detail信息
     *
     * @param id
     * @param detail
     */
    void modEvidenceOrFactNode(int id, String detail);

    /**
     * 增加证据节点和事实节点连线
     * 由于在证据链建模图中证据和事实节点是多对多的关系，因此需要当证据节点已存在父事实节点时，需要复制一个证据节点
     *
     * @param caseID
     * @param evidenceID
     * @param factID
     */
    void addLinkForEvidenceAndFactNode(int caseID, int evidenceID, int factID);

    /**
     * 删除一个案件中所有证据节点和事实节点的连线，同时删除所有复制的证据节点，仅保留初始证据节点
     *
     * @param caseID
     */
    void deleteAllLinksBetweenEvidenceAndFactNode(int caseID);

    LogicNode getNode(int id);

    LogicNode saveNode(LogicNode logicNode);

    /**
     * 保存案件的所有节点，保存前先清空所有节点
     *
     * @param caseID
     * @param logicNodes
     */
    void saveAllNodesInSameCase(int caseID, List<LogicNode> logicNodes);

    void deleteNode(int id);

    void deleteAllNodesByCaseID(int caseID);

    String generateXMLFile(int caseID);

    String generateExcelFile(int caseID);
}

package com.ecm.dao;

import com.ecm.model.LogicNode;
import com.ecm.model.LogicNodeMaxValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by deng on 2018/3/28.
 */
public interface LogicNodeDao extends JpaRepository<LogicNode, Integer> {
    LogicNode findById(int id);

    LogicNode findByCaseIDAndNodeID(int caseID, int nodeID);

    List<LogicNode> findByCaseID(int caseID);

    LogicNode save(LogicNode logicNode);

    void deleteById(int id);

    void deleteByCaseID(int caseID);

    @Query(value = "select new com.ecm.model.LogicNodeMaxValue(max(node.y),max(node.nodeID)) from LogicNode node where node.caseID = ?1")
    LogicNodeMaxValue getLogicNodeMaxValueByCaseID(int caseID);
}

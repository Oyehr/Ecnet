package com.ecm.dao;

import com.ecm.model.MODPK;
import com.ecm.model.MOD_Fact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MOD_FactDao extends JpaRepository<MOD_Fact, Integer> {

    public MOD_Fact save(MOD_Fact fact);

    public void deleteById(int id);

    @Query(value = "select f.logicNodeID from MOD_Fact f where f.id= ?1 and f.caseID=?2")
    public int getLogicNodeIDByIDAndCaseID(int id,int cid);

//    public void deleteById(int id);

    public void deleteByIdAndCaseID(int id,int cid);

    public void deleteAllByCaseID(int cid);

    public List<MOD_Fact> findAllByCaseID(int caseID);

    public MOD_Fact findById(int id);

    @Query(value = "select f.logicNodeID from MOD_Fact f where f.id=?1")
    public int getLogicNodeIDByID(int id);

    @Modifying
    @Query(value = "update MOD_Fact f set f.confirm=?2 where f.id=?1")
    public void updateConfirmById(int fid,int confirm);

    @Modifying
    @Query(value = "update MOD_Fact f set f.content=?2 where f.id=?1")
    public void updateContentById(int fid,String content);
}

package com.ecm.dao;

import com.ecm.model.MODPK;
import com.ecm.model.MOD_Arrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MOD_ArrowDao extends JpaRepository<MOD_Arrow, Integer> {

    public MOD_Arrow save(MOD_Arrow arrow);

    public void deleteByIdAndCaseID(int id,int cid);

    public void deleteById(int id);

    public void deleteAllByCaseID(int cid);

    @Modifying
    @Query(value = "delete from MOD_Arrow a where a.nodeFrom_hid=?1")
    public void deleteAllByNodeFrom_hid(int hid);

    @Modifying
    @Query(value = "delete from MOD_Arrow a where a.nodeTo_jid=?1")
    public void deleteAllByJointID(int jid);

    public List<MOD_Arrow> findAllByCaseID(int cid);

    @Query(value = "select a from MOD_Arrow a where a.caseID=?1 and a.nodeTo_jid=?2")
    public List<MOD_Arrow> findAllByCaseIDAndJointID(int cid,int jid);

    @Query(value = "select a.nodeFrom_hid from MOD_Arrow a where a.nodeTo_jid=?1 and a.caseID=?2")
    public List<Integer> getHeaderIdByJointIdAndCaseID(int jid,int cid);

    @Query(value = "select a from MOD_Arrow a where a.nodeFrom_hid=?1")
    public List<MOD_Arrow> findAllByHeaderID(int hid);
}

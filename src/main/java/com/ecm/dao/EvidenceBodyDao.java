package com.ecm.dao;


import com.ecm.model.Evidence_Body;
import com.ecm.model.Evidence_Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;


public interface EvidenceBodyDao extends JpaRepository<Evidence_Body, Integer> {

    @Query(value = "select u from Evidence_Body u where u.documentid= ?1")
    public List<Evidence_Body> getAllByDocumentid(int documentid);

//    @Query(value = "select u from Evidence_Body u where u.documentid= ?1 and u.trust=1")
    public List<Evidence_Body> findAllByDocumentidAndTrust(int documentID,int trust);

    public List<Evidence_Body> findAllByCaseIDAndTrust(int caseID,int trust);

    public List<Evidence_Body> findAllByCaseID(int caseID);

    public Evidence_Body save(Evidence_Body evi);

    public Evidence_Body findById(int id);

    @Query(value = "select b.body from Evidence_Body b where b.id= ?1")
    public String getContentById(int id);

    public void deleteById(int id);

    public void deleteAllByDocumentid(int document_id);

    public void deleteAllByCaseID(int caseID);

    @Modifying
    @Query("update Evidence_Body c set c.body = ?1 where c.id=?2")
    public void updateBodyById(String body, int id);

    @Modifying
    @Query("update Evidence_Body c  set c.type = ?1 where c.id=?2")
    public void updateTypeById(int type, int id);

    @Modifying
    @Query("update Evidence_Body c  set c.trust = ?1 where c.id=?2")
    public void updateTrustById(int trust, int id);

    @Query(value = "select u.logicNodeID from Evidence_Body u where u.id = ?1")
    public int findLogicId(int id);


    @Query(value = "select u.id from Evidence_Body u where u.documentid = ?1")
    public List<Integer> findAllByDocumentid(int id);

}


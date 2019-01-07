package com.ecm.dao;


import com.ecm.model.Evidence_Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface EvidenceDocuDao extends JpaRepository<Evidence_Document, Integer> {

    @Query(value = "select u from  Evidence_Document u where u.caseID= ?1 and u.type=?2")
    public Evidence_Document getEvidenceDocument(int caseID,int type);

    public Evidence_Document save(Evidence_Document evi);

    public List<Evidence_Document> findAllByCaseID(int caseID);

}

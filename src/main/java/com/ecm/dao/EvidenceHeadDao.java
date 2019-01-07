package com.ecm.dao;


import com.ecm.model.Evidence_Document;
import com.ecm.model.Evidence_Head;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface EvidenceHeadDao extends JpaRepository<Evidence_Head, Integer> {

    @Query(value = "select u from  Evidence_Head u where u.documentid=?1")
    public List<Evidence_Head> getEvidenceHead(int documentid);

    public List<Evidence_Head> findAllByCaseIDAndBodyid(int caseID, int bodyID);
    public List<Evidence_Head> findAllByBodyid(int bodyid);
    public List<Evidence_Head> findAllByCaseIDAndDocumentidAndBodyid(int c,int d,int b);

    @Query(value = "select u.head from  Evidence_Head u where u.caseID=?1 and u.bodyid=?2")
    public List<String> findContentsByCaseIDAndBodyid(int caseID, int bodyID);

    @Query(value = "select h.id from  Evidence_Head h where h.bodyid=?1 and h.head=?2")
    public int findIdByBodyidAndHead(int bid,String head);

    public Evidence_Head findById(int id);

    public Evidence_Head save(Evidence_Head head);

    public void deleteAllByCaseID(int caseID);

    public void deleteAllByDocumentid(int documentid);

    public void deleteAllByBodyid(int bodyid);

    public void deleteById(int id);

    @Modifying
    @Query("update Evidence_Head c set c.head = ?1 where c.id=?2")
    public void updateHeadById(String head, int id);

    @Modifying
    @Query("update Evidence_Head h set h.bodyid=?2 where h.id=?1")
    public void updateBodyIdById(int hid, int bodyid);

}

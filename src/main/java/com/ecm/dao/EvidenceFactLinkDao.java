package com.ecm.dao;

import com.ecm.model.EvidenceFactLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by deng on 2018/4/3.
 */
public interface EvidenceFactLinkDao extends JpaRepository<EvidenceFactLink, Integer> {
    EvidenceFactLink save(EvidenceFactLink evidenceFactLink);

    EvidenceFactLink findByCaseIDAndInitEviIDAndFactID(int caseID, int initEviID, int factID);

    List<EvidenceFactLink> findByCaseID(int caseID);

    void deleteByCaseID(int caseID);
}

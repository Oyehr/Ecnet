package com.ecm.dao;

import com.ecm.model.Case;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CaseDao extends JpaRepository<Case, Integer> {

    public Case findById(int id);
}

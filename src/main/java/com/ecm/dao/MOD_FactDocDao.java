package com.ecm.dao;

import com.ecm.model.MOD_Fact_Doc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MOD_FactDocDao extends JpaRepository<MOD_Fact_Doc, Integer> {

    public MOD_Fact_Doc save(MOD_Fact_Doc factDoc);

    public MOD_Fact_Doc findById(int id);

    public MOD_Fact_Doc findByCaseID(int cid);
}

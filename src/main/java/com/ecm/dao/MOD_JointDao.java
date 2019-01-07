package com.ecm.dao;

import com.ecm.model.MODPK;
import com.ecm.model.MOD_Joint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MOD_JointDao extends JpaRepository<MOD_Joint, Integer> {

    public MOD_Joint save(MOD_Joint joint);

    public void deleteById(int id);

    public void deleteByIdAndCaseID(int id,int cid);

    public void deleteAllByCaseID(int cid);

//    public void deleteById(int id);

    public void deleteAllByFactIDAndCaseID(int factID,int cid);

    public List<MOD_Joint> findAllByCaseID(int cid);

    public List<MOD_Joint> findAllByFactIDAndCaseID(int factID,int cid);

    public List<MOD_Joint> findAllByFactID(int fid);

    public MOD_Joint findByIdAndCaseID(int id,int cid);

    public MOD_Joint findById(int id);

    @Modifying
    @Query(value = "update MOD_Joint j set j.factID=-1 where j.factID=?1")
    public void updateFactID(int fid);

    @Modifying
    @Query(value = "update MOD_Joint j set j.content=?2 where j.id=?1")
    public void updateContentById(int id,String content);
}

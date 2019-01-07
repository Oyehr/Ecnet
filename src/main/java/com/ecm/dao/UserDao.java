package com.ecm.dao;

import com.ecm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserDao extends JpaRepository<User, String> {

    @Query(value = "select u.password from User u where u.name = ?1")
    public String getPassword(String name);

    public User findUserByName(String name);
}

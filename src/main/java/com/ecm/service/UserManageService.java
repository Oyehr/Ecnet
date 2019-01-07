package com.ecm.service;

import com.ecm.model.User;

public interface UserManageService {

    public String getPassword(String name);

    public User getUserByName(String name);
}

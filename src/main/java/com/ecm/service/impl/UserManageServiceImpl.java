package com.ecm.service.impl;

import com.ecm.dao.UserDao;
import com.ecm.model.User;
import com.ecm.service.UserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserManageServiceImpl implements UserManageService {

    @Autowired
    private UserDao userDao;

    @Override
    public String getPassword(String name) {
        return userDao.getPassword(name);
    }

    @Override
    public User getUserByName(String name) {
        return userDao.findUserByName(name);
    }
}

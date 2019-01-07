package com.ecm.controller;

import com.ecm.model.User;
import com.ecm.service.UserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginController {

    @Autowired
    private UserManageService userService;

    @RequestMapping(value="/checkLogin")
    public String getAllCourses(@RequestParam("username") String username, @RequestParam("password") String password){
//        String password_true = userService.getPassword(username);
        User user = userService.getUserByName(username);

        if(user==null)
            return "not exist";
        else if(password.equals(user.getPassword()))
            return user.getRealName();
        else
            return "wrong";
    }
}

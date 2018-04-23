package com.cohen.scheduletracking.controller;

import com.cohen.scheduletracking.entity.MessageBody;
import com.cohen.scheduletracking.service.LoginService;
import com.cohen.scheduletracking.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping("index")
    public String index() {
        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public MessageBody login(Employee emp, HttpSession session, MessageBody msg) {
        return loginService.login(emp, msg, session);
    }

    @ResponseBody
    @RequestMapping(value = "logout",method = RequestMethod.POST)
    public MessageBody logout(HttpSession session, MessageBody msg) {
        System.out.println("logout");
        session.setAttribute("user",null);
        msg.setStatus("1");
        msg.setBody("已退出登录！");
        return msg;
    }

    @RequestMapping(value = "checkJurisdiction",method = RequestMethod.GET)
    public MessageBody checkJurisdiction(MessageBody msg,HttpSession session){
        return null;
    }
}
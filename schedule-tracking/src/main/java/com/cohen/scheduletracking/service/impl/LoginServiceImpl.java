package com.cohen.scheduletracking.service.impl;

import com.cohen.scheduletracking.dao.LoginMapper;
import com.cohen.scheduletracking.entity.MessageBody;
import com.cohen.scheduletracking.service.LoginService;
import com.cohen.scheduletracking.entity.Employee;
import com.cohen.scheduletracking.utils.MD5Util;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Service
@Transactional(rollbackFor = Exception.class)
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;

    public MessageBody login(Employee emp, MessageBody msg, HttpSession session) throws RuntimeException {
        if (emp != null) {
            if (emp.getUserName() != null && emp.getUserName() != "") {
                if (emp.getPassword() != null && emp.getPassword() != "") {
                    Employee empResult = loginMapper.checkUserName(emp.getUserName());
                    if (empResult != null && empResult.getId() > 0) {
                        String password = emp.getPassword();
                        emp.setPassword(MD5Util.MD5(emp.getPassword()));
                        empResult = loginMapper.checkPassword(emp.getUserName(), emp.getPassword());
                        emp.setPassword(password);
                        if (empResult != null && empResult.getId() > 0) {
                            emp.setPassword(MD5Util.MD5(emp.getPassword()));
                            empResult = loginMapper.checkValid(emp.getUserName(), emp.getPassword(), "1");
                            if (empResult != null && empResult.getId() > 0) {
                                msg.setStatus("1");
                                msg.setBody("登陆成功！");
                                empResult.setPassword(null);
                                session.setAttribute("user", empResult);
                                return msg;
                            } else {
                                msg.setStatus("-3");
                                msg.setBody("该用户已被禁止登陆！请联系管理员！");
                                return msg;
                            }
                        } else {
                            msg.setStatus("-2");
                            msg.setBody("密码不正确！");
                            return msg;
                        }
                    } else {
                        msg.setStatus("-1");
                        msg.setBody("用户名不存在！");
                        return msg;
                    }
                } else {
                    msg.setStatus("-2");
                    msg.setBody("密码不能为空！");
                    return msg;
                }
            } else {
                msg.setStatus("-1");
                msg.setBody("用户名不能为空！");
                return msg;
            }
        } else {
            throw new RuntimeException(this.getClass().getTypeName() + " throw an exception!");
        }
    }

    @Override
    public Employee login(String username, String password) {
        Employee employee = loginMapper.checkUserName(username);
        if (employee != null) {
            employee = loginMapper.checkPassword(username, password);
            if (employee != null) {
                employee = loginMapper.checkValid(username, password, "1");
            }
        }

        return employee;
    }

    @Override
    public MessageBody checkJurisdiction(MessageBody msg, HttpSession session) {
        SimplePrincipalCollection attribute = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        Employee user = (Employee)attribute.getPrimaryPrincipal();
        if (user != null) {
            msg.setStatus("1");
            msg.setBody("权限获取成功！");
        } else {
            msg.setStatus("0");// 未登录
            msg.setBody("未登录！");
        }
        return msg;
    }
}

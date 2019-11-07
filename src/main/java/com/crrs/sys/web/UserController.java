package com.crrs.sys.web;

import com.crrs.sys.entity.User;
import com.crrs.sys.service.UserService;
import com.crrs.util.BaseCode;
import com.crrs.util.WebUtil;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/sys")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("getUser/{id}")
    public String GetUser(@PathVariable int id){
        return "index";
        //return userService.Sel(id).toString();
    }

    @RequestMapping("getmodel")
    protected void GetModel(HttpServletRequest request,
        HttpServletResponse response){
            String uname = request.getParameter("uname");
            User user = userService.queryUserName(uname);
            JSONObject json = new JSONObject();
            json.put("data",user);
        WebUtil.packResponse(json, BaseCode.SITE_OK.getCode(),response);
        //return "index";
        //return userService.Sel(id).toString();
    }

    @RequestMapping("/loginUser")
    public String loginUser(String username,String password,HttpSession session) {
        //授权认证
        UsernamePasswordToken usernamePasswordToken=new UsernamePasswordToken(username,password);
        Subject subject = SecurityUtils.getSubject();
        try {
            //完成登录
            subject.login(usernamePasswordToken);
            //获得用户对象
            User user=(User) subject.getPrincipal();
            //存入session
            session.setAttribute("user", user);
            return "index";
        } catch(Exception e) {
            return "login";//返回登录页面
        }
    }

    /**
     * 退出
     * @param session
     * @return
     */
    @RequestMapping("/logOut")
    public String logOut(HttpSession session) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        //session.removeAttribute("user");
        return "login";
    }
}


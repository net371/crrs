package com.crrs.sys.web;

import com.crrs.sys.entity.User;
import com.crrs.sys.service.UserService;
import com.crrs.util.BaseCode;
import com.crrs.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.catalina.filters.ExpiresFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys")
@Api(tags = "用户管理Controller")
public class UserController {
    @Autowired
    private UserService userService;

    //@ApiOperation(value = "根据用户主键查询用户信息", notes = "根据用户主键查询用户信息")
    //@ApiImplicitParam(name = "id", value = "用户主键", required = true, dataType = "String", paramType = "query")
    @RequestMapping("getUser/{id}")
    public String GetUser(@PathVariable int id){
        return "index";
        //return userService.Sel(id).toString();
    }

    //@ApiOperation(value = "根据用户名查询用户信息", notes = "根据用户名查询用户信息")
    //@ApiImplicitParam(name = "uname", value = "用户名", required = true, dataType = "String", paramType = "query")
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

    //@ApiOperation(value = "用户登录", notes = "录入用户名和密码进行登录")
    //@ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "query")
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

    @RequestMapping("insert")
    protected void insertModel(HttpServletRequest request,
                            HttpServletResponse response){
        JSONObject json = new JSONObject();
        try {
            String uname = request.getParameter("uname");
            String pwd = request.getParameter("pwd");
            User user = new User(uname, pwd);
            userService.insertModel(user);
            WebUtil.packResponse(json, BaseCode.SITE_OK.getCode(), response);
        }catch (Exception ex){
            WebUtil.packResponse(json, BaseCode.SITE_NG.getCode(), response);
        }
    }

    /**
     * 查询用户列表
     * @param request
     * @param response
     */
    @RequestMapping("findlist")
    protected void findUserlist(HttpServletRequest request,
                                HttpServletResponse response) {
        JSONObject json=new JSONObject();
      try {
          String name=request.getParameter("uname");
          String curr=request.getParameter("curr");
          String nums=request.getParameter("nums");
          int pageindex=0;
          int pagesize=0;
          if (!StringUtils.isEmpty(curr)){
              pageindex=Integer.parseInt(curr)-1;
              pagesize=Integer.parseInt(nums);
          }
          Map<String,Object> map=new HashMap<String,Object>();
          if(!StringUtils.isEmpty(name)){
              map.put("uname", URLDecoder.decode(name,"utf-8"));
          }
          map.put("offset",pageindex*pagesize);
          map.put("limit",pagesize);
          List<User> list= userService.findlist(map);
          Integer count=userService.findlistCount(map);
          json.put("data",list);
          json.put("count",count);
         WebUtil.packResponse(json,BaseCode.SITE_OK.getCode(),response);
      }catch (Exception e){
          e.printStackTrace();
          WebUtil.packResponse(json,BaseCode.SITE_NG.getCode(),response);
      }
    }

    @RequestMapping("/findByPage/{curr}/{nums}")
    protected List<User> findByPage(@PathVariable String curr,@PathVariable String nums){
        int pageindex=0;
        int pagesize=0;
        if (!StringUtils.isEmpty(curr)){
            pageindex=Integer.parseInt(curr)-1;
            pagesize=Integer.parseInt(nums);
        }
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("offset",pageindex*pagesize);
        map.put("limit",pagesize);
        return  userService.findlist(map);
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


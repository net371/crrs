package com.crrs.sys.web;

import com.crrs.sys.entity.User;
import com.crrs.sys.service.UserService;
import com.crrs.util.BaseCode;
import com.crrs.util.WebUtil;
import io.swagger.annotations.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.catalina.Session;
import org.apache.catalina.filters.ExpiresFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.jasper.tagplugins.jstl.core.ForEach;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.web.bind.annotation.*;

import javax.management.ValueExp;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys")
@Api(tags = "用户管理Controller")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "主键查询用户", notes = "根据用户主键查询用户信息")
    @ApiImplicitParam(name = "id", value = "用户主键", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "getUser", method = RequestMethod.POST)
    protected void GetUser(@RequestParam String id, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            User user = userService.dindbyid(id);
            json.put("data", user);
            WebUtil.packResponse(json, BaseCode.SITE_OK.getCode(), response);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("msg", "查询数据异常!");
            WebUtil.packResponse(json, BaseCode.SITE_NG.getCode(), response);
        }
    }

    @ApiOperation(value = "用户名查询用户", notes = "根据用户名查询用户信息")
    @ApiImplicitParam(name = "uname", value = "用户名", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "getmodel", method = RequestMethod.GET)
    protected void GetModel(HttpServletRequest request,
                            HttpServletResponse response) {
        String uname = request.getParameter("uname");
        User user = userService.queryUserName(uname);
        JSONObject json = new JSONObject();
        json.put("data", user);
        WebUtil.packResponse(json, BaseCode.SITE_OK.getCode(), response);
        //return "index";
        //return userService.Sel(id).toString();
    }

    @ApiOperation(value = "用户登录", notes = "录入用户名和密码进行登录")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "query")
    @PostMapping("/loginUser")
    public void loginUser(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //授权认证
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        JSONObject json = new JSONObject();
        try {
            //完成登录
            subject.login(usernamePasswordToken);
            //获得用户对象
            User user = (User) subject.getPrincipal();
            //存入session
            request.getSession().setAttribute("user", user);
            json.put("msg", "登陆成功!");
            WebUtil.packResponse(json, BaseCode.SITE_OK.getCode(), response);
//            return "index";
        } catch (Exception e) {
            json.put("msg", "登陆失败!");
            WebUtil.packResponse(json, BaseCode.SITE_NG.getCode(), response);
        }
    }

    @ApiOperation(value = "获取缓存数据信息", notes = "登陆后的缓存数据")
    @PostMapping("findsession")
    protected void findSession(HttpServletRequest request,
                               HttpServletResponse response, HttpSession session) {
        JSONObject json = new JSONObject();
        try {
            User User = (User) request.getSession().getAttribute("user");
            json.put("data", User);
            WebUtil.packResponse(json, BaseCode.SITE_OK.getCode(), response);
        } catch (Exception ex) {
            WebUtil.packResponse(json, BaseCode.SITE_NG.getCode(), response);
        }
    }

    @ApiOperation(value = "新增用户", notes = "录入用户名和密码进行登录")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "query")
    @PostMapping("insert")
    protected void insertModel(HttpServletRequest request,
                               HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            String uname = request.getParameter("uname");
            String pwd = request.getParameter("pwd");
            User user = null;
//            new User(uname, pwd);
            userService.insertModel(user);
            WebUtil.packResponse(json, BaseCode.SITE_OK.getCode(), response);
        } catch (Exception ex) {
            WebUtil.packResponse(json, BaseCode.SITE_NG.getCode(), response);
        }
    }

    /**
     * 查询用户列表
     *
     * @param request
     * @param response
     */
//    @GetMapping("findlist")
    @ApiOperation(value = "获取用户列表", notes = "列表信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "curr", value = "当前页数", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "nums", value = "每页显示数量", required = true, paramType = "query", dataType = "String")
    })
    @ApiResponse(code = 1, message = "参数查询异常！")
    @RequestMapping(value = "findlist", method = RequestMethod.POST)
    protected void findUserlist(@RequestParam(value = "userName", required = false) String userName,
                                HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            String curr = request.getParameter("curr");
            String nums = request.getParameter("nums");
            int pageindex = 0;
            int pagesize = 0;
            if (!StringUtils.isEmpty(curr)) {
                pageindex = Integer.parseInt(curr) - 1;
                pagesize = Integer.parseInt(nums);
            }
            Map<String, Object> map = new HashMap<String, Object>();
            if (!StringUtils.isEmpty(userName)) {
                map.put("uname", URLDecoder.decode(userName, "utf-8"));
            }
            map.put("offset", pageindex * pagesize);
            map.put("limit", pagesize);
            List<User> list = userService.findlist(map);
            Integer count = userService.findlistCount(map);
            json.put("data", list);
            json.put("count", count);
            WebUtil.packResponse(json, BaseCode.SITE_OK.getCode(), response);
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.packResponse(json, BaseCode.SITE_NG.getCode(), response);
        }
    }

    @ApiOperation(value = "添加用户信息")
    @ApiResponses({
            @ApiResponse(code = 1, message = "用户添加失败！"),
            @ApiResponse(code = 0, message = "用户信息添加成功！")
    })
    @RequestMapping(value = "/creadUser", method = RequestMethod.GET)
    protected void creadUser(User user, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            User model = new User(user.getUserName(), user.getPassWord(), "0");
            userService.insertModel(user);
            json.put("msg", "用户添加成功!");
            WebUtil.packResponse(json, BaseCode.SITE_OK.getCode(), response);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("msg", "用户添加失败!");
            WebUtil.packResponse(json, BaseCode.SITE_NG.getCode(), response);
        }
    }

    @ApiOperation(value = "修改用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户主键id", name = "id", dataType = "String", required = true),
            @ApiImplicitParam(value = "用户名称", name = "userName", dataType = "String", required = false),
            @ApiImplicitParam(value = "用户密码", name = "passWord", dataType = "String", required = false)
    })
    @RequestMapping(value = "Edit", method = RequestMethod.POST)
    protected void Edit(@RequestParam String id,
                        @RequestParam(value = "userName", required = false) String userName,
                        @RequestParam(value = "passWord", required = false) String passWord,
                        HttpServletResponse response) {
        JSONObject JSONObject = new JSONObject();
        try {
            Map<String, Object> map = new Hashtable<>();
            map.put("id", id);
            map.put("userName", userName);
            if (!StringUtils.isEmpty(id)) {
                String salt = id.substring(0, 6);
                String ps = new Sha512Hash(passWord, salt).toString();
                map.put("passWord", ps);
            }
            userService.Update(map);
            WebUtil.packResponse(JSONObject, BaseCode.SITE_OK.getCode(), response);
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject.put("msg", "修改用户信息异常！");
            WebUtil.packResponse(JSONObject, BaseCode.SITE_NG.getCode(), response);
        }
    }

    @ApiOperation(value = "删除用户信息")
    @ApiImplicitParam(name = "id", value = "用户主键id", dataType = "String", required = true)
    @RequestMapping(value = "DelUser", method = RequestMethod.POST)
    protected void delUser(@RequestParam String id, HttpServletResponse response) {
        JSONObject JSONObject = new JSONObject();
        try {
            if (!StringUtils.isEmpty(id)) {
                String[] ids = id.split(",");
                for (String s : ids) userService.delUser(s);
            }
            JSONObject.put("msg", "数据删除成功!");
            WebUtil.packResponse(JSONObject, BaseCode.SITE_OK.getCode(), response);
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject.put("msg", "数据删除失败!");
            WebUtil.packResponse(JSONObject, BaseCode.SITE_NG.getCode(), response);
        }
    }

    /**
     * 用户列表信息查询
     *
     * @param curr
     * @param nums
     * @return
     */
    @ApiOperation(value = "用户列表信息", notes = "查询用户列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "curr", value = "每页显示数量", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "nums", value = "当前页码", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/findByPage/{curr}/{nums}")
    protected List<User> findByPage(@PathVariable String curr, @PathVariable String nums) {
        int pageindex = 0;
        int pagesize = 0;
        if (!StringUtils.isEmpty(curr)) {
            pageindex = Integer.parseInt(curr) - 1;
            pagesize = Integer.parseInt(nums);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("offset", pageindex * pagesize);
        map.put("limit", pagesize);
        return userService.findlist(map);
    }


    /**
     * 退出
     *
     * @param session
     * @return
     */
    @ApiOperation(value = "退出", notes = "退回系统，清空session")
    @PostMapping("/logOut")
    public String logOut(HttpSession session) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        //session.removeAttribute("user");
        return "login";
    }
}


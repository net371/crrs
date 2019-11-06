package com.crrs.sys.web;

import com.crrs.sys.entity.User;
import com.crrs.sys.service.UserService;
import com.crrs.util.BaseCode;
import com.crrs.util.WebUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
            String id = request.getParameter("id");
            User user = userService.Sel(Integer.parseInt(id));
            JSONObject json = new JSONObject();
            json.put("data",user);
        WebUtil.packResponse(json, BaseCode.SITE_OK.getCode(),response);
        //return "index";
        //return userService.Sel(id).toString();
    }
}


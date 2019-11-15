package com.crrs.sys.service;

import com.crrs.sys.entity.User;
import com.crrs.sys.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public User queryUserName(String uname) {
        List<User> list = userMapper.queryUserName(uname);
        if(list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }

    /**
     * 新增
     * @param user
     */
    public void insertModel(User user) {
        userMapper.insertModel(user);
    }

    /***
     *查询用户列表
     * @param map
     * @return
     */
    public List<User> findlist(Map<String, Object> map) {
       return userMapper.findlist(map);
    }

    public Integer findlistCount(Map<String, Object> map) {
        return userMapper.findlistCount(map);
    }

    public User dindbyid(String id) {
       return userMapper.findById(id);
    }

    /***
     * 修改用户信息
     * @param map
     */
    public void Update(Map<String, Object> map) {
        userMapper.Update(map);
    }

    /**
     * 通过用户主键id删除用户信息
     * @param id
     */
    public void delUser(String id) {
        userMapper.delUser(id);
    }
}

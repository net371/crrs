package com.crrs.sys.service;

import com.crrs.sys.entity.User;
import com.crrs.sys.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public User Sel(int id) {
        List<User> list = userMapper.Sel(id);
        if(list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }
}

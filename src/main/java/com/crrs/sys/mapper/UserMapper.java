package com.crrs.sys.mapper;

import com.crrs.sys.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    List<User> queryUserName(String uname);
}

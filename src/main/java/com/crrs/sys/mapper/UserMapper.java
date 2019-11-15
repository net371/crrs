package com.crrs.sys.mapper;

import com.crrs.sys.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper {
    List<User> queryUserName(String uname);

    /**
     * 新增
     * @param user
     */
    void insertModel(User user);

    /***
     * 查询用户列表
     * @param map
     * @return
     */
    List<User> findlist(Map<String, Object> map);

    Integer findlistCount(Map<String, Object> map);

    User findById(String id);

    void Update(Map<String, Object> map);

    void delUser(String id);
}

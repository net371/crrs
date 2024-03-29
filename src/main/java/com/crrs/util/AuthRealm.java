package com.crrs.util;

import com.crrs.sys.entity.User;
import com.crrs.sys.mapper.UserMapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class AuthRealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;

    //认证.登录
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        UsernamePasswordToken utoken=(UsernamePasswordToken) token;//获取用户输入的token
        String username = utoken.getUsername();
        List<User> ulist = userMapper.queryUserName(username);
        User user = null;
        if(ulist!=null&&ulist.size()>0){
            user = ulist.get(0);
        }
        String credentials = new Sha512Hash(utoken.getPassword(), user.getSalt()).toString();
        utoken.setPassword(credentials.toCharArray());
        //放入shiro.调用CredentialsMatcher检验密码
        return new SimpleAuthenticationInfo(user, user.getPassWord(),this.getClass().getName());
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        //获取session中的用户
        User user=(User) principal.fromRealm(this.getClass().getName()).iterator().next();
        List<String> permissions=new ArrayList<>();
        //Set<Role> roles = user.getRoles();
        //if(roles.size()>0) {
        //    for(Role role : roles) {
        //        Set<Module> modules = role.getModules();
//                if(modules.size()>0) {
//                    for(Module module : modules) {
//                        permissions.add(module.getMname());
//                    }
//                }
//            }
//        }
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        //将权限放入shiro中.
        info.addStringPermissions(permissions);
        return info;
    }

}

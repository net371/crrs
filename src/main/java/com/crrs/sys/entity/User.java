package com.crrs.sys.entity;

import lombok.Setter;
import lombok.Getter;
import org.apache.shiro.crypto.hash.Sha512Hash;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class User implements Serializable {
    private String id;
    private String userName;
    private String passWord;
    private String salt;
    private String locked;

    public User(String username, String password) {
        this.id = UUID.randomUUID().toString().replace("-", "");
        this.userName = username;
        this.salt = this.id.substring(0, 6);
        this.passWord = new Sha512Hash(password, this.salt).toString();
    }
}

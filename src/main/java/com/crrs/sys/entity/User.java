package com.crrs.sys.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Setter;
import lombok.Getter;
import org.apache.shiro.crypto.hash.Sha512Hash;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@ApiModel
public class User implements Serializable {
    @ApiModelProperty(value = "用户主键", required = true)
    @NotBlank(message = "用户主键自增")
    private String id;
    @ApiModelProperty(value = "用户名称", required = true)
    @NotBlank(message = "用户名称不能为空")
    private String userName;
    @ApiModelProperty(value = "用户密码", required = true)
    @NotBlank(message = "用户密码不能为空")
    private String passWord;
    @ApiModelProperty(value = "盐", required = true)
    @NotBlank(message = "加密盐")
    private String salt;
    @ApiModelProperty(value = "锁", required = true)
    @NotBlank(message = "密码超过一定次后自动锁定")
    private String locked;

     public User(String userName, String passWord,String locked) {
        this.id = UUID.randomUUID().toString().replace("-", "");
        this.userName = userName;
        this.salt = this.id.substring(0, 6);
        this.passWord = new Sha512Hash(passWord, this.salt).toString();
        this.locked=locked;
    }
    public  User(){
         System.out.println("111111");
    }

}

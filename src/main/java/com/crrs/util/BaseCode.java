package com.crrs.util;

public enum BaseCode {
    OK(200),
    PARAM_ERROR(201),			//参数错误
    UDF_ERROR(202),				//未知错误
    LOGIN_TIMEOUT(203),			//登录超时
    USER_ALREADY_ERROR(301),	//用户已存在
    USER_NOTFOUND_ERROR(302),	//用户不存在
    USER_LOGIN_ERROR(303),		//密码错误

    DATA_ERROR(401),

    USER_LOGIN(100),		//用户登录
    ADMIN_LOGIN(101),		//管理员登录
    USER_LOGIN_PASSER(102),	//用户登录密码错误
    ADMIN_LOGIN_PASSER(103),//管理员登录密码错误

    SITE_OK(0),//网站参数成功
    SITE_NG(1)//网站参数失败
    ;

    private int code;

    private BaseCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

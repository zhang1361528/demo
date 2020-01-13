package cn.zl.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    /**
    * id
    */
    private Integer userId;

    /**
    * 用户名
    */
    private String userName;

    /**
    * 密码
    */
    private Integer userPwd;

    /**
    * 手机号
    */
    private String mobile;

    /**
    * 邮箱
    */
    private String email;

    /**
    * 盐值
    */
    private String salt;

    /**
    * 激活标记
    */
    private Boolean activateMark;
}
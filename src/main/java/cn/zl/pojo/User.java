package cn.zl.pojo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
@Entity
@Table(name = "user")
public class User implements Serializable {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    /**
     * 姓名
     */
    @Column(name = "user_name")
    private String userName;
    /**
     * 密码
     */
    @Column(name = "user_pwd")
    private Integer userPwd;
    /**
     * 手机号
     */
    @Column(name = "mobile")
    private String mobile;
    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;
    /**
     * 激活标记
     */
    @Column(name = "activate_mark")
    private boolean activateMark;
}

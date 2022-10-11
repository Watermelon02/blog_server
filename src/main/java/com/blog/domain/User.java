package com.blog.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 
     */
    @TableField(value = "email")
    private String email;

    /**
     * 
     */
    @TableField(value = "avatar_url")
    private String avatarUrl;

    /**
     * 
     */
    @TableField(value = "name")
    private String name;

    /**
     * 博客地址
     */
    @TableField(value = "blog")
    private String blog;

    /**
     */
    @TableField(value = "password")
    private String password;

    @TableField(value = "role")
    private String role;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
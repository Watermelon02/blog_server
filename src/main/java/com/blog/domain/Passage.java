package com.blog.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 
 * @TableName passage
 */
@TableName(value ="passage")
@Data
@NoArgsConstructor
public class Passage implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long passage_id;

    /**
     * 标题
     */
    @NotNull
    private String title;

    /**
     * 子标题
     */
    @NotNull
    private String sub_title;

    /**
     * 文章内容
     */
    @NotNull
    private String content;

    /**
     * 发布时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 封面的url
     */
    @NotNull
    private String cover;

    @TableField(exist = false)
    private List<Tag> tags;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 文章访问量
     */
    private String visitor;

    public Passage(String title,String sub_title,String content,String cover){
        this.passage_id = 0L;
        this.title = title;
        this.sub_title = sub_title;
        this.content = content;
        this.cover =cover;
    }
}
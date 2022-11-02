package com.blog.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName passage_tag
 */
@TableName(value ="passage_tag")
@Data
public class PassageTag implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    @TableField(value = "tag_id")
    private Integer tagId;

    /**
     * 
     */
    @TableField(value = "passage_id")
    private Long passageId;

    public PassageTag(Integer tagId, Long passageId){
        this.tagId = tagId;
        this.passageId = passageId;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
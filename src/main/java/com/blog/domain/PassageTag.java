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
    private Integer tag_id;

    /**
     * 
     */
    @TableField(value = "passage_id")
    private Long passageId;

    public PassageTag(Integer tag_id, Long passageId){
        this.tag_id = tag_id;
        this.passageId = passageId;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
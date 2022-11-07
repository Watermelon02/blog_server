package com.blog.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @TableName comment
 */
@TableName(value = "comment")
@Data
public class Comment implements Serializable {
    /**
     *
     */
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Long commentId;

    /**
     *
     */
    @TableField(value = "passage_id")
    private Long passageId;

    /**
     *
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     *
     */
    @TableField(value = "content")
    private String content;

    /**
     *
     */
    @TableField(value = "create_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)		// 为了redis存储反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class)		// 为了redis存储序列化
    private LocalDateTime createTime;

    public Comment(Long passageId, Long userId, String content) {
        this.passageId = passageId;
        this.userId = userId;
        this.content = content;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Data
    public static class CommentResponse {
        private Long commentId;
        private Long passageId;
        private Long userId;
        private String content;
        private LocalDateTime createTime;
        //user_name
        private String name;
        private String avatarUrl;

        public CommentResponse(Comment comment,String name, String avatarUrl) {
            this.commentId= comment.getCommentId();
            this.passageId = comment.getPassageId();
            this.userId = comment.getUserId();
            this.content = comment.getContent();
            this.createTime = comment.getCreateTime();
            this.name = name;
            this.avatarUrl = avatarUrl;
        }
    }

}
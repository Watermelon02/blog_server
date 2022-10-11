package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blog.domain.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
* @author xigua
* @description 针对表【comment】的数据库操作Mapper
* @createDate 2022-10-11 08:07:16
* @Entity .domain.Comment
*/
@Component
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    IPage<Comment> selectByPassageId(@Param("passageId") Long passageId, IPage<Comment> page);
}





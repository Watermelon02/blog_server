package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.Comment;

import java.util.List;

/**
* @author xigua
* @description 针对表【comment】的数据库操作Service
* @createDate 2022-10-11 08:07:16
*/
public interface CommentService extends IService<Comment> {
    public List<Comment> selectByPassageId(Long passage_id, int curPage);
}

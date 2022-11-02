package com.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.domain.Comment;
import com.blog.mapper.CommentMapper;
import com.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xigua
 * @description 针对表【comment】的数据库操作Service实现
 * @createDate 2022-10-11 08:07:16
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {
    private final static int COMMENT_PAGE_SIZE = 8;
    @Autowired
    CommentMapper commentMapper;

    @Override
    public List<Comment> selectByPassageId(Long passageId, int currentPage) {
        Page<Comment> page = new Page<>(currentPage, COMMENT_PAGE_SIZE);
        return commentMapper.selectByPassageId(passageId, page).getRecords();
    }
}





package com.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.domain.Comment;
import com.blog.mapper.CommentMapper;
import com.blog.service.CommentService;
import org.springframework.stereotype.Service;

/**
* @author xigua
* @description 针对表【comment】的数据库操作Service实现
* @createDate 2022-09-17 15:31:26
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService {

}





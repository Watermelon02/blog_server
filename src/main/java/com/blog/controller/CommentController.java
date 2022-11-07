package com.blog.controller;

import com.blog.domain.Comment;
import com.blog.domain.User;
import com.blog.domain.response.Result;
import com.blog.service.CommentService;
import com.blog.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/comment")
@CacheConfig(cacheNames = "comment")
@CrossOrigin(originPatterns = "*", allowCredentials = "true", allowedHeaders = "*", methods = {})
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;

    @PostMapping("/add")
    @CachePut(key = "'passage_id:'+#p0")
    @RequiresRoles(value = {"admin", "user"}, logical = Logical.OR)
    public Result<Comment> addComment(@RequestParam("passageId") Long passageId, @RequestParam("content") String content) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Comment comment = new Comment(passageId, user.getUserId(), content);
        commentService.save(comment);
        return new Result<Comment>(200, 1L, comment);
    }

    @Cacheable(key = "'passage_id:'+#p0")
    @GetMapping("/select")
    public Result<List<Comment.CommentResponse>> selectByPassageId(@RequestParam("passageId") Long passageId, @RequestParam("currentPage") Integer currentPage) {
        List<Comment> commentList = commentService.selectByPassageId(passageId, currentPage);
        ArrayList<Comment.CommentResponse> commentResponses = new ArrayList<>();
        for (Comment comment : commentList) {
            User user = userService.selectByUserId(comment.getUserId());
            commentResponses.add(new Comment.CommentResponse(comment, user.getName(), user.getAvatarUrl()));
        }
        return new Result<List<Comment.CommentResponse>>(200, (long) commentResponses.size(), commentResponses);
    }
}

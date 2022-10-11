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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;

    @PostMapping("/add")
    @RequiresRoles(value = {"admin", "user"}, logical = Logical.OR)
    public Result<Comment> addComment(@RequestParam("passage_id") Long passage_id, @RequestParam("content") String content) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Comment comment = new Comment(passage_id, user.getUserId(), content);
        commentService.save(comment);
        return new Result<Comment>(200, 1L, comment);
    }

    @PostMapping("/select")
    public Result<List<Comment.CommentResponse>> selectByPassageId(@RequestParam("passage_id") Long passage_id, @RequestParam("curPage") Integer curPage) {
        List<Comment> commentList = commentService.selectByPassageId(passage_id, curPage);
        ArrayList<Comment.CommentResponse> commentResponses = new ArrayList<>();
        for (Comment comment : commentList) {
            User user = userService.selectByUserId(comment.getUserId());
            commentResponses.add(new Comment.CommentResponse(comment, user.getName(), user.getAvatarUrl()));
        }
        return new Result<List<Comment.CommentResponse>>(200, (long) commentResponses.size(), commentResponses);
    }
}

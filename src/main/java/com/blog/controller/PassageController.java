package com.blog.controller;

import com.blog.domain.Passage;
import com.blog.domain.PassageTag;
import com.blog.domain.response.Result;
import com.blog.service.PassageService;
import com.blog.service.PassageTagService;
import com.blog.util.ImageUtil;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passage")
@CrossOrigin(originPatterns = "*",allowCredentials="true",allowedHeaders = "",methods = {})
public class PassageController {
    @Value("${server.port}")
    private int port;
    @Autowired
    private PassageService passageService;
    @Autowired
    private PassageTagService passageTagService;
    @Autowired
    private ImageUtil imageUtil;

    @GetMapping("/select")
    public Result<List<Passage>> select(@RequestParam("curPage") Integer curPage) {
        
        return passageService.select(curPage);
    }

    @GetMapping("/selectByTag")
    public Result<List<Passage>> selectByTag(@RequestParam("tag_id") Integer tag_id, @RequestParam("curPage") Integer curPage) {
        return passageService.selectByTagsAndPage(tag_id, curPage);
    }

    @GetMapping("/selectLike")
    public Result<List<Passage>> selectLike(@RequestParam("keyword") String keyword, @RequestParam("curPage") Integer curPage) {
        return passageService.selectLike("%" + keyword + "%", curPage);
    }

    @GetMapping("/selectById")
    public Result<Passage> selectById(@RequestParam("passage_id") Long passage_id){
        return passageService.selectOne(passage_id);
    }

    @RequiresRoles(value = {"admin"},logical= Logical.AND)
    @PostMapping("/update")
    public int update(@RequestBody Passage passage) {
        return passageService.update(passage);
    }

    @Transactional
    @RequiresRoles(value = {"admin"},logical= Logical.AND)
    @PostMapping("/post")
    public Result<String> post(@RequestParam("title") String title, @RequestParam("sub_title") String sub_title, @RequestParam("content") String content, @RequestParam("tags") Integer[] tags) {
        Result<String> result = new Result<String>(403, 0L, "没有先上传封面");
        try {
            if (imageUtil.transaction) {
                String coverUrl = imageUtil.imageUrl;
                Passage passage = new Passage(title, sub_title, content, coverUrl);
                passageService.save(passage);
                for (int tag : tags) {
                    passageTagService.save(new PassageTag(tag, passage.getPassage_id()));
                }
                result = new Result<String>(200, 0L, "success");
            }
        } catch (Exception e) {
            imageUtil.deleteLastImage();
            result = new Result<String>(404, 0L, e.getMessage());
        } finally {
            imageUtil.transaction = false;
        }
        return result;
    }
}

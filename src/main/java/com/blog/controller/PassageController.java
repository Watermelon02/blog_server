package com.blog.controller;

import com.blog.domain.Passage;
import com.blog.domain.PassageTag;
import com.blog.domain.User;
import com.blog.domain.response.Result;
import com.blog.service.PassageService;
import com.blog.service.PassageTagService;
import com.blog.util.ImageUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passage")
@CacheConfig(cacheNames = "passage")
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
    @Cacheable(key = "'passages_page:'+#p0")
    public Result<List<Passage>> select(@RequestParam("curPage") Integer curPage) {
        return passageService.select(curPage);
    }

    @GetMapping("/selectByTag")
    @Cacheable(key = "'passages_tag:'+#p0+':'+#p1")
    public Result<List<Passage>> selectByTag(@RequestParam("tag_id") Integer tag_id, @RequestParam("curPage") Integer curPage) {
        return passageService.selectByTagsAndPage(tag_id, curPage);
    }

    @GetMapping("/selectLike")
    public Result<List<Passage>> selectLike(@RequestParam("keyword") String keyword, @RequestParam("curPage") Integer curPage) {
        return passageService.selectLike("%" + keyword + "%", curPage);
    }

    @GetMapping("/selectById")
    @Cacheable(key = "'passages_id:'+#p0")
    public Result<Passage> selectById(@RequestParam("passage_id") Long passage_id) {
        return passageService.selectOne(passage_id);
    }

    @RequiresRoles(value = {"admin"}, logical = Logical.AND)
    @PostMapping("/update")
    @CachePut(key = "'passages_id:'+#p0.passage_id")
    @CacheEvict(key = "'passage_id:'+#p0",allEntries = true)
    public int update(@RequestBody Passage passage) {
        return passageService.update(passage);
    }

    @Transactional
    @RequiresRoles(value = {"admin"})
    @PostMapping("/post")
    @CachePut(key = "'passages_id:'+#result.data.passage_id")
    @CacheEvict(key = "'passage_id:'+#p0",allEntries = true)
    public Result<Passage> post(@RequestParam("title") String title, @RequestParam("sub_title") String sub_title, @RequestParam("content") String content,@RequestParam("cover")String cover,@RequestParam("tags") Integer[] tags) {
        Result<Passage> result = new Result<Passage>(403, 0L, null);
        try {
            if (imageUtil.transaction) {
                Passage passage = new Passage(title, sub_title, content, cover);
                passageService.save(passage);
                for (int tag : tags) {
                    passageTagService.save(new PassageTag(tag, passage.getPassage_id()));
                }
                result = new Result<Passage>(200, 0L, passage);
            }
        } catch (Exception e) {
            imageUtil.deleteLastImage();
            result = new Result<Passage>(404, 0L, null);
        } finally {
            imageUtil.transaction = false;
        }
        return result;
    }

    @GetMapping("/delete")
    @CacheEvict(key = "'passage_id:'+#p0",allEntries = true)
    @RequiresRoles(value = {"admin"})
    public Result<String> delete(@RequestParam("passage_id")Long passage_id){
        return passageService.delete(passage_id);
    }
}

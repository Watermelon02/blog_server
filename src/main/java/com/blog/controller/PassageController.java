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
@CrossOrigin(originPatterns = "*", allowCredentials = "true", allowedHeaders = "*", methods = {})
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
    public Result<List<Passage>> select(@RequestParam("currentPage") Integer currentPage) {
        return passageService.select(currentPage);
    }

    @GetMapping("/selectByTag")
    @Cacheable(key = "'passages_tag:'+#p0+':'+#p1")
    public Result<List<Passage>> selectByTag(@RequestParam("tagId") Integer tagId, @RequestParam("currentPage") Integer currentPage) {
        return passageService.selectByTagsAndPage(tagId, currentPage);
    }

    @GetMapping("/selectLike")
    public Result<List<Passage>> selectLike(@RequestParam("keyword") String keyword, @RequestParam("currentPage") Integer currentPage) {
        return passageService.selectLike("%" + keyword + "%", currentPage);
    }

    @GetMapping("/selectById")
    @Cacheable(key = "'passages_id:'+#p0")
    public Result<Passage> selectById(@RequestParam("passageId") Long passageId) {
        return passageService.selectOne(passageId);
    }

    @RequiresRoles(value = {"admin"}, logical = Logical.AND)
    @PostMapping("/update")
    @CachePut(key = "'passages_id:'+#p0.passageId")
    @CacheEvict(key = "'passage_id:'+#p0",allEntries = true)
    public int update(@RequestBody Passage passage) {
        return passageService.update(passage);
    }

    @Transactional
    @RequiresRoles(value = {"admin"})
    @PostMapping("/post")
    @CachePut(key = "'passages_id:'+#result.data.passageId")
    @CacheEvict(key = "'passage_id:'+#p0",allEntries = true)
    public Result<Passage> post(@RequestParam("title") String title, @RequestParam("subTitle") String subTitle, @RequestParam("content") String content, @RequestParam("cover")String cover, @RequestParam("tags") Integer[] tags) {
        Result<Passage> result = new Result<Passage>(403, 0L, null);
        try {
            if (imageUtil.transaction) {
                Passage passage = new Passage(title, subTitle, content, cover);
                passageService.save(passage);
                for (int tag : tags) {
                    passageTagService.save(new PassageTag(tag, passage.getPassageId()));
                }
                result = new Result<Passage>(200, 0L, passage);
            }
        } catch (Exception e) {
            imageUtil.deleteLastImage();
            result = new Result<Passage>(404, 0L, null);
            e.printStackTrace();
        } finally {
            imageUtil.transaction = false;
        }
        return result;
    }

    @GetMapping("/delete")
    @CacheEvict(key = "'passage_id:'+#p0",allEntries = true)
    @RequiresRoles(value = {"admin"})
    public Result<String> delete(@RequestParam("passageId")Long passageId){
        return passageService.delete(passageId);
    }
}

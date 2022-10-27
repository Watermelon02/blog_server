package com.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.domain.response.Result;
import com.blog.mapper.TagMapper;
import com.blog.service.PassageService;
import com.blog.domain.Passage;
import com.blog.mapper.PassageMapper;
import com.blog.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

import static com.blog.aop.VisitorAspect.VISITOR_PASSAGE;

/**
 * @author xigua
 * @description 针对表【passage】的数据库操作Service实现
 * @createDate 2022-09-16 10:22:03
 */
@Service
public class PassageServiceImpl extends ServiceImpl<PassageMapper, Passage>
        implements PassageService {
    private static final int HOME_PASSAGE_SIZE = 4;
    private static final int SEARCH_PASSAGE_SIZE = 6;
    @Autowired
    private PassageMapper passageMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ImageUtil imageUtil;

    @Transactional
    @Override
    public boolean save(Passage entity) {
        return super.save(entity);
    }

    @Override
    public Result<List<Passage>> select(Integer curPage) {
        Page<Passage> page = new Page<>(curPage, HOME_PASSAGE_SIZE);
        List<Passage> passages = passageMapper.selectByPage(page).getRecords();
        /**在业务逻辑中查询当前页数的passage对应的tags，不使用联合查询，因为分页多表查询会出现分页中记录比size小的问题
         * */
        for (Passage passage : passages) {
            passage.setTags(tagMapper.selectTagByPassageId(passage.getPassage_id()));
        }
        return new Result<List<Passage>>(200, page.getTotal(), passages);
    }

    @Override
    public Result<List<Passage>> selectByTagsAndPage(Integer tag_id, Integer curPage) {
        Page<Passage> page = new Page<>(curPage, SEARCH_PASSAGE_SIZE);
        List<Passage> passages = passageMapper.selectByTagAndPage(tag_id, page).getRecords();
        return new Result<List<Passage>>(200, page.getTotal(), passages);
    }

    @Override
    public Result<Passage> selectOne(Long passage_id) {
        Passage passage = passageMapper.selectById(passage_id);
        passage.setTags(tagMapper.selectTagByPassageId(passage.getPassage_id()));
        passage.setVisitor(redisTemplate.opsForValue().get(VISITOR_PASSAGE + passage_id));
        return new Result<Passage>(200, 1L, passage);
    }

    @Override
    public Result<List<Passage>> selectLike(String keyword, Integer curPage) {
        Page<Passage> page = new Page<>(curPage, SEARCH_PASSAGE_SIZE);
        List<Passage> passages = passageMapper.selectLike(keyword, page).getRecords();
        return new Result<>(200, page.getTotal(), passages);
    }

    @Override
    public int update(Passage passage) {
        return passageMapper.updateById(passage);
    }

    @Override
    public Result<String> delete(Long passage_id) {
        try {
            Passage passage = passageMapper.selectById(passage_id);
            //删除封面
            File file = new File("./" + imageUtil.getImagePath(passage.getCover()));
            file.deleteOnExit();
            passageMapper.deleteById(passage_id);
            return new Result<String>(200, 0L, "success");
        } catch (Exception e) {
            return new Result<String>(200, 0L, "failure");
        }
    }
}





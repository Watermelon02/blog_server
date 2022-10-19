package com.blog.service;

import com.blog.domain.Passage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.response.Result;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author xigua
* @description 针对表【passage】的数据库操作Service
* @createDate 2022-09-16 10:22:03
*/
public interface PassageService extends IService<Passage> {
    Result<List<Passage>> select(Integer page);

    Result<List<Passage>> selectByTagsAndPage(Integer tag, Integer curPage);

    Result<Passage> selectOne(Long passage_id);

    Result<List<Passage>> selectLike(String keyword, Integer curPage);

    int update(Passage update);

    Result<String> delete(Long passage_id);
}

package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.PassageTag;
import org.springframework.transaction.annotation.Transactional;

/**
* @author xigua
* @description 针对表【passage_tag】的数据库操作Service
* @createDate 2022-09-17 15:31:26
*/
public interface PassageTagService extends IService<PassageTag> {
    @Transactional
    @Override
    default boolean save(PassageTag entity) {
        return IService.super.save(entity);
    }
}

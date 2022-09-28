package com.blog.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.domain.PassageTag;
import com.blog.mapper.PassageTagMapper;
import com.blog.service.PassageTagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xigua
 * @description 针对表【passage_tag】的数据库操作Service实现
 * @createDate 2022-09-17 15:31:26
 */
@Service
public class PassageTagServiceImpl extends ServiceImpl<PassageTagMapper, PassageTag>
        implements PassageTagService {
    @Transactional
    @Override
    public boolean save(PassageTag entity) {
        return PassageTagService.super.save(entity);
    }
}





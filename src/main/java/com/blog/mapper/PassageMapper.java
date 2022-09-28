package com.blog.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blog.domain.Passage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author xigua
 * @description 针对表【passage】的数据库操作Mapper
 * @createDate 2022-09-16 10:22:03
 * @Entity generator.domain.Passage
 */
@Component
@Mapper
public interface PassageMapper extends BaseMapper<Passage> {
    int insertAll(Passage passage);

    IPage<Passage> selectByPage(IPage<Passage> page);

    IPage<Passage> selectByTagAndPage(@Param("tag_id") Serializable tag_id, @Param("page") IPage<Passage> page);

    IPage<Passage> selectLike(@Param("keyword") String keyword,IPage<Passage> page);
}





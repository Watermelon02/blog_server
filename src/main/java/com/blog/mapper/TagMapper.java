package com.blog.mapper;
import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.domain.Tag;
import org.springframework.stereotype.Component;

/**
* @author xigua
* @description 针对表【tag】的数据库操作Mapper
* @createDate 2022-09-17 15:31:26
* @Entity .domain.Tag
*/
@Component
@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    public List<Tag> selectTagByPassageId(@Param("passage_id") Serializable passage_id);
}





package com.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.domain.Tag;
import com.blog.mapper.TagMapper;
import com.blog.service.TagService;
import org.springframework.stereotype.Service;

/**
* @author xigua
* @description 针对表【tag】的数据库操作Service实现
* @createDate 2022-09-17 15:31:26
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService {

}





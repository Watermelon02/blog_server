package com.blog.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
* @author xigua
* @description 针对表【user】的数据库操作Mapper
* @createDate 2022-09-27 18:44:37
* @Entity .domain.com.blog.domain.User
*/
@Component
@Mapper
public interface UserMapper extends BaseMapper<User> {
    User selectByEmailEquals(@Param("email") String email);

    User selectByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    User selectByUserId(@Param("user_id")Long user_id);
}





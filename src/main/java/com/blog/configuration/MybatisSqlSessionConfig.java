/*
package com.blog.configuration;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;


@Configuration
public class MybatisSqlSessionConfig {
    @Bean
    public String myInterceptor(SqlSessionFactory sqlSessionFactory) {
        // 实例化插件
        MybatisInterceptor sqlInterceptor = new MybatisInterceptor();
        // 创建属性值
        Properties properties = new Properties();
        properties.setProperty("prop", "value");
        // 将属性值设置到插件中
        sqlInterceptor.setProperties(properties);
        // 将插件添加到SqlSessionFactory工厂
        sqlSessionFactory.getConfiguration().addInterceptor(sqlInterceptor);
        return "interceptor";
    }
}*/

package com.blog.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class VisitorAspect {
    public static final String VISITOR_TOTAL = "visitor.total";
    public static final String VISITOR_PASSAGE = "visitor.passage:";
    @Autowired
    StringRedisTemplate redisTemplate;

    @Pointcut("execution(public * com.blog.controller.PassageController.*(*))")
    public void anyVisitPointcut() {
    }

    @Pointcut("execution(public * com.blog.controller.PassageController.selectById(*))")
    public void passageVisit() {
    }

    /**
     * 统计总访问人数
     */
    @After("anyVisitPointcut()")
    public void countVisitor() {
        redisTemplate.opsForValue().increment(VISITOR_TOTAL);
    }

    /**
     * 统计单篇博客浏览量
     */
    @After("passageVisit()")
    public void countPassageVisit(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long passageId = (Long) args[0];
        redisTemplate.opsForValue().increment(VISITOR_PASSAGE + passageId);
    }
}

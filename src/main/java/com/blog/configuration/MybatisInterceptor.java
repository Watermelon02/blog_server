package com.blog.configuration;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

@Intercepts({
        // type指定代理的是那个对象，method指定代理Executor中的那个方法,args指定Executor中的query方法都有哪些参数对象
        // 由于Executor中有两个query，因此需要两个@Signature
        // 需要代理的对象和方法
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        // 需要代理的对象和方法
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class MybatisInterceptor implements Interceptor {

    // 日志打印
    private static final Logger LOGGER = LoggerFactory.getLogger(MybatisInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            // 获得查询方法的参数，比如selectById(Integer id,String name)，那么就可以获取到四个参数分别是：
            // {id:1,name:"user1",param1:1,param2:"user1"}
            parameter = invocation.getArgs()[1];
        }
        // 获得mybatis的*mapper.xml文件中映射的方法，如：com.best.dao.UserMapper.selectById
        String sqlId = mappedStatement.getId();

        // 将参数和映射文件组合在一起得到BoundSql对象
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        // 获取配置信息
        Configuration configuration = mappedStatement.getConfiguration();
        Object returnValue = null;

        LOGGER.debug("##########[begin]调用Dao层接口的方法名：" + sqlId + "##########");
        // 通过配置信息和BoundSql对象来生成带值得sql语句
        String sql = getCompleteSql(configuration, boundSql);
        // 打印sql语句
        LOGGER.debug("##########[SQL完整语句]：" + sql);
        // 先记录执行sql语句前的时间
        long start = System.currentTimeMillis();
        try {
            // 开始执行sql语句
            returnValue = invocation.proceed();
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        // 记录执行sql语句后的时间
        long end = System.currentTimeMillis();
        // 得到执行sql语句的用了多长时间
        long time = (end - start);
        // 以毫秒为单位打印
        LOGGER.debug("##########[end]SQL执行时间：" + time + "毫秒##########");
        // 返回值，如果是多条记录，那么此对象是一个list，如果是一个bean对象，那么此处就是一个对象，也有可能是一个map
        return returnValue;
    }

    /**
     * 如果是字符串对象则加上单引号返回，如果是日期则也需要转换成字符串形式，如果是其他则直接转换成字符串返回。
     *
     * @param obj
     * @return
     * @author zhouziyu
     */
    private static String getParameterValue(Object obj) {
        String value;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(obj) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "''";
            }

        }
        return value;
    }

    /**
     * 生成对应的带有值得sql语句
     *
     * @param configuration
     * @param boundSql
     * @return
     */
    public static String getCompleteSql(Configuration configuration, BoundSql boundSql) {

        // 获得参数对象，如{id:1,name:"user1",param1:1,param2:"user1"}
        Object parameterObject = boundSql.getParameterObject();
        // 获得映射的对象参数
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        // 获得带问号的sql语句
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        // 如果参数个数大于0且参数对象不为空，说明该sql语句是带有条件的
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            // 检查该参数是否是一个参数
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                // getParameterValue用于返回是否带有单引号的字符串，如果是字符串则加上单引号
                // 如果是一个参数则只替换一次，将问号直接替换成值
                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));

            } else {
                // 将映射文件的参数和对应的值返回，比如：id，name以及对应的值。
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                // 遍历参数，如:id,name等
                for (ParameterMapping parameterMapping : parameterMappings) {
                    // 获得属性名，如id,name等字符串
                    String propertyName = parameterMapping.getProperty();
                    // 检查该属性是否在metaObject中
                    if (metaObject.hasGetter(propertyName)) {
                        // 如果在metaObject中，那么直接获取对应的值
                        Object obj = metaObject.getValue(propertyName);
                        // 然后将问号?替换成对应的值。
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    }
                }
            }
        }
        // 最后将sql语句返回
        return sql;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        LOGGER.debug("创建属性值:" + properties);
    }
}
package org.jinzijun.mybatis.session;

import java.io.Closeable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * 这是MyBatis主要的一个类，用来执行SQL，获取映射器，管理事务
 *
 * 通常情况下，我们在应用程序中使用的Mybatis的API就是这个接口定义的方法。
 *
 */
public interface SqlSession extends AutoCloseable{

    /**
     * Retrieves a mapper.
     * 得到映射器
     * 这个巧妙的使用了泛型，使得类型安全
     * 到了MyBatis 3，还可以用注解,这样xml都不用写了
     * @param <T> the mapper type
     * @param type Mapper interface class
     * @return a mapper bound to this SqlSession
     */
    <T> T getMapper(Class<T> type);

}
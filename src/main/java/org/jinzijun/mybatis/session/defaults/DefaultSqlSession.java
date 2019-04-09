package org.jinzijun.mybatis.session.defaults;

import org.jinzijun.mybatis.binding.MapperProxy;
import org.jinzijun.mybatis.session.Configuration;
import org.jinzijun.mybatis.session.SqlSession;

import java.io.Closeable;
import java.lang.reflect.Proxy;
import java.sql.Connection;

public class DefaultSqlSession implements SqlSession {

    private final Configuration configuration;

    private final Connection connection;

    public DefaultSqlSession(Configuration configuration, Connection connection) {
        this.configuration = configuration;
        this.connection =  connection;
    }

    /**
     * 获取mapper代理对象
     */
    @Override
    public <T> T getMapper(Class<T> type) {
        return (T)Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, new MapperProxy<T>(configuration,connection));
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}

package org.jinzijun.mybatis;

import org.jinzijun.mybatis.builder.xml.XMLConfigBuilder;
import org.jinzijun.mybatis.io.Resources;
import org.jinzijun.mybatis.session.Configuration;
import org.jinzijun.mybatis.session.defaults.DefaultSqlSessionFactory;

import java.io.InputStream;

/**
 * 构建SqlSessionFactory的builder
 * @author jinzijun
 */
public class SqlSessionFactoryBuilder {
    private Configuration configuration;

    public SqlSessionFactory build(InputStream inputStream) {
        XMLConfigBuilder parser = new XMLConfigBuilder(inputStream);
        return build(parser.parse());
    }

    public SqlSessionFactory build(Configuration config) {
        return new DefaultSqlSessionFactory(config);
    }

}

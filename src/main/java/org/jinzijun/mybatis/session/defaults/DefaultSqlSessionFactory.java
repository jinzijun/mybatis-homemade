package org.jinzijun.mybatis.session.defaults;

import org.jinzijun.mybatis.SqlSessionFactory;
import org.jinzijun.mybatis.session.Configuration;
import org.jinzijun.mybatis.session.DataSourceConfig;
import org.jinzijun.mybatis.session.SqlSession;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        DataSourceConfig config = configuration.getDataSourceConfig();
        Connection conn = null;
        try {
            Class.forName(config.getDriver());
            conn = DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("unsupport driver type");
        } catch (SQLException e) {
            throw new RuntimeException("create sql session fail;",e);
        }
        return new DefaultSqlSession(configuration,conn);
    }
}

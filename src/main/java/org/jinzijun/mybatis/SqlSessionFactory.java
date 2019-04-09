package org.jinzijun.mybatis;

import org.jinzijun.mybatis.session.SqlSession;

public interface SqlSessionFactory {
    SqlSession openSession();
}

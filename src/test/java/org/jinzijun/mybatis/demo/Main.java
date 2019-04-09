package org.jinzijun.mybatis.demo;

import org.jinzijun.mybatis.SqlSessionFactory;
import org.jinzijun.mybatis.SqlSessionFactoryBuilder;
import org.jinzijun.mybatis.io.Resources;
import org.jinzijun.mybatis.session.SqlSession;

public class Main {

    public static void main(String[] args) throws Exception{
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-config.xml"));
        SqlSession sqlSession = sessionFactory.openSession();
        PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);
        Person person = mapper.getById(1);
        System.out.println(person);
    }
}
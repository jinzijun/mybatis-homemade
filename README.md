# mybatis-homemade
仿制的mybatis

已实现的mybatis功能：
1. 读取配置文件
2. 动态代理执行数据库操作
3. 将查询结果解析成bean

可以达到如下效果
```
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-config.xml"));
        SqlSession sqlSession = sessionFactory.openSession();
        PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);
        Person person = mapper.getById(1);
```
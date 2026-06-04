package org.example.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

public class SqlSessionFactoryUtil
{
    private static SqlSessionFactory factory;

    static {
        try {
            // 加载 MyBatis 配置文件
            String resource = "mybatis-config.xml";
            Reader reader = Resources.getResourceAsReader(resource);
            factory = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return factory;
    }

    private SqlSessionFactoryUtil() {
    }

    private static class Holder {
        private static final SqlSessionFactoryUtil INSTANCE = new SqlSessionFactoryUtil();
    }

    public static SqlSessionFactoryUtil getInstance() {
        return Holder.INSTANCE;
    }
}

package org.example.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.LocalDateTimeTypeHandler;
import org.apache.ibatis.type.TypeHandler;
import org.example.handler.DoubleArrayTypeHandler;
import org.example.handler.SamplingPeriodArrayTypeHandler;
import org.example.handler.TextArrayTypeHandler;
import org.example.handler.JtsGeometryTypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
public class MyBatisConfig
{
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        sessionFactory.setMapperLocations(
                new PathMatchingResourcePatternResolver()
                        .getResources("classpath:mapper/*.xml"));

        // 注册自定义类型处理器
        sessionFactory.setTypeHandlers(new TypeHandler[]{
                new DoubleArrayTypeHandler(),
                new LocalDateTimeTypeHandler(),
                new org.apache.ibatis.type.DateTypeHandler(),  // 处理Date类型
                new org.apache.ibatis.type.SqlTimestampTypeHandler(),  // 处理Timestamp类型
                new SamplingPeriodArrayTypeHandler(), //枚举数组处理器
                new TextArrayTypeHandler(),
                new JtsGeometryTypeHandler()
        });
        return sessionFactory.getObject();
    }
}

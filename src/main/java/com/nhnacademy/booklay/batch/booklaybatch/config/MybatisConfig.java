package com.nhnacademy.booklay.batch.booklaybatch.config;

import com.nhnacademy.booklay.batch.booklaybatch.dto.DatasourceInfo;
import java.io.IOException;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@MapperScan(basePackages = "**.mapper.**", sqlSessionFactoryRef = "sqlSessionFactoryBean")
public class MybatisConfig {
    @Bean
    public DataSource dataSource(DatasourceInfo datasourceInfo) {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUsername(datasourceInfo.getUsername());
        basicDataSource.setPassword(datasourceInfo.getPasswword());
        basicDataSource.setUrl(datasourceInfo.getDbUrl());
        basicDataSource.setDriverClassName(com.mysql.cj.jdbc.Driver.class.getName());
        //숙제 팀원들에게 설명하기 (dbcp 최적화) yml이랑 맞춰야 함
        basicDataSource.setInitialSize(2);
        basicDataSource.setMaxTotal(2);
        basicDataSource.setMaxIdle(2);
        basicDataSource.setMinIdle(2);
        //둘은 같이 써야 함
        basicDataSource.setValidationQuery("SELECT 1");
        basicDataSource.setTestOnBorrow(true);
        //dbcp 최적화

        return basicDataSource;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource(null)); //null 넣으면 자동으로 넣어줌
        sessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:**/maps/*.xml"));
        sessionFactoryBean.setConfigLocation(resolver.getResource("classpath:mybatis-config.xml"));

        //xml 읽어서 데이터소스 읽어서 매핑(?)한다
        //xml쿼리로 매핑하는 매개체 필요함
        return sessionFactoryBean;
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return new DataSourceTransactionManager(dataSource(null));
    }
}

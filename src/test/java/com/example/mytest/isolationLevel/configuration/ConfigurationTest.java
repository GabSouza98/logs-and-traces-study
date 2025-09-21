package com.example.mytest.isolationLevel.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Profile("test")
@TestConfiguration
public class ConfigurationTest {

//    @Bean
//    @Primary
//    public PlatformTransactionManager transactionManager(DataSource dataSource) {
//        return new JdbcTransactionManager(dataSource);
//    }

//    @Bean
//    @Primary
//    public PlatformTransactionManager transactionManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }

//    @Bean
//    @Primary
//    public PlatformTransactionManager transactionManager(DataSource dataSource) {
//        return new JpaTransactionManager(dataSource);
//    }

}

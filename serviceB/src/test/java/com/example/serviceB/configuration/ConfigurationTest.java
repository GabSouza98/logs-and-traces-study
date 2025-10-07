package com.example.serviceB.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;

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

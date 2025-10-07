package com.example.serviceB.configuration;

import com.example.serviceB.repository.StatusRepository;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.spi.JdbiPlugin;
import org.jdbi.v3.core.statement.Slf4JSqlLogger;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class JdbiConfiguration {

    @Bean
    public JdbiPlugin sqlObjectPlugin() {
        return new SqlObjectPlugin();
    }

    @Bean
    public Jdbi jdbi(DataSource dataSource, List<JdbiPlugin> jdbiPlugins) {
        TransactionAwareDataSourceProxy dataSourceProxy = new TransactionAwareDataSourceProxy(dataSource);
        Jdbi jdbi = Jdbi.create(dataSourceProxy);

        jdbiPlugins.forEach(jdbi::installPlugin);

        jdbi.setSqlLogger(new Slf4JSqlLogger());

        return jdbi;
    }

    @Bean
    public StatusRepository statusRepository(Jdbi jdbi) {
        return jdbi.onDemand(StatusRepository.class);
    }

}

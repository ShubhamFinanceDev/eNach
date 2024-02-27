package com.enach.DataBaseconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class JdbcConfig {

    @Autowired
    public Environment environment;

    @Bean(name = "jdbcDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.jdbc")
    public DataSource dataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setUrl(environment.getProperty("spring.datasource.jdbc.url"));
        dataSource.setDriverClassName(environment.getProperty("spring.datasource.jdbc.driver-class-name"));
        dataSource.setUsername(environment.getProperty("spring.datasource.jdbc.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource.jdbc.password"));



        return dataSource;
    }

    @Bean(name = "jdbcJdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("jdbcDataSource") DataSource dataSource) {
          return new JdbcTemplate(dataSource);
    }

}

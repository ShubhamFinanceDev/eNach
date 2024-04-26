package com.enach.DataBaseconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class OracleDataSourceConfig {


    @Value("${spring.datasource.oracle.url}")
    private String url;

    @Value("${spring.datasource.oracle.username}")
    private String username;

    @Value("${spring.datasource.oracle.password}")
    private String password;

    @Value("${spring.datasource.oracle.driver-class-name}")
    private String driverClassName;

    @Bean(name = "jdbcJdbcTemplate")
    public JdbcTemplate jdbcTemplate() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return new JdbcTemplate(dataSource);
    }

}

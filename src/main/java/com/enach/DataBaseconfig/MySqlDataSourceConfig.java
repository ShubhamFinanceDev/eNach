package com.enach.DataBaseconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "mysqlEntityManagerFactoryBean",
        transactionManagerRef = "mysqlTransactionManager",
        basePackages = {"com.enach.Repository"}
)
public class MySqlDataSourceConfig {

    @Autowired
    public Environment environment;

    @Bean(name ="mysqlDataSource")
    @Primary
    public  DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setUrl(environment.getProperty("spring.datasource.mysql.url"));
        dataSource.setDriverClassName(environment.getProperty("spring.datasource.mysql.driver-class-name"));
        dataSource.setUsername(environment.getProperty("spring.datasource.mysql.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource.mysql.password"));

        return dataSource;
    }

    @Bean(name = "mysqlEntityManagerFactoryBean")
    @Primary
    public LocalContainerEntityManagerFactoryBean jpaEntityManagerFactoryBean(){

        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource());
        JpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        bean.setJpaVendorAdapter(adapter);
        HashMap<String, String> props = new HashMap<>();


        bean.setJpaPropertyMap(props);
        bean.setPackagesToScan("com.enach.Entity");

        return  bean;

    }


    @Primary
    @Bean(name = "mysqlTransactionManager")
    public PlatformTransactionManager jpaTransactionManager(){
        JpaTransactionManager manager = new JpaTransactionManager();
        manager.setEntityManagerFactory(jpaEntityManagerFactoryBean().getObject());

        return manager;
    }

}

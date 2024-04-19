package com.scraper.config;


import javax.sql.DataSource;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = "com.scraper.dao")
public class JpaConfig {

    private final DbConfig dbConfig;

    public JpaConfig(
            @Autowired DbConfig dbConfig
    ) {
        this.dbConfig = dbConfig;
    }

    @Bean
    public DataSource dataSource() {
        String dbUrl = "jdbc:postgresql://" + dbConfig.getDbHost() + ":" + dbConfig.getDbPort() +
            "/" + dbConfig.getDbName() + "?&targetServerType=master&prepareThreshold=0&ssl=true&sslmode=verify-full" +
            "&sslrootcert=.pg/root.crt";
        org.postgresql.ds.PGSimpleDataSource ds = new org.postgresql.ds.PGSimpleDataSource();// .ds. PGSimpleDataSource();
        ds.setUrl(dbUrl);
        ds.setUser(dbConfig.getDbUser());
        ds.setPassword(dbConfig.getDbPass());
//        ds.setSslMode("require");
//        ds.setSslfactory("org.postgresql.ssl.NonValidatingFactory");
        return ds;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        bean.setPackagesToScan("com.scraper.models", "com.scraper.dao");
//        bean.setPersistenceUnitName("PersistenceUnit");
        bean.setDataSource(dataSource());
        ((HibernateJpaVendorAdapter) bean.getJpaVendorAdapter()).setGenerateDdl(true);
//        bean.ddlGenerate = true;
        return bean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }
}

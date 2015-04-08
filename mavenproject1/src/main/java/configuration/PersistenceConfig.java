package configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import java.util.Collection;
import java.util.HashSet;

@Configuration
@ComponentScan(basePackages = {"persistence"}, includeFilters = @ComponentScan.Filter(value = {org.springframework.stereotype.Repository.class}, type = FilterType.ANNOTATION))
@EnableTransactionManagement
public class PersistenceConfig {

    @Value("${jdbc.driver}")
    private String driverClassName;
    @Value("${jdbc.connection.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;
    @Value("${hibernate.dialect}")
    private String databasePlatform;

    @Bean
    public DataSource dataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(driverClassName);
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setInitialSize(100);
        ds.setMaxActive(100);
        return ds;
    }

    @Bean
    public PersistenceUnitManager persistenceUnitManager() {
        DefaultPersistenceUnitManager pum = new DefaultPersistenceUnitManager();
        pum.setDefaultDataSource(this.dataSource());
        return pum;
    }

    @Bean
    public SimpleCacheManager cacheManager() {
        SimpleCacheManager cm = new SimpleCacheManager();
        Collection<ConcurrentMapCache> caches = new HashSet<>();
        ConcurrentMapCache managers = new ConcurrentMapCache("managers");
        caches.add(managers);
        ConcurrentMapCache plays = new ConcurrentMapCache("plays");
        caches.add(plays);
        ConcurrentMapCache users = new ConcurrentMapCache("users");
        caches.add(users);
        ConcurrentMapCache places = new ConcurrentMapCache("places");
        caches.add(places);
        cm.setCaches(caches);
        cm.afterPropertiesSet();
        return cm;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean fb = new LocalContainerEntityManagerFactoryBean();
        fb.setPackagesToScan("model");
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabasePlatform(databasePlatform);
        jpaVendorAdapter.setShowSql(false);
        jpaVendorAdapter.setGenerateDdl(true);
        jpaVendorAdapter.postProcessEntityManagerFactory(fb.getObject());
        fb.setJpaVendorAdapter(jpaVendorAdapter);
        fb.setPersistenceUnitManager(this.persistenceUnitManager());
        return fb;
    }

    @Bean
    public JpaTransactionManager jpaTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(this.entityManagerFactory().getObject());
        transactionManager.setDataSource(this.dataSource());
        return transactionManager;
    }
}

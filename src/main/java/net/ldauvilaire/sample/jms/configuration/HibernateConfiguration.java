package net.ldauvilaire.sample.jms.configuration;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.jta.JtaTransactionManager;

import net.ldauvilaire.sample.jms.hibernate.SampleInterceptor;

@Configuration
public class HibernateConfiguration extends HibernateJpaAutoConfiguration {

    @Autowired
    SampleInterceptor sampleInterceptor;

    public HibernateConfiguration(
            DataSource dataSource,
            JpaProperties jpaProperties,
            ObjectProvider<JtaTransactionManager> jtaTransactionManagerProvider) {
        super(dataSource, jpaProperties, jtaTransactionManagerProvider);
    }

    @Override
    protected void customizeVendorProperties(Map<String, Object> vendorProperties) {
        vendorProperties.put("hibernate.ejb.interceptor", sampleInterceptor);
    }
}
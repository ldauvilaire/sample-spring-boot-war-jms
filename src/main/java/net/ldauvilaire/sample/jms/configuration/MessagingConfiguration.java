package net.ldauvilaire.sample.jms.configuration;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.jndi.JndiTemplate;

@Configuration
@EnableJms
public class MessagingConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessagingConfiguration.class);

	@Value("${jndi.naming.factory.initial}")
	private String jndiNamingFactoryInitial;

	@Value("${jndi.naming.factory.url.pkgs}")
	private String jndiNamingFactoryUrlPkgs;

	@Value("${jndi.naming.provider.url}")
	private String jndiNamingProviderUrl;

	@Value("${spring.jms.jndi-name}")
	private String jmsConnectionFactoryName;

	@Bean(name="jndiTemplate")
	public JndiTemplate jndiTemplate() throws NamingException {

		LOGGER.info("*** JNDI Template - {} = [{}] ***", Context.INITIAL_CONTEXT_FACTORY, jndiNamingFactoryInitial);
		LOGGER.info("*** JNDI Template - {} = [{}] ***", Context.URL_PKG_PREFIXES, jndiNamingFactoryUrlPkgs);
		LOGGER.info("*** JNDI Template - {} = [{}] ***", Context.PROVIDER_URL, jndiNamingProviderUrl);

		Properties environment = new Properties();
		if (   (jndiNamingFactoryInitial != null)
		    && (! jndiNamingFactoryInitial.trim().isEmpty()) ) {
			environment.put(Context.INITIAL_CONTEXT_FACTORY, jndiNamingFactoryInitial.trim());
		}
		if (   (jndiNamingFactoryUrlPkgs != null)
			    && (! jndiNamingFactoryUrlPkgs.trim().isEmpty()) ) {
			environment.put(Context.URL_PKG_PREFIXES, jndiNamingFactoryUrlPkgs.trim());
		}
		if (   (jndiNamingProviderUrl != null)
			    && (! jndiNamingProviderUrl.trim().isEmpty()) ) {
			environment.put(Context.PROVIDER_URL, jndiNamingProviderUrl.trim());
		}

		JndiTemplate jndiTemplate = new JndiTemplate();
		jndiTemplate.setEnvironment(environment);

		return jndiTemplate;
	}

	@Bean(name="jndiConnectionFactory")
	public JndiObjectFactoryBean jndiConnectionFactory(
			@Qualifier("jndiTemplate") JndiTemplate jndiTemplate) throws NamingException {

		JndiObjectFactoryBean connectionFactoryBean = new JndiObjectFactoryBean();
		connectionFactoryBean.setJndiTemplate(jndiTemplate);
		connectionFactoryBean.setJndiName(jmsConnectionFactoryName);
		connectionFactoryBean.afterPropertiesSet();
		return connectionFactoryBean;
	}

	@Bean(name="jmsConnectionFactory")
	@Primary
	public CachingConnectionFactory jmsConnectionFactory(
			@Qualifier("jndiConnectionFactory") JndiObjectFactoryBean jndiConnectionFactory) {

		CachingConnectionFactory cachingConnectionFactory =  new CachingConnectionFactory();
		ConnectionFactory connectionFactory = (ConnectionFactory) jndiConnectionFactory.getObject();
		cachingConnectionFactory.setTargetConnectionFactory(connectionFactory);
		cachingConnectionFactory.afterPropertiesSet();
		return cachingConnectionFactory;
	}
}

package net.ldauvilaire.sample.jms.configuration;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.annotation.EnableJms;

@Configuration
@EnableJms
@ComponentScan(basePackages = { "net.ldauvilaire.sample" })
@PropertySource("classpath:application.properties")
public class ApplicationTestConfiguration {

    static {
        System.setProperty("org.jboss.logging.provider", "slf4j");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationTestConfiguration.class);

//	private static final String JNDI_ENV_PREFIX = "java:comp/env";

	public static final String BROKER_URL = "vm://localhost";

	@Value("${jndi.naming.factory.initial}")
	private String jndiNamingFactoryInitial;

	@Value("${jndi.naming.factory.url.pkgs}")
	private String jndiNamingFactoryUrlPkgs;

	@Value("${jndi.naming.provider.url}")
	private String jndiNamingProviderUrl;

	@Value("${spring.jms.jndi-name}")
	private String jmsConnectionFactoryName;

	@Value("${jms.queue.first}")
	private String jmsQueueFirst;

	@Value("${jms.queue.second}")
	private String jmsQueueSecond;

	@PostConstruct
	public void postConstruct() throws NamingException {

		LOGGER.info("--- POST CONSTRUCT - Start ---------------------------------------------------------");

		LOGGER.info("--- JNDI Config - {} = [{}] ---", Context.INITIAL_CONTEXT_FACTORY, jndiNamingFactoryInitial);
		LOGGER.info("--- JNDI Config - {} = [{}] ---", Context.URL_PKG_PREFIXES, jndiNamingFactoryUrlPkgs);
		LOGGER.info("--- JNDI Config - {} = [{}] ---", Context.PROVIDER_URL, jndiNamingProviderUrl);

		LOGGER.info("--- JMS Connnection Factory = [{}] ---", jmsConnectionFactoryName);
		LOGGER.info("--- JMS Queue First = [{}] ---", jmsQueueFirst);
		LOGGER.info("--- JMS Queue Second = [{}] ---", jmsQueueSecond);

		List<String> subContexts = new ArrayList<String>();
		{
			String[] jmsNames = { jmsConnectionFactoryName, jmsQueueFirst, jmsQueueSecond };

			subContexts.add("java:");
			subContexts.add("java:/comp");
			subContexts.add("java:/comp/env");

			for (String jmsName : jmsNames) {
				String[] namingParts = jmsName.split("/");
//				StringBuilder namingBuilder = new StringBuilder(JNDI_ENV_PREFIX);
				StringBuilder namingBuilder = new StringBuilder();
				int nbParts = namingParts.length;
				for (int i=0; i<(nbParts-1); i++) {
					String namingPart = namingParts[i];
					namingBuilder.append(namingPart);
					String subContext = namingBuilder.toString();
					if (! subContexts.contains(subContext)) {
						subContexts.add(subContext);
					}
					namingBuilder.append('/');
				}
			}
		}

		Hashtable<String, Object> environment = new Hashtable<String, Object>();
		{
			environment.put(Context.INITIAL_CONTEXT_FACTORY, jndiNamingFactoryInitial);
			environment.put(Context.URL_PKG_PREFIXES, jndiNamingFactoryUrlPkgs);
		}
		InitialContext ic = new InitialContext(environment);
		for (String subContext : subContexts) {
			LOGGER.info("--- Creating JNDI Sub-Context [{}] ---", subContext);
			ic.createSubcontext(subContext);
		}

		LOGGER.info("--- Binding JMS ConnectionFactory to JNDI Name [{}] ---", jmsConnectionFactoryName);
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL(BROKER_URL);
		ic.bind(jmsConnectionFactoryName, connectionFactory);

		LOGGER.info("--- Binding JMS Queue to JNDI Name [{}] ---", jmsQueueFirst);
		ActiveMQQueue firstQueue = new ActiveMQQueue();
		firstQueue.setPhysicalName("QUEUE.FIRST");
		ic.bind(jmsQueueFirst, firstQueue);

		LOGGER.info("--- Binding JMS Queue to JNDI Name [{}] ---", jmsQueueSecond);
		ActiveMQQueue secondQueue = new ActiveMQQueue();
		secondQueue.setPhysicalName("QUEUE.SECOND");
		ic.bind(jmsQueueSecond, secondQueue);

		LOGGER.info("--- POST CONSTRUCT - End -----------------------------------------------------------");
	}
}

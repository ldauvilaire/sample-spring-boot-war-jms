package net.ldauvilaire.sample.jms.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.boot.autoconfigure.jms.JmsProperties.AcknowledgeMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.transaction.PlatformTransactionManager;

import net.ldauvilaire.sample.jms.MessagingConstants;
import net.ldauvilaire.sample.jms.messaging.converter.JsonPersonMessageConverter;
import net.ldauvilaire.sample.jms.messaging.converter.XmlPersonMessageConverter;

@Configuration
public class MessagingListenerConfiguration {

	@Autowired
	private JmsProperties jmsProperties;

	@Autowired
	private CachingConnectionFactory connectionFactory;

	@Autowired
	private JsonPersonMessageConverter jsonPersonMessageConverter;

	@Autowired
	private XmlPersonMessageConverter xmlPersonMessageConverter;

	@Bean(name=MessagingConstants.JMS_CONTAINER_FIRST_FACTORY)
	public DefaultJmsListenerContainerFactory jmsFirstContainerFactory(PlatformTransactionManager transactionManager) {

		Integer minThread = jmsProperties.getListener().getConcurrency();
		Integer maxThread = jmsProperties.getListener().getMaxConcurrency();
		AcknowledgeMode acknowledgeMode = jmsProperties.getListener().getAcknowledgeMode();

		if (minThread == null) {
			minThread = 1;
		}
		if (maxThread == null) {
			maxThread = minThread;
		}

		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		{
			factory.setConnectionFactory(connectionFactory);
			factory.setConcurrency(minThread.toString() + "-" + maxThread.toString());
			factory.setMessageConverter(jsonPersonMessageConverter);
			factory.setSessionTransacted(true);
			factory.setTransactionManager(transactionManager);
			if (acknowledgeMode != null) {
				factory.setSessionAcknowledgeMode(acknowledgeMode.getMode());
			}
		}

		return factory;
	}

	@Bean(name=MessagingConstants.JMS_CONTAINER_SECOND_FACTORY)
	public DefaultJmsListenerContainerFactory jmsSecondContainerFactory(PlatformTransactionManager transactionManager) {

		Integer minThread = jmsProperties.getListener().getConcurrency();
		Integer maxThread = jmsProperties.getListener().getMaxConcurrency();
		AcknowledgeMode acknowledgeMode = jmsProperties.getListener().getAcknowledgeMode();

		if (minThread == null) {
			minThread = 1;
		}
		if (maxThread == null) {
			maxThread = minThread;
		}

		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		{
			factory.setConnectionFactory(connectionFactory);
			factory.setConcurrency(minThread.toString() + "-" + maxThread.toString());
			factory.setMessageConverter(xmlPersonMessageConverter);
			factory.setSessionTransacted(true);
			factory.setTransactionManager(transactionManager);
			if (acknowledgeMode != null) {
				factory.setSessionAcknowledgeMode(acknowledgeMode.getMode());
			}
		}

		return factory;
	}
}

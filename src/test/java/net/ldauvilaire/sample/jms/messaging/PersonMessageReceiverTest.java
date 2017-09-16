package net.ldauvilaire.sample.jms.messaging;

import java.util.Hashtable;

import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.broker.BrokerService;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import net.ldauvilaire.sample.jms.configuration.ApplicationTestConfiguration;
import net.ldauvilaire.sample.jms.domain.dto.PersonDTO;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationTestConfiguration.class)
public class PersonMessageReceiverTest extends AbstractJUnit4SpringContextTests {

	private static final Logger LOGGER = LoggerFactory.getLogger(PersonMessageReceiverTest.class);

	protected static final ObjectMapper JSON_MAPPER = new ObjectMapper();
	protected static final XmlMapper XML_MAPPER = new XmlMapper();

	private static BrokerService brokerService;

	@Value("${jndi.naming.factory.initial}")
	private String jndiNamingFactoryInitial;

	@Value("${jndi.naming.factory.url.pkgs}")
	private String jndiNamingFactoryUrlPkgs;

	@Value("${spring.jms.jndi-name}")
	private String jmsConnectionFactoryName;

	@Value("${jms.queue.first}")
	private String jmsQueueFirst;

	@Value("${jms.queue.second}")
	private String jmsQueueSecond;

	private InitialContext initialContext;

	@BeforeClass
	public static void beforeClass() throws Exception {
		LOGGER.info("--- Before-Class - Start -----------------------------------------------------------");
		brokerService = new BrokerService();
		brokerService.addConnector(ApplicationTestConfiguration.BROKER_URL);
		brokerService.setUseJmx(false);
		brokerService.setPersistent(false);
		brokerService.start();
		LOGGER.info("--- Before-Class - End -------------------------------------------------------------");
	}

	@AfterClass
	public static void afterClass() throws Exception {
		LOGGER.info("--- After-Class - Start ------------------------------------------------------------");

		brokerService.stop();
		LOGGER.info("--- After-Class - End --------------------------------------------------------------");
	}

	@Before
	public void beforeTest() throws NamingException {
		Hashtable<String, Object> environment = new Hashtable<String, Object>();
		{
			environment.put(Context.INITIAL_CONTEXT_FACTORY, jndiNamingFactoryInitial);
			environment.put(Context.URL_PKG_PREFIXES, jndiNamingFactoryUrlPkgs);
		}
		initialContext = new InitialContext(environment);
	}

	@Test
	public void testReceiverJsonMessage() throws Exception {

		LOGGER.info("--- testReceiverJsonMessage - Start ---------------------------------------------------");

		ConnectionFactory jmsConnectionFactory = null;
		{
			Object objectConnectionFactory = initialContext.lookup(jmsConnectionFactoryName);
			if (objectConnectionFactory instanceof ConnectionFactory) {
				jmsConnectionFactory = (ConnectionFactory) objectConnectionFactory;
			} else {
				LOGGER.error("JNDI Lookup to [{}] did not return a ConnectionFactory object", jmsConnectionFactoryName);
			}
		}

		JmsTemplate jmsTemplate = new JmsTemplate();
		jmsTemplate.setConnectionFactory(jmsConnectionFactory);

		PersonDTO dto = new PersonDTO("testFirst", "testLast");
		String message = JSON_MAPPER.writeValueAsString(dto);
		String destination = jmsQueueFirst;

		LOGGER.info("*** Sending JMS Message [{}] to Queue [{}] ...", message, destination);
		jmsTemplate.send(destination, (session) -> session.createTextMessage(message));

		//-- Given son time to process --
		long millis = 2000;
		LOGGER.info("*** Waiting {} ms ...", millis);
		Thread.sleep(millis);

		LOGGER.info("*** Test is over..");

		LOGGER.info("--- testReceiverJsonMessage - Stop ----------------------------------------------------");
	}

	@Test
	public void testReceiverXmlMessage() throws Exception {

		LOGGER.info("--- testReceiverXmlMessage - Start ----------------------------------------------------");

		ConnectionFactory jmsConnectionFactory = null;
		{
			Object objectConnectionFactory = initialContext.lookup(jmsConnectionFactoryName);
			if (objectConnectionFactory instanceof ConnectionFactory) {
				jmsConnectionFactory = (ConnectionFactory) objectConnectionFactory;
			} else {
				LOGGER.error("JNDI Lookup to [{}] did not return a ConnectionFactory object", jmsConnectionFactoryName);
			}
		}

		JmsTemplate jmsTemplate = new JmsTemplate();
		jmsTemplate.setConnectionFactory(jmsConnectionFactory);

		PersonDTO dto = new PersonDTO("testFirst", "testLast");
		String message = XML_MAPPER.writeValueAsString(dto);
		String destination = jmsQueueSecond;

		LOGGER.info("*** Sending JMS Message [{}] to Queue [{}] ...", message, destination);
		jmsTemplate.send(destination, (session) -> session.createTextMessage(message));

		//-- Given son time to process --
		long millis = 2000;
		LOGGER.info("*** Waiting {} ms ...", millis);
		Thread.sleep(millis);

		LOGGER.info("*** Test is over..");

		LOGGER.info("--- testReceiverXmlMessage - Stop -----------------------------------------------------");
	}
}

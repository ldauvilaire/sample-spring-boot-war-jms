package net.ldauvilaire.sample.jms.messaging;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import net.ldauvilaire.sample.jms.MessagingConstants;
import net.ldauvilaire.sample.jms.domain.command.MacroCommand;
import net.ldauvilaire.sample.jms.domain.command.impl.PersonCommand;
import net.ldauvilaire.sample.jms.domain.dto.PersonDTO;
import net.ldauvilaire.sample.jms.domain.spring.PersonService;

@Component
public class FirstMessageReceiver {

	static final Logger LOGGER = LoggerFactory.getLogger(FirstMessageReceiver.class);

	@Autowired
	PersonService personService;

	@JmsListener(destination = "${jms.queue.first}",
	             containerFactory = MessagingConstants.JMS_CONTAINER_FIRST_FACTORY)
	public void receiveMessage(final Message<PersonDTO> message) throws JMSException {

		LOGGER.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
		MessageHeaders headers =  message.getHeaders();
		LOGGER.info("Application : headers received : {}", headers);

		PersonDTO response = message.getPayload();
		LOGGER.info("Application : response received : {}", response);

		MacroCommand macro = new MacroCommand();
		PersonCommand command = new PersonCommand(response, macro, personService);
		macro.add(command);

		macro.execute();

		LOGGER.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
	}

}

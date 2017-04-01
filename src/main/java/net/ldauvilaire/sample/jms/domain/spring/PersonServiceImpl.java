package net.ldauvilaire.sample.jms.domain.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.ldauvilaire.sample.jms.domain.dto.PersonDTO;

@Service
public class PersonServiceImpl implements PersonService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PersonServiceImpl.class);

	@Override
	public void processPerson(PersonDTO person) {
		LOGGER.info("=> Person : {}", (person == null) ? "<none>" : person.toString());
	}

	@Override
	public void processFirstName(PersonDTO person) {
		LOGGER.info("=> FirstName : {}", (person == null) ? "" : person.getFirstName());
	}

	@Override
	public void processLastName(PersonDTO person) {
		LOGGER.info("=> LastName : {}", (person == null) ? "" : person.getLastName());
	}
}

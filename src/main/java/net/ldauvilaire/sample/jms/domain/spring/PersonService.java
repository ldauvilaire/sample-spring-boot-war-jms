package net.ldauvilaire.sample.jms.domain.spring;

import net.ldauvilaire.sample.jms.domain.dto.PersonDTO;

public interface PersonService {

	void processPerson(PersonDTO person);
	void processFirstName(PersonDTO person);
	void processLastName(PersonDTO person);
}

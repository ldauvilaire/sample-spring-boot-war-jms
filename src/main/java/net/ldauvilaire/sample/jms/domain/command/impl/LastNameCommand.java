package net.ldauvilaire.sample.jms.domain.command.impl;

import net.ldauvilaire.sample.jms.domain.command.AbstractBaseCommand;
import net.ldauvilaire.sample.jms.domain.dto.PersonDTO;
import net.ldauvilaire.sample.jms.domain.spring.PersonService;

public class LastNameCommand extends AbstractBaseCommand {

	private PersonService service;
	private PersonDTO person;

	public LastNameCommand(PersonDTO person, PersonService service) {
		super();
		this.service = service;
		this.person = person;
	}

	@Override
	public void execute() {
		this.service.processLastName(person);
	}
}

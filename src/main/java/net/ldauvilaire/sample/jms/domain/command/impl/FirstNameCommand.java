package net.ldauvilaire.sample.jms.domain.command.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ldauvilaire.sample.jms.domain.command.AbstractBaseCommand;
import net.ldauvilaire.sample.jms.domain.dto.PersonDTO;

public class FirstNameCommand extends AbstractBaseCommand {

	private static final Logger LOGGER = LoggerFactory.getLogger(FirstNameCommand.class);

	private PersonDTO person;

	public FirstNameCommand(PersonDTO person) {
		super();
		this.person = person;
	}

	@Override
	public void execute() {
		LOGGER.info("=> FirstName : {}", (this.person == null) ? "" : this.person.getFirstName());
	}
}

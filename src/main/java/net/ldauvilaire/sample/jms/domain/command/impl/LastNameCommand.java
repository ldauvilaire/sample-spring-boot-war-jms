package net.ldauvilaire.sample.jms.domain.command.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ldauvilaire.sample.jms.domain.command.AbstractBaseCommand;
import net.ldauvilaire.sample.jms.domain.dto.PersonDTO;

public class LastNameCommand extends AbstractBaseCommand {

	private static final Logger LOGGER = LoggerFactory.getLogger(LastNameCommand.class);

	private PersonDTO person;

	public LastNameCommand(PersonDTO person) {
		super();
		this.person = person;
	}

	@Override
	public void execute() {
		LOGGER.info("=> LastName : {}", (this.person == null) ? "" : this.person.getLastName());
	}
}

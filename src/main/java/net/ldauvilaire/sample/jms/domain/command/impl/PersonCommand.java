package net.ldauvilaire.sample.jms.domain.command.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ldauvilaire.sample.jms.domain.command.AbstractRecursiveCommand;
import net.ldauvilaire.sample.jms.domain.command.MacroCommand;
import net.ldauvilaire.sample.jms.domain.dto.PersonDTO;

public class PersonCommand extends AbstractRecursiveCommand {

	private static final Logger LOGGER = LoggerFactory.getLogger(PersonCommand.class);

	private PersonDTO person;

	public PersonCommand(PersonDTO person, MacroCommand macro) {
		super(macro);
		this.person = person;
	}

	@Override
	public void execute() {
		LOGGER.info("=> Person : {}", (this.person == null) ? "<none>" : this.person.toString());
		getMacro().add(new FirstNameCommand(this.person));
		getMacro().add(new LastNameCommand(this.person));
	}
}

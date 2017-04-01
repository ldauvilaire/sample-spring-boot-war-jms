package net.ldauvilaire.sample.jms.domain.command.impl;

import net.ldauvilaire.sample.jms.domain.command.AbstractRecursiveCommand;
import net.ldauvilaire.sample.jms.domain.command.MacroCommand;
import net.ldauvilaire.sample.jms.domain.dto.PersonDTO;
import net.ldauvilaire.sample.jms.domain.spring.PersonService;

public class PersonCommand extends AbstractRecursiveCommand {

	private PersonService service;
	private PersonDTO person;

	public PersonCommand(PersonDTO person, MacroCommand macro, PersonService service) {
		super(macro);
		this.service = service;
		this.person = person;
	}

	@Override
	public void execute() {
		this.service.processPerson(person);
		getMacro().add(new FirstNameCommand(this.person, this.service));
		getMacro().add(new LastNameCommand(this.person, this.service));
	}
}

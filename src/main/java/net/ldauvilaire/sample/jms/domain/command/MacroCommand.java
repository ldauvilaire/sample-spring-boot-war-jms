package net.ldauvilaire.sample.jms.domain.command;

import java.util.ArrayDeque;
import java.util.Deque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MacroCommand implements Command {

	private static final Logger LOGGER = LoggerFactory.getLogger(MacroCommand.class);

	private Deque<Command> commands;

	public MacroCommand() {
		super();
		this.commands = new ArrayDeque<Command>();
	}

	public void add(Command command) {
		this.commands.add(command);
	}

	@Override
	public void execute() {
		Command command = this.commands.poll();
		while (command != null) {
			LOGGER.debug("Executing command [{}] - Start ...", command.getClass().getSimpleName());
			command.execute();
			LOGGER.debug("Executing command [{}] - Stop.", command.getClass().getSimpleName());
			command = this.commands.poll();
		}
	}
}

package net.ldauvilaire.sample.jms.domain.command;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

		long startTimeNano = System.nanoTime();
		while (command != null) {
			command.execute();
			command = this.commands.poll();
		}
		long stopTimeNano = System.nanoTime();
		Map<TimeUnit, Long> result = computeDiff(startTimeNano, stopTimeNano);
		LOGGER.info("Executed Macro Command, Duration = {}.", result);
	}

	public static Map<TimeUnit, Long> computeDiff(long startTimeNano, long stopTimeNano) {
		long durationNano = stopTimeNano - startTimeNano;
		List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
		Collections.reverse(units);
		Map<TimeUnit, Long> result = new LinkedHashMap<TimeUnit, Long>();
		long nanoSecondsRest = durationNano;
		for (TimeUnit unit : units) {
			long diff = unit.convert(nanoSecondsRest, TimeUnit.NANOSECONDS);
			long diffInNanoSecondsForUnit = unit.toMillis(diff);
			nanoSecondsRest = nanoSecondsRest - diffInNanoSecondsForUnit;
			result.put(unit,diff);
		}
		return result;
	}
}

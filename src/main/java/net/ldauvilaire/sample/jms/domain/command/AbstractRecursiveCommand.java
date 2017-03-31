package net.ldauvilaire.sample.jms.domain.command;

public abstract class AbstractRecursiveCommand implements Command {

	private MacroCommand macro;

	public AbstractRecursiveCommand(MacroCommand macro) {
		super();
		this.macro = macro;
	}

	protected MacroCommand getMacro() {
		return macro;
	}
}

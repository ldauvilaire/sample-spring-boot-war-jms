package net.ldauvilaire.sample.jms.domain.xsd.filter.logic;

import net.ldauvilaire.sample.jms.domain.xsd.filter.AbstractFilter;

public abstract class AbstractLogicFilter extends AbstractFilter {

	protected String operator;

	public AbstractLogicFilter(String operator) {
		this.operator = operator;
	}

	@Override
	public boolean isLogic() {
		return true;
	}

	public String getOperator() {
		return this.operator;
	}
}

package net.ldauvilaire.sample.jms.domain.xsd.filter.comparison;

import net.ldauvilaire.sample.jms.domain.xsd.filter.AbstractFilter;

public abstract class AbstractComparisonFilter extends AbstractFilter {

	protected String operator;
	protected String attribute;

	public AbstractComparisonFilter(String operator, String attribute) {
		this.operator = operator;
		this.attribute = attribute;
	}

	@Override
	public boolean isLogic() {
		return false;
	}

	public String getOperator() {
		return this.operator;
	}

	public String getAttribute() {
		return this.attribute;
	}
}

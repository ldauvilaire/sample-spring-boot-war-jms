package net.ldauvilaire.sample.jms.domain.xsd.filter.logic;

import java.util.List;

import net.ldauvilaire.sample.jms.domain.xsd.filter.AbstractFilter;

public abstract class AbstractBinaryLogicFilter extends AbstractLogicFilter {

	protected List<AbstractFilter> filters;

	public AbstractBinaryLogicFilter(String operator, List<AbstractFilter> filters) {
		super(operator);
		this.filters = filters;
	}

	public List<AbstractFilter> getFilters() {
		return this.filters;
	}
}

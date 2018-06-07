package net.ldauvilaire.sample.jms.domain.xsd.filter.logic;

import java.util.List;

import net.ldauvilaire.sample.jms.domain.xsd.filter.AbstractFilter;

public class OrFilter extends AbstractBinaryLogicFilter {

	public OrFilter(List<AbstractFilter> filters) {
		super("Or", filters);
	}

	@Override
	public String toSqlString() {
		StringBuilder builder = new StringBuilder("(");
		boolean first= true;
		for (AbstractFilter filter : filters) {
			if (! first) {
				builder.append(" or ");
			}
			first = false;
			builder.append(filter.toSqlString());
		}
		builder.append(")");
		return builder.toString();
	}
}

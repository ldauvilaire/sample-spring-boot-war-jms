package net.ldauvilaire.sample.jms.domain.xsd.filter.logic;

import java.util.List;

import net.ldauvilaire.sample.jms.domain.xsd.filter.AbstractFilter;

public class AndFilter extends AbstractBinaryLogicFilter {

	public AndFilter(List<AbstractFilter> filters) {
		super("And", filters);
	}

	@Override
	public String toSqlString() {
		StringBuilder builder = new StringBuilder("(");
		boolean first= true;
		for (AbstractFilter filter : filters) {
			if (! first) {
				builder.append(" and ");
			}
			first = false;
			builder.append(filter.toSqlString());
		}
		builder.append(")");
		return builder.toString();
	}
}

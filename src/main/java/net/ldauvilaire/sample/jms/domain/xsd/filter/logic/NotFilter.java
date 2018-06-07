package net.ldauvilaire.sample.jms.domain.xsd.filter.logic;

import net.ldauvilaire.sample.jms.domain.xsd.filter.AbstractFilter;

public class NotFilter extends AbstractUnaryLogicFilter {

	public NotFilter(AbstractFilter filter) {
		super("Not", filter);
	}

	@Override
	public String toSqlString() {
		StringBuilder builder = new StringBuilder("not (");
		builder.append(this.filter.toSqlString());
		builder.append(")");
		return builder.toString();
	}
}

package net.ldauvilaire.sample.jms.domain.xsd.filter.comparison;

import java.util.List;

public class InFilter extends AbstractComparisonFilter {

	private List<String> values;

	public InFilter(String attribute, List<String> values) {
		super("Between", attribute);
		this.values = values;
	}

	public List<String> getValues() {
		return this.values;
	}

	@Override
	public String toSqlString() {
		StringBuilder builder = new StringBuilder(this.attribute);
		builder.append(" in (");
		boolean first = true;
		for (String value : values) {
			if (! first) {
				builder.append(", ");
			}
			first = false;
			builder.append("'").append(value).append("'");
		}
		builder.append(")");
		return builder.toString();
	}
}

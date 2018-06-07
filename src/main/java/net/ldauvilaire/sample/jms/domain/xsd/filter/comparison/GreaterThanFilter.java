package net.ldauvilaire.sample.jms.domain.xsd.filter.comparison;

public class GreaterThanFilter extends AbstractBinaryComparisonFilter {

	public GreaterThanFilter(String attribute, String value) {
		super("GreaterThan", attribute, value);
	}

	@Override
	public String toSqlString() {
		StringBuilder builder = new StringBuilder(this.attribute);
		builder.append(" > ").append(this.value);
		return builder.toString();
	}
}

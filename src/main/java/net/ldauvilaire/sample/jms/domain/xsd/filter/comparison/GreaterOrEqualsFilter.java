package net.ldauvilaire.sample.jms.domain.xsd.filter.comparison;

public class GreaterOrEqualsFilter extends AbstractBinaryComparisonFilter {

	public GreaterOrEqualsFilter(String attribute, String value) {
		super("GreaterOrEquals", attribute, value);
	}

	@Override
	public String toSqlString() {
		StringBuilder builder = new StringBuilder(this.attribute);
		builder.append(" >= ").append(this.value);
		return builder.toString();
	}
}

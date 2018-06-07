package net.ldauvilaire.sample.jms.domain.xsd.filter.comparison;

public class LessOrEqualsFilter extends AbstractBinaryComparisonFilter {

	public LessOrEqualsFilter(String attribute, String value) {
		super("LessOrEquals", attribute, value);
	}

	@Override
	public String toSqlString() {
		StringBuilder builder = new StringBuilder(this.attribute);
		builder.append(" <= ").append(this.value);
		return builder.toString();
	}
}

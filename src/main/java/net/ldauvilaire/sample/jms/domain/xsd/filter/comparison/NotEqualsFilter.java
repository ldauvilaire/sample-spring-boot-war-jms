package net.ldauvilaire.sample.jms.domain.xsd.filter.comparison;

public class NotEqualsFilter extends AbstractBinaryComparisonFilter {

	public NotEqualsFilter(String attribute, String value) {
		super("NotEquals", attribute, value);
	}

	@Override
	public String toSqlString() {
		StringBuilder builder = new StringBuilder(this.attribute);
		builder.append(" != '").append(this.value).append("'");
		return builder.toString();
	}
}

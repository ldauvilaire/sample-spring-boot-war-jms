package net.ldauvilaire.sample.jms.domain.xsd.filter.comparison;

public class LessThanFilter extends AbstractBinaryComparisonFilter {

	public LessThanFilter(String attribute, String value) {
		super("LessThan", attribute, value);
	}

	@Override
	public String toSqlString() {
		StringBuilder builder = new StringBuilder(this.attribute);
		builder.append(" < ").append(this.value);
		return builder.toString();
	}
}

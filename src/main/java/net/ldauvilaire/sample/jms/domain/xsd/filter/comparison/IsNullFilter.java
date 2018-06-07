package net.ldauvilaire.sample.jms.domain.xsd.filter.comparison;

public class IsNullFilter extends AbstractUnaryComparisonFilter {

	public IsNullFilter(String attribute) {
		super("IsNull", attribute);
	}

	@Override
	public String toSqlString() {
		StringBuilder builder = new StringBuilder(this.attribute);
		builder.append(" is null");
		return builder.toString();
	}
}

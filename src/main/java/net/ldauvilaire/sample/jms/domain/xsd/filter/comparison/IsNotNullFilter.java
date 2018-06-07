package net.ldauvilaire.sample.jms.domain.xsd.filter.comparison;

public class IsNotNullFilter extends AbstractUnaryComparisonFilter {

	public IsNotNullFilter(String attribute) {
		super("IsNotNull", attribute);
	}

	@Override
	public String toSqlString() {
		StringBuilder builder = new StringBuilder(this.attribute);
		builder.append(" is not null");
		return builder.toString();
	}
}

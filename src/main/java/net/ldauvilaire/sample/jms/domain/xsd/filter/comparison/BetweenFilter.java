package net.ldauvilaire.sample.jms.domain.xsd.filter.comparison;

public class BetweenFilter extends AbstractComparisonFilter {

	private String lower;
	private String upper;

	public BetweenFilter(String attribute, String lower, String upper) {
		super("Between", attribute);
		this.lower = lower;
		this.upper = upper;
	}

	public String getLower() {
		return this.lower;
	}

	public String getUpper() {
		return this.upper;
	}

	@Override
	public String toSqlString() {
		StringBuilder builder = new StringBuilder(this.attribute);
		builder.append(" between ").append(this.lower);
		builder.append(" and ").append(this.upper);
		return builder.toString();
	}
}

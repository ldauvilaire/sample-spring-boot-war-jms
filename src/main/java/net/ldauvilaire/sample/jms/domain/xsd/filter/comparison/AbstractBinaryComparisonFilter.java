package net.ldauvilaire.sample.jms.domain.xsd.filter.comparison;

public abstract class AbstractBinaryComparisonFilter extends AbstractComparisonFilter {

	protected String value;

	public AbstractBinaryComparisonFilter(String operator, String attribute, String value) {
		super(operator, attribute);
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}

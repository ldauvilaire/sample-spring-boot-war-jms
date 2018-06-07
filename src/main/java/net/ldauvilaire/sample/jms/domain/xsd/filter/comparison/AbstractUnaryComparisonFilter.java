package net.ldauvilaire.sample.jms.domain.xsd.filter.comparison;

public abstract class AbstractUnaryComparisonFilter extends AbstractComparisonFilter {

	public AbstractUnaryComparisonFilter(String operator, String attribute) {
		super(operator, attribute);
	}
}

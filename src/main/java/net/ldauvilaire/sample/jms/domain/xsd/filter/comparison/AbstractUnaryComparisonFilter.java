package net.ldauvilaire.sample.jms.domain.xsd.filter.comparison;

import net.ldauvilaire.sample.jms.domain.xsd.filter.ComparisonOperator;

public abstract class AbstractUnaryComparisonFilter extends AbstractComparisonFilter {

    public AbstractUnaryComparisonFilter(ComparisonOperator operator, String attribute) {
        super(operator, attribute);
    }
}

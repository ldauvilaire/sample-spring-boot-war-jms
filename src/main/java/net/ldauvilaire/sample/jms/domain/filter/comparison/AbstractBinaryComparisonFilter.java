package net.ldauvilaire.sample.jms.domain.filter.comparison;

import net.ldauvilaire.sample.jms.domain.filter.ComparisonOperator;

public abstract class AbstractBinaryComparisonFilter extends AbstractComparisonFilter {

    protected String value;

    public AbstractBinaryComparisonFilter(ComparisonOperator operator, String attribute, String value) {
        super(operator, attribute);
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}

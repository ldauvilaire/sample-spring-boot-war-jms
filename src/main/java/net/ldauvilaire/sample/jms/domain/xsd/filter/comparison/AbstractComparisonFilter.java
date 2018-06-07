package net.ldauvilaire.sample.jms.domain.xsd.filter.comparison;

import net.ldauvilaire.sample.jms.domain.xsd.filter.AbstractFilter;
import net.ldauvilaire.sample.jms.domain.xsd.filter.ComparisonOperator;

public abstract class AbstractComparisonFilter extends AbstractFilter {

    protected ComparisonOperator operator;
    protected String attribute;

    public AbstractComparisonFilter(ComparisonOperator operator, String attribute) {
        this.operator = operator;
        this.attribute = attribute;
    }

    @Override
    public boolean isLogic() {
        return false;
    }

    public ComparisonOperator getOperator() {
        return this.operator;
    }

    public String getAttribute() {
        return this.attribute;
    }
}

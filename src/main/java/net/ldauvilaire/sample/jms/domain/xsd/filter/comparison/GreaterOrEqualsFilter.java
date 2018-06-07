package net.ldauvilaire.sample.jms.domain.xsd.filter.comparison;

import net.ldauvilaire.sample.jms.domain.xsd.filter.ComparisonOperator;

public class GreaterOrEqualsFilter extends AbstractBinaryComparisonFilter {

    public GreaterOrEqualsFilter(String attribute, String value) {
        super(ComparisonOperator.GREATER_OR_EQUALS, attribute, value);
    }

    @Override
    public String toSqlString() {
        StringBuilder builder = new StringBuilder(this.attribute);
        builder.append(" >= ").append(this.value);
        return builder.toString();
    }
}

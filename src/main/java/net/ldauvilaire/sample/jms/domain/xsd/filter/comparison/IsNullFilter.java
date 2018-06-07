package net.ldauvilaire.sample.jms.domain.xsd.filter.comparison;

import net.ldauvilaire.sample.jms.domain.xsd.filter.ComparisonOperator;

public class IsNullFilter extends AbstractUnaryComparisonFilter {

    public IsNullFilter(String attribute) {
        super(ComparisonOperator.IS_NULL, attribute);
    }

    @Override
    public String toSqlString() {
        StringBuilder builder = new StringBuilder(this.attribute);
        builder.append(" is null");
        return builder.toString();
    }
}

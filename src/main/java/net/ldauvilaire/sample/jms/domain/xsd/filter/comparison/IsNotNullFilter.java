package net.ldauvilaire.sample.jms.domain.xsd.filter.comparison;

import net.ldauvilaire.sample.jms.domain.xsd.filter.ComparisonOperator;

public class IsNotNullFilter extends AbstractUnaryComparisonFilter {

    public IsNotNullFilter(String attribute) {
        super(ComparisonOperator.IS_NOT_NULL, attribute);
    }

    @Override
    public String toSqlString() {
        StringBuilder builder = new StringBuilder(this.attribute);
        builder.append(" is not null");
        return builder.toString();
    }
}

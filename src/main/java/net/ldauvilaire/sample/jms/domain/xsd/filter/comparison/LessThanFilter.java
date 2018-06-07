package net.ldauvilaire.sample.jms.domain.xsd.filter.comparison;

import net.ldauvilaire.sample.jms.domain.xsd.filter.ComparisonOperator;

public class LessThanFilter extends AbstractBinaryComparisonFilter {

    public LessThanFilter(String attribute, String value) {
        super(ComparisonOperator.LESS_THAN, attribute, value);
    }

    @Override
    public String toSqlString() {
        StringBuilder builder = new StringBuilder(this.attribute);
        builder.append(" < ").append(this.value);
        return builder.toString();
    }
}

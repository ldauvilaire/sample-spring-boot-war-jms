package net.ldauvilaire.sample.jms.domain.xsd.filter.comparison;

import net.ldauvilaire.sample.jms.domain.xsd.filter.ComparisonOperator;

public class EqualsFilter extends AbstractBinaryComparisonFilter {

    public EqualsFilter(String attribute, String value) {
        super(ComparisonOperator.EQUALS, attribute, value);
    }

    @Override
    public String toSqlString() {
        StringBuilder builder = new StringBuilder(this.attribute);
        builder.append(" = '").append(this.value).append("'");
        return builder.toString();
    }
}

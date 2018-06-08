package net.ldauvilaire.sample.jms.domain.filter.comparison;

import org.apache.commons.lang.StringUtils;

import net.ldauvilaire.sample.jms.domain.filter.ComparisonOperator;

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

    @Override
    public String toXmlString(int level) {
        String prefix = StringUtils.leftPad("", level*3, " ");
        StringBuilder builder = new StringBuilder("");
        builder.append(prefix).append("<").append(this.operator.name()).append(">").append("\n");
        builder.append(prefix).append("   ").append("<ATTRIBUTE>").append(this.attribute).append("</ATTRIBUTE>").append("\n");
        builder.append(prefix).append("</").append(this.operator.name()).append(">").append("\n");
        return builder.toString();
    }
}

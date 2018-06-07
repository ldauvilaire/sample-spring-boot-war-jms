package net.ldauvilaire.sample.jms.domain.xsd.filter.comparison;

import org.apache.commons.lang.StringUtils;

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

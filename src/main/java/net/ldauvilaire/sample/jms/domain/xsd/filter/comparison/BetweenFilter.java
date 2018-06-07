package net.ldauvilaire.sample.jms.domain.xsd.filter.comparison;

import org.apache.commons.lang.StringUtils;

import net.ldauvilaire.sample.jms.domain.xsd.filter.ComparisonOperator;

public class BetweenFilter extends AbstractComparisonFilter {

    private String lower;
    private String upper;

    public BetweenFilter(String attribute, String lower, String upper) {
        super(ComparisonOperator.BETWEEN, attribute);
        this.lower = lower;
        this.upper = upper;
    }

    public String getLower() {
        return this.lower;
    }

    public String getUpper() {
        return this.upper;
    }

    @Override
    public String toSqlString() {
        StringBuilder builder = new StringBuilder(this.attribute);
        builder.append(" BETWEEN ").append(this.lower);
        builder.append(" AND ").append(this.upper);
        return builder.toString();
    }

    @Override
    public String toXmlString(int level) {
        String prefix = StringUtils.leftPad("", level*3, " ");
        StringBuilder builder = new StringBuilder("");
        builder.append(prefix).append("<").append(this.operator.name()).append(">").append("\n");
        builder.append(prefix).append("   ").append("<ATTRIBUTE>").append(this.attribute).append("</ATTRIBUTE>").append("\n");
        builder.append(prefix).append("   ").append("<LOWER>").append(this.lower).append("</LOWER>").append("\n");
        builder.append(prefix).append("   ").append("<UPPER>").append(this.upper).append("</UPPER>").append("\n");
        builder.append(prefix).append("</").append(this.operator.name()).append(">").append("\n");
        return builder.toString();
    }
}

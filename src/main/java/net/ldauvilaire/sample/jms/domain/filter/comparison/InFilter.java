package net.ldauvilaire.sample.jms.domain.filter.comparison;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.ldauvilaire.sample.jms.domain.filter.ComparisonOperator;

public class InFilter extends AbstractComparisonFilter {

    private List<String> values;

    public InFilter(String attribute, List<String> values) {
        super(ComparisonOperator.IN, attribute);
        this.values = values;
    }

    public List<String> getValues() {
        return this.values;
    }

    @Override
    public String toSqlString() {
        StringBuilder builder = new StringBuilder(this.attribute);
        builder.append(" in (");
        boolean first = true;
        for (String value : values) {
            if (! first) {
                builder.append(", ");
            }
            first = false;
            builder.append("'").append(value).append("'");
        }
        builder.append(")");
        return builder.toString();
    }

    @Override
    public String toXmlString(int level) {
        String prefix = StringUtils.leftPad("", level*3, " ");
        StringBuilder builder = new StringBuilder("");
        builder.append(prefix).append("<").append(this.operator.name()).append(">").append("\n");
        builder.append(prefix).append("   ").append("<ATTRIBUTE>").append(this.attribute).append("</ATTRIBUTE>").append("\n");
        for (String value : values) {
            builder.append(prefix).append("   ").append("<VALUE>").append(value).append("</VALUE>").append("\n");
        }
        builder.append(prefix).append("</").append(this.operator.name()).append(">").append("\n");
        return builder.toString();
    }
}

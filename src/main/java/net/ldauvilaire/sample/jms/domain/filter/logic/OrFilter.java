package net.ldauvilaire.sample.jms.domain.filter.logic;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.ldauvilaire.sample.jms.domain.filter.AbstractFilter;
import net.ldauvilaire.sample.jms.domain.filter.LogicOperator;

public class OrFilter extends AbstractBinaryLogicFilter {

    public OrFilter(List<AbstractFilter> filters) {
        super(LogicOperator.OR, filters);
    }

    @Override
    public String toSqlString() {
        StringBuilder builder = new StringBuilder("");
        boolean first= true;
        for (AbstractFilter filter : filters) {
            if (! first) {
                builder.append(" OR ");
            }
            first = false;
            builder.append("(").append(filter.toSqlString()).append(")");
        }
        return builder.toString();
    }

    @Override
    public String toXmlString(int level) {
        String prefix = StringUtils.leftPad("", level*3, " ");
        StringBuilder builder = new StringBuilder("");
        builder.append(prefix).append("<").append(this.operator.name()).append(">").append("\n");
        for (AbstractFilter filter : this.filters) {
            builder.append(filter.toXmlString(level+1));
        }
        builder.append(prefix).append("</").append(this.operator.name()).append(">").append("\n");
        return builder.toString();
    }
}

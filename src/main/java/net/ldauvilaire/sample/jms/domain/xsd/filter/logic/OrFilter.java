package net.ldauvilaire.sample.jms.domain.xsd.filter.logic;

import java.util.List;

import net.ldauvilaire.sample.jms.domain.xsd.filter.AbstractFilter;
import net.ldauvilaire.sample.jms.domain.xsd.filter.LogicOperator;

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
                builder.append(" or ");
            }
            first = false;
            builder.append("(").append(filter.toSqlString()).append(")");
        }
        return builder.toString();
    }
}

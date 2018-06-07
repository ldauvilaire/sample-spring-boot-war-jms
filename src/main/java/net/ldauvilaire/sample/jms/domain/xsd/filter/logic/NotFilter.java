package net.ldauvilaire.sample.jms.domain.xsd.filter.logic;

import net.ldauvilaire.sample.jms.domain.xsd.filter.AbstractFilter;
import net.ldauvilaire.sample.jms.domain.xsd.filter.LogicOperator;

public class NotFilter extends AbstractUnaryLogicFilter {

    public NotFilter(AbstractFilter filter) {
        super(LogicOperator.NOT, filter);
    }

    @Override
    public String toSqlString() {
        StringBuilder builder = new StringBuilder("not ");
        builder.append("(").append(this.filter.toSqlString()).append(")");
        return builder.toString();
    }
}

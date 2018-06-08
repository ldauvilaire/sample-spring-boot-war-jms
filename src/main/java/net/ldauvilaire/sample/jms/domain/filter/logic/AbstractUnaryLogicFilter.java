package net.ldauvilaire.sample.jms.domain.filter.logic;

import net.ldauvilaire.sample.jms.domain.filter.AbstractFilter;
import net.ldauvilaire.sample.jms.domain.filter.LogicOperator;

public abstract class AbstractUnaryLogicFilter extends AbstractLogicFilter {

    protected AbstractFilter filter;

    public AbstractUnaryLogicFilter(LogicOperator operator, AbstractFilter filter) {
        super(operator);
        this.filter = filter;
    }

    public AbstractFilter getFilter() {
        return this.filter;
    }
}

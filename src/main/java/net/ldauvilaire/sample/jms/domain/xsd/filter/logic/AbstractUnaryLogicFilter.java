package net.ldauvilaire.sample.jms.domain.xsd.filter.logic;

import net.ldauvilaire.sample.jms.domain.xsd.filter.AbstractFilter;
import net.ldauvilaire.sample.jms.domain.xsd.filter.LogicOperator;

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

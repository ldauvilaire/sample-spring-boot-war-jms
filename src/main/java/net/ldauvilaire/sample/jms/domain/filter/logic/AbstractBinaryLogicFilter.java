package net.ldauvilaire.sample.jms.domain.filter.logic;

import java.util.List;

import net.ldauvilaire.sample.jms.domain.filter.AbstractFilter;
import net.ldauvilaire.sample.jms.domain.filter.LogicOperator;

public abstract class AbstractBinaryLogicFilter extends AbstractLogicFilter {

    protected List<AbstractFilter> filters;

    public AbstractBinaryLogicFilter(LogicOperator operator, List<AbstractFilter> filters) {
        super(operator);
        this.filters = filters;
    }

    public List<AbstractFilter> getFilters() {
        return this.filters;
    }
}

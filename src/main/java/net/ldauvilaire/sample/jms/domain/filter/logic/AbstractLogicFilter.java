package net.ldauvilaire.sample.jms.domain.filter.logic;

import net.ldauvilaire.sample.jms.domain.filter.AbstractFilter;
import net.ldauvilaire.sample.jms.domain.filter.LogicOperator;

public abstract class AbstractLogicFilter extends AbstractFilter {

    protected LogicOperator operator;

    public AbstractLogicFilter(LogicOperator operator) {
        this.operator = operator;
    }

    @Override
    public boolean isLogic() {
        return true;
    }

    public LogicOperator getOperator() {
        return this.operator;
    }
}

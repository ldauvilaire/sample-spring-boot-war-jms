package net.ldauvilaire.sample.jms.domain.xsd.filter.logic;

import net.ldauvilaire.sample.jms.domain.xsd.filter.AbstractFilter;
import net.ldauvilaire.sample.jms.domain.xsd.filter.LogicOperator;

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

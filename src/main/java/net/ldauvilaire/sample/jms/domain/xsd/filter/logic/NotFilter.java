package net.ldauvilaire.sample.jms.domain.xsd.filter.logic;

import org.apache.commons.lang.StringUtils;

import net.ldauvilaire.sample.jms.domain.xsd.filter.AbstractFilter;
import net.ldauvilaire.sample.jms.domain.xsd.filter.LogicOperator;

public class NotFilter extends AbstractUnaryLogicFilter {

    public NotFilter(AbstractFilter filter) {
        super(LogicOperator.NOT, filter);
    }

    @Override
    public String toSqlString() {
        StringBuilder builder = new StringBuilder("NOT ");
        builder.append("(").append(this.filter.toSqlString()).append(")");
        return builder.toString();
    }

    @Override
    public String toXmlString(int level) {
        String prefix = StringUtils.leftPad("", level*3, " ");
        StringBuilder builder = new StringBuilder("");
        builder.append(prefix).append("<").append(this.operator.name()).append(">").append("\n");
        builder.append(this.filter.toXmlString(level+1));
        builder.append(prefix).append("</").append(this.operator.name()).append(">").append("\n");
        return builder.toString();
    }
}

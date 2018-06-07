package net.ldauvilaire.sample.jms.domain.xsd.filter;

public abstract class AbstractFilter {
    public abstract boolean isLogic();
    public abstract String toSqlString();
}

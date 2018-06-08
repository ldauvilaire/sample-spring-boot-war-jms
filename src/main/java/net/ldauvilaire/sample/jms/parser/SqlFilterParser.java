package net.ldauvilaire.sample.jms.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.ldauvilaire.sample.jms.domain.filter.AbstractFilter;
import net.ldauvilaire.sample.jms.domain.filter.ComparisonOperator;
import net.ldauvilaire.sample.jms.domain.filter.LogicOperator;
import net.ldauvilaire.sample.jms.domain.filter.comparison.BetweenFilter;
import net.ldauvilaire.sample.jms.domain.filter.comparison.EqualsFilter;
import net.ldauvilaire.sample.jms.domain.filter.comparison.GreaterOrEqualsFilter;
import net.ldauvilaire.sample.jms.domain.filter.comparison.GreaterThanFilter;
import net.ldauvilaire.sample.jms.domain.filter.comparison.InFilter;
import net.ldauvilaire.sample.jms.domain.filter.comparison.IsNotNullFilter;
import net.ldauvilaire.sample.jms.domain.filter.comparison.IsNullFilter;
import net.ldauvilaire.sample.jms.domain.filter.comparison.LessOrEqualsFilter;
import net.ldauvilaire.sample.jms.domain.filter.comparison.LessThanFilter;
import net.ldauvilaire.sample.jms.domain.filter.comparison.NotEqualsFilter;
import net.ldauvilaire.sample.jms.domain.filter.logic.AndFilter;
import net.ldauvilaire.sample.jms.domain.filter.logic.NotFilter;
import net.ldauvilaire.sample.jms.domain.filter.logic.OrFilter;

@Service
public class SqlFilterParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlFilterParser.class);

    public AbstractFilter parseSqlFilter(String sqlFilter) {

        if (sqlFilter == null) {
            return null;
        }

        SqlParser sqlParser = SqlParser.create(sqlFilter);
        SqlNode sqlNode = null;
        try {
            sqlNode = sqlParser.parseExpression();
        } catch (SqlParseException ex) {
            LOGGER.warn("A SqlParseException has occured while parsing sql expression [" + sqlFilter + "]", ex);
        }

        return this.parseSqlFilter(sqlNode);
    }

    public AbstractFilter parseSqlFilter(SqlNode sqlNode) {

        if (sqlNode == null) {
            return null;
        }

        AbstractFilter filter = null;

        if (sqlNode instanceof SqlCall) {
            SqlCall sqlCall = (SqlCall) sqlNode;
            SqlOperator operator = sqlCall.getOperator();
            String operatorName = operator.getName();
            List<SqlNode> operandList = sqlCall.getOperandList();

            ComparisonOperator comparisonOperator = findComparisonOperator(operatorName);
            LogicOperator logicOperator = null;
            if (comparisonOperator == null) {
                logicOperator = findLogicOperator(operatorName);
            }

            if (comparisonOperator != null) {

                switch (comparisonOperator) {
                    case IS_NULL:
                    case IS_NOT_NULL:
                        filter = parseUnaryComparisonFilter(comparisonOperator, operandList);
                        break;
                    case EQUALS:
                    case NOT_EQUALS:
                    case LESS_THAN:
                    case LESS_OR_EQUALS:
                    case GREATER_THAN:
                    case GREATER_OR_EQUALS:
                        filter = parseBinaryComparisonFilter(comparisonOperator, operandList);
                        break;
                    case BETWEEN:
                        filter = parseBetweenComparisonFilter(comparisonOperator, operandList);
                        break;
                    case IN:
                        filter = parseInComparisonFilter(comparisonOperator, operandList);
                        break;
                    default:
                        break;
                }

            } else if (logicOperator != null) {

                switch (logicOperator) {
                    case NOT:
                        filter = parseUnaryLogicFilter(logicOperator, operandList);
                        break;
                    case AND:
                    case OR:
                        filter = parseBinaryLogicFilter(logicOperator, operandList);
                        break;
                    default:
                        break;
                }

            }
        }

        return  filter;
    }

    private AbstractFilter parseUnaryComparisonFilter(ComparisonOperator operator, List<SqlNode> childNodes) {

        if ((operator == null) || (childNodes == null)) {
            return null;
        }

        AbstractFilter filter = null;

        String attribute = getAttribute(childNodes);
        if (StringUtils.isNotBlank(attribute)) {
            switch (operator) {
                case IS_NULL:
                    filter = new IsNullFilter(attribute);
                    break;
                case IS_NOT_NULL:
                    filter = new IsNotNullFilter(attribute);
                    break;
                default:
                    break;
            }
        }

        return  filter;
    }

    private AbstractFilter parseBinaryComparisonFilter(ComparisonOperator operator, List<SqlNode> childNodes) {

        if ((operator == null) || (childNodes == null)) {
            return null;
        }

        AbstractFilter filter = null;

        String attribute = getAttribute(childNodes);
        if (attribute != null) {
            String value = getBinaryValue(childNodes);
            if (StringUtils.isNoneBlank(value)) {
                switch (operator) {
                    case EQUALS:
                        filter = new EqualsFilter(attribute, value);
                       break;
                    case NOT_EQUALS:
                        filter = new NotEqualsFilter(attribute, value);
                        break;
                    case LESS_THAN:
                        filter = new LessThanFilter(attribute, value);
                        break;
                    case LESS_OR_EQUALS:
                        filter = new LessOrEqualsFilter(attribute, value);
                        break;
                    case GREATER_THAN:
                        filter = new GreaterThanFilter(attribute, value);
                        break;
                    case GREATER_OR_EQUALS:
                        filter = new GreaterOrEqualsFilter(attribute, value);
                        break;
                    default:
                        break;
                }
            }
        }

        return  filter;
    }

    private AbstractFilter parseBetweenComparisonFilter(ComparisonOperator operator, List<SqlNode> childNodes) {

        if ((operator == null) || (childNodes == null)) {
            return null;
        }

        AbstractFilter filter = null;

        String attribute = getAttribute(childNodes);
        if (attribute != null) {
            Pair<String, String> values = getBetweenValues(childNodes);
            if (values != null) {
                String lower = values.getLeft();
                String upper = values.getRight();
                if (StringUtils.isNotBlank(lower) && StringUtils.isNotBlank(upper)) {
                    filter = new BetweenFilter(attribute, lower, upper);
                }
            }
        }

        return  filter;
    }

    private AbstractFilter parseInComparisonFilter(ComparisonOperator operator, List<SqlNode> childNodes) {

        if ((operator == null) || (childNodes == null)) {
            return null;
        }

        AbstractFilter filter = null;

        String attribute = getAttribute(childNodes);
        if (attribute != null) {
            List<String> values = getListValues(childNodes, true);
            if ((values != null) && (! values.isEmpty())) {
                filter = new InFilter(attribute, values);
            }
        }

        return  filter;
    }

    private AbstractFilter parseUnaryLogicFilter(LogicOperator operator, List<SqlNode> childNodes) {

        if ((operator == null) || (childNodes == null)) {
            return null;
        }

        AbstractFilter filter = null;

        for (SqlNode childNode : childNodes) {
            AbstractFilter childFilter = this.parseSqlFilter(childNode);
            if (childFilter != null) {
                switch (operator) {
                    case NOT:
                        filter = new NotFilter(childFilter);
                        break;
                    default:
                        break;
                }
            }
        }

        return  filter;
    }

    private AbstractFilter parseBinaryLogicFilter(LogicOperator operator, List<SqlNode> childNodes) {

        if ((operator == null) || (childNodes == null)) {
            return null;
        }

        AbstractFilter filter = null;

        List<AbstractFilter> childFilters = new ArrayList<>();
        for (SqlNode childNode : childNodes) {
            AbstractFilter childFilter = this.parseSqlFilter(childNode);
            if (childFilter != null) {
                childFilters.add(childFilter);
            }
        }

        if ((childFilters != null) && (childFilters.size() >= 2)) {
            switch (operator) {
                case AND:
                    filter = new AndFilter(childFilters);
                    break;
                case OR:
                    filter = new OrFilter(childFilters);
                    break;
                default:
                    break;
            }
        }

        return  filter;
    }

    private String getAttribute(List<SqlNode> sqlNodes) {

        String identifier = null;

        if (sqlNodes != null) {
            for (SqlNode sqlNode : sqlNodes) {
                if (sqlNode instanceof SqlIdentifier) {
                    SqlIdentifier sqlIdentifier = (SqlIdentifier) sqlNode;
                    identifier = sqlIdentifier.getSimple();
                    break;
                }
            }
        }

        return identifier;
    }

    private String getBinaryValue(List<SqlNode> sqlNodes) {

        String value = null;

        List<String> values = getListValues(sqlNodes, true);
        if ((values != null) && (! values.isEmpty())) {
            value = values.get(0);
        }

        return value;
    }

    private Pair<String, String> getBetweenValues(List<SqlNode> sqlNodes) {

        String lower = null;
        String upper = null;

        List<String> values = getListValues(sqlNodes, true);
        if ((values != null) && (values.size() >= 2)) {
            lower = values.get(0);
            upper = values.get(1);
        }

        return (StringUtils.isBlank(lower) || StringUtils.isBlank(upper)) ? null : Pair.of(lower, upper);
    }

    private List<String> getListValues(List<SqlNode> sqlNodes, boolean skipFirst) {

        List<String> values = new ArrayList<>();

        if (sqlNodes != null) {
            boolean firstIdentifier = true;
            for (SqlNode sqlNode : sqlNodes) {
                String value = null;
                if (sqlNode instanceof SqlIdentifier) {
                    SqlIdentifier sqlIdentifier = (SqlIdentifier) sqlNode;
                    if (! (skipFirst && firstIdentifier)) {
                        value = sqlIdentifier.getSimple();
                    }
                    firstIdentifier = false;
                    if (value != null) {
                        values.add(value);
                    }
                } else if (sqlNode instanceof SqlLiteral) {
                    SqlLiteral sqlLiteral = (SqlLiteral) sqlNode;
                    Object objectValue = sqlLiteral.getValue();
                    value = (objectValue == null) ? null : objectValue.toString();
                    if (value != null) {
                        values.add(value);
                    }
                } else if (sqlNode instanceof SqlNodeList) {
                    SqlNodeList sqlNodeList = (SqlNodeList) sqlNode;
                    List<SqlNode> sqlSubNodes = sqlNodeList.getList();
                    if (sqlSubNodes != null) {
                        List<String> subValues = getListValues(sqlSubNodes, false);
                        if ((subValues != null) && (! subValues.isEmpty())) {
                            values.addAll(subValues);
                        }
                    }
                }
            }
        }

        return values;
    }

    private ComparisonOperator findComparisonOperator(String operatorName) {
        ComparisonOperator operator = null;
        if (operatorName != null) {
            switch (operatorName.toUpperCase()) {
                case "IS NULL":
                    operator = ComparisonOperator.IS_NULL;
                    break;
                case "IS NOT NULL":
                    operator = ComparisonOperator.IS_NOT_NULL;
                    break;
                case "=":
                    operator = ComparisonOperator.EQUALS;
                    break;
                case "<>":
                case "!=":
                    operator = ComparisonOperator.NOT_EQUALS;
                    break;
                case "<":
                    operator = ComparisonOperator.LESS_THAN;
                    break;
                case "<=":
                    operator = ComparisonOperator.LESS_OR_EQUALS;
                    break;
                case ">":
                    operator = ComparisonOperator.GREATER_THAN;
                    break;
                case ">=":
                    operator = ComparisonOperator.GREATER_OR_EQUALS;
                    break;
                case "BETWEEN ASYMMETRIC":
                    operator = ComparisonOperator.BETWEEN;
                    break;
                case "IN":
                    operator = ComparisonOperator.IN;
                    break;
                default:
                    LOGGER.info("Unknown Comparison Operator [{}].", operatorName);
                    break;
            }
        }
        return operator;
    }

    private LogicOperator findLogicOperator(String operatorName) {
        LogicOperator operator = null;
        if (operatorName != null) {
            switch (operatorName.toUpperCase()) {
                case "AND":
                    operator = LogicOperator.AND;
                    break;
                case "OR":
                    operator = LogicOperator.OR;
                    break;
                case "NOT":
                    operator = LogicOperator.NOT;
                    break;
                default:
                    LOGGER.info("Unknown Logic Operator [{}].", operatorName);
                    break;
            }
        }
        return operator;
    }
}

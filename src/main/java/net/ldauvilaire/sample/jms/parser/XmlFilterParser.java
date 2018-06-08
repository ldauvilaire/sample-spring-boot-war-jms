package net.ldauvilaire.sample.jms.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.ldauvilaire.sample.jms.domain.filter.AbstractFilter;
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
import net.ldauvilaire.xsd.filter.AndLogicType;
import net.ldauvilaire.xsd.filter.BetweenComparisonType;
import net.ldauvilaire.xsd.filter.BinaryComparisonType;
import net.ldauvilaire.xsd.filter.BinaryLogicType;
import net.ldauvilaire.xsd.filter.ComparisonType;
import net.ldauvilaire.xsd.filter.EqualsComparisonType;
import net.ldauvilaire.xsd.filter.FilterType;
import net.ldauvilaire.xsd.filter.GreaterComparisonType;
import net.ldauvilaire.xsd.filter.GreaterOrEqualsComparisonType;
import net.ldauvilaire.xsd.filter.InComparisonType;
import net.ldauvilaire.xsd.filter.IsNotNullComparisonType;
import net.ldauvilaire.xsd.filter.IsNullComparisonType;
import net.ldauvilaire.xsd.filter.LessComparisonType;
import net.ldauvilaire.xsd.filter.LessOrEqualsComparisonType;
import net.ldauvilaire.xsd.filter.LogicType;
import net.ldauvilaire.xsd.filter.NotEqualsComparisonType;
import net.ldauvilaire.xsd.filter.NotLogicType;
import net.ldauvilaire.xsd.filter.OrLogicType;
import net.ldauvilaire.xsd.filter.UnaryComparisonType;
import net.ldauvilaire.xsd.filter.UnaryLogicType;

@Service
public class XmlFilterParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlFilterParser.class);

    public AbstractFilter parseXmlFilter(FilterType xmlFilter) {

        if (xmlFilter == null) {
            return null;
        }

        AbstractFilter filter = null;

        JAXBElement<? extends LogicType> logicElement = xmlFilter.getLogic();
        JAXBElement<? extends ComparisonType> comparisonElement = xmlFilter.getComparison();

        if (logicElement != null) {
            filter = this.parseXmlLogic(logicElement);
        } else if (comparisonElement != null) {
            filter = this.parseXmlComparison(comparisonElement);
        }

        return filter;
    }

    @SuppressWarnings("unchecked")
    public List<AbstractFilter> parseXmlFilters(List<JAXBElement<?>> elements) {

        List<AbstractFilter> filters = new ArrayList<>();

        if (elements != null) {
            elements.forEach(element -> {
                Object value = element.getValue();
                AbstractFilter filter = null;
                if (element.getValue() instanceof LogicType) {
                    filter = this.parseXmlLogic((JAXBElement<LogicType>) element);
                } else if (value instanceof ComparisonType) {
                    filter = this.parseXmlComparison((JAXBElement<ComparisonType>) element);
                }
                if (filter != null) {
                    filters.add(filter);
                }
            });
        }

        return filters;
    }

    public AbstractFilter parseXmlComparison(JAXBElement<? extends ComparisonType> xmlElement) {

        AbstractFilter filter = null;

        String operator = getTagName(xmlElement);
        ComparisonType xmlValue = xmlElement.getValue();

        if (xmlValue instanceof UnaryComparisonType) {
            filter = parseUnaryComparison(operator, (UnaryComparisonType) xmlValue);

        } else if (xmlValue instanceof BinaryComparisonType) {
            filter = parseBinaryComparison(operator, (BinaryComparisonType) xmlValue);

        } else if (xmlValue instanceof BetweenComparisonType) {
            filter = parseBetweenComparison(operator, (BetweenComparisonType) xmlValue);

        } else if (xmlValue instanceof InComparisonType) {
            filter = parseInComparison(operator, (InComparisonType) xmlValue);

        }

        return filter;
    }

    public AbstractFilter parseUnaryComparison(String operator, UnaryComparisonType comparator) {

        AbstractFilter filter = null;

        if (comparator instanceof IsNullComparisonType) {
            IsNullComparisonType isNullComparator = (IsNullComparisonType) comparator;
            String attribute = isNullComparator.getAttribute();
            filter = new IsNullFilter(attribute);
            LOGGER.info("Parsing XML IsNullComparisonType as IsNullFilter for attribute [{}] ...", attribute);

        } else if (comparator instanceof IsNotNullComparisonType) {
            IsNotNullComparisonType isNotNullComparator = (IsNotNullComparisonType) comparator;
            String attribute = isNotNullComparator.getAttribute();
            filter = new IsNotNullFilter(attribute);
            LOGGER.info("Parsing XML IsNotNullComparisonType as IsNotNullFilter for attribute [{}] ...", attribute);

        }

        return filter;
    }

    public AbstractFilter parseBinaryComparison(String operator, BinaryComparisonType comparator) {

        AbstractFilter filter = null;

        if (comparator instanceof EqualsComparisonType) {
            EqualsComparisonType equalsComparator = (EqualsComparisonType) comparator;
            String attribute = equalsComparator.getAttribute();
            String value = equalsComparator.getValue();
            filter = new EqualsFilter(attribute, value);
            LOGGER.info("Parsing XML EqualsComparisonType as EqualsFilter for attribute [{}] and value [{}]...", attribute, value);

        } else if (comparator instanceof NotEqualsComparisonType) {
            NotEqualsComparisonType notEqualsComparator = (NotEqualsComparisonType) comparator;
            String attribute = notEqualsComparator.getAttribute();
            String value = notEqualsComparator.getValue();
            filter = new NotEqualsFilter(attribute, value);
            LOGGER.info("Parsing XML EqualsComparisonType as NotEqualsFilter for attribute [{}] and value [{}]...", attribute, value);

        } else if (comparator instanceof LessComparisonType) {
            LessComparisonType lessComparator = (LessComparisonType) comparator;
            String attribute = lessComparator.getAttribute();
            String value = lessComparator.getValue();
            filter = new LessThanFilter(attribute, value);
            LOGGER.info("Parsing XML LessComparisonType as LessThanFilter for attribute [{}] and value [{}]...", attribute, value);

        } else if (comparator instanceof LessOrEqualsComparisonType) {
            LessOrEqualsComparisonType lessOrEqualsComparator = (LessOrEqualsComparisonType) comparator;
            String attribute = lessOrEqualsComparator.getAttribute();
            String value = lessOrEqualsComparator.getValue();
            filter = new LessOrEqualsFilter(attribute, value);
            LOGGER.info("Parsing XML LessOrEqualsComparisonType as LessOrEqualsFilter for attribute [{}] and value [{}]...", attribute, value);

        } else if (comparator instanceof GreaterComparisonType) {
            GreaterComparisonType greaterComparator = (GreaterComparisonType) comparator;
            String attribute = greaterComparator.getAttribute();
            String value = greaterComparator.getValue();
            filter = new GreaterThanFilter(attribute, value);
            LOGGER.info("Parsing XML GreaterComparisonType as GreaterThanFilter for attribute [{}] and value [{}]...", attribute, value);

        } else if (comparator instanceof GreaterOrEqualsComparisonType) {
            GreaterOrEqualsComparisonType greaterOrEqualsComparator = (GreaterOrEqualsComparisonType) comparator;
            String attribute = greaterOrEqualsComparator.getAttribute();
            String value = greaterOrEqualsComparator.getValue();
            filter = new GreaterOrEqualsFilter(attribute, value);
            LOGGER.info("Parsing XML GreaterOrEqualsComparisonType as GreaterOrEqualsFilter for attribute [{}] and value [{}]...", attribute, value);

        }

        return filter;
    }

    public AbstractFilter parseBetweenComparison(String operator, BetweenComparisonType comparator) {

        AbstractFilter filter = null;

        String attribute = comparator.getAttribute();
        String lower = comparator.getLower();
        String upper = comparator.getUpper();
        filter = new BetweenFilter(attribute, lower, upper);
        LOGGER.info("Parsing XML BetweenComparisonType as BetweenFilter for attribute [{}] and lower [{}] and upper [{}]...", attribute, lower, upper);

        return filter;
    }

    public AbstractFilter parseInComparison(String operator, InComparisonType comparator) {

        AbstractFilter filter = null;

        String attribute = comparator.getAttribute();
        List<String> values = comparator.getValue();
        filter = new InFilter(attribute, values);
        if (LOGGER.isInfoEnabled()) {
            StringJoiner builder = new StringJoiner(",");
            values.forEach(e -> builder.add(e));
            LOGGER.info("Parsing XML InComparisonType as InFilter for attribute [{}] and values [{}]...", attribute, builder.toString());
        }

        return filter;
    }

    public AbstractFilter parseXmlLogic(JAXBElement<? extends LogicType> xmlElement) {

        AbstractFilter filter = null;

        String operator = getTagName(xmlElement);
        LogicType xmlValue = xmlElement.getValue();

        if (xmlValue instanceof UnaryLogicType) {
            filter = parseUnaryLogic(operator, (UnaryLogicType) xmlValue);

        } else if (xmlValue instanceof BinaryLogicType) {
            filter = parseBinaryLogic(operator, (BinaryLogicType) xmlValue);

        }

        return filter;
    }

    public AbstractFilter parseUnaryLogic(String operator, UnaryLogicType logic) {

        AbstractFilter filter = null;

        if (logic instanceof NotLogicType) {
            NotLogicType notLogic = (NotLogicType) logic;
            AbstractFilter childFilter = null;
            {
                JAXBElement<? extends LogicType> logicElement = notLogic.getLogic();
                JAXBElement<? extends ComparisonType> comparisonElement = notLogic.getComparison();

                if (logicElement != null) {
                    childFilter = this.parseXmlLogic(logicElement);
                } else if (comparisonElement != null) {
                    childFilter = this.parseXmlComparison(comparisonElement);
                }
            }
            if (childFilter != null) {
                filter = new NotFilter(childFilter);
                LOGGER.info("Parsing XML NotLogicType as NotFilter ...");
            }
        }

        return filter;
    }

    public AbstractFilter parseBinaryLogic(String operator, BinaryLogicType logic) {

        AbstractFilter filter = null;


        if (logic instanceof AndLogicType) {
            AndLogicType andLogic = (AndLogicType) logic;
            List<AbstractFilter> childFilters = this.parseXmlFilters(andLogic.getComparisonOrLogic());
            filter = new AndFilter(childFilters);
            if ((childFilters != null) && (! childFilters.isEmpty())) {
                LOGGER.info("Parsing XML AndLogicType as AndFilter for [{}] child filters ...", childFilters.size());
            }

        } else if (logic instanceof OrLogicType) {
            OrLogicType orLogic = (OrLogicType) logic;
            List<AbstractFilter> childFilters = this.parseXmlFilters(orLogic.getComparisonOrLogic());
            if ((childFilters != null) && (! childFilters.isEmpty())) {
                filter = new OrFilter(childFilters);
                LOGGER.info("Parsing XML OrLogicType as OrFilter for [{}] child filters ...", childFilters.size());
            }
        }

        return filter;
    }

    public String getTagName(JAXBElement<?> element) {
        QName qName = (element == null) ? null : element.getName();
        String tagName = (qName == null) ? null : qName.getLocalPart();
        return tagName;
    }
}

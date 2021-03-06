package net.ldauvilaire.sample.xsd.parser;

import static net.ldauvilaire.sample.jms.domain.filter.XmlFilterConstants.FILTER_SCHEMA_FILE;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.calcite.sql.parser.SqlParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.xml.sax.SAXException;

import net.ldauvilaire.sample.jms.configuration.ApplicationTestConfiguration;
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
import net.ldauvilaire.sample.jms.parser.XmlFilterParser;
import net.ldauvilaire.xsd.filter.FilterType;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationTestConfiguration.class)
public class XmlFilterParserTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlFilterParserTest.class);

    private static final String COMPLEX_FILTER = "data/xsd/complex-filter.xml";

    private static final String LOGIC_AND_FILTER = "data/xsd/logic-and-filter.xml";
    private static final String LOGIC_NOT_FILTER = "data/xsd/logic-not-filter.xml";
    private static final String LOGIC_OR_FILTER = "data/xsd/logic-or-filter.xml";

    private static final String SIMPLE_BETWEEN_COMPARATOR = "data/xsd/simple-between-comparator.xml";
    private static final String SIMPLE_EQUALS_COMPARATOR = "data/xsd/simple-equals-comparator.xml";
    private static final String SIMPLE_GREATER_OR_EQUALS_COMPARATOR = "data/xsd/simple-greater-or-equals-comparator.xml";
    private static final String SIMPLE_GREATER_THAN_COMPARATOR = "data/xsd/simple-greater-than-comparator.xml";
    private static final String SIMPLE_IN_COMPARATOR = "data/xsd/simple-in-comparator.xml";
    private static final String SIMPLE_LESS_OR_EQUALS_COMPARATOR = "data/xsd/simple-less-or-equals-comparator.xml";
    private static final String SIMPLE_LESS_THAN_COMPARATOR = "data/xsd/simple-less-than-comparator.xml";
    private static final String SIMPLE_NOT_EQUALS_COMPARATOR = "data/xsd/simple-not-equals-comparator.xml";
    private static final String SIMPLE_NOT_NULL_COMPARATOR = "data/xsd/simple-not-null-comparator.xml";
    private static final String SIMPLE_NULL_COMPARATOR = "data/xsd/simple-null-comparator.xml";

    static {
        System.setProperty("org.jboss.logging.provider", "slf4j");
    }

    @Autowired
    private XmlFilterParser xmlFilterParser;

    @Test
    public void shouldParseComplexFilter() throws SqlParseException {

        FilterType filterType = parseResource(COMPLEX_FILTER);
        Assert.assertNotNull(filterType);
        LOGGER.info("Filter: \n{}", toXml(filterType));

        AbstractFilter result = xmlFilterParser.parseXmlFilter(filterType);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof AndFilter);

        LOGGER.info("Filter as SQL: \n{}", result.toSqlString());
        LOGGER.info("Filter as XML: \n{}", result.toXmlString(1));

        AndFilter filter = (AndFilter) result;
        Assert.assertEquals(LogicOperator.AND, filter.getOperator());
        Assert.assertNotNull(filter.getFilters());
        Assert.assertEquals(3, filter.getFilters().size());
        Assert.assertTrue(filter.getFilters().get(0) instanceof InFilter);
        Assert.assertTrue(filter.getFilters().get(1) instanceof IsNullFilter);
        Assert.assertTrue(filter.getFilters().get(2) instanceof OrFilter);

        InFilter inFilter = (InFilter) filter.getFilters().get(0);
        Assert.assertEquals(ComparisonOperator.IN, inFilter.getOperator());
        Assert.assertEquals("AIRLINE_CODE", inFilter.getAttribute());
        Assert.assertNotNull(inFilter.getValues());
        Assert.assertEquals(3, inFilter.getValues().size());
        Assert.assertEquals("AF", inFilter.getValues().get(0));
        Assert.assertEquals("KL", inFilter.getValues().get(1));
        Assert.assertEquals("A5", inFilter.getValues().get(2));

        IsNullFilter isNullFilter = (IsNullFilter) filter.getFilters().get(1);
        Assert.assertEquals(ComparisonOperator.IS_NULL, isNullFilter.getOperator());
        Assert.assertEquals("OPERATIONAL_SUFFIX", isNullFilter.getAttribute());

        OrFilter orFilter = (OrFilter) filter.getFilters().get(2);
        Assert.assertEquals(LogicOperator.OR, orFilter.getOperator());
        Assert.assertNotNull(orFilter.getFilters());
        Assert.assertEquals(2, orFilter.getFilters().size());
        Assert.assertTrue(orFilter.getFilters().get(0) instanceof NotFilter);
        Assert.assertTrue(orFilter.getFilters().get(1) instanceof EqualsFilter);

        NotFilter notFilter = (NotFilter) orFilter.getFilters().get(0);
        Assert.assertEquals(LogicOperator.NOT, notFilter.getOperator());
        Assert.assertNotNull(notFilter.getFilter());
        Assert.assertTrue(notFilter.getFilter() instanceof EqualsFilter);

        EqualsFilter firstEqualsFilter = (EqualsFilter) notFilter.getFilter();
        Assert.assertEquals(ComparisonOperator.EQUALS, firstEqualsFilter.getOperator());
        Assert.assertEquals("IATA_AIRPORT_CODE", firstEqualsFilter.getAttribute());
        Assert.assertEquals("CDG", firstEqualsFilter.getValue());

        EqualsFilter secondEqualsFilter = (EqualsFilter) orFilter.getFilters().get(1);
        Assert.assertEquals(ComparisonOperator.EQUALS, secondEqualsFilter.getOperator());
        Assert.assertEquals("FLIGHT_NUMBER", secondEqualsFilter.getAttribute());
        Assert.assertEquals("1234", secondEqualsFilter.getValue());
    }

    @Test
    public void shouldParseLogicAndFilter() throws SqlParseException {

        FilterType filterType = parseResource(LOGIC_AND_FILTER);
        Assert.assertNotNull(filterType);
        LOGGER.info("Filter: \n{}", toXml(filterType));

        AbstractFilter result = xmlFilterParser.parseXmlFilter(filterType);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof AndFilter);

        LOGGER.info("Filter as SQL: \n{}", result.toSqlString());
        LOGGER.info("Filter as XML: \n{}", result.toXmlString(1));

        AndFilter filter = (AndFilter) result;
        Assert.assertEquals(LogicOperator.AND, filter.getOperator());
        Assert.assertNotNull(filter.getFilters());
        Assert.assertEquals(3, filter.getFilters().size());
        Assert.assertTrue(filter.getFilters().get(0) instanceof InFilter);
        Assert.assertTrue(filter.getFilters().get(1) instanceof EqualsFilter);
        Assert.assertTrue(filter.getFilters().get(2) instanceof IsNullFilter);

        InFilter inFilter = (InFilter) filter.getFilters().get(0);
        Assert.assertEquals(ComparisonOperator.IN, inFilter.getOperator());
        Assert.assertEquals("AIRLINE_CODE", inFilter.getAttribute());
        Assert.assertNotNull(inFilter.getValues());
        Assert.assertEquals(3, inFilter.getValues().size());
        Assert.assertEquals("AF", inFilter.getValues().get(0));
        Assert.assertEquals("KL", inFilter.getValues().get(1));
        Assert.assertEquals("A5", inFilter.getValues().get(2));

        EqualsFilter equalsFilter = (EqualsFilter) filter.getFilters().get(1);
        Assert.assertEquals(ComparisonOperator.EQUALS, equalsFilter.getOperator());
        Assert.assertEquals("FLIGHT_NUMBER", equalsFilter.getAttribute());
        Assert.assertEquals("1234", equalsFilter.getValue());

        IsNullFilter isNullFilter = (IsNullFilter) filter.getFilters().get(2);
        Assert.assertEquals(ComparisonOperator.IS_NULL, isNullFilter.getOperator());
        Assert.assertEquals("OPERATIONAL_SUFFIX", isNullFilter.getAttribute());
    }

    @Test
    public void shouldParseLogicNotFilter() throws SqlParseException {

        FilterType filterType = parseResource(LOGIC_NOT_FILTER);
        Assert.assertNotNull(filterType);
        LOGGER.info("Filter: \n{}", toXml(filterType));

        AbstractFilter result = xmlFilterParser.parseXmlFilter(filterType);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof NotFilter);

        LOGGER.info("Filter as SQL: \n{}", result.toSqlString());
        LOGGER.info("Filter as XML: \n{}", result.toXmlString(1));

        NotFilter filter = (NotFilter) result;
        Assert.assertEquals(LogicOperator.NOT, filter.getOperator());
        Assert.assertNotNull(filter.getFilter());
        Assert.assertTrue(filter.getFilter() instanceof InFilter);

        InFilter inFilter = (InFilter) filter.getFilter();
        Assert.assertEquals(ComparisonOperator.IN, inFilter.getOperator());
        Assert.assertEquals("IATA_AIRPORT_CODE", inFilter.getAttribute());
        Assert.assertNotNull(inFilter.getValues());
        Assert.assertEquals(2, inFilter.getValues().size());
        Assert.assertEquals("MLH", inFilter.getValues().get(0));
        Assert.assertEquals("BSL", inFilter.getValues().get(1));
    }

    @Test
    public void shouldParseLogicOrFilter() throws SqlParseException {

        FilterType filterType = parseResource(LOGIC_OR_FILTER);
        Assert.assertNotNull(filterType);
        LOGGER.info("Filter: \n{}", toXml(filterType));

        AbstractFilter result = xmlFilterParser.parseXmlFilter(filterType);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof OrFilter);

        LOGGER.info("Filter as SQL: \n{}", result.toSqlString());
        LOGGER.info("Filter as XML: \n{}", result.toXmlString(1));

        OrFilter filter = (OrFilter) result;
        Assert.assertEquals(LogicOperator.OR, filter.getOperator());
        Assert.assertNotNull(filter.getFilters());
        Assert.assertEquals(2, filter.getFilters().size());
        Assert.assertTrue(filter.getFilters().get(0) instanceof InFilter);
        Assert.assertTrue(filter.getFilters().get(1) instanceof EqualsFilter);

        InFilter inFilter = (InFilter) filter.getFilters().get(0);
        Assert.assertEquals(ComparisonOperator.IN, inFilter.getOperator());
        Assert.assertEquals("IATA_AIRPORT_CODE", inFilter.getAttribute());
        Assert.assertNotNull(inFilter.getValues());
        Assert.assertEquals(2, inFilter.getValues().size());
        Assert.assertEquals("MLH", inFilter.getValues().get(0));
        Assert.assertEquals("BSL", inFilter.getValues().get(1));

        EqualsFilter equalsFilter = (EqualsFilter) filter.getFilters().get(1);
        Assert.assertEquals(ComparisonOperator.EQUALS, equalsFilter.getOperator());
        Assert.assertEquals("AIRCRAFT_TYPE", equalsFilter.getAttribute());
        Assert.assertEquals("RFS", equalsFilter.getValue());
    }

    @Test
    public void shouldParseSimpleBetweenFilter() throws SqlParseException {

        FilterType filterType = parseResource(SIMPLE_BETWEEN_COMPARATOR);
        Assert.assertNotNull(filterType);
        LOGGER.info("Filter: \n{}", toXml(filterType));

        AbstractFilter result = xmlFilterParser.parseXmlFilter(filterType);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof BetweenFilter);

        LOGGER.info("Filter as SQL: \n{}", result.toSqlString());
        LOGGER.info("Filter as XML: \n{}", result.toXmlString(1));

        BetweenFilter filter = (BetweenFilter) result;
        Assert.assertEquals(ComparisonOperator.BETWEEN, filter.getOperator());
        Assert.assertEquals("FLIGHT_NUMBER", filter.getAttribute());
        Assert.assertEquals("1200", filter.getLower());
        Assert.assertEquals("1300", filter.getUpper());
    }

    @Test
    public void shouldParseSimpleEqualsFilter() throws SqlParseException {

        FilterType filterType = parseResource(SIMPLE_EQUALS_COMPARATOR);
        Assert.assertNotNull(filterType);
        LOGGER.info("Filter: \n{}", toXml(filterType));

        AbstractFilter result = xmlFilterParser.parseXmlFilter(filterType);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof EqualsFilter);

        LOGGER.info("Filter as SQL: \n{}", result.toSqlString());
        LOGGER.info("Filter as XML: \n{}", result.toXmlString(1));

        EqualsFilter filter = (EqualsFilter) result;
        Assert.assertEquals(ComparisonOperator.EQUALS, filter.getOperator());
        Assert.assertEquals("FLIGHT_NUMBER", filter.getAttribute());
        Assert.assertEquals("1234", filter.getValue());
    }

    @Test
    public void shouldParseSimpleGreaterOrEqualsFilter() throws SqlParseException {

        FilterType filterType = parseResource(SIMPLE_GREATER_OR_EQUALS_COMPARATOR);
        Assert.assertNotNull(filterType);
        LOGGER.info("Filter: \n{}", toXml(filterType));

        AbstractFilter result = xmlFilterParser.parseXmlFilter(filterType);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof GreaterOrEqualsFilter);

        LOGGER.info("Filter as SQL: \n{}", result.toSqlString());
        LOGGER.info("Filter as XML: \n{}", result.toXmlString(1));

        GreaterOrEqualsFilter filter = (GreaterOrEqualsFilter) result;
        Assert.assertEquals(ComparisonOperator.GREATER_OR_EQUALS, filter.getOperator());
        Assert.assertEquals("FLIGHT_NUMBER", filter.getAttribute());
        Assert.assertEquals("1234", filter.getValue());
    }

    @Test
    public void shouldParseSimpleGreaterThanFilter() throws SqlParseException {

        FilterType filterType = parseResource(SIMPLE_GREATER_THAN_COMPARATOR);
        Assert.assertNotNull(filterType);
        LOGGER.info("Filter: \n{}", toXml(filterType));

        AbstractFilter result = xmlFilterParser.parseXmlFilter(filterType);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof GreaterThanFilter);

        LOGGER.info("Filter as SQL: \n{}", result.toSqlString());
        LOGGER.info("Filter as XML: \n{}", result.toXmlString(1));

        GreaterThanFilter filter = (GreaterThanFilter) result;
        Assert.assertEquals(ComparisonOperator.GREATER_THAN, filter.getOperator());
        Assert.assertEquals("FLIGHT_NUMBER", filter.getAttribute());
        Assert.assertEquals("1234", filter.getValue());
    }

    @Test
    public void shouldParseSimpleInFilter() throws SqlParseException {

        FilterType filterType = parseResource(SIMPLE_IN_COMPARATOR);
        Assert.assertNotNull(filterType);
        LOGGER.info("Filter: \n{}", toXml(filterType));

        AbstractFilter result = xmlFilterParser.parseXmlFilter(filterType);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof InFilter);

        LOGGER.info("Filter as SQL: \n{}", result.toSqlString());
        LOGGER.info("Filter as XML: \n{}", result.toXmlString(1));

        InFilter filter = (InFilter) result;
        Assert.assertEquals(ComparisonOperator.IN, filter.getOperator());
        Assert.assertEquals("FLIGHT_NUMBER", filter.getAttribute());
        Assert.assertNotNull(filter.getValues());
        Assert.assertEquals(3, filter.getValues().size());
        Assert.assertEquals("1234", filter.getValues().get(0));
        Assert.assertEquals("1256", filter.getValues().get(1));
        Assert.assertEquals("1278", filter.getValues().get(2));
    }

    @Test
    public void shouldParseSimpleLessOrEqualsFilter() throws SqlParseException {

        FilterType filterType = parseResource(SIMPLE_LESS_OR_EQUALS_COMPARATOR);
        Assert.assertNotNull(filterType);
        LOGGER.info("Filter: \n{}", toXml(filterType));

        AbstractFilter result = xmlFilterParser.parseXmlFilter(filterType);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof LessOrEqualsFilter);

        LOGGER.info("Filter as SQL: \n{}", result.toSqlString());
        LOGGER.info("Filter as XML: \n{}", result.toXmlString(1));

        LessOrEqualsFilter filter = (LessOrEqualsFilter) result;
        Assert.assertEquals(ComparisonOperator.LESS_OR_EQUALS, filter.getOperator());
        Assert.assertEquals("FLIGHT_NUMBER", filter.getAttribute());
        Assert.assertEquals("1234", filter.getValue());
    }

    @Test
    public void shouldParseSimpleLessThanFilter() throws SqlParseException {

        FilterType filterType = parseResource(SIMPLE_LESS_THAN_COMPARATOR);
        Assert.assertNotNull(filterType);
        LOGGER.info("Filter: \n{}", toXml(filterType));

        AbstractFilter result = xmlFilterParser.parseXmlFilter(filterType);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof LessThanFilter);

        LOGGER.info("Filter as SQL: \n{}", result.toSqlString());
        LOGGER.info("Filter as XML: \n{}", result.toXmlString(1));

        LessThanFilter filter = (LessThanFilter) result;
        Assert.assertEquals(ComparisonOperator.LESS_THAN, filter.getOperator());
        Assert.assertEquals("FLIGHT_NUMBER", filter.getAttribute());
        Assert.assertEquals("1234", filter.getValue());
    }

    @Test
    public void shouldParseSimpleNotEqualsFilter() throws SqlParseException {

        FilterType filterType = parseResource(SIMPLE_NOT_EQUALS_COMPARATOR);
        Assert.assertNotNull(filterType);
        LOGGER.info("Filter: \n{}", toXml(filterType));

        AbstractFilter result = xmlFilterParser.parseXmlFilter(filterType);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof NotEqualsFilter);

        LOGGER.info("Filter as SQL: \n{}", result.toSqlString());
        LOGGER.info("Filter as XML: \n{}", result.toXmlString(1));

        NotEqualsFilter filter = (NotEqualsFilter) result;
        Assert.assertEquals(ComparisonOperator.NOT_EQUALS, filter.getOperator());
        Assert.assertEquals("FLIGHT_NUMBER", filter.getAttribute());
        Assert.assertEquals("1234", filter.getValue());
    }

    @Test
    public void shouldParseSimpleIsNotNullFilter() throws SqlParseException {

        FilterType filterType = parseResource(SIMPLE_NOT_NULL_COMPARATOR);
        Assert.assertNotNull(filterType);
        LOGGER.info("Filter: \n{}", toXml(filterType));

        AbstractFilter result = xmlFilterParser.parseXmlFilter(filterType);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof IsNotNullFilter);

        LOGGER.info("Filter as SQL: \n{}", result.toSqlString());
        LOGGER.info("Filter as XML: \n{}", result.toXmlString(1));

        IsNotNullFilter filter = (IsNotNullFilter) result;
        Assert.assertEquals(ComparisonOperator.IS_NOT_NULL, filter.getOperator());
        Assert.assertEquals("OPERATIONAL_SUFFIX", filter.getAttribute());
    }

    @Test
    public void shouldParseSimpleIsNullFilter() throws SqlParseException {

        FilterType filterType = parseResource(SIMPLE_NULL_COMPARATOR);
        Assert.assertNotNull(filterType);
        LOGGER.info("Filter: \n{}", toXml(filterType));

        AbstractFilter result = xmlFilterParser.parseXmlFilter(filterType);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof IsNullFilter);

        LOGGER.info("Filter as SQL: \n{}", result.toSqlString());
        LOGGER.info("Filter as XML: \n{}", result.toXmlString(1));

        IsNullFilter filter = (IsNullFilter) result;
        Assert.assertEquals(ComparisonOperator.IS_NULL, filter.getOperator());
        Assert.assertEquals("OPERATIONAL_SUFFIX", filter.getAttribute());
    }

    @SuppressWarnings("unchecked")
    private static FilterType parseResource(String path) {

        FilterType filter;
        {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);

            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            try {
                URL resource = Thread.currentThread().getContextClassLoader().getResource(FILTER_SCHEMA_FILE);
                Schema schema = sf.newSchema(resource);
                JAXBContext jc = JAXBContext.newInstance(FilterType.class);
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                unmarshaller.setSchema(schema);
                JAXBElement<FilterType> object = (JAXBElement<FilterType>) unmarshaller.unmarshal(is);
                filter = object.getValue();
            } catch (JAXBException | SAXException exception) {
                throw new RuntimeException("error during the unmarshalling of the file:" + path, exception);
            }
        }

        return filter;
    }

    private static String toXml(FilterType filter) {
        StringWriter stringWriter = new StringWriter();
        try {
            JAXBContext jc = JAXBContext.newInstance(filter.getClass());  
            Marshaller marshaller = jc.createMarshaller();  
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            QName rootQName = new QName("http://www.af-klm.com/filter", "Filter");
            JAXBElement<FilterType> root = new JAXBElement<>(rootQName, FilterType.class, filter);
            marshaller.marshal(root, stringWriter);
        } catch (JAXBException ex) {
            LOGGER.error("A JAXBException has occurred while converting FilterType to XML String", ex);
        }
        return stringWriter.toString();
    }
}

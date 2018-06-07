package net.ldauvilaire.sample.xsd.parser;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import net.ldauvilaire.xsd.filter.BetweenComparisonType;
import net.ldauvilaire.xsd.filter.BinaryComparisonType;
import net.ldauvilaire.xsd.filter.BinaryLogicType;
import net.ldauvilaire.xsd.filter.ComparisonType;
import net.ldauvilaire.xsd.filter.FilterType;
import net.ldauvilaire.xsd.filter.InComparisonType;
import net.ldauvilaire.xsd.filter.LogicType;
import net.ldauvilaire.xsd.filter.UnaryComparisonType;
import net.ldauvilaire.xsd.filter.UnaryLogicType;

public class FilterParserTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(FilterParserTest.class);

	private static final String FILTER_SCHEMA_FILE = "xsd/filter/filter.xsd";

	private static final String COMPLEX_FILTER = "data/xsd/complex-filter.xml";

	private static final String LOGIC_AND_FILTER = "data/xsd/logic-and-filter.xml";
	private static final String LOGIC_OR_FILTER = "data/xsd/logic-or-filter.xml";
	private static final String LOGIC_NOT_FILTER = "data/xsd/logic-not-filter.xml";

	private static final String SIMPLE_BETWEEN_COMPARATOR = "data/xsd/simple-between-comparator.xml";
	private static final String SIMPLE_EQUALS_COMPARATOR = "data/xsd/simple-equals-comparator.xml";
	private static final String SIMPLE_GREATER_OR_EQUALS_COMPARATOR = "data/xsd/simple-greater-or-equals-comparator.xml";
	private static final String SIMPLE_GREATER_THAN_COMPARATOR = "data/xsd/simple-greater-than-comparator.xml";
	private static final String SIMPLE_IN_COMPARATOR = "data/xsd/simple-in-comparator.xml";
	private static final String SIMPLE_LESS_OR_EQUALS_COMPARATOR = "data/xsd/simple-less-or-equals-comparator.xml";
	private static final String SIMPLE_LESS_THAN_COMPARATOR = "data/xsd/simple-less-than-comparator.xml";
	private static final String SIMPLE_NOT_EQUALS_COMPARATOR = "data/xsd/simple-not-equals-comparator.xml";
	private static final String SIMPLE_NULL_COMPARATOR = "data/xsd/simple-null-comparator.xml";
	private static final String SIMPLE_NOT_NULL_COMPARATOR = "data/xsd/simple-not-null-comparator.xml";

	@Before
	public void setUp() {
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldParseComplexFilter() throws SqlParseException {

		FilterType filter = parseResource(COMPLEX_FILTER);

		Assert.assertNotNull(filter);
		LOGGER.info("Filter: \n{}", toXml(filter));

		JAXBElement<? extends LogicType> element = filter.getLogic();
		Pair<String, List<JAXBElement<?>>> pair = extractBinaryLogic(element);

		String operator = pair.getLeft();
		List<JAXBElement<?>> comparatorOrLogicList = pair.getRight();

		Assert.assertEquals("And", operator);
		Assert.assertNotNull(comparatorOrLogicList);
		Assert.assertEquals(3, comparatorOrLogicList.size());

		JAXBElement<?> firstComparatorElement = comparatorOrLogicList.get(0);
		JAXBElement<?> secondComparatorElement = comparatorOrLogicList.get(1);
		JAXBElement<?> thirdComparatorElement = comparatorOrLogicList.get(2);

		Assert.assertTrue(firstComparatorElement.getValue() instanceof ComparisonType);
		Assert.assertTrue(secondComparatorElement.getValue() instanceof ComparisonType);
		Assert.assertTrue(thirdComparatorElement.getValue() instanceof LogicType);

		Triple<String, String, List<String>> firstParameters = extractInComparator((JAXBElement<ComparisonType>) firstComparatorElement);
		String firstOperator = firstParameters.getLeft();
		String firstAttribute = firstParameters.getMiddle();
		List<String> firstValues = firstParameters.getRight();

		Assert.assertEquals("In", firstOperator);
		Assert.assertEquals("airlineCode", firstAttribute);
		Assert.assertNotNull(firstValues);
		Assert.assertEquals(3, firstValues.size());
		Assert.assertEquals("AF", firstValues.get(0));
		Assert.assertEquals("KL", firstValues.get(1));
		Assert.assertEquals("A5", firstValues.get(2));

		Pair<String, String> secondParameters = extractUnaryComparator((JAXBElement<ComparisonType>) secondComparatorElement);
		String secondOperator = secondParameters.getLeft();
		String secondAttribute = secondParameters.getRight();

		Assert.assertEquals("IsNull", secondOperator);
		Assert.assertEquals("operationalSuffix", secondAttribute);

		Pair<String, List<JAXBElement<?>>> thirdParameters = extractBinaryLogic((JAXBElement<LogicType>) thirdComparatorElement);
		String thirdOperator = thirdParameters.getLeft();
		List<JAXBElement<?>> thirdComparatorOrLogicList = thirdParameters.getRight(); 

		Assert.assertEquals("Or", thirdOperator);
		Assert.assertNotNull(thirdComparatorOrLogicList);
		Assert.assertEquals(2, thirdComparatorOrLogicList.size());

		JAXBElement<?> firstThirdComparatorElement = thirdComparatorOrLogicList.get(0);
		JAXBElement<?> secondThirdComparatorElement = thirdComparatorOrLogicList.get(1);

		Assert.assertTrue(firstThirdComparatorElement.getValue() instanceof LogicType);
		Assert.assertTrue(secondThirdComparatorElement.getValue() instanceof ComparisonType);

		Pair<String, JAXBElement<ComparisonType>> firstThirdParameters = extractUnaryLogicWithComparators((JAXBElement<LogicType>) firstThirdComparatorElement);
		String firstThirdOperator = firstThirdParameters.getLeft();
		JAXBElement<ComparisonType> firstThirdComparator = firstThirdParameters.getRight();

		Assert.assertEquals("Not", firstThirdOperator);
		Assert.assertNotNull(firstThirdComparator);

		Triple<String, String, String> firstThirdComparatorParameters = extractBinaryComparator(firstThirdComparator);
		String firstThirdComparatorOperator = firstThirdComparatorParameters.getLeft();
		String firstThirdComparatorAttribute = firstThirdComparatorParameters.getMiddle();
		String firstThirdComparatorValue = firstThirdComparatorParameters.getRight();

		Assert.assertEquals("Equals", firstThirdComparatorOperator);
		Assert.assertEquals("iataAirportCode", firstThirdComparatorAttribute);
		Assert.assertEquals("CDG", firstThirdComparatorValue);

		Triple<String, String, String> secondThirdParameters = extractBinaryComparator((JAXBElement<ComparisonType>) secondThirdComparatorElement);
		String secondThirdOperator = secondThirdParameters.getLeft();
		String secondThirdAttribute = secondThirdParameters.getMiddle();
		String secondThirdValue = secondThirdParameters.getRight();

		Assert.assertEquals("Equals", secondThirdOperator);
		Assert.assertEquals("flightNumber", secondThirdAttribute);
		Assert.assertEquals("1234", secondThirdValue);
	}

	@Test
	public void shouldParseLogicAndFilter() throws SqlParseException {

		FilterType filter = parseResource(LOGIC_AND_FILTER);

		Assert.assertNotNull(filter);
		LOGGER.info("Filter: \n{}", toXml(filter));

		JAXBElement<? extends LogicType> element = filter.getLogic();
		Pair<String, List<JAXBElement<ComparisonType>>> pair = extractBinaryLogicWithComparators(element);
		String operator = pair.getLeft();
		List<JAXBElement<ComparisonType>> comparators = pair.getRight();

		Assert.assertEquals("And", operator);
		Assert.assertNotNull(comparators);
		Assert.assertEquals(3, comparators.size());

		JAXBElement<ComparisonType> firstComparatorElement = comparators.get(0);
		JAXBElement<ComparisonType> secondComparatorElement = comparators.get(1);
		JAXBElement<ComparisonType> thirdComparatorElement = comparators.get(2);

		Triple<String, String, List<String>> firstParameters = extractInComparator(firstComparatorElement);
		String firstOperator = firstParameters.getLeft();
		String firstAttribute = firstParameters.getMiddle();
		List<String> firstValues = firstParameters.getRight();

		Assert.assertEquals("In", firstOperator);
		Assert.assertEquals("airlineCode", firstAttribute);
		Assert.assertNotNull(firstValues);
		Assert.assertEquals(3, firstValues.size());
		Assert.assertEquals("AF", firstValues.get(0));
		Assert.assertEquals("KL", firstValues.get(1));
		Assert.assertEquals("A5", firstValues.get(2));

		Triple<String, String, String> secondParameters = extractBinaryComparator(secondComparatorElement);
		String secondOperator = secondParameters.getLeft();
		String secondAttribute = secondParameters.getMiddle();
		String secondValue = secondParameters.getRight();

		Assert.assertEquals("Equals", secondOperator);
		Assert.assertEquals("flightNumber", secondAttribute);
		Assert.assertEquals("1234", secondValue);

		Pair<String, String> thirdParameters = extractUnaryComparator(thirdComparatorElement);
		String thirdOperator = thirdParameters.getLeft();
		String thirdAttribute = thirdParameters.getRight();

		Assert.assertEquals("IsNull", thirdOperator);
		Assert.assertEquals("operationalSuffix", thirdAttribute);
	}

	@Test
	public void shouldParseLogicOrFilter() throws SqlParseException {

		FilterType filter = parseResource(LOGIC_OR_FILTER);

		Assert.assertNotNull(filter);
		LOGGER.info("Filter: \n{}", toXml(filter));

		JAXBElement<? extends LogicType> element = filter.getLogic();
		Pair<String, List<JAXBElement<ComparisonType>>> pair = extractBinaryLogicWithComparators(element);
		String operator = pair.getLeft();
		List<JAXBElement<ComparisonType>> comparators = pair.getRight();

		Assert.assertEquals("Or", operator);
		Assert.assertNotNull(comparators);
		Assert.assertEquals(2, comparators.size());

		JAXBElement<ComparisonType> firstComparatorElement = comparators.get(0);
		JAXBElement<ComparisonType> secondComparatorElement = comparators.get(1);

		Triple<String, String, List<String>> firstParameters = extractInComparator(firstComparatorElement);
		String firstOperator = firstParameters.getLeft();
		String firstAttribute = firstParameters.getMiddle();
		List<String> firstValues = firstParameters.getRight();

		Assert.assertEquals("In", firstOperator);
		Assert.assertEquals("iataAirportCode", firstAttribute);
		Assert.assertNotNull(firstValues);
		Assert.assertEquals(2, firstValues.size());
		Assert.assertEquals("MLH", firstValues.get(0));
		Assert.assertEquals("BSL", firstValues.get(1));

		Triple<String, String, String> secondParameters = extractBinaryComparator(secondComparatorElement);
		String secondOperator = secondParameters.getLeft();
		String secondAttribute = secondParameters.getMiddle();
		String secondValue = secondParameters.getRight();

		Assert.assertEquals("Equals", secondOperator);
		Assert.assertEquals("aircraftType", secondAttribute);
		Assert.assertEquals("RFS", secondValue);
	}

	@Test
	public void shouldParseLogicNotFilter() throws SqlParseException {

		FilterType filter = parseResource(LOGIC_NOT_FILTER);

		Assert.assertNotNull(filter);
		LOGGER.info("Filter: \n{}", toXml(filter));

		JAXBElement<? extends LogicType> element = filter.getLogic();
		Pair<String, JAXBElement<ComparisonType>> pair = extractUnaryLogicWithComparators(element);
		String operator = pair.getLeft();
		JAXBElement<ComparisonType> comparator = pair.getRight();

		Assert.assertEquals("Not", operator);
		Assert.assertNotNull(comparator);

		Triple<String, String, List<String>> parameters = extractInComparator(comparator);
		String comparatorOperator = parameters.getLeft();
		String comparatorAttribute = parameters.getMiddle();
		List<String> comparatorValues = parameters.getRight();

		Assert.assertEquals("In", comparatorOperator);
		Assert.assertEquals("iataAirportCode", comparatorAttribute);
		Assert.assertNotNull(comparatorValues);
		Assert.assertEquals(2, comparatorValues.size());
		Assert.assertEquals("MLH", comparatorValues.get(0));
		Assert.assertEquals("BSL", comparatorValues.get(1));
	}

	@Test
	public void shouldParseSimpleBetweenComparator() throws SqlParseException {

		FilterType filter = parseResource(SIMPLE_BETWEEN_COMPARATOR);

		Assert.assertNotNull(filter);
		LOGGER.info("Filter: \n{}", toXml(filter));

		JAXBElement<? extends ComparisonType> element = filter.getComparison();
		Pair<String, Triple<String,String,String>> extracted = extractBetweenComparator(element);
		String operator = extracted.getLeft();
		Triple<String,String,String> triple = extracted.getRight();
		String attribute = triple.getLeft();
		String lower = triple.getMiddle();
		String upper = triple.getRight();

		Assert.assertEquals("Between", operator);
		Assert.assertEquals("flightNumber", attribute);
		Assert.assertEquals("1200", lower);
		Assert.assertEquals("1300", upper);
	}

	@Test
	public void shouldParseSimpleEqualsComparator() throws SqlParseException {

		FilterType filter = parseResource(SIMPLE_EQUALS_COMPARATOR);

		Assert.assertNotNull(filter);
		LOGGER.info("Filter: \n{}", toXml(filter));

		JAXBElement<? extends ComparisonType> element = filter.getComparison();
		Triple<String,String,String> triple = extractBinaryComparator(element);
		String operator = triple.getLeft();
		String attribute = triple.getMiddle();
		String value = triple.getRight();

		Assert.assertEquals("Equals", operator);
		Assert.assertEquals("flightNumber", attribute);
		Assert.assertEquals("1234", value);
	}

	@Test
	public void shouldParseSimpleGreaterOrEqualsComparator() throws SqlParseException {

		FilterType filter = parseResource(SIMPLE_GREATER_OR_EQUALS_COMPARATOR);

		Assert.assertNotNull(filter);
		LOGGER.info("Filter: \n{}", toXml(filter));

		JAXBElement<? extends ComparisonType> element = filter.getComparison();
		Triple<String,String,String> triple = extractBinaryComparator(element);
		String operator = triple.getLeft();
		String attribute = triple.getMiddle();
		String value = triple.getRight();

		Assert.assertEquals("GreaterOrEquals", operator);
		Assert.assertEquals("flightNumber", attribute);
		Assert.assertEquals("1234", value);
	}

	@Test
	public void shouldParseSimpleGreaterThanComparator() throws SqlParseException {

		FilterType filter = parseResource(SIMPLE_GREATER_THAN_COMPARATOR);

		Assert.assertNotNull(filter);
		LOGGER.info("Filter: \n{}", toXml(filter));

		JAXBElement<? extends ComparisonType> element = filter.getComparison();
		Triple<String,String,String> triple = extractBinaryComparator(element);
		String operator = triple.getLeft();
		String attribute = triple.getMiddle();
		String value = triple.getRight();

		Assert.assertEquals("Greater", operator);
		Assert.assertEquals("flightNumber", attribute);
		Assert.assertEquals("1234", value);
	}

	@Test
	public void shouldParseSimpleInComparator() throws SqlParseException {

		FilterType filter = parseResource(SIMPLE_IN_COMPARATOR);

		Assert.assertNotNull(filter);
		LOGGER.info("Filter: \n{}", toXml(filter));

		JAXBElement<? extends ComparisonType> element = filter.getComparison();
		Triple<String, String, List<String>> triple = extractInComparator(element);
		String operator = triple.getLeft();
		String attribute = triple.getMiddle();
		List<String> values = triple.getRight();

		Assert.assertEquals("In", operator);
		Assert.assertEquals("flightNumber", attribute);
		Assert.assertNotNull(values);
		Assert.assertEquals(3, values.size());
		Assert.assertEquals("1234", values.get(0));
		Assert.assertEquals("1256", values.get(1));
		Assert.assertEquals("1278", values.get(2));
	}

	@Test
	public void shouldParseSimpleLessOrEqualsComparator() throws SqlParseException {

		FilterType filter = parseResource(SIMPLE_LESS_OR_EQUALS_COMPARATOR);

		Assert.assertNotNull(filter);
		LOGGER.info("Filter: \n{}", toXml(filter));

		JAXBElement<? extends ComparisonType> element = filter.getComparison();
		Triple<String,String,String> triple = extractBinaryComparator(element);
		String operator = triple.getLeft();
		String attribute = triple.getMiddle();
		String value = triple.getRight();

		Assert.assertEquals("LessOrEquals", operator);
		Assert.assertEquals("flightNumber", attribute);
		Assert.assertEquals("1234", value);
	}

	@Test
	public void shouldParseSimpleLessThanComparator() throws SqlParseException {

		FilterType filter = parseResource(SIMPLE_LESS_THAN_COMPARATOR);

		Assert.assertNotNull(filter);
		LOGGER.info("Filter: \n{}", toXml(filter));

		JAXBElement<? extends ComparisonType> element = filter.getComparison();
		Triple<String,String,String> triple = extractBinaryComparator(element);
		String operator = triple.getLeft();
		String attribute = triple.getMiddle();
		String value = triple.getRight();

		Assert.assertEquals("Less", operator);
		Assert.assertEquals("flightNumber", attribute);
		Assert.assertEquals("1234", value);
	}

	@Test
	public void shouldParseSimpleNotEqualsComparator() throws SqlParseException {

		FilterType filter = parseResource(SIMPLE_NOT_EQUALS_COMPARATOR);

		Assert.assertNotNull(filter);
		LOGGER.info("Filter: \n{}", toXml(filter));

		JAXBElement<? extends ComparisonType> element = filter.getComparison();
		Triple<String,String,String> triple = extractBinaryComparator(element);
		String operator = triple.getLeft();
		String attribute = triple.getMiddle();
		String value = triple.getRight();

		Assert.assertEquals("NotEquals", operator);
		Assert.assertEquals("flightNumber", attribute);
		Assert.assertEquals("1234", value);
	}

	@Test
	public void shouldParseSimpleNullComparator() throws SqlParseException {

		FilterType filter = parseResource(SIMPLE_NULL_COMPARATOR);

		Assert.assertNotNull(filter);
		LOGGER.info("Filter: \n{}", toXml(filter));

		JAXBElement<? extends ComparisonType> element = filter.getComparison();
		Pair<String, String> pair = extractUnaryComparator(element);
		String operator = pair.getLeft();
		String attribute = pair.getRight();

		Assert.assertEquals("IsNull", operator);
		Assert.assertEquals("operationalSuffix", attribute);
	}

	@Test
	public void shouldParseSimpleNotNullComparator() throws SqlParseException {

		FilterType filter = parseResource(SIMPLE_NOT_NULL_COMPARATOR);

		Assert.assertNotNull(filter);
		LOGGER.info("Filter: \n{}", toXml(filter));

		JAXBElement<? extends ComparisonType> element = filter.getComparison();
		Pair<String, String> pair = extractUnaryComparator(element);
		String operator = pair.getLeft();
		String attribute = pair.getRight();

		Assert.assertEquals("IsNotNull", operator);
		Assert.assertEquals("operationalSuffix", attribute);
	}

	private Pair<String, Triple<String, String, String>> extractBetweenComparator(JAXBElement<? extends ComparisonType> element) {

		ComparisonType elementValue = element.getValue();
		if (! (elementValue instanceof BetweenComparisonType)) {
			return null;
		}

		BetweenComparisonType comparator = (BetweenComparisonType) elementValue;

		QName elementName = element.getName();
		String operator = elementName.getLocalPart();
		String attribute = comparator.getAttribute();
		String lower = comparator.getLower();
		String upper = comparator.getUpper();

		return Pair.of(operator, Triple.of(attribute, lower, upper));
	}

	private Triple<String, String, String> extractBinaryComparator(JAXBElement<? extends ComparisonType> element) {

		ComparisonType elementValue = element.getValue();
		if (! (elementValue instanceof BinaryComparisonType)) {
			return null;
		}

		BinaryComparisonType comparator = (BinaryComparisonType) elementValue;

		QName elementName = element.getName();
		String operator = elementName.getLocalPart();
		String attribute = comparator.getAttribute();
		String value = comparator.getValue();

		return Triple.of(operator, attribute, value);
	}

	private Pair<String, String> extractUnaryComparator(JAXBElement<? extends ComparisonType> element) {

		ComparisonType elementValue = element.getValue();
		if (! (elementValue instanceof UnaryComparisonType)) {
			return null;
		}

		UnaryComparisonType comparator = (UnaryComparisonType) elementValue;

		QName elementName = element.getName();
		String operator = elementName.getLocalPart();
		String attribute = comparator.getAttribute();

		return Pair.of(operator, attribute);
	}

	private Triple<String, String, List<String>> extractInComparator(JAXBElement<? extends ComparisonType> element) {

		ComparisonType elementValue = element.getValue();
		if (! (elementValue instanceof InComparisonType)) {
			return null;
		}

		InComparisonType comparator = (InComparisonType) elementValue;

		QName elementName = element.getName();
		String operator = elementName.getLocalPart();
		String attribute = comparator.getAttribute();
		List<String> values = comparator.getValue();

		return Triple.of(operator, attribute, values);
	}

	private Pair<String, List<JAXBElement<?>>> extractBinaryLogic(JAXBElement<? extends LogicType> element) {

		LogicType elementValue = element.getValue();
		if (! (elementValue instanceof BinaryLogicType)) {
			return null;
		}

		BinaryLogicType logic = (BinaryLogicType) elementValue;

		QName elementName = element.getName();
		String operator = elementName.getLocalPart();
		List<JAXBElement<?>> comparators = logic.getComparisonOrLogic();

		return Pair.of(operator, comparators);
	}

	@SuppressWarnings("unchecked")
	private Pair<String, List<JAXBElement<ComparisonType>>> extractBinaryLogicWithComparators(JAXBElement<? extends LogicType> element) {

		LogicType elementValue = element.getValue();
		if (! (elementValue instanceof BinaryLogicType)) {
			return null;
		}

		BinaryLogicType logic = (BinaryLogicType) elementValue;

		QName elementName = element.getName();
		String operator = elementName.getLocalPart();
		List<JAXBElement<ComparisonType>> comparators = new ArrayList<>();

		List<JAXBElement<?>> comparisonOrLogicList = logic.getComparisonOrLogic();
		if (comparisonOrLogicList != null) {
			for (JAXBElement<?> comparisonOrLogic : comparisonOrLogicList) {
				if (comparisonOrLogic.getValue() instanceof ComparisonType) {
					comparators.add((JAXBElement<ComparisonType>) comparisonOrLogic);
				}
			}
		}

		return Pair.of(operator, comparators);
	}

	@SuppressWarnings("unchecked")
	private Pair<String, JAXBElement<ComparisonType>> extractUnaryLogicWithComparators(JAXBElement<? extends LogicType> element) {

		LogicType elementValue = element.getValue();
		if (! (elementValue instanceof UnaryLogicType)) {
			return null;
		}

		UnaryLogicType logic = (UnaryLogicType) elementValue;

		QName elementName = element.getName();
		String operator = elementName.getLocalPart();
		JAXBElement<ComparisonType> comparison = (JAXBElement<ComparisonType>) logic.getComparison();

		return Pair.of(operator, comparison);
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

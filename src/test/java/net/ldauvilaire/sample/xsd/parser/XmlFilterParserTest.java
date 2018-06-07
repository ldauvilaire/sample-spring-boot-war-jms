package net.ldauvilaire.sample.xsd.parser;

import static net.ldauvilaire.sample.jms.domain.xsd.filter.XmlFilterConstants.FILTER_SCHEMA_FILE;

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
import net.ldauvilaire.sample.jms.domain.xsd.filter.AbstractFilter;
import net.ldauvilaire.sample.jms.domain.xsd.filter.logic.AndFilter;
import net.ldauvilaire.sample.jms.domain.xsd.parser.XmlFilterParser;
import net.ldauvilaire.xsd.filter.FilterType;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationTestConfiguration.class)
public class XmlFilterParserTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlFilterParserTest.class);

    private static final String COMPLEX_FILTER = "data/xsd/complex-filter.xml";

    static {
        System.setProperty("org.jboss.logging.provider", "slf4j");
    }

    @Autowired
    private XmlFilterParser xmlFilterParser;

    @Test
    public void shouldParseComplexFilter() throws SqlParseException {

        FilterType filter = parseResource(COMPLEX_FILTER);
        Assert.assertNotNull(filter);
        LOGGER.info("Filter: \n{}", toXml(filter));

        AbstractFilter result = xmlFilterParser.parseXmlFilter(filter);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof AndFilter);

        LOGGER.info("Filter as SQL: \n{}", result.toSqlString());
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

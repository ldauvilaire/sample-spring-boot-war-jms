package net.ldauvilaire.sample.sql.parser;

import java.util.List;

import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlParserTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SqlParserTest.class);

	@Before
	public void setUp() {
	}

	@Test
	public void shouldParseSimpleStatement() throws SqlParseException {

		SqlParser sqlParser = SqlParser.create("(AIRLINE_CODE = \"AF\" AND FLIGHT_NUMBER like \"%06\") OR NOT (AIRLINE_CODE = \"KLM\")");
		SqlNode sqlNode = sqlParser.parseExpression();

		LOGGER.info("Node Kind = {}, Node Class = {}.", sqlNode.getKind(), sqlNode.getClass().getSimpleName());
		traceSqlNode(0, sqlNode);
	}

	private void traceSqlNode(int level, SqlNode node) {

		String prefix = StringUtils.repeat("   ", level);

		if (node instanceof SqlCall) {
			SqlCall call = (SqlCall) node;
			SqlOperator operator = call.getOperator();
			List<SqlNode> operands = call.getOperandList();

			LOGGER.info("{} - {}Operator: [{}]", level, prefix, operator.getName());
			for (int i=0; i<operands.size(); i++) {
				SqlNode operand = operands.get(i);
				traceSqlNode(level+1, operand);
			}

		} else if (node instanceof SqlIdentifier) {
			SqlIdentifier identifier = (SqlIdentifier) node;
			LOGGER.info("{} - {}Identifier: [{}]", level, prefix, identifier.getSimple());

		} else if (node instanceof SqlLiteral) {
			SqlLiteral literal = (SqlLiteral) node;
			LOGGER.info("{} - {}Literal: [{}]", level, prefix, literal.getValue());

		}
	}
}

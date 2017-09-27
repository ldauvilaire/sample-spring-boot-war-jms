package net.ldauvilaire.sample.iata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ldauvilaire.sample.telegram.TelegramBaseListener;
import net.ldauvilaire.sample.telegram.TelegramLexer;
import net.ldauvilaire.sample.telegram.TelegramListener;
import net.ldauvilaire.sample.telegram.TelegramParser;
import net.ldauvilaire.sample.telegram.TelegramParser.AddressSectionContext;
import net.ldauvilaire.sample.telegram.TelegramParser.AddresseeIndicatorContext;
import net.ldauvilaire.sample.telegram.TelegramParser.DiversionLineContext;
import net.ldauvilaire.sample.telegram.TelegramParser.NormalAdressLineContext;
import net.ldauvilaire.sample.telegram.TelegramParser.OriginSectionContext;
import net.ldauvilaire.sample.telegram.TelegramParser.OriginatorIndicatorContext;
import net.ldauvilaire.sample.telegram.TelegramParser.ShortAdressLineContext;
import net.ldauvilaire.sample.telegram.TelegramParser.TelegramContext;
import net.ldauvilaire.sample.telegram.TelegramParser.TextSectionContext;

public class TelegramTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramTest.class);

    @Test
    public void testParseAsmIata1() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("iata/ASM_1.txt");
        String payload = read(is);
        LOGGER.info("Payload:\n{}", payload);

        // Walk it and attach our listener
        TelegramListener listener = new TelegramBaseListener() {

            @Override
            public void visitTerminal(TerminalNode node) {
                LOGGER.info("visitTerminal ...");
            }

            @Override
            public void visitErrorNode(ErrorNode node) {
                LOGGER.info("visitErrorNode ...");
            }

            @Override
            public void enterEveryRule(ParserRuleContext ctx) {
                LOGGER.info("enterEveryRule ...");
            }

            @Override
            public void exitEveryRule(ParserRuleContext ctx) {
                LOGGER.info("exitEveryRule ...");
            }

            @Override
            public void enterTelegram(TelegramContext ctx) {
                LOGGER.info("enterTelegram ...");
            }

            @Override
            public void exitTelegram(TelegramContext ctx) {
                LOGGER.info("exitTelegram ...");
            }

            @Override
            public void enterAddressSection(AddressSectionContext ctx) {
                LOGGER.info("enterAddressSection ...");
            }

            @Override
            public void exitAddressSection(AddressSectionContext ctx) {
                LOGGER.info("exitAddressSection ...");
            }

            @Override
            public void enterDiversionLine(DiversionLineContext ctx) {
                LOGGER.info("enterDiversionLine ...");
            }

            @Override
            public void exitDiversionLine(DiversionLineContext ctx) {
                LOGGER.info("exitDiversionLine ...");
            }

            @Override
            public void enterShortAdressLine(ShortAdressLineContext ctx) {
                LOGGER.info("enterShortAdressLine ...");
            }

            @Override
            public void exitShortAdressLine(ShortAdressLineContext ctx) {
                LOGGER.info("exitShortAdressLine ...");
            }

            @Override
            public void enterNormalAdressLine(NormalAdressLineContext ctx) {
                LOGGER.info("enterNormalAdressLine ...");
            }

            @Override
            public void exitNormalAdressLine(NormalAdressLineContext ctx) {
                LOGGER.info("exitNormalAdressLine ...");
            }

            @Override
            public void enterAddresseeIndicator(AddresseeIndicatorContext ctx) {
                LOGGER.info("enterAddresseeIndicator ...");
            }

            @Override
            public void exitAddresseeIndicator(AddresseeIndicatorContext ctx) {
                LOGGER.info("exitAddresseeIndicator ...");
            }

            @Override
            public void enterOriginSection(OriginSectionContext ctx) {
                LOGGER.info("enterOriginSection ...");
            }

            @Override
            public void exitOriginSection(OriginSectionContext ctx) {
                LOGGER.info("exitOriginSection ...");
            }

            @Override
            public void enterOriginatorIndicator(OriginatorIndicatorContext ctx) {
                LOGGER.info("enterOriginatorIndicator ...");
            }

            @Override
            public void exitOriginatorIndicator(OriginatorIndicatorContext ctx) {
                LOGGER.info("exitOriginatorIndicator ...");
            }

            @Override
            public void enterTextSection(TextSectionContext ctx) {
                LOGGER.info("enterTextSection ...");
            }

            @Override
            public void exitTextSection(TextSectionContext ctx) {
                LOGGER.info("exitTextSection ...");
            }
        };


        LOGGER.info("Building Telegram Lexer ...");
        CodePointCharStream inputStream = CharStreams.fromString(payload);
        TelegramLexer lexer = new TelegramLexer(inputStream);

        LOGGER.info("Accessing Telegram Lexer All Tokens ...");
        lexer.getAllTokens();

        LOGGER.info("Building Telegram Parser ...");
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TelegramParser parser = new TelegramParser(tokens);

        LOGGER.info("Parsing Telegram ...");
        TelegramContext context = parser.telegram();

        LOGGER.info("Running Telegram Walker ...");
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, context);
    }

    public static String read(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }
}

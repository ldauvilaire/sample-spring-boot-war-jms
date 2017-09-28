package net.ldauvilaire.sample.iata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
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
import net.ldauvilaire.sample.telegram.TelegramParser.DiversionLineContext;
import net.ldauvilaire.sample.telegram.TelegramParser.NormalAddressLineContext;
//import net.ldauvilaire.sample.telegram.TelegramParser.OriginSectionContext;
import net.ldauvilaire.sample.telegram.TelegramParser.ShortAddressLineContext;
import net.ldauvilaire.sample.telegram.TelegramParser.TelegramContext;
//import net.ldauvilaire.sample.telegram.TelegramParser.TextSectionContext;

public class TelegramTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramTest.class);

    @Test
    public void testParseAsmIata1() throws IOException {

        InputStream is = this.getClass().getClassLoader().getResourceAsStream("iata/ASM_1.txt");
        String payload = read(is);
        LOGGER.info("Payload:\n{}\n{}\n{}",
                "--------------------------------------------------------------",
                payload,
                "--------------------------------------------------------------");

        LOGGER.info("Building Telegram Lexer ...");
        CodePointCharStream charStream = CharStreams.fromString(payload);
        TelegramLexer lexer = new TelegramLexer(charStream);

        LOGGER.info("Accessing Telegram Lexer All Tokens ...");
        List<? extends Token> tokens = lexer.getAllTokens();
        int tokenIndex = 0;
        for (Token token : tokens) {
            tokenIndex++;
            LOGGER.info("... Token[{}]: [{}]",
                    tokenIndex,
                    token.getText().replaceAll("\n", "\\\\n"));
        }

        LOGGER.info("Re-Building Telegram Lexer ...");
        charStream = CharStreams.fromString(payload);
        lexer = new TelegramLexer(charStream);

        LOGGER.info("Building Telegram Parser ...");
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        TelegramParser parser = new TelegramParser(tokenStream);

        LOGGER.info("Parsing Telegram ...");
        TelegramContext context = parser.telegram();

        LOGGER.info("Running Telegram Walker ...");
        ParseTreeWalker walker = new ParseTreeWalker();
        TelegramListener listener = new TelegramBaseListener() {

            @Override
            public void visitTerminal(TerminalNode node) {
                String nodeValue = node.getText().replaceAll("\n", "\\\\n");
                LOGGER.info("--- visitTerminal [{}] ...", nodeValue);
            }
            @Override
            public void visitErrorNode(ErrorNode node) {
                String nodeValue = node.getText().replaceAll("\n", "\\\\n");
                LOGGER.info("--- visitErrorNode [{}] ...", nodeValue);
            }

            @Override
            public void enterEveryRule(ParserRuleContext ctx) {
                int ruleIndex = ctx.getRuleIndex();
                String ruleName = TelegramParser.ruleNames[ruleIndex];
                LOGGER.info(">>> enterEveryRule [{}]", ruleName);
            }
            @Override
            public void exitEveryRule(ParserRuleContext ctx) {
                int ruleIndex = ctx.getRuleIndex();
                String ruleName = TelegramParser.ruleNames[ruleIndex];
                LOGGER.info("<<< exitEveryRule [{}]", ruleName);
            }

            @Override
            public void enterTelegram(TelegramContext ctx) {
                LOGGER.info(">>> enterTelegram ...");
            }
            @Override
            public void exitTelegram(TelegramContext ctx) {
                LOGGER.info("<<< exitTelegram ...");
            }

            @Override
            public void enterAddressSection(AddressSectionContext ctx) {
                LOGGER.info(">>> enterAddressSection ...");
            }
            @Override
            public void exitAddressSection(AddressSectionContext ctx) {
                if (ctx.isEmpty()) {
                    LOGGER.info("<<< exitAddressSection is <empty>");
                } else {
                    DiversionLineContext diversionLine = ctx.diversionLine();
                    if (diversionLine == null) {
                        LOGGER.info("<<< exitAddressSection - Diversion Line is <null>");
                    } else if (diversionLine.isEmpty()) {
                        LOGGER.info("<<< exitAddressSection - Diversion Line is <empty>");
                    } else {
                        TerminalNode routing = diversionLine.RoutingIndicator();
                        String routingValue = (routing == null) ? "<null>" : routing.getText();
                        LOGGER.info("<<< exitAddressSection - Diversion Line - Routing = [{}]", routingValue);
                    }
                }
            }

            @Override
            public void enterDiversionLine(DiversionLineContext ctx) {
                LOGGER.info(">>> enterDiversionLine ...");
            }
            @Override
            public void exitDiversionLine(DiversionLineContext ctx) {
                TerminalNode routing = ctx.RoutingIndicator();
                String routingValue = (routing == null) ? "<null>" : routing.getText();
                LOGGER.info("<<< exitDiversionLine - Routing = [{}]", routingValue);
            }

            @Override
            public void enterShortAddressLine(ShortAddressLineContext ctx) {
                LOGGER.info(">>> enterShortAddressLine ...");
            }
            @Override
            public void exitShortAddressLine(ShortAddressLineContext ctx) {
                logShortAddressLine("<<< exitShortAddressLine", ctx);
            }

            @Override
            public void enterNormalAddressLine(NormalAddressLineContext ctx) {
                LOGGER.info(">>> enterNormalAddressLine ...");
            }
            @Override
            public void exitNormalAddressLine(NormalAddressLineContext ctx) {
                logNormalAddressLine("<<< exitNormalAddressLine", ctx);
            }
/*
            @Override
            public void enterOriginSection(OriginSectionContext ctx) {
                LOGGER.info(">>> enterOriginSection ...");
            }
            @Override
            public void exitOriginSection(OriginSectionContext ctx) {
                TerminalNode originator = ctx.OriginatorIndicator();
                LOGGER.info("<<< exitOriginSection - Originator Indicator = [{}]", (originator == null) ? "<null>" : originator.getText());
                TerminalNode signature = ctx.DoubleSignature();
                LOGGER.info("<<< exitOriginSection - Signature = [{}]", (signature == null) ? "<null>" : signature.getText());
                TerminalNode identity = ctx.MessageIdentity();
                LOGGER.info("<<< exitOriginSection - Identity = [{}]", (identity == null) ? "<null>" : identity.getText());
            }
*/

/*
            @Override
            public void enterTextSection(TextSectionContext ctx) {
                LOGGER.info(">>> enterTextSection ...");
            }
            @Override
            public void exitTextSection(TextSectionContext ctx) {
                LOGGER.info("<<< exitTextSection ...");
                List<TerminalNode> textLines = ctx.TextLine();
                if (textLines == null) {
                    LOGGER.info("<<< exitTextSection - Text Lines is <null>");
                } else if (textLines.isEmpty()) {
                    LOGGER.info("<<< exitTextSection - Text Lines is <empty>");
                } else {
                    int textLineIndex = 0;
                    for (TerminalNode textLine : textLines) {
                        textLineIndex++;
                        LOGGER.info("<<< exitTextSection - Text Lines [{}] = [{}]", textLineIndex, textLine.getText());
                    }
                }
            }
*/
        };
        walker.walk(listener, context);
    }

    public static String read(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }

    public static void logShortAddressLine(String prefix, ShortAddressLineContext ctx) {
        NormalAddressLineContext normalAddress = ctx.normalAddressLine();
        if (normalAddress == null) {
            LOGGER.info("{} - Normal Address is <null>", prefix);
        } else if (normalAddress.isEmpty()) {
            LOGGER.info("{} - Normal Address is <empty>", prefix);
        } else {
            logNormalAddressLine(prefix + " - Normal Address", normalAddress);
        }
    }

    public static void logNormalAddressLine(String prefix, NormalAddressLineContext ctx) {
        TerminalNode priorityCode = ctx.PriorityCode();
        LOGGER.info("{} - Priority = [{}]", prefix, (priorityCode == null) ? "<null>" : priorityCode.getText());
        List<TerminalNode> addresses = ctx.AddresseeIndicator();
        if (addresses == null) {
            LOGGER.info("{} - Addresses is <null>", prefix);
        } else if (addresses.isEmpty()) {
            LOGGER.info("{} - Addresses is <empty>", prefix);
        } else {
            int addressIndex = 0;
            for (TerminalNode address : addresses) {
                addressIndex++;
                LOGGER.info("{} - Addresses[{}] = [{}]", prefix, addressIndex, address.getText());
            }
        }

    }
}

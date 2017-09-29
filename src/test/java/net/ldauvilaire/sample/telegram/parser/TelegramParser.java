package net.ldauvilaire.sample.telegram.parser;

import java.util.StringJoiner;

import net.ldauvilaire.sample.telegram.dto.Heading;
import net.ldauvilaire.sample.telegram.dto.Telegram;

public class TelegramParser {

    private String[] inputLines;

    public TelegramParser(String message) {
        super();
        if (message != null) {
            inputLines = message.split("\n");
        }
    }

    public Telegram parse() {

        Telegram telegram = new Telegram();
        Heading heading = new Heading();
        telegram.setHeading(heading);

        StringJoiner payloadJoiner = new StringJoiner("\n");

        boolean headingMode = true;
        boolean payloadMode = false;

        if (inputLines != null) {
            int nbLines = inputLines.length;
            for (int lineIndex = 0; lineIndex < nbLines; lineIndex++) {
                String inputLine = inputLines[lineIndex];

                if (payloadMode) {
                    payloadJoiner.add(inputLine);

                } else if (headingMode) {
                    
                }
            }
        }

        return telegram;
    }
}

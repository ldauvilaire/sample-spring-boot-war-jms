package net.ldauvilaire.sample.telegram.dto;

public class Telegram {

    private Heading heading;
    private String payload;

    public Telegram() {
        super();
    }

    public Telegram(Heading heading, String payload) {
        super();
        this.heading = heading;
        this.payload = payload;
    }

    public Heading getHeading() {
        return heading;
    }
    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    public String getPayload() {
        return payload;
    }
    public void setPayload(String payload) {
        this.payload = payload;
    }
}

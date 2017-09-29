package net.ldauvilaire.sample.telegram.dto;

import java.util.List;

public class Heading {

    private String routingIndicator;
    private List<AddressLine> shortAddressLines;
    private AddressLine normalAddressLine;
    private Origin origin;

    public Heading() {
        super();
    }

    public Heading(
            String routingIndicator,
            List<AddressLine> shortAddressLines,
            AddressLine normalAddressLine,
            Origin origin) {
        super();
        this.routingIndicator = routingIndicator;
        this.shortAddressLines = shortAddressLines;
        this.normalAddressLine = normalAddressLine;
        this.origin = origin;
    }

    public String getRoutingIndicator() {
        return routingIndicator;
    }
    public void setRoutingIndicator(String routingIndicator) {
        this.routingIndicator = routingIndicator;
    }

    public List<AddressLine> getShortAddressLines() {
        return shortAddressLines;
    }
    public void setShortAddressLines(List<AddressLine> shortAddressLines) {
        this.shortAddressLines = shortAddressLines;
    }

    public AddressLine getNormalAddressLine() {
        return normalAddressLine;
    }
    public void setNormalAddressLine(AddressLine normalAddressLine) {
        this.normalAddressLine = normalAddressLine;
    }

    public Origin getOrigin() {
        return origin;
    }
    public void setOrigin(Origin origin) {
        this.origin = origin;
    }
}

package net.ldauvilaire.sample.telegram.dto;

public class Origin {

    private String originator;
    private String signature;
    private String identity;

    public Origin() {
        super();
    }

    public Origin(String originator, String signature, String identity) {
        super();
        this.originator = originator;
        this.signature = signature;
        this.identity = identity;
    }

    public String getOriginator() {
        return originator;
    }
    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public String getSignature() {
        return signature;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getIdentity() {
        return identity;
    }
    public void setIdentity(String identity) {
        this.identity = identity;
    }
}

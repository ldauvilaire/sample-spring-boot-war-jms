package net.ldauvilaire.sample.telegram.dto;

import java.util.List;

public class AddressLine {

    private String priorityCode;
    private List<String> address;

    public AddressLine() {
        super();
    }

    public AddressLine(String priorityCode, List<String> address) {
        super();
        this.priorityCode = priorityCode;
        this.address = address;
    }

    public String getPriorityCode() {
        return priorityCode;
    }
    public void setPriorityCode(String priorityCode) {
        this.priorityCode = priorityCode;
    }

    public List<String> getAddress() {
        return address;
    }
    public void setAddress(List<String> address) {
        this.address = address;
    }
}

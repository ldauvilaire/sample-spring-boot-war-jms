package net.ldauvilaire.sample.jms.event;

public class EntityUpdateEvent {

    private String entity;
    private String attributeName;
    private String attributeTypeName;
    private Object previousValue;
    private Object currentValue;

    public EntityUpdateEvent(
            String entity,
            String attributeName,
            String attributeTypeName,
            Object previousValue,
            Object currentValue) {
        super();
        this.entity = entity;
        this.attributeName = attributeName;
        this.attributeTypeName = attributeTypeName;
        this.previousValue = previousValue;
        this.currentValue = currentValue;
    }

    public String getEntity() {
        return entity;
    }
    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getAttributeName() {
        return attributeName;
    }
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeTypeName() {
        return attributeTypeName;
    }
    public void setAttributeTypeName(String attributeTypeName) {
        this.attributeTypeName = attributeTypeName;
    }

    public Object getPreviousValue() {
        return previousValue;
    }
    public void setPreviousValue(Object previousValue) {
        this.previousValue = previousValue;
    }

    public Object getCurrentValue() {
        return currentValue;
    }
    public void setCurrentValue(Object currentValue) {
        this.currentValue = currentValue;
    }
}

package net.ldauvilaire.sample.jms.event;

public class EntityCreateEvent {

    private String entity;
    private String attributeName;
    private String attributeTypeName;
    private Object currentValue;

    public EntityCreateEvent(
            String entity,
            String attributeName,
            String attributeTypeName,
            Object currentValue) {
        super();
        this.entity = entity;
        this.attributeName = attributeName;
        this.attributeTypeName = attributeTypeName;
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

    public Object getCurrentValue() {
        return currentValue;
    }
    public void setCurrentValue(Object currentValue) {
        this.currentValue = currentValue;
    }
}

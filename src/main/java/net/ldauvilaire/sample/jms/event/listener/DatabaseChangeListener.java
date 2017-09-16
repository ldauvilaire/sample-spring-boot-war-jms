package net.ldauvilaire.sample.jms.event.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import net.ldauvilaire.sample.jms.event.EntityCreateEvent;
import net.ldauvilaire.sample.jms.event.EntityDeleteEvent;
import net.ldauvilaire.sample.jms.event.EntityUpdateEvent;

@Component
public class DatabaseChangeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseChangeListener.class);

    @EventListener
    public void handleEntityCreateEvent(EntityCreateEvent event) {
        LOGGER.info(">>> Entity [{}] Create Event for Attribute [{}] ({}) : [{}]",
                event.getEntity(),
                event.getAttributeName(),
                event.getAttributeTypeName(),
                event.getCurrentValue());
    }

    @EventListener
    public void handleEntityUpdateEvent(EntityUpdateEvent event) {
        LOGGER.info(">>> Entity [{}] Update Event for Attribute [{}] ({}) : [{}] => [{}]",
                event.getEntity(),
                event.getAttributeName(),
                event.getAttributeTypeName(),
                event.getPreviousValue(),
                event.getCurrentValue());
    }

    @EventListener
    public void handleEntityDeleteEvent(EntityDeleteEvent event) {
        LOGGER.info(">>> Entity [{}] Delete Event for Attribute [{}] ({}) : [{}]",
                event.getEntity(),
                event.getAttributeName(),
                event.getAttributeTypeName(),
                event.getCurrentValue());
    }
}

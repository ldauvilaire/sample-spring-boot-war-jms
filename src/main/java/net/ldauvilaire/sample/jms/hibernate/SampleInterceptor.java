package net.ldauvilaire.sample.jms.hibernate;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import net.ldauvilaire.sample.jms.event.EntityCreateEvent;
import net.ldauvilaire.sample.jms.event.EntityDeleteEvent;
import net.ldauvilaire.sample.jms.event.EntityUpdateEvent;

@Component
public class SampleInterceptor extends EmptyInterceptor {

    private static final long serialVersionUID = 1408926216355150539L;
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleInterceptor.class);

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public boolean onSave(
            Object entity,
            Serializable id,
            Object[] state,
            String[] propertyNames,
            Type[] types) {

        boolean result = super.onSave(entity, id, state, propertyNames, types);
        LOGGER.info("==========================================");
        LOGGER.info("=== Saving Entity [{}] with ID [{}] ===", entity.getClass().getSimpleName(), id);
        int nbProperties = propertyNames.length;
        for (int i=0; i<nbProperties; i++) {
            String propertyName = propertyNames[i];
            Type propertyType = types[i];
            Object currentValue = state[i];
            publisher.publishEvent(new EntityCreateEvent(
                    entity.getClass().getSimpleName(),
                    propertyName,
                    propertyType.getName(),
                    currentValue));
        }
        LOGGER.info("==========================================");
        return result;
    }

    @Override
    public boolean onFlushDirty(
            Object entity,
            Serializable id,
            Object[] currentState,
            Object[] previousState,
            String[] propertyNames,
            Type[] types) {

        boolean result = super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
        LOGGER.info("==========================================");
        LOGGER.info("=== Flushing Dirty Entity [{}] with ID [{}] ===", entity.getClass().getSimpleName(), id);

        int nbProperties = propertyNames.length;
        for (int i=0; i<nbProperties; i++) {
            String propertyName = propertyNames[i];
            Type propertyType = types[i];
            Object previousValue = previousState[i];
            Object currentValue = currentState[i];

            boolean dirty = ! propertyType.isSame(previousValue, currentValue);
            if (dirty) {
                publisher.publishEvent(new EntityUpdateEvent(
                        entity.getClass().getSimpleName(),
                        propertyName,
                        propertyType.getName(),
                        previousValue,
                        currentValue));
            }
        }
        LOGGER.info("==========================================");

        return result;
    }

    @Override
    public void onDelete(
            Object entity,
            Serializable id,
            Object[] state,
            String[] propertyNames,
            Type[] types) {

        super.onDelete(entity, id, state, propertyNames, types);
        LOGGER.info("==========================================");
        LOGGER.info("=== Deleting Entity [{}] with ID [{}] ===", entity.getClass().getSimpleName(), id);
        int nbProperties = propertyNames.length;
        for (int i=0; i<nbProperties; i++) {
            String propertyName = propertyNames[i];
            Type propertyType = types[i];
            Object currentValue = state[i];
            publisher.publishEvent(new EntityDeleteEvent(
                    entity.getClass().getSimpleName(),
                    propertyName,
                    propertyType.getName(),
                    currentValue));
        }
        LOGGER.info("==========================================");
    }
}

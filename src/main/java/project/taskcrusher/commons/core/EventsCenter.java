package project.taskcrusher.commons.core;

import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;

import project.taskcrusher.commons.events.BaseEvent;

/**
 * Manages the event dispatching of the app.
 */
public class EventsCenter {
    private static final Logger logger = LogsCenter.getLogger(EventsCenter.class);
    private final EventBus eventBus;
    private static EventsCenter instance;

    public static EventsCenter getInstance() {
        if (instance == null) {
            instance = new EventsCenter();
        }
        return instance;
    }

    public static void clearSubscribers() {
        instance = null;
    }

    private EventsCenter() {
        eventBus = new EventBus();
    }

    public EventsCenter registerHandler(Object handler) {
        eventBus.register(handler);
        return this;
    }

    public EventsCenter unregisterHandler(Object handler) {
        eventBus.unregister(handler);
        return this;
    }

    /**
     * Posts an event to the event bus.
     */
    public <E extends BaseEvent> EventsCenter post(E event) {
        logger.info("------[Event Posted] " + event.getClass().getCanonicalName() + ": " + event.toString());
        eventBus.post(event);
        return this;
    }

}

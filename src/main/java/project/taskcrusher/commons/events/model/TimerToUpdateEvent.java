package project.taskcrusher.commons.events.model;

import project.taskcrusher.commons.events.BaseEvent;

//@@author A0127737X
/**
 * This event is used to notify the UI to update the timer used to check overdue status of events and tasks
 * that it displays. This event is to be raised every time before the user action updates the filteredLists.
 */
public class TimerToUpdateEvent extends BaseEvent {

    public TimerToUpdateEvent () {
    }

    @Override
    public String toString() {
        return "Timer to update BEFORE the list gets filtered";
    }

}

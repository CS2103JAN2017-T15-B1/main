package project.taskcrusher.model;

import java.util.Set;
import java.util.logging.Logger;

import javafx.collections.transformation.FilteredList;
import project.taskcrusher.commons.core.ComponentManager;
import project.taskcrusher.commons.core.LogsCenter;
import project.taskcrusher.commons.core.UnmodifiableObservableList;
import project.taskcrusher.commons.events.model.AddressBookChangedEvent;
import project.taskcrusher.commons.events.model.ListsToShowUpdatedEvent;
import project.taskcrusher.commons.util.CollectionUtil;
import project.taskcrusher.commons.util.StringUtil;
import project.taskcrusher.model.event.Event;
import project.taskcrusher.model.event.ReadOnlyEvent;
import project.taskcrusher.model.event.Timeslot;
import project.taskcrusher.model.event.UniqueEventList.DuplicateEventException;
import project.taskcrusher.model.event.UniqueEventList.EventNotFoundException;
import project.taskcrusher.model.shared.ReadOnlyUserToDo;
import project.taskcrusher.model.task.ReadOnlyTask;
import project.taskcrusher.model.task.Task;
import project.taskcrusher.model.task.UniqueTaskList;
import project.taskcrusher.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final UserInbox userInbox;
    private final FilteredList<ReadOnlyTask> filteredTasks;
    private final FilteredList<ReadOnlyEvent> filteredEvents;
    private static final boolean LIST_EMPTY = true;

    /**
     * Initializes a ModelManager with the given userInbox and userPrefs.
     */
    public ModelManager(ReadOnlyUserInbox userInbox, UserPrefs userPrefs) {
        super();
        assert !CollectionUtil.isAnyNull(userInbox, userPrefs);

        logger.fine("Initializing with user inbox: " + userInbox + " and user prefs " + userPrefs);

        this.userInbox = new UserInbox(userInbox);
        filteredTasks = new FilteredList<>(this.userInbox.getTaskList());
        filteredEvents = new FilteredList<>(this.userInbox.getEventList());
    }

    public ModelManager() {
        this(new UserInbox(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyUserInbox newData) {
        userInbox.resetData(newData);
        indicateUserInboxChanged();
    }

    @Override
    public ReadOnlyUserInbox getUserInbox() {
        return userInbox;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateUserInboxChanged() {
        raise(new AddressBookChangedEvent(userInbox));
    }

    private void prepareListsForUi() {
        boolean taskListToShowEmpty = false, eventListToShowEmpty = false;
        if (filteredEvents.isEmpty()) {
            eventListToShowEmpty = LIST_EMPTY;
        }
        if (filteredTasks.isEmpty()) {
            taskListToShowEmpty = LIST_EMPTY;
        }
        raise(new ListsToShowUpdatedEvent(eventListToShowEmpty, taskListToShowEmpty));
    }

    //=========== Task operations =========================================================================

    @Override
    public synchronized void deleteTask(ReadOnlyTask target) throws TaskNotFoundException {
        userInbox.removeTask(target);
        indicateUserInboxChanged();
        prepareListsForUi();
    }

    @Override
    public synchronized void addTask(Task task) throws UniqueTaskList.DuplicateTaskException {
        userInbox.addTask(task);
        updateFilteredTaskListToShowAll();
        indicateUserInboxChanged();
    }

    @Override
    public synchronized void updateTask(int filteredTaskListIndex, ReadOnlyTask editedTask)
            throws UniqueTaskList.DuplicateTaskException {
        assert editedTask != null;

        int taskListIndex = filteredTasks.getSourceIndex(filteredTaskListIndex);
        userInbox.updateTask(taskListIndex, editedTask);
        indicateUserInboxChanged();
        prepareListsForUi();
    }

    //=========== Event operations =========================================================================

    @Override
    public synchronized void deleteEvent(ReadOnlyEvent target) throws EventNotFoundException {
        userInbox.removeEvent(target);
        indicateUserInboxChanged();
    }

    @Override
    public synchronized void updateEvent(int filteredEventListIndex, ReadOnlyEvent editedEvent)
            throws DuplicateEventException {
        assert editedEvent != null;

        int eventListIndex = filteredEvents.getSourceIndex(filteredEventListIndex);
        userInbox.updateEvent(eventListIndex, editedEvent);
        indicateUserInboxChanged();
    }

    @Override
    public synchronized void addEvent(Event event) throws DuplicateEventException {
        userInbox.addEvent(event);
        updateFilteredEventListToShowAll();
        indicateUserInboxChanged();
    }

    public synchronized void confirmEventTime(int filteredEventListIndex, int timeslotIndex) {
        int eventListIndex = filteredEvents.getSourceIndex(filteredEventListIndex);
        userInbox.confirmEventTime(eventListIndex, timeslotIndex);
        indicateUserInboxChanged();
    }

    public UnmodifiableObservableList<ReadOnlyEvent> getEventsWithOverlappingTimeslots(Timeslot candidate) {
        return new UnmodifiableObservableList<>(userInbox.getEventsWithOverlappingTimeslots(candidate));
    }

    //=========== Filtered Task List Accessors =============================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList() {
        return new UnmodifiableObservableList<>(filteredTasks);
    }

    @Override
    public void updateFilteredTaskListToShowAll() {
        filteredTasks.setPredicate(null);
        prepareListsForUi();
    }

    @Override
    public void updateFilteredTaskList(Set<String> keywords) {
        updateFilteredTaskList(new PredicateExpression(new NameQualifier(keywords)));
    }

    @Override
    public void updateFilteredTaskList(Timeslot userInterestedTimeslot) {
        updateFilteredTaskList(new PredicateExpression(new TimeslotQualifier(userInterestedTimeslot)));
    }

    private void updateFilteredTaskList(Expression expression) {
        filteredTasks.setPredicate(expression::satisfies);
        prepareListsForUi();
    }

    //=========== Filtered Event List Accessors =============================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyEvent> getFilteredEventList() {
        return new UnmodifiableObservableList<>(filteredEvents);
    }

    @Override
    public void updateFilteredEventListToShowAll() {
        filteredEvents.setPredicate(null);
        prepareListsForUi();
    }

    @Override
    public void updateFilteredEventList(Set<String> keywords) {
        updateFilteredEventList(new PredicateExpression(new NameQualifier(keywords)));
    }

    @Override
    public void updateFilteredEventList(Timeslot userInterestedTimeslot) {
        updateFilteredEventList(new PredicateExpression(new TimeslotQualifier(userInterestedTimeslot)));
    }

    private void updateFilteredEventList(Expression expression) {
        filteredEvents.setPredicate(expression::satisfies);
        prepareListsForUi();
    }

    //========== Inner classes/interfaces used for filtering =================================================

    interface Expression {
        boolean satisfies(ReadOnlyUserToDo item);
        String toString();
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(ReadOnlyUserToDo item) {
            return qualifier.run(item);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(ReadOnlyUserToDo item);
        String toString();
    }

    private class NameQualifier implements Qualifier {
        private Set<String> nameKeyWords;

        NameQualifier(Set<String> nameKeyWords) {
            this.nameKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(ReadOnlyUserToDo item) {
            return nameKeyWords.stream()
                    .filter(keyword -> StringUtil.containsWordIgnoreCase(item.getName().toString(), keyword))
                    .findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", nameKeyWords);
        }
    }

    /**
     * checks if:
     * (1) if the ToDo item is an active task, its deadline falls within the given timeslot
     * (2) if the ToDO item is an active event, its timeslots overlaps with the given timeslot
     */
    private class TimeslotQualifier implements Qualifier {
        private Timeslot userInterestedTimeslot;

        TimeslotQualifier(Timeslot timeslot) {
            assert timeslot != null;
            this.userInterestedTimeslot = timeslot;
        }

        @Override
        public boolean run(ReadOnlyUserToDo item) {
            if (item instanceof ReadOnlyEvent) {
                ReadOnlyEvent event = (ReadOnlyEvent) item;
                if (event.isComplete()) {
                    return false;
                } else if (event.hasOverlappingTimeslot(userInterestedTimeslot)) {
                    return true;
                } else {
                    return false;
                }
            } else if (item instanceof ReadOnlyTask) {
                ReadOnlyTask task = (ReadOnlyTask) item;
                if (task.isComplete()) {
                    return false;
                } else if (task.getDeadline().isWithin(userInterestedTimeslot)) {
                    return true;
                } else {
                    return false;
                }
            }
            assert false;
            return false; //should not reach here
        }

        @Override
        public String toString() {
            return "user-interested timeslot is " + userInterestedTimeslot.toString();
        }
    }
}

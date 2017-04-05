package project.taskcrusher.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import project.taskcrusher.commons.core.UnmodifiableObservableList;
import project.taskcrusher.model.event.Event;
import project.taskcrusher.model.event.ReadOnlyEvent;
import project.taskcrusher.model.event.Timeslot;
import project.taskcrusher.model.event.UniqueEventList;
import project.taskcrusher.model.task.ReadOnlyTask;
import project.taskcrusher.model.task.Task;
import project.taskcrusher.model.task.UniqueTaskList;
import project.taskcrusher.model.task.UniqueTaskList.DuplicateTaskException;
import project.taskcrusher.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * The API of the Model component.
 */
public interface Model {
	//@@author A0163639W
    static ArrayList<Integer> adddel = new ArrayList<Integer>();
    //@@author
    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyUserInbox newData);

    /** Returns the UserInbox */
    ReadOnlyUserInbox getUserInbox();

    //========== for tasks =================================================
    /** Deletes the given task. */
    void deleteTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException;
    //@@author A0163639W
    void deleteUndoTask(ReadOnlyTask target) throws TaskNotFoundException;
   
    /** Done the given task. */
    void doneTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException;
//@@author
    /** Adds the given task */
    void addTask(Task task) throws UniqueTaskList.DuplicateTaskException;
    //@@author A0163639W
    void addUndoTask(Task task) throws UniqueTaskList.DuplicateTaskException;
//@@author
    /**
     * Updates the task located at {@code filteredTaskListIndex} with {@code editedTask}.
     *
     * @throws DuplicateTaskException if updating the task's details causes the task to be equivalent to
     *      another existing task in the list.
     * @throws IndexOutOfBoundsException if {@code filteredTaskListIndex} < 0 or >= the size of the filtered list.
     */
    void updateTask(int filteredTaskListIndex, ReadOnlyTask editedTask)
            throws UniqueTaskList.DuplicateTaskException;

    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList();
    //@@author A0163639W
    UnmodifiableObservableList<ReadOnlyTask> getFilteredAddedList();
    UnmodifiableObservableList<ReadOnlyTask> getFilteredDeletedList();
//@@author
    /** Updates the filter of the filtered task list to show all tasks */
    void updateFilteredTaskListToShowAll();

    /** Updates the filter of the filtered task list to filter by the given keywords*/
    void updateFilteredTaskList(Set<String> keywords);

    /** Updates the filter of the filtered task list to filter by the given up-to Date*/
    void updateFilteredTaskList(Date upToDate);

    //========== for events =================================================

    /**deletes the given event */
    void deleteEvent(ReadOnlyEvent target) throws UniqueEventList.EventNotFoundException;

    /**
     * Updates the event located at {@code filteredEventListIndex} with {@code editedEvent}.
     *
     * @throws UniqueEventList.DuplicateEventException if updating the event's details cause the event
     *  to be equivalent to another existing event in the list.
     * @throws IndexOutOfBoundsException if {@code filteredEventListIndex} < 0 or >= the size of the filtered list.
     */
    void updateEvent(int fileteredEventListIndex, ReadOnlyEvent editedEvent)
            throws UniqueEventList.DuplicateEventException;

    /** Adds the given event */
    void addEvent(Event event) throws UniqueEventList.DuplicateEventException;

    /** Returns the filtered event list as an {@code UnmodifiableObservableList<ReadOnlyEvent>} */
    UnmodifiableObservableList<ReadOnlyEvent> getFilteredEventList();

    /** Updates the filter of the filtered event list to show all events */
    void updateFilteredEventListToShowAll();

    /** Updates the filter of the filtered event list to filter by the given keywords*/
    void updateFilteredEventList(Set<String> keywords);

    /** Updates the filter of the filtered event list to filter by the given timeslot*/
    void updateFilteredEventList(Timeslot userInterestedTimeSlot);
}

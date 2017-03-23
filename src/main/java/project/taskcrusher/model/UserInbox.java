package project.taskcrusher.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import project.taskcrusher.commons.core.UnmodifiableObservableList;
import project.taskcrusher.model.tag.Tag;
import project.taskcrusher.model.tag.UniqueTagList;
import project.taskcrusher.model.task.ReadOnlyTask;
import project.taskcrusher.model.task.Task;
import project.taskcrusher.model.task.UniqueTaskList;
import project.taskcrusher.model.task.UniqueTaskList.DuplicateTaskException;
import project.taskcrusher.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .equals comparison)
 */
public class UserInbox implements ReadOnlyUserInbox {

    private final UniqueTaskList tasks;
    private final UniqueTaskList deleted;
    private final UniqueTaskList added;
    private final UniqueTagList tags;
    {
        tasks = new UniqueTaskList();
        tags = new UniqueTagList();
        deleted = new UniqueTaskList();
        added = new UniqueTaskList();
    }

    public UserInbox() {}

    /**
     * Creates an UserInbox using the Tasks and Tags in the {@code toBeCopied}
     */
    public UserInbox(ReadOnlyUserInbox toBeCopied) {
        this();
        resetData(toBeCopied);
    }

//// list overwrite operations

    public void setTasks(List<? extends ReadOnlyTask> tasks)
            throws UniqueTaskList.DuplicateTaskException {
        this.tasks.setTasks(tasks);
    }

    //TODO added this
    public void setTasks(UniqueTaskList tasks)
            throws UniqueTaskList.DuplicateTaskException {
        this.tasks.setTasks(tasks);
    }

    public void setTags(Collection<Tag> tags) throws UniqueTagList.DuplicateTagException {
        this.tags.setTags(tags);
    }

    public void resetData(ReadOnlyUserInbox newData) {
        assert newData != null;
        try {
            setTasks(newData.getTaskList());
        } catch (UniqueTaskList.DuplicateTaskException e) {
            assert false : "AddressBooks should not have duplicate persons";
        }
        try {
            setTags(newData.getTagList());
        } catch (UniqueTagList.DuplicateTagException e) {
            assert false : "AddressBooks should not have duplicate tags";
        }
        syncMasterTagListWith(tasks);
    }

//// task-level operations

    /**
     * Adds a task to the user inbox.
     * Also checks the new task's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the task to point to those in {@link #tags}.
     *
     * @throws UniqueTaskList.DuplicateTaskException if an equivalent task already exists.
     */
    public void addTask(Task p) throws UniqueTaskList.DuplicateTaskException {
    	syncMasterTagListWith(p);
        tasks.add(p);
        added.add(p);
    }
    
    public void addUndoTask(Task p) throws UniqueTaskList.DuplicateTaskException, UniqueTaskList.TaskNotFoundException {
        syncMasterTagListWith(p);
        tasks.add(p);
        deleted.remove(p);
    }

    /**
     * Updates the task in the list at position {@code index} with {@code editedReadOnlyPerson}.
     * {@code AddressBook}'s tag list will be updated with the tags of {@code editedReadOnlyPerson}.
     * @see #syncMasterTagListWith(Task)
     *
     * @throws DuplicateTaskException if updating the task's details causes the task to be equivalent to
     *      another existing task in the list.
     * @throws IndexOutOfBoundsException if {@code index} < 0 or >= the size of the list.
     */
    public void updateTask(int index, ReadOnlyTask editedReadOnlyTask)
            throws UniqueTaskList.DuplicateTaskException {
        assert editedReadOnlyTask != null;

        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any task
        // in the task list.
        FilteredList<Task> editTasks = new FilteredList<>(tasks.asObservableList());
        int addressBookIndex = editTasks.getSourceIndex(index);
        Task t = editTasks.get(addressBookIndex);
        System.out.println(t.getTaskName());
        deleted.add(t);
        Task editedTask = new Task(editedReadOnlyTask);
        syncMasterTagListWith(editedTask);
        System.out.println(editedTask.getTaskName());
        System.out.println(t.getTaskName());
        added.add(editedTask);
        tasks.updateTask(index, editedTask);
    }

    /**
     * Ensures that every tag in this task:
     *  - exists in the master list {@link #tags}
     *  - points to a Tag object in the master list
     */
    private void syncMasterTagListWith(Task task) {
        final UniqueTagList taskTags = task.getTags();
        tags.mergeFrom(taskTags);

        // Create map with values = tag object references in the master list
        // used for checking person tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of person tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        taskTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        task.setTags(new UniqueTagList(correctTagReferences));
    }

    /**
     * Ensures that every tag in these tasks:
     *  - exists in the master list {@link #tags}
     *  - points to a Tag object in the master list
     *  @see #syncMasterTagListWith(Task)
     */
    private void syncMasterTagListWith(UniqueTaskList tasks) {
        tasks.forEach(this::syncMasterTagListWith);
    }

    public boolean removeTask(ReadOnlyTask key) throws UniqueTaskList.TaskNotFoundException, UniqueTaskList.DuplicateTaskException {
    	if (tasks.remove(key)) {
        	deleted.add((Task) key);
            return true;
        } else {
            throw new UniqueTaskList.TaskNotFoundException();
        }
    }
    
    public boolean removeUndoTask(ReadOnlyTask key) throws UniqueTaskList.TaskNotFoundException, UniqueTaskList.DuplicateTaskException {
        if (tasks.remove(key)) {
        	added.remove(key);
            return true;
        } else {
            throw new UniqueTaskList.TaskNotFoundException();
        }
    }

//// tag-level operations

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }

//// util methods

    @Override
    public String toString() {
        return tasks.asObservableList().size() + " persons, " + tags.asObservableList().size() +  " tags";
        // TODO: refine later
    }

    @Override
    public ObservableList<ReadOnlyTask> getTaskList() {
        return new UnmodifiableObservableList<>(tasks.asObservableList());
    }
    
    public ObservableList<ReadOnlyTask> getDeletedList() {
        return new UnmodifiableObservableList<>(deleted.asObservableList());
    }
    
    public ObservableList<ReadOnlyTask> getAddedList() {
        return new UnmodifiableObservableList<>(added.asObservableList());
    }

    @Override
    public ObservableList<Tag> getTagList() {
        return new UnmodifiableObservableList<>(tags.asObservableList());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UserInbox // instanceof handles nulls
                && this.tasks.equals(((UserInbox) other).tasks)
                && this.tags.equalsOrderInsensitive(((UserInbox) other).tags));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(tasks, tags);
    }
}

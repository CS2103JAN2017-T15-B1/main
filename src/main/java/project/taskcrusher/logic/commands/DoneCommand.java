package project.taskcrusher.logic.commands;

import java.util.List;

import project.taskcrusher.commons.core.Messages;
import project.taskcrusher.logic.commands.exceptions.CommandException;
import project.taskcrusher.model.task.ReadOnlyTask;
import project.taskcrusher.model.task.Task;
import project.taskcrusher.model.task.UniqueTaskList;

/**
 * Done the details of an existing person in the address book.
 */
public class DoneCommand extends Command {

    public static final String COMMAND_WORD = "done";

    public static final String MESSAGE_USAGE = COMMAND_WORD + "Marks the task done "
            + "by the index number used in the last task listing. "
            + "Parameters: INDEX (must be a positive integer) [TASK_NAME]"
            + " [d/DEADLINE] [p/PRIORITY] [//DESCRIPTION] [t/TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 p/2 //a description";

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Done task: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to done must be provided.";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the active list.";

    private final int filteredTaskListIndex;

    /**
     * @param filteredTaskListIndex the index of the task in the filtered task list to done
     * @param doneTaskDescriptor details to done the task with
     */
    public DoneCommand(int filteredTaskListIndex) {
        assert filteredTaskListIndex > 0;

        // converts filteredPersonListIndex from one-based to zero-based.
        this.filteredTaskListIndex = filteredTaskListIndex - 1;
    }

    @Override
    public CommandResult execute() throws CommandException {
        List<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (filteredTaskListIndex >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        ReadOnlyTask taskToDone = lastShownList.get(filteredTaskListIndex);
        Task doneTask = new Task(taskToDone.getName(), taskToDone.getDeadline(), taskToDone.getPriority(), taskToDone.getDescription(), taskToDone.getTags(), true);

        try {
            model.updateTask(filteredTaskListIndex, doneTask);
        } catch (UniqueTaskList.DuplicateTaskException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        }
        model.updateFilteredTaskListToShowAll();
        return new CommandResult(String.format(MESSAGE_EDIT_TASK_SUCCESS, taskToDone));
    }
}
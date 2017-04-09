package guitests;

//@@author A0163639W

import org.junit.Test;

import project.taskcrusher.commons.core.Messages;
import project.taskcrusher.logic.commands.MarkCommand;
import project.taskcrusher.testutil.TestEventCard;
import project.taskcrusher.testutil.TestTaskCard;

public class MarkCommandTest extends TaskcrusherGuiTest {

	@Test
	public void MarkTasks() {

		TestTaskCard[] currentTaskList = td.getTypicalTasks();

		// Checking for invalid index
		commandBox.runCommand("mark t" + " " + (currentTaskList.length) + 1 + " " + "complete");
		assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);

		// Checks for success of marking the task as complete
		int targetIndex = 1;
		TestTaskCard taskToMark1 = currentTaskList[targetIndex - 1];
		commandBox.runCommand("mark t" + " " + targetIndex + " " + "complete");
		assertResultMessage(String.format(MarkCommand.MESSAGE_MARK_TASK_SUCCESS, taskToMark1));

		//// Checks for success of marking the task as incomplete in middle of
		//// list
		targetIndex = currentTaskList.length / 2;
		TestTaskCard taskToMark = currentTaskList[targetIndex];
		commandBox.runCommand("mark t" + " " + targetIndex + " " + "incomplete");

		assertResultMessage(String.format(MarkCommand.MESSAGE_MARK_TASK_SUCCESS, taskToMark));

	}

	@Test
	public void MarkEvents() {

		TestEventCard[] currentEventList = td.getTypicalEvents();

		// Checking for invalid index
		commandBox.runCommand("mark e" + " " + (currentEventList.length) + 1 + " " + "complete");
		assertResultMessage(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);

		// Checks for success of marking the event as incomplete
		int targetIndex = 1;
		TestEventCard eventToMark1 = currentEventList[targetIndex - 1];
		commandBox.runCommand("mark e" + " " + targetIndex + " " + "incomplete");
		assertResultMessage(String.format(MarkCommand.MESSAGE_MARK_EVENT_SUCCESS, eventToMark1));

		//// Checks for success of marking the event as complete in the middle
		//// of list
		targetIndex = currentEventList.length / 2;
		TestEventCard eventToMark = currentEventList[targetIndex];
		commandBox.runCommand("mark e" + " " + targetIndex + " " + "complete");

		assertResultMessage(String.format(MarkCommand.MESSAGE_MARK_EVENT_SUCCESS, eventToMark));

	}

}

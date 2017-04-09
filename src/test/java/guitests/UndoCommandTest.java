package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.emory.mathcs.backport.java.util.Arrays;
import project.taskcrusher.logic.commands.UndoCommand;
import project.taskcrusher.testutil.TestEventCard;
import project.taskcrusher.testutil.TestTaskCard;

//@@author A0163639W
public class UndoCommandTest extends TaskcrusherGuiTest {

	@Test
	public void undoTasks() {

		// //without any last operation
		commandBox.runCommand("undo");
		assertResultMessage(UndoCommand.MESSAGE_FAILURE);

		TestTaskCard[] currentTaskList = td.getTypicalTasks();

		// undo a delete operation
		commandBox.runCommand("delete t " + 1);
		assertUndoCommandSuccess(1, currentTaskList);
		//
		// //undo an edit operation
		commandBox.runCommand("edit t 1 Hello");
		assertUndoCommandSuccess(1, currentTaskList);

		// undo an add operation
		TestTaskCard taskToAdd = td.notAddedBuyTicket;
		commandBox.runCommand(taskToAdd.getAddCommand());
		assertUndoCommandSuccess(1, currentTaskList);

		// undo a mark operation
		commandBox.runCommand("mark t 3 complete");
		assertUndoCommandSuccess(1, currentTaskList);

		// run done, delete, invalid and clear operations
		commandBox.runCommand("mark t 1 complete");
		commandBox.runCommand("delete t 1");
		commandBox.runCommand("clear");
		assertUndoCommandSuccess(3, currentTaskList);

	}

	@Test
	public void undoEvents() {

		TestEventCard[] currentEventList = td.getTypicalEvents();

		// undo a delete operation on event
		commandBox.runCommand("delete e " + 1);
		assertUndoCommandSuccess(1, currentEventList);

		// undo an edit operation on event
		commandBox.runCommand("edit e 1 Meeting");
		assertUndoCommandSuccess(1, currentEventList);

		// undo an add operation for event
		TestEventCard eventToAdd = td.notAddedYetTownFestival;
		commandBox.runCommand(eventToAdd.getAddCommand());
		assertUndoCommandSuccess(1, currentEventList);

		// undo a mark operation for event
		commandBox.runCommand("mark e 3 complete");
		assertUndoCommandSuccess(1, currentEventList);

		// run done, delete, invalid and clear operations on events on 6 levels
		commandBox.runCommand("mark e 1 complete");
		commandBox.runCommand("delete e 1");
		commandBox.runCommand("mark e 2 complete");
		commandBox.runCommand("edit e 2 Trip");
		commandBox.runCommand("delete e " + 2);
		commandBox.runCommand("clear");

		assertUndoCommandSuccess(6, currentEventList);

	}

	private void assertUndoCommandSuccess(int times, final TestTaskCard[] currentList) {
		for (int i = 0; i < times; i++)
			commandBox.runCommand("undo");
		Arrays.sort(currentList);
		assertTrue(userInboxPanel.isListMatching(currentList));
	}

	private void assertUndoCommandSuccess(int times, final TestEventCard[] currentList) {
		for (int i = 0; i < times; i++)
			commandBox.runCommand("undo");
		Arrays.sort(currentList);
		assertTrue(userInboxPanel.isListMatching(currentList));
	}
}

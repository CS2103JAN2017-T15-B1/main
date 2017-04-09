package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.emory.mathcs.backport.java.util.Arrays;
import project.taskcrusher.logic.commands.RedoCommand;
import project.taskcrusher.testutil.TestTaskCard;
import project.taskcrusher.testutil.TestUtil;

//@@author A0163639W
public class RedoCommandTest extends TaskcrusherGuiTest {

    @Test
    public void redoTest() {
        // without any last undo operation
        commandBox.runCommand("redo");
        assertResultMessage(RedoCommand.MESSAGE_FAILURE);

        // redo an undo operation
        TestTaskCard taskToAdd = td.notAddedYetQuiz;
        TestTaskCard[] currentTaskList = td.getTypicalTasks();
        commandBox.runCommand(taskToAdd.getAddCommand());
        commandBox.runCommand("undo");
        currentTaskList = TestUtil.addTasksToList(currentTaskList, taskToAdd);
        assertRedoCommandSuccess(1, currentTaskList);

        // redo two undo consecutively
        taskToAdd = td.notAddedBuyTicket;
        commandBox.runCommand(taskToAdd.getAddCommand());
        commandBox.runCommand("delete t " + 1);
        commandBox.runCommand("undo");
        commandBox.runCommand("undo");
        currentTaskList = TestUtil.addTasksToList(currentTaskList, taskToAdd);
        currentTaskList = TestUtil.removeTaskFromList(currentTaskList, 1);

        assertRedoCommandSuccess(2, currentTaskList);

        // redo operation fails when apply delete operation is performed after
        // undo operation
        commandBox.runCommand("undo");
        commandBox.runCommand("delete t 1");
        commandBox.runCommand("redo");
        assertResultMessage(RedoCommand.MESSAGE_FAILURE);
        // redo clear command
        commandBox.runCommand("clear");
        commandBox.runCommand("undo");
        commandBox.runCommand("redo");
        assertResultMessage(RedoCommand.MESSAGE_SUCCESS);
    }

    private void assertRedoCommandSuccess(int times, final TestTaskCard[] currentList) {
        for (int i = 0; i < times; i++) {
            commandBox.runCommand("redo");
        }

        Arrays.sort(currentList);
        assertTrue(userInboxPanel.isListMatching(currentList));
        assertResultMessage(RedoCommand.MESSAGE_SUCCESS);

    }
}

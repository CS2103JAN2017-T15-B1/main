package project.taskcrusher.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static project.taskcrusher.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static project.taskcrusher.commons.core.Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX;
import static project.taskcrusher.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.eventbus.Subscribe;

import project.taskcrusher.commons.core.EventsCenter;
import project.taskcrusher.commons.events.model.AddressBookChangedEvent;
import project.taskcrusher.commons.events.ui.JumpToListRequestEvent;
import project.taskcrusher.commons.events.ui.ShowHelpRequestEvent;
import project.taskcrusher.logic.commands.AddCommand;
import project.taskcrusher.logic.commands.ClearCommand;
import project.taskcrusher.logic.commands.Command;
import project.taskcrusher.logic.commands.CommandResult;
import project.taskcrusher.logic.commands.DeleteCommand;
import project.taskcrusher.logic.commands.ExitCommand;
import project.taskcrusher.logic.commands.FindCommand;
import project.taskcrusher.logic.commands.HelpCommand;
import project.taskcrusher.logic.commands.ListCommand;
import project.taskcrusher.logic.commands.SelectCommand;
import project.taskcrusher.logic.commands.exceptions.CommandException;
import project.taskcrusher.model.Model;
import project.taskcrusher.model.ModelManager;
import project.taskcrusher.model.ReadOnlyUserInbox;
import project.taskcrusher.model.UserInbox;
import project.taskcrusher.model.event.Event;
import project.taskcrusher.model.event.Location;
import project.taskcrusher.model.event.ReadOnlyEvent;
import project.taskcrusher.model.event.Timeslot;
import project.taskcrusher.model.shared.DateUtilApache;
import project.taskcrusher.model.shared.Description;
import project.taskcrusher.model.shared.Name;
import project.taskcrusher.model.shared.Priority;
import project.taskcrusher.model.tag.Tag;
import project.taskcrusher.model.tag.UniqueTagList;
import project.taskcrusher.model.task.Deadline;
import project.taskcrusher.model.task.ReadOnlyTask;
import project.taskcrusher.model.task.Task;
import project.taskcrusher.storage.StorageManager;

public class LogicManagerTest {

    @Rule
    public TemporaryFolder saveFolder = new TemporaryFolder();

    private Model model;
    private Logic logic;

    // These are for checking the correctness of the events raised
    private ReadOnlyUserInbox latestSavedUserInbox;
    private boolean helpShown;
    private int targetedJumpIndex;

    @Subscribe
    private void handleLocalModelChangedEvent(AddressBookChangedEvent abce) {
        latestSavedUserInbox = new UserInbox(abce.data);
    }

    @Subscribe
    private void handleShowHelpRequestEvent(ShowHelpRequestEvent she) {
        helpShown = true;
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent je) {
        targetedJumpIndex = je.targetIndex;
    }

    @Before
    public void setUp() {
        model = new ModelManager();
        String tempUserInboxFile = saveFolder.getRoot().getPath() + "TempUserInbox.xml";
        String tempPreferencesFile = saveFolder.getRoot().getPath() + "TempPreferences.json";
        logic = new LogicManager(model, new StorageManager(tempUserInboxFile, tempPreferencesFile));
        EventsCenter.getInstance().registerHandler(this);

        latestSavedUserInbox = new UserInbox(model.getUserInbox()); // last
                                                                    // saved
                                                                    // assumed
                                                                    // to be up
                                                                    // to date
        helpShown = false;
        targetedJumpIndex = -1; // non yet
    }

    @After
    public void tearDown() {
        EventsCenter.clearSubscribers();
    }

    @Test
    public void execute_invalid() {
        String invalidCommand = "       ";
        assertCommandFailure(invalidCommand, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
    }

    /**
     * Executes the command, confirms that a CommandException is not thrown and
     * that the result message is correct. Also confirms that both the 'address
     * book' and the 'last shown list' are as specified.
     *
     * @see #assertCommandBehavior(boolean, String, String, ReadOnlyUserInbox,
     *      List)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage, ReadOnlyUserInbox expectedUserInbox,
            List<? extends ReadOnlyTask> expectedShownTaskList, List<? extends ReadOnlyEvent> expectedShownEventList) {
        assertCommandBehavior(false, inputCommand, expectedMessage, expectedUserInbox, expectedShownTaskList,
                expectedShownEventList);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that
     * the result message is correct. Both the 'address book' and the 'last
     * shown list' are verified to be unchanged.
     *
     * @see #assertCommandBehavior(boolean, String, String, ReadOnlyUserInbox,
     *      List)
     */
    private void assertCommandFailure(String inputCommand, String expectedMessage) {
        UserInbox expectedUserInbox = new UserInbox(model.getUserInbox());
        List<ReadOnlyTask> expectedShownTaskList = new ArrayList<>(model.getFilteredTaskList());
        List<ReadOnlyEvent> expectedShownEventList = new ArrayList<>(model.getFilteredEventList());
        assertCommandBehavior(true, inputCommand, expectedMessage, expectedUserInbox, expectedShownTaskList,
                expectedShownEventList);
    }

    /**
     * Executes the command, confirms that the result message is correct and
     * that a CommandException is thrown if expected and also confirms that the
     * following three parts of the LogicManager object's state are as
     * expected:<br>
     * - the internal address book data are same as those in the
     * {@code expectedAddressBook} <br>
     * - the backing list shown by UI matches the {@code shownList} <br>
     * - {@code expectedAddressBook} was saved to the storage file. <br>
     */
    private void assertCommandBehavior(boolean isCommandExceptionExpected, String inputCommand, String expectedMessage,
            ReadOnlyUserInbox expectedAddressBook, List<? extends ReadOnlyTask> expectedShownTaskList,
            List<? extends ReadOnlyEvent> expectedShownEventList) {

        try {
            CommandResult result = logic.execute(inputCommand);
            assertFalse("CommandException expected but was not thrown.", isCommandExceptionExpected);
            assertEquals(expectedMessage, result.feedbackToUser);
        } catch (CommandException e) {
            assertTrue("CommandException not expected but was thrown.", isCommandExceptionExpected);
            assertEquals(expectedMessage, e.getMessage());
        }

        // Confirm the ui display elements should contain the right data
        assertEquals(expectedShownTaskList, model.getFilteredTaskList());
        assertEquals(expectedShownEventList, model.getFilteredEventList());

        // Confirm the state of data (saved and in-memory) is as expected
        assertEquals(expectedAddressBook, model.getUserInbox());
        assertEquals(expectedAddressBook, latestSavedUserInbox);
    }

    @Test
    public void execute_unknownCommandWord() {
        String unknownCommand = "uicfhmowqewca";
        assertCommandFailure(unknownCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_help() {
        assertCommandSuccess("help", HelpCommand.SHOWING_HELP_MESSAGE, new UserInbox(), Collections.emptyList(),
                Collections.emptyList());
        assertTrue(helpShown);
    }

    @Test
    public void execute_exit() {
        assertCommandSuccess("exit", ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT, new UserInbox(), Collections.emptyList(),
                Collections.emptyList());
    }

    @Test
    public void execute_clear() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        model.addTask(helper.generateTask(1));
        model.addTask(helper.generateTask(2));
        model.addTask(helper.generateTask(3));
        model.addEvent(helper.generateEvent(1));
        model.addEvent(helper.generateEvent(2));
        model.addEvent(helper.generateEvent(3));

        assertCommandSuccess("clear", ClearCommand.MESSAGE_SUCCESS, new UserInbox(), Collections.emptyList(),
                Collections.emptyList());
    }

    @Test
    public void execute_add_invalidArgsFormat() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);
        assertCommandFailure("add ", expectedMessage);
        assertCommandFailure("add noflag", expectedMessage);
        assertCommandFailure("add z Valid Name p/3 //valid description", expectedMessage);
    }

    @Test
    public void execute_add_invalidTaskData() {
        assertCommandFailure("add t d/2017-11-11 p/1 //validdescription",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        assertCommandFailure("add t validname p/not_numbers d/2017-11-11 //validdescription",
                Priority.MESSAGE_PRIORITY_CONSTRAINTS);
        assertCommandFailure("add t validname p/111111 d/2017-11-11 //validdescription",
                Priority.MESSAGE_PRIORITY_CONSTRAINTS);
        assertCommandFailure("add t validname p/-1 d/2017-11-11 //validdescription",
                Priority.MESSAGE_PRIORITY_CONSTRAINTS);
        assertCommandFailure("add t validname p/2 d/2016-11-11 //validdescription", DateUtilApache.MESSAGE_DATE_PASSED);
        assertCommandFailure("add t validname p/2 d/ewrio232 //validdescription",
                DateUtilApache.MESSAGE_DATE_NOT_FOUND);
        assertCommandFailure("add t validname p/1 d/2017-11-11 //validdescription t/invalid_-[.tag",
                Tag.MESSAGE_TAG_CONSTRAINTS);
    }

    @Test
    public void execute_add_invalidEventData() {
        assertCommandFailure("add e d/2017-11-11 p/1 //validdescription",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        assertCommandFailure("add e validname l/validlocation d/ //validdescription", Timeslot.MESSAGE_TIMESLOT_DNE);
        assertCommandFailure("add e validname l/validlocation d/2020-11-11 03:00PM to 2020-11-11 05:00PM"
                + " or 2021-11-11 03:00PM //validdescription", Timeslot.MESSAGE_TIMESLOT_PAIRS);
        assertCommandFailure(
                "add e validname l/validlocation d/2020-11-11 11:00PM to 2020-11-11 05:00PM //validdescription",
                Timeslot.MESSAGE_TIMESLOT_RANGE);
        assertCommandFailure("add e validname l/validlocation d/2016-11-11 to 2017-11-11 //validdescription",
                DateUtilApache.MESSAGE_DATE_PASSED);
        assertCommandFailure("add e validname l/validlocation d/ewrio232 to 54rthg //validdescription",
                DateUtilApache.MESSAGE_DATE_NOT_FOUND);
        assertCommandFailure("add e validname l/validlocation d/2020-11-11 03:00PM to 2020-11-11 05:00PM"
                + " //validdescription t/invalid_-[.tag", Tag.MESSAGE_TAG_CONSTRAINTS);
    }

    @Test
    public void execute_add_successful() throws Exception {
        // add valid task
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.homework();
        UserInbox expectedInbox = new UserInbox();
        expectedInbox.addTask(toBeAdded);

        // execute command and verify result
        assertCommandSuccess(helper.generateAddTaskCommand(toBeAdded),
                String.format(AddCommand.MESSAGE_TASK_SUCCESS, toBeAdded), expectedInbox, expectedInbox.getTaskList(),
                expectedInbox.getEventList());

        // add event
        Event toBeAdded2 = helper.reviewSession();
        expectedInbox.addEvent(toBeAdded2);
        assertCommandSuccess(helper.generateAddEventCommand(toBeAdded2),
                String.format(AddCommand.MESSAGE_EVENT_SUCCESS, toBeAdded2), expectedInbox, expectedInbox.getTaskList(),
                expectedInbox.getEventList());

        // add tentative event
        Event toBeAdded3 = helper.reviewSessionTentative();
        expectedInbox.addEvent(toBeAdded3);
        assertCommandSuccess(helper.generateAddEventCommand(toBeAdded3),
                String.format(AddCommand.MESSAGE_EVENT_SUCCESS, toBeAdded3), expectedInbox, expectedInbox.getTaskList(),
                expectedInbox.getEventList());

    }

    @Test
    public void execute_addDuplicate_notAllowed() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.homework();

        // setup starting state
        model.addTask(toBeAdded); // task already in internal user inbox

        // execute command and verify result
        assertCommandFailure(helper.generateAddTaskCommand(toBeAdded), AddCommand.MESSAGE_DUPLICATE_TASK);

        Event toBeAdded2 = helper.reviewSession();
        model.addEvent(toBeAdded2); // event already in internal user inbox
        assertCommandFailure(helper.generateAddEventCommand(toBeAdded2), AddCommand.MESSAGE_DUPLICATE_EVENT);

        Event toBeAdded3 = helper.reviewSessionTentative();
        model.addEvent(toBeAdded3); // event already in internal user inbox
        assertCommandFailure(helper.generateAddEventCommand(toBeAdded3), AddCommand.MESSAGE_DUPLICATE_EVENT);

    }

    @Test
    public void execute_list_showsAllPersons() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        UserInbox expectedInbox = helper.generateUserInbox(2);
        List<? extends ReadOnlyTask> expectedTaskList = expectedInbox.getTaskList();
        List<? extends ReadOnlyEvent> expectedEventList = expectedInbox.getEventList();

        // prepare address book state
        helper.addToModel(model, 2);

        assertCommandSuccess("list", ListCommand.MESSAGE_SUCCESS, expectedInbox, expectedTaskList, expectedEventList);
    }

    /**
     * Confirms the 'invalid argument index number behaviour' for the given
     * command targeting a single person in the shown list, using visible index.
     *
     * @param commandWord
     *            to test assuming it targets a single person in the last shown
     *            list based on visible index.
     */
    private void assertIncorrectIndexFormatBehaviorForCommand(String commandWord, String expectedMessage)
            throws Exception {
        assertCommandFailure(commandWord, expectedMessage); // index missing
        assertCommandFailure(commandWord + " +1", expectedMessage); // index
                                                                    // should be
                                                                    // unsigned
        assertCommandFailure(commandWord + " -1", expectedMessage); // index
                                                                    // should be
                                                                    // unsigned
        assertCommandFailure(commandWord + " 0", expectedMessage); // index
                                                                   // cannot be
                                                                   // 0
        assertCommandFailure(commandWord + " not_a_number", expectedMessage);
    }

    /**
     * Confirms the 'invalid argument index number behaviour' for the given
     * command targeting a single person in the shown list, using visible index.
     *
     * @param commandWord
     *            to test assuming it targets a single person in the last shown
     *            list based on visible index.
     */
    private void assertIndexNotFoundBehaviorForCommand(String commandWord) throws Exception {
        String expectedMessage = MESSAGE_INVALID_TASK_DISPLAYED_INDEX;
        TestDataHelper helper = new TestDataHelper();
        List<Task> taskList = helper.generateTaskList(2);

        // set UserInbox state to 2 tasks
        model.resetData(new UserInbox());
        for (Task p : taskList) {
            model.addTask(p);
        }

        assertCommandFailure(commandWord + " 3", expectedMessage);
    }

    @Test
    public void execute_selectInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("select", expectedMessage);
    }

    @Test
    public void execute_selectIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("select");
    }

    // TODO CAN WE GET RID OF THIS USELESS COMMAND
    @Test
    public void execute_select_jumpsToCorrectPerson() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Task> threeTasks = helper.generateTaskList(3);
        List<Event> fiveEvents = helper.generateEventList(5);

        UserInbox expectedInbox = helper.generateUserInbox(threeTasks, fiveEvents);
        helper.addToModel(model, threeTasks, fiveEvents);

        assertCommandSuccess("select 2", String.format(SelectCommand.MESSAGE_SELECT_PERSON_SUCCESS, 2), expectedInbox,
                expectedInbox.getTaskList(), expectedInbox.getEventList());
        assertEquals(1, targetedJumpIndex);
        assertEquals(model.getFilteredTaskList().get(1), threeTasks.get(1));
    }

    @Test
    public void execute_deleteInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("delete", expectedMessage);
    }

    @Test
    public void execute_deleteIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("delete t");
    }

    @Test
    public void execute_delete_removesCorrectUserToDo() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Task> threeTasks = helper.generateTaskList(3);
        List<Event> fiveEvents = helper.generateEventList(5);

        UserInbox expectedInbox = helper.generateUserInbox(threeTasks, fiveEvents);
        expectedInbox.removeTask(threeTasks.get(1));
        helper.addToModel(model, threeTasks, fiveEvents);

        assertCommandSuccess("delete t 2", String.format(DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS, threeTasks.get(1)),
                expectedInbox, expectedInbox.getTaskList(), expectedInbox.getEventList());

        assertCommandSuccess("clear", ClearCommand.MESSAGE_SUCCESS, new UserInbox(), Collections.emptyList(),
                Collections.emptyList());

        List<Task> oneTask = new ArrayList<>();
        oneTask.add(helper.generateTaskWithName("28t5j3"));
        List<Event> oneEvent = new ArrayList<>();
        oneEvent.add(helper.generateEventWithName("elkrtewlr225"));

        UserInbox expectedInbox2 = helper.generateUserInbox(oneTask, oneEvent);
        expectedInbox2.removeEvent(oneEvent.get(0));
        helper.addToModel(model, oneTask, oneEvent);

        assertCommandSuccess("delete e 1", String.format(DeleteCommand.MESSAGE_DELETE_EVENT_SUCCESS, oneEvent.get(0)),
                expectedInbox2, expectedInbox2.getTaskList(), expectedInbox2.getEventList());
    }

    @Test
    public void execute_find_invalidArgsFormat() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        assertCommandFailure("find ", expectedMessage);
    }

    @Test
    public void execute_find_onlyMatchesFullWordsInNames() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task tTarget1 = helper.generateTaskWithName("bla bla KEY bla");
        Task tTarget2 = helper.generateTaskWithName("bla KEY bla bceofeia");
        Task t1 = helper.generateTaskWithName("KE Y");
        Task t2 = helper.generateTaskWithName("KEYKEYKEY sduauo");

        Event eTarget1 = helper.generateEventWithName("bla bla KEY bla");
        Event eTarget2 = helper.generateEventWithName("bla KEY bla bceofeia");
        Event e1 = helper.generateEventWithName("KE Y");
        Event e2 = helper.generateEventWithName("KEYKEYKEY sduauo");

        List<Task> fourTasks = helper.generateTaskList(t1, tTarget1, t2, tTarget2);
        List<Event> fiveEvents = helper.generateEventList(e1, eTarget1, e2, eTarget2);

        UserInbox expectedInbox = helper.generateUserInbox(fourTasks, fiveEvents);

        List<Task> expectedTaskList = helper.generateTaskList(tTarget1, tTarget2);
        List<Event> expectedEventList = helper.generateEventList(eTarget1, eTarget2);

        helper.addToModel(model, fourTasks, fiveEvents);

        assertCommandSuccess("find KEY",
                Command.getMessageForPersonListShownSummary(expectedTaskList.size(), expectedEventList.size()),
                expectedInbox, expectedTaskList, expectedEventList);
    }

    @Test
    public void execute_find_isNotCaseSensitive() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task t1 = helper.generateTaskWithName("bla bla KEY bla");
        Task t2 = helper.generateTaskWithName("bla KEY bla bceofeia");
        Task t3 = helper.generateTaskWithName("key key");
        Task t4 = helper.generateTaskWithName("KEy sduauo");

        Event e1 = helper.generateEventWithName("bla bla KEY bla");
        Event e2 = helper.generateEventWithName("bla KEY bla bceofeia");
        Event e3 = helper.generateEventWithName("key key");

        List<Task> fourTasks = helper.generateTaskList(t3, t1, t4, t2);
        List<Event> threeEvents = helper.generateEventList(e3, e1, e2);
        UserInbox expectedInbox = helper.generateUserInbox(fourTasks, threeEvents);
        List<Task> expectedTaskList = fourTasks;
        List<Event> expectedEventList = threeEvents;

        helper.addToModel(model, fourTasks, threeEvents);

        assertCommandSuccess("find KEY",
                Command.getMessageForPersonListShownSummary(expectedTaskList.size(), expectedEventList.size()),
                expectedInbox, expectedTaskList, expectedEventList);
    }

    @Test
    public void execute_find_matchesIfAnyKeywordPresent() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task tTarget1 = helper.generateTaskWithName("bla bla KEY bla");
        Task tTarget2 = helper.generateTaskWithName("bla rAnDoM bla bceofeia");
        Task tTarget3 = helper.generateTaskWithName("key key");
        Task t1 = helper.generateTaskWithName("sduauo");

        Event eTarget1 = helper.generateEventWithName("bla bla KEY bla");
        Event eTarget2 = helper.generateEventWithName("bla rAnDoM bla bceofeia");
        Event eTarget3 = helper.generateEventWithName("key key");
        Event e1 = helper.generateEventWithName("sduauo");

        List<Task> fourTasks = helper.generateTaskList(tTarget1, t1, tTarget2, tTarget3);
        List<Event> fourEvents = helper.generateEventList(eTarget1, e1, eTarget2, eTarget3);
        UserInbox expectedInbox = helper.generateUserInbox(fourTasks, fourEvents);
        List<Task> expectedTaskList = helper.generateTaskList(tTarget1, tTarget2, tTarget3);
        List<Event> expectedEventList = helper.generateEventList(eTarget1, eTarget2, eTarget3);
        helper.addToModel(model, fourTasks, fourEvents);

        assertCommandSuccess("find key rAnDoM",
                Command.getMessageForPersonListShownSummary(expectedTaskList.size(), expectedEventList.size()),
                expectedInbox, expectedTaskList, expectedEventList);
    }

    /**
     * A utility class to generate test data.
     */
    class TestDataHelper {

        Task homework() throws Exception {
            Name name = new Name("CS2103 homework");
            Deadline deadline = new Deadline("");
            Priority priority = new Priority("3");
            Description description = new Description("do or die");
            Tag tag1 = new Tag("tag1");
            Tag tag2 = new Tag("longertag2");
            UniqueTagList tags = new UniqueTagList(tag1, tag2);
            return new Task(name, deadline, priority, description, tags);
        }

        Event reviewSession() throws Exception {
            Name name = new Name("CS2103 review session");
            Timeslot timeslot = new Timeslot("2017-09-23 03:00PM", "2017-09-23 05:00PM");
            List<Timeslot> timeslots = new ArrayList<>();
            timeslots.add(timeslot);
            Location location = new Location("i3 Aud");
            Description description = new Description("makes life easier");
            Tag tag3 = new Tag("sometag3");
            Tag tag4 = new Tag("sometag4");
            UniqueTagList tags = new UniqueTagList(tag3, tag4);
            return new Event(name, timeslots, location, description, tags);
        }

        Event reviewSessionTentative() throws Exception {
            Name name = new Name("CS2103 review session probably");
            Timeslot timeslot1 = new Timeslot("2020-09-23 03:00PM", "2020-09-23 05:00PM");
            Timeslot timeslot2 = new Timeslot("2021-09-23 06:00PM", "2021-09-23 08:00PM");
            List<Timeslot> timeslots = new ArrayList<>();
            timeslots.add(timeslot1);
            timeslots.add(timeslot2);
            Location location = new Location("i3 Aud");
            Description description = new Description("makes life easier");
            Tag tag3 = new Tag("sometag3");
            Tag tag4 = new Tag("sometag4");
            UniqueTagList tags = new UniqueTagList(tag3, tag4);
            return new Event(name, timeslots, location, description, tags);
        }

        /**
         * Generates a valid person using the given seed. Running this function
         * with the same parameter values guarantees the returned person will
         * have the same state. Each unique seed will generate a unique Person
         * object.
         *
         * @param seed
         *            used to generate the person data field values
         */
        Task generateTask(int seed) throws Exception {
            return new Task(new Name("Task " + seed), new Deadline(""),
                    new Priority(String.valueOf((int) Math.random() * 4)), new Description("description is " + seed),
                    new UniqueTagList(new Tag("tag" + Math.abs(seed)), new Tag("tag" + Math.abs(seed + 1))));
        }

        Event generateEvent(int seed) throws Exception {
            Timeslot timeslot = new Timeslot("2020-09-23 03:00PM", "2020-9-23 05:00PM");
            DateUtils.addHours(timeslot.start, seed);
            DateUtils.addHours(timeslot.end, seed);
            List<Timeslot> timeslots = new ArrayList<Timeslot>();
            timeslots.add(timeslot);
            return new Event(new Name("Event " + seed), timeslots, new Location("Location" + seed),
                    new Description("description is " + seed),
                    new UniqueTagList(new Tag("tag" + Math.abs(seed)), new Tag("tag" + Math.abs(seed + 1))));
        }

        /** Generates the correct add command based on the person given */
        String generateAddTaskCommand(Task task) {
            StringBuffer cmd = new StringBuffer();

            cmd.append("add ");
            cmd.append("t ");

            cmd.append(task.getName().toString());
            cmd.append(" d/").append(task.getDeadline());
            cmd.append(" p/").append(task.getPriority());
            cmd.append(" //").append(task.getDescription());

            UniqueTagList tags = task.getTags();
            for (Tag t : tags) {
                cmd.append(" t/").append(t.tagName);
            }

            return cmd.toString();
        }

        String generateAddEventCommand(Event event) {
            StringBuffer cmd = new StringBuffer();

            cmd.append("add ");
            cmd.append("e ");

            cmd.append(event.getName().toString());

            List<Timeslot> timeslots = event.getTimeslots();
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < timeslots.size(); i++) {
                sb.append(timeslots.get(i));
                if (i < timeslots.size() - 1) {
                    sb.append(" or ");
                }
            }

            cmd.append(" d/").append(sb.toString());
            cmd.append(" l/").append(event.getLocation());
            cmd.append(" //").append(event.getDescription());

            UniqueTagList tags = event.getTags();
            for (Tag t : tags) {
                cmd.append(" t/").append(t.tagName);
            }

            return cmd.toString();
        }

        /**
         * Generates an AddressBook with auto-generated persons.
         */
        UserInbox generateUserInbox(int numGenerated) throws Exception {
            UserInbox userInbox = new UserInbox();
            addToUserInbox(userInbox, numGenerated);
            return userInbox;
        }

        /**
         * Generates an AddressBook based on the list of Persons given.
         */
        UserInbox generateUserInbox(List<Task> tasks, List<Event> events) throws Exception {
            UserInbox userInbox = new UserInbox();
            addToUserInbox(userInbox, tasks, events);
            return userInbox;
        }

        /**
         * Adds auto-generated Task objects to the given UserInbox
         *
         * @param userInbox
         *            The UserInbox to which the Tasks will be added
         */
        void addToUserInbox(UserInbox userInbox, int numGenerated) throws Exception {
            addToUserInbox(userInbox, generateTaskList(numGenerated), generateEventList(numGenerated));
        }

        /**
         * Adds the given list of Tasks to the given UserInbox
         */
        void addToUserInbox(UserInbox userInbox, List<Task> tasks, List<Event> events) throws Exception {
            for (Task t : tasks) {
                userInbox.addTask(t);
            }

            for (Event e : events) {
                userInbox.addEvent(e);
            }
        }

        /**
         * Adds auto-generated Person objects to the given model
         *
         * @param model
         *            The model to which the Persons will be added
         */
        void addToModel(Model model, int numGenerated) throws Exception {
            addToModel(model, generateTaskList(numGenerated), generateEventList(numGenerated));
        }

        /**
         * Adds the given list of Persons to the given model
         */
        void addToModel(Model model, List<Task> tasksToAdd, List<Event> eventsToAdd) throws Exception {
            for (Task t : tasksToAdd) {
                model.addTask(t);
            }

            for (Event e : eventsToAdd) {
                model.addEvent(e);
            }
        }

        /**
         * Generates a list of Persons based on the flags.
         */
        List<Task> generateTaskList(int numGenerated) throws Exception {
            List<Task> tasks = new ArrayList<>();
            for (int i = 1; i <= numGenerated; i++) {
                tasks.add(generateTask(i));
            }
            return tasks;
        }

        List<Task> generateTaskList(Task... tasks) {
            return Arrays.asList(tasks);
        }

        List<Event> generateEventList(int numGenerated) throws Exception {
            List<Event> events = new ArrayList<>();
            for (int i = 1; i <= numGenerated; i++) {
                events.add(generateEvent(i));
            }
            return events;
        }

        List<Event> generateEventList(Event... events) {
            return Arrays.asList(events);
        }

        /**
         * Generates a Task object with given name. Other fields will have some
         * dummy values.
         */
        Task generateTaskWithName(String name) throws Exception {
            return new Task(new Name(name), new Deadline(""), new Priority("1"), new Description("sample description"),
                    new UniqueTagList(new Tag("tag")));
        }

        Event generateEventWithName(String name) throws Exception {
            List<Timeslot> timeslots = new ArrayList<>();
            timeslots.add(new Timeslot("2020-09-23 03:00PM", "2020-9-23 05:00PM"));
            return new Event(new Name(name), timeslots, new Location("somewhere"),
                    new Description("sample description"), new UniqueTagList(new Tag("tag")));
        }
    }
}

package seedu.address.model.util;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.ReadOnlyUserInbox;
import seedu.address.model.UserInbox;
import seedu.address.model.shared.Description;
import seedu.address.model.shared.Name;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList.DuplicateTaskException;

public class SampleDataUtil {
    public static Task[] getSampleTasks() {
        try {
            return new Task[] {
               new Task(new Name("CS2103 tutorial"), new Deadline(""), new Priority("3"), 
                       new Description("submit assignment"), new UniqueTagList("school"))
            };
        } catch (IllegalValueException e) {
            throw new AssertionError("sample data cannot be invalid", e);
        }
    }

    public static ReadOnlyUserInbox getSampleAddressBook() {
        try {
            UserInbox sampleuserInbox = new UserInbox();
            for (Task sampleTask : getSampleTasks()) {
                sampleuserInbox.addTask(sampleTask);
            }
            return sampleuserInbox;
        } catch (DuplicateTaskException e) {
            throw new AssertionError("sample data cannot contain duplicate persons", e);
        }
    }
}

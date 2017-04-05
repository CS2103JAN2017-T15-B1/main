package project.taskcrusher.model.util;

import project.taskcrusher.commons.exceptions.IllegalValueException;
import project.taskcrusher.model.ReadOnlyUserInbox;
import project.taskcrusher.model.UserInbox;
import project.taskcrusher.model.shared.Description;
import project.taskcrusher.model.shared.Name;
import project.taskcrusher.model.tag.UniqueTagList;
import project.taskcrusher.model.task.Deadline;
import project.taskcrusher.model.task.Priority;
import project.taskcrusher.model.task.Task;
import project.taskcrusher.model.task.UniqueTaskList.DuplicateTaskException;

public class SampleDataUtil {
    public static Task[] getSampleTasks() {
        try {
            return new Task[] {
                new Task(new Name("CS2103 tutorial"), new Deadline("tomorrow"), new Priority("3"),
                            new Description("presentation"), new UniqueTagList("school")),
                new Task(new Name("CS2106 assignment"), new Deadline("next Monday"), new Priority("2"),
                            new Description("submit assignment"), new UniqueTagList("school")),
                //@@author A0163639W
                new Task(new Name("security assignment"), new Deadline("next Tuesday"), new Priority("3"),
                        new Description("submit assignment"), new UniqueTagList("school")),
            new Task(new Name("Marketing project"), new Deadline("today"), new Priority("3"),
                    new Description("brand audit"), new UniqueTagList("school")),
            new Task(new Name("Meeting"), new Deadline("tomorrrow"), new Priority("2"),
                    new Description("nuclear deal"), new UniqueTagList("office")),
            new Task(new Name("Invite for dinner"), new Deadline("next Monday"), new Priority("1"),
                    new Description("Mr XYZ"), new UniqueTagList("Home")),
            new Task(new Name("Auditing"), new Deadline("tomorrow"), new Priority("2"),
                    new Description("Accounts"), new UniqueTagList("office")),
            new Task(new Name("Tennis match"), new Deadline("today"), new Priority("1"),
                    new Description("With John"), new UniqueTagList("Home")),
            new Task(new Name("Movie"), new Deadline("next Tuesday"), new Priority("1"),
                    new Description("iron man"), new UniqueTagList("home")),
            new Task(new Name("Buy a phone"), new Deadline("next Thursday"), new Priority("1"),
                    new Description("gift for friend"), new UniqueTagList("home")),
            new Task(new Name("Cricket match"), new Deadline("today"), new Priority("1"),
                    new Description("india wc final"), new UniqueTagList("home")),
            new Task(new Name("maths project"), new Deadline("next Tuesday"), new Priority("1"),
                    new Description("submit assignment"), new UniqueTagList("school")),
            new Task(new Name("Shopping"), new Deadline("today"), new Priority("1"),
                    new Description("ikea"), new UniqueTagList("home")),
            new Task(new Name("Repair watch"), new Deadline("next wednesday"), new Priority("3"),
                    new Description("swatch"), new UniqueTagList("home")),
            new Task(new Name("Physics hw"), new Deadline("today"), new Priority("3"),
                    new Description("Kinematics"), new UniqueTagList("school")),
            new Task(new Name("Chemistry lab"), new Deadline("today"), new Priority("3"),
                    new Description("salt analysis"), new UniqueTagList("school")),
            new Task(new Name("Club meeting"), new Deadline("next Tuesday"), new Priority("3"),
                    new Description("organising committee"), new UniqueTagList("school")),
            new Task(new Name("Pick a friend"), new Deadline("tomorrow"), new Priority("1"),
                    new Description("Mr Smith"), new UniqueTagList("home")),
            new Task(new Name("Physics lab"), new Deadline("next Tuesday"), new Priority("3"),
                    new Description("Sonometer"), new UniqueTagList("school")),
            new Task(new Name("Book tickets"), new Deadline("today"), new Priority("3"),
                    new Description("delhi-singapore"), new UniqueTagList("home")),
            new Task(new Name("Fix a meeting"), new Deadline("next Thursday"), new Priority("3"),
                    new Description("russian invasion"), new UniqueTagList("office")),
            new Task(new Name("Computer lab"), new Deadline("next Tuesday"), new Priority("3"),
                    new Description("C++ OOP"), new UniqueTagList("school")),
            new Task(new Name("C++ assignment"), new Deadline("today"), new Priority("1"),
                    new Description("Directory with double pointers"), new UniqueTagList("school")),
            new Task(new Name("Dinner"), new Deadline("next Tuesday"), new Priority("1"),
                    new Description("Hilton hotel"), new UniqueTagList("home")),
            new Task(new Name("Marriage invite"), new Deadline("today"), new Priority("3"),
                    new Description("Church"), new UniqueTagList("home")),
            new Task(new Name("Lunch"), new Deadline("next monday"), new Priority("3"),
                    new Description("MCD"), new UniqueTagList("school")),
            new Task(new Name("Assignment"), new Deadline("next Tuesday"), new Priority("3"),
                    new Description("submit assignment"), new UniqueTagList("school")),
            new Task(new Name("comp 2011"), new Deadline("tomorrow"), new Priority("3"),
                    new Description("submit assignment"), new UniqueTagList("school")),
            new Task(new Name("paper checking"), new Deadline("today"), new Priority("3"),
                    new Description("comp2711"), new UniqueTagList("school")),
            new Task(new Name("trip "), new Deadline("next Tuesday"), new Priority("2"),
                    new Description("India"), new UniqueTagList("home")),
            new Task(new Name("Get a gun"), new Deadline("next monday"), new Priority("3"),
                    new Description("shoot all enemies"), new UniqueTagList("school")),
            new Task(new Name("comp3711 assignment"), new Deadline("next friday"), new Priority("3"),
                    new Description("submit assignment"), new UniqueTagList("school")),
            new Task(new Name("comp2012 assignment"), new Deadline("next Tuesday"), new Priority("3"),
                    new Description("submit assignment"), new UniqueTagList("school")),
            new Task(new Name("security assignment"), new Deadline("next Tuesday"), new Priority("3"),
                    new Description("submit assignment"), new UniqueTagList("school")),
            new Task(new Name("cs3111"), new Deadline("12-03-17"), new Priority("3"),
                    new Description("submit assignment"), new UniqueTagList("school")),
            new Task(new Name("cs2001"), new Deadline("14-03-17"), new Priority("3"),
                    new Description("submit assignment"), new UniqueTagList("school")),
            new Task(new Name("Plan a trip"), new Deadline("14-04-17"), new Priority("3"),
                    new Description("london"), new UniqueTagList("home")),
            new Task(new Name("Vote"), new Deadline("12-05-17"), new Priority("1"),
                    new Description("Elections"), new UniqueTagList("school")),
            new Task(new Name("office trip"), new Deadline("14-02-17"), new Priority("3"),
                    new Description("london"), new UniqueTagList("office")),
            new Task(new Name("Mgmt project"), new Deadline("13-05-17"), new Priority("3"),
                    new Description("OB"), new UniqueTagList("school")),
            new Task(new Name("Book seats"), new Deadline("17-04-17"), new Priority("3"),
                    new Description("london"), new UniqueTagList("home")),
            new Task(new Name("teach friend"), new Deadline("14-03-17"), new Priority("3"),
                    new Description("organic chemistry"), new UniqueTagList("school")),
            new Task(new Name("Marriage"), new Deadline("14-06-17"), new Priority("3"),
                    new Description("Delhi"), new UniqueTagList("home")),
            new Task(new Name("TT match"), new Deadline("15-04-17"), new Priority("3"),
                    new Description("MPSH"), new UniqueTagList("school"))
                
         
            };
            //@@author
        } catch (IllegalValueException e) {
            throw new AssertionError("sample data cannot be invalid", e);
        }
    }

    public static ReadOnlyUserInbox getSampleUserInbox() {
        try {
            UserInbox sampleuserInbox = new UserInbox();
            for (Task sampleTask : getSampleTasks()) {
                sampleuserInbox.addTask(sampleTask);
            }
            return sampleuserInbox;
        } catch (DuplicateTaskException e) {
            throw new AssertionError("sample data cannot contain duplicate tasks", e);
        }
    }
}

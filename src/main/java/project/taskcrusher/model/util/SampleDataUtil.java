package project.taskcrusher.model.util;

import java.util.ArrayList;
import java.util.List;

import project.taskcrusher.commons.exceptions.IllegalValueException;
import project.taskcrusher.model.ReadOnlyUserInbox;
import project.taskcrusher.model.UserInbox;
import project.taskcrusher.model.event.Event;
import project.taskcrusher.model.event.Location;
import project.taskcrusher.model.event.Timeslot;
import project.taskcrusher.model.event.UniqueEventList.DuplicateEventException;
import project.taskcrusher.model.shared.Description;
import project.taskcrusher.model.shared.Name;
import project.taskcrusher.model.shared.Priority;
import project.taskcrusher.model.tag.UniqueTagList;
import project.taskcrusher.model.task.Deadline;
import project.taskcrusher.model.task.Task;
import project.taskcrusher.model.task.UniqueTaskList.DuplicateTaskException;

//@@author A0163639W
public class SampleDataUtil {
    public static Task[] getSampleTasks() {
        try {
            return new Task[] {
                new Task(new Name("CS2103 tutorial"), new Deadline("2017-11-11"), new Priority("3"),
                        new Description("presentation"), new UniqueTagList("school", "groupwork")),
                new Task(new Name("CS2106 assignment"), new Deadline("2017-06-20 6pm"), new Priority("2"),
                        new Description("submit assignment"), new UniqueTagList("school")),

                new Task(new Name("security assignment"), new Deadline("2017-12-11 10:00am"), new Priority("3"),
                        new Description("submit assignment"), new UniqueTagList("school", "assignment")),
                new Task(new Name("Marketing project"), new Deadline("2017-11-10"), new Priority("3"),
                        new Description("brand audit"), new UniqueTagList("school")),
                new Task(new Name("Meeting"), new Deadline("12-11-17 1400"), new Priority("2"),
                        new Description("important person"), new UniqueTagList("office")),
                new Task(new Name("Invite for dinner"), new Deadline("08-10-17 6pm"), new Priority("1"),
                        new Description("Mr XYZ"), new UniqueTagList("Home")),
                new Task(new Name("Auditing"), new Deadline("2017-12-13 10:00am"), new Priority("2"),
                        new Description("Accounts"), new UniqueTagList("office")),
                new Task(new Name("Tennis match"), new Deadline("2017-7-1 10:00am"), new Priority("1"),
                        new Description("With John"), new UniqueTagList("Home")),
                new Task(new Name("Watch Harry Potter series"), new Deadline("2017-5-1"), new Priority("1"),
                        new Description("compare with the books"), new UniqueTagList("home")),
                new Task(new Name("Buy a phone"), new Deadline("2017-12-11"), new Priority("1"),
                        new Description("gift for friend"), new UniqueTagList("home")),
                new Task(new Name("Cricket match ticket booking"), new Deadline("June 21, 2108"), new Priority("1"),
                        new Description("india wc final"), new UniqueTagList("home")),
                new Task(new Name("maths project"), new Deadline("may 20"), new Priority("1"),
                        new Description("submit assignment"), new UniqueTagList("school")),
                new Task(new Name("Shopping"), new Deadline("Today"), new Priority("1"), new Description("ikea"),
                        new UniqueTagList("home")),
                new Task(new Name("Repair watch"), new Deadline("Tomorrow"), new Priority("3"),
                        new Description("swatch"), new UniqueTagList("home")),
                new Task(new Name("Physics hw"), new Deadline("Next week"), new Priority("3"),
                        new Description("Kinematics"), new UniqueTagList("school")),
                new Task(new Name("Chemistry lab"), new Deadline("Monday"), new Priority("3"),
                        new Description("salt analysis"), new UniqueTagList("school")),
                new Task(new Name("Club meeting organisation"), new Deadline("Tuesday"), new Priority("3"),
                        new Description("organising committee"), new UniqueTagList("school")),
                new Task(new Name("Pick a friend"), new Deadline("2017-07-15 09:00am"), new Priority("1"),
                        new Description("Mr Smith"), new UniqueTagList("home")),
                new Task(new Name("Physics lab"), new Deadline("2017-06-11 0914 am"), new Priority("0"),
                        new Description("Sonometer"), new UniqueTagList("school")),
                new Task(new Name("Book tickets"), new Deadline("2017-11-12 2113"), new Priority("0"),
                        new Description("delhisingapore"), new UniqueTagList("home")),
                new Task(new Name("Fix a meeting"), new Deadline("07-15"), new Priority("0"),
                        new Description("russian invasion"), new UniqueTagList("office")),
                new Task(new Name("Computer lab"), new Deadline("2016-10-15 12am"), new Priority("3"),
                        new Description("C OOP"), new UniqueTagList("school")),
                new Task(new Name("C assignment"), new Deadline("06-01-2017 1300"), new Priority("1"),
                        new Description("Directory with double pointers"), new UniqueTagList("school")),
                new Task(new Name("Ask her out for Dinner"), new Deadline("2016-11-24 12pm"), new Priority("1"),
                        new Description("Hilton hotel"), new UniqueTagList("call")),
                new Task(new Name("Marriage invite"), new Deadline("2017-03-22 1700"), new Priority("0"),
                        new Description("Church"), new UniqueTagList("home")),
                new Task(new Name("Lunch"), new Deadline("Next sunday"), new Priority("3"), new Description("where?"),
                        new UniqueTagList("canteen", "foodcourt")),
                new Task(new Name("UROP research paper"), new Deadline("Wednesday 1400am"), new Priority("0"),
                        new Description("artificial intelligence"), new UniqueTagList("school", "research")),
                new Task(new Name("Mid term preparation"), new Deadline("Saturday"), new Priority("3"),
                        new Description("print out notes for the day"), new UniqueTagList("print", "test")),
                new Task(new Name("paper checking"), new Deadline("2017-10-09 1500"), new Priority("3"),
                        new Description("TA"), new UniqueTagList("school")),
                new Task(new Name("holiday trip planning"), new Deadline("2017-11-3 11pm"), new Priority("2"),
                        new Description("India"),
                        new UniqueTagList("home")),
                new Task(new Name("Send parcel for birthday gift"), new Deadline("Today"), new Priority("3"),
                        new Description("Amy's birthday!"), new UniqueTagList("family")),
                new Task(new Name("Masters application"), new Deadline("Sunday noon"), new Priority("3"),
                        new Description("Urban planning or computer science"), new UniqueTagList("graduate")),
                new Task(new Name("Hostel application"), new Deadline("Tomorrow night"), new Priority("3"),
                        new Description("submit assignment"), new UniqueTagList("school")) };
        } catch (IllegalValueException e) {
            throw new AssertionError("sample data cannot be invalid", e);
        }
    }

    public static Event[] getSampleEvents() {
        try {
            return new Event[] {
                new Event(new Name("Guitar lesson"),
                        constructTimeslotList(new Timeslot("2017-11-11", "2017-11-12")), new Priority("1"),
                        new Location("NUS"), new Description("presentation"), new UniqueTagList("school")),
                new Event(new Name("Town festival"),
                        constructTimeslotList(new Timeslot("2018-03-20", "2018-03-31")), new Priority("0"),
                        new Location("Iloilo"), new Description("submit assignment"), new UniqueTagList("school")),

                new Event(new Name("Party planning"),
                        constructTimeslotList(new Timeslot("2017-12-11 03:00PM", "2017-12-11 05:00PM")),
                        new Priority("2"), new Location("Secret base"), new Description("submit assignment"),
                        new UniqueTagList("school")),
                new Event(new Name("Fix roof"), constructTimeslotList(new Timeslot("2017-11-19", "2017-11-20")),
                        new Priority("0"), new Location("home"), new Description("brand audit"),
                        new UniqueTagList("school")),
                new Event(new Name("Meet friend from Australia"),
                        constructTimeslotListTentative(new Timeslot("2019-11-11 02:00PM", "2019-11-11 05:00PM"),
                                new Timeslot("2019-11-11 06:00PM", "2019-11-11 07:00PM")),
                        new Priority("0"), new Location("TBC"), new Description("where to take him around?"),
                        new UniqueTagList("office")),
                new Event(new Name("Island trip"), constructTimeslotList(new Timeslot("2017-10-12", "2017-10-15")),
                        new Priority("3"), new Location("Middle of nowhere"), new Description("Mr XYZ"),
                        new UniqueTagList("Home")),
                new Event(new Name("Swimming lesson"),
                        constructTimeslotList(new Timeslot("2017-11-11 1300", "2017-11-11 1500")), new Priority("0"),
                        new Location("Clementi"), new Description("for lifesaving class"),
                        new UniqueTagList("fitness")),
                new Event(new Name("Calculus paper"),
                        constructTimeslotList(new Timeslot("2018-03-20 1400", "2018-03-20 1600")), new Priority("0"),
                        new Location("Maths lab"), new Description(""),
                        new UniqueTagList("school")),

                new Event(new Name("Campaign"),
                        constructTimeslotList(new Timeslot("2017-12-11 03:00PM", "05:00PM")), new Priority("2"),
                        new Location("For mayor election"), new Description(""), new UniqueTagList("school")),
                new Event(new Name("shopping with brother"),
                        constructTimeslotList(new Timeslot("2017-11-19 1300", "2017-11-19 1500")), new Priority("0"),
                        new Location("suits for internship"), new Description("within 500dollars"),
                        new UniqueTagList("work")),
                new Event(new Name("Music concert"),
                        constructTimeslotListTentative(new Timeslot("2019-11-11 02:00PM", "1700"),
                                new Timeslot("2019-11-13 06:00PM", "2019-11-18 07:00PM")),
                        new Priority("0"), new Location("Local bar"), new Description("Jazz"),
                        new UniqueTagList("optional")),
                new Event(new Name("Soccer match"),
                        constructTimeslotList(new Timeslot("saturday 1500", "saturday 1600")), new Priority("1"),
                        new Location("SRC"), new Description("Weekly"), new UniqueTagList("SRC", "fitness")),

                new Event(new Name("Part time work"),
                        constructTimeslotList(new Timeslot("2017-12-14 03:00PM", "08:00PM")), new Priority("3"),
                        new Location("Guardian"), new Description("wear uniform"), new UniqueTagList("work")),
                new Event(new Name("CCA meeting"),
                        constructTimeslotList(new Timeslot("yesterday 10pm", "11pm")), new Priority("2"),
                        new Location("TBC"), new Description("vote for president"),
                        new UniqueTagList("school")),
                new Event(new Name("Cricket game"),
                        constructTimeslotListTentative(new Timeslot("2019-11-12 02:00PM", "1800"),
                                new Timeslot("2019-11-17 03:00PM", "2019-11-18 07:00PM")),
                        new Priority("1"), new Location("Delhi"), new Description("RR vs Kkr"),
                        new UniqueTagList("sports")),
                new Event(new Name("Software engineering demo"),
                        constructTimeslotList(new Timeslot("thursday 1pm", "thursday 2pm")), new Priority("3"),
                        new Location("COM1"), new Description("Let's do this!"),
                        new UniqueTagList("school")) };
            // @@author
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
            for (Event sampleEvent : getSampleEvents()) {
                sampleuserInbox.addEvent(sampleEvent);
            }

            return sampleuserInbox;
        } catch (DuplicateTaskException e) {
            throw new AssertionError("sample data cannot contain duplicate tasks", e);
        } catch (DuplicateEventException e2) {
            throw new AssertionError("sample data cannot contain duplicate events", e2);
        }
    }

    public static List<Timeslot> constructTimeslotList(Timeslot t) {
        List<Timeslot> timeslots = new ArrayList<>();
        timeslots.add(t);
        return timeslots;
    }

    public static List<Timeslot> constructTimeslotListTentative(Timeslot t, Timeslot t2) {
        List<Timeslot> timeslots = new ArrayList<>();
        timeslots.add(t);
        timeslots.add(t2);
        return timeslots;
    }
}

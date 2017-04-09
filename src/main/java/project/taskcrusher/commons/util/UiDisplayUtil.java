package project.taskcrusher.commons.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import project.taskcrusher.model.event.Location;
import project.taskcrusher.model.event.Timeslot;
import project.taskcrusher.model.shared.Priority;
import project.taskcrusher.model.task.Deadline;

//@@author A0127737X
/**
 * This class provides the utility to adjust the displayed texts of some of the Event and Task attributes to cater
 * for better UI.
 */
public class UiDisplayUtil {
    public static final String[] PARSE_PATTERNS = { "MMM dd yyyy hh:mma", "EEEE hh:mma",
        "MMM dd hh:mma", "hh:mma" };
    public static final int FORMAT_DATE_ABSOLUTE = 0;
    public static final int FORMAT_DATE_THIS_WEEK = 1;
    public static final int FORMAT_DATE_THIS_YEAR = 2;
    public static final int FORMAT_DATE_RELATIVE = 3;
    public static final String MESSAGE_NO_DEADLINE = "no deadline";

    /**
     * returns a string representation of the given deadline in a user-friendly format for display
     */
    public static String renderDeadlineAsStringForUi(Deadline deadline) {
        assert deadline != null;
        if (!deadline.hasDeadline()) {
            return MESSAGE_NO_DEADLINE;
        }

        Date date = deadline.getDate().get();
        String deadlineFormat, prepend = "By ";

        if (isToday(date)) {
            deadlineFormat = PARSE_PATTERNS[FORMAT_DATE_RELATIVE];
            prepend += "Today ";
        } else if (isThisWeek(date)) {
            deadlineFormat = PARSE_PATTERNS[FORMAT_DATE_THIS_WEEK];
        } else if (isThisYear(date)) {
            deadlineFormat = PARSE_PATTERNS[FORMAT_DATE_THIS_YEAR];
        }  else {
            deadlineFormat = PARSE_PATTERNS[FORMAT_DATE_ABSOLUTE];
        }
        SimpleDateFormat formatter = new SimpleDateFormat(deadlineFormat, Locale.ENGLISH);
        return prepend + formatter.format(date);
    }

    private static boolean isThisYear(Date d) {
        Date now = new Date();
        SimpleDateFormat yearChecker = new SimpleDateFormat("yyyy");
        return yearChecker.format(now).equals(yearChecker.format(d));
    }

    private static boolean isToday(Date d) {
        Date now = new Date();
        SimpleDateFormat dateChecker = new SimpleDateFormat("yyyyMMdd");
        return dateChecker.format(now).equals(dateChecker.format(d));
    }

    private static boolean isThisWeek(Date d) {
        Calendar currentCalendar = Calendar.getInstance();
        int week = currentCalendar.get(Calendar.WEEK_OF_YEAR);
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(d);
        int targetWeek = targetCalendar.get(Calendar.WEEK_OF_YEAR);
        return week == targetWeek;
    }

    /**
     * returns a string representation of the given time slot, with redundant details stripped for display
     */
    public static String renderTimeslotAsStringForUi(Timeslot timeslot) {
        assert timeslot != null;
        String endFormat, startFormat, startPrepend = "", endPrepend = "";

        /////endFormat
        if (isSameDate(timeslot.start, timeslot.end)) {
            endFormat = PARSE_PATTERNS[FORMAT_DATE_RELATIVE];
        } else if (isToday(timeslot.end)) {
            endFormat = PARSE_PATTERNS[FORMAT_DATE_RELATIVE];
            endPrepend += "Today ";
        } else if (isThisWeek(timeslot.end)) {
            endFormat = PARSE_PATTERNS[FORMAT_DATE_THIS_WEEK];
        } else if (isThisYear(timeslot.end)) {
            endFormat = PARSE_PATTERNS[FORMAT_DATE_THIS_YEAR];
        } else {
            endFormat = PARSE_PATTERNS[FORMAT_DATE_ABSOLUTE];
        }

        /////startFormat
        if (isToday(timeslot.start)) {
            startFormat = PARSE_PATTERNS[FORMAT_DATE_RELATIVE];
            startPrepend = "Today ";
        } else if (isThisWeek(timeslot.start)) {
            startFormat = PARSE_PATTERNS[FORMAT_DATE_THIS_WEEK];
        } else if (isThisYear(timeslot.start)) {
            startFormat = PARSE_PATTERNS[FORMAT_DATE_THIS_YEAR];
        } else {
            startFormat = PARSE_PATTERNS[FORMAT_DATE_ABSOLUTE];
        }

        SimpleDateFormat sdf = new SimpleDateFormat(startFormat, Locale.ENGLISH);
        startPrepend += sdf.format(timeslot.start) + " to ";
        sdf.applyPattern(endFormat);
        startPrepend += endPrepend + sdf.format(timeslot.end);
        return startPrepend;
    }

    private static boolean isSameDate(Date d1, Date d2) {
        SimpleDateFormat dateChecker = new SimpleDateFormat("yyyyMMdd");
        return dateChecker.format(d1).equals(dateChecker.format(d2));
    }

    public static String priorityForUi(Priority p) {
        return "p=" + p.priority;
    }

    public static String getLocationStringForUi(Location location) {
        return location.hasLocation() ? "@ " + location.location : "";
    }

}

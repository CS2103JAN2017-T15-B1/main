package t15b1.taskcrusher.model.task;

import static org.junit.Assert.*;

import org.junit.Test;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

public class DeadlineTest {
    //before set up the current time so that you can make past deadlines that are invalid
    @Test
    public void isValidDeadline() {
        //invalid deadline
        assertFalse(Deadline.isValidDeadline("1-1-1995", true));
        assertFalse(Deadline.isValidDeadline("yesterday", true));
        
        //valid deadline
        assertTrue(Deadline.isValidDeadline(Deadline.NO_DEADLINE, true));
        assertTrue(Deadline.isValidDeadline("24-12-2019", false));
        assertTrue(Deadline.isValidDeadline("tomorrow", true));
        assertTrue(Deadline.isValidDeadline("next week", true));
        assertTrue(Deadline.isValidDeadline("next Tuesday", true));
        
        
        
        
    }

}

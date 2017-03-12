package t15b1.taskcrusher.testutil;

import seedu.address.model.shared.Description;
import seedu.address.model.shared.Name;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Email;
import seedu.address.model.task.Priority;
import seedu.address.model.task.ReadOnlyTask;

/**
 * A mutable person object. For testing only.
 */
public class TestPerson implements ReadOnlyTask {

    private Name name;
    private Description description;
    private Deadline deadline;
    private Priority priority;
    private UniqueTagList tags;

    public TestPerson() {
        tags = new UniqueTagList();
    }

    /**
     * Creates a copy of {@code taskToCopy}.
     */
    public TestPerson(TestPerson taskToCopy) {
        this.name = taskToCopy.getTaskName();
        this.priority = taskToCopy.getPriority();
        this.deadline = taskToCopy.getDeadline();
        this.description = taskToCopy.getDescription();
        this.tags = taskToCopy.getTags();
    }

    public void setName(Name name) {
        this.name = name;
    }

    public void setDescription(Description description) {
        this.description = description;
    }
    
    public void setDeadline(Deadline deadline){
        this.deadline = deadline;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setTags(UniqueTagList tags) {
        this.tags = tags;
    }

    @Override
    public Name getTaskName() {
        return name;
    }
    
    @Override
    public Deadline getDeadline() {
        return deadline;
    }
    
    @Override
    public Priority getPriority() {
        return priority;
    }

    @Override
    public Description getDescription() {
        return description;
    }

    @Override
    public UniqueTagList getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return getAsText();
    }

    public String getAddCommand() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("add " + this.getTaskName().taskName + " ");
//        sb.append("a/" + this.getDescription().value + " ");
//        sb.append("p/" + this.getPriority().value + " ");
//        sb.append("e/" + this.getEmail().value + " ");
//        this.getTags().asObservableList().stream().forEach(s -> sb.append("t/" + s.tagName + " "));
//        return sb.toString();
        StringBuilder sb = new StringBuilder();
        sb.append("add " + this.getTaskName().taskName + " ");
        sb.append("d/" + this.getDeadline().value + " ");
        sb.append("p/" + this.getPriority().value + " ");
        sb.append("//" + this.getDescription().value + " ");
        this.getTags().asObservableList().stream().forEach(s -> sb.append("t/" + s.tagName + " "));
        return sb.toString();
    }
}

package t15b1.taskcrusher.testutil;

import t15b1.taskcrusher.model.shared.Description;
import t15b1.taskcrusher.model.shared.Name;
import t15b1.taskcrusher.model.tag.UniqueTagList;
import t15b1.taskcrusher.model.task.Deadline;
import t15b1.taskcrusher.model.task.Priority;
import t15b1.taskcrusher.model.task.ReadOnlyTask;
/**
 * A mutable task object. For testing only.
 */
public class TestCard implements ReadOnlyTask {

    private Name name;
    private Description description;
    private Deadline deadline;
    private Priority priority;
    private UniqueTagList tags;

    public TestCard() {
        tags = new UniqueTagList();
    }

    /**
     * Creates a copy of {@code taskToCopy}.
     */
    public TestCard(TestCard taskToCopy) {
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
        //TODO: What if the deadlines and descriptions are empty 
        StringBuilder sb = new StringBuilder();
        sb.append("add " + this.getTaskName().name + " ");
        sb.append("d/" + this.getDeadline().deadline + " ");
        sb.append("p/" + this.getPriority().priority + " ");
        sb.append("//" + this.getDescription().description + " ");
        this.getTags().asObservableList().stream().forEach(s -> sb.append("t/" + s.tagName + " "));
        return sb.toString();
    }
}
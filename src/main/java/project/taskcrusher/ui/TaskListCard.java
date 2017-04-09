package project.taskcrusher.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import project.taskcrusher.commons.util.UiDisplayUtil;
import project.taskcrusher.model.task.ReadOnlyTask;

//@@author A0127737X
/**
 * Controller for TaskListCard.fxml. Reads a ReadOnlyTask and create the layout accordingly.
 */
public class TaskListCard extends UiPart<Region> {

    private static final String FXML = "TaskListCard.fxml";

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label name;
    @FXML
    private Label priority;
    @FXML
    private Label deadline;
    @FXML
    private Label description;
    @FXML
    private FlowPane tags;
    @FXML
    private ImageView tickIcon;
    @FXML
    private ImageView overdueIcon;

    public TaskListCard(ReadOnlyTask task, int displayedIndex, boolean isOverdue) {
        super(FXML);
        showId(displayedIndex);
        showName(task);
        showPriorityIfAny(task);
        showDeadline(task);
        showDescriptionIfAny(task);
        showCompleteTickIfApplicable(task);
        showOverdueStatusIfApplicable(task, isOverdue);

        initTags(task);
    }

    private void showId(int displayedIndex) {
        id.setText(displayedIndex + ". ");
    }

    private void showName(ReadOnlyTask task) {
        name.setText(task.getName().name);
//        name.setMinWidth(Region.USE_PREF_SIZE);
    }

    private void showCompleteTickIfApplicable(ReadOnlyTask task) {
        if (!task.isComplete()) {
            tickIcon.setVisible(false);
        }
    }

    private void showOverdueStatusIfApplicable(ReadOnlyTask task, boolean isOverdue) {
        if (!task.isComplete() && isOverdue) {
            overdueIcon.setVisible(true);
            overdueIcon.setManaged(true);
            deadline.setStyle("-fx-text-fill: red"); //done this way to overwrite the CSS properties
        } else {
            overdueIcon.setVisible(false);
            overdueIcon.setManaged(false);
        }
    }

    private void showDescriptionIfAny(ReadOnlyTask task) {
        description.setText(task.getDescription().toString()); //still set the text even if empty for GuiTest
        if (!task.getDescription().hasDescription()) {
            description.setVisible(false);;
        }
    }

    private void showPriorityIfAny(ReadOnlyTask task) {
        priority.setText(UiDisplayUtil.priorityForUi(task.getPriority()));
        switch (task.getPriority().priority) {
        case "1":
            priority.getStyleClass().add("priority-one");
            break;
        case "2":
            priority.getStyleClass().add("priority-two");
            break;
        case "3":
            priority.getStyleClass().add("priority-three");
            break;
        default:
            priority.setVisible(false);
            priority.setManaged(false);
        }
        priority.setMinWidth(Region.USE_PREF_SIZE);
    }

    private void showDeadline(ReadOnlyTask task) {
        deadline.setText(UiDisplayUtil.renderDeadlineAsStringForUi(task.getDeadline()));
        deadline.setMinWidth(Region.USE_PREF_SIZE);
    }

    private void initTags(ReadOnlyTask person) {
        person.getTags().forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }
}

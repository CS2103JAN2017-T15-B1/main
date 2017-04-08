package project.taskcrusher.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import project.taskcrusher.commons.util.UiDisplayUtil;
import project.taskcrusher.model.event.ReadOnlyEvent;

//@@author A0127737X
/**
 * Controller for EventListCard.fxml. Reads a ReadOnlyEvent and create the layout accordingly.
 */
public class EventListCard extends UiPart<Region> {

    private static final String FXML = "EventListCard.fxml";

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label name;
    @FXML
    private Label eventLocation; //named like this to avoid collision with the built-in "location" element
    @FXML
    private Label description;
    @FXML
    private Label priority;
    @FXML
    private FlowPane timeslots;
    @FXML
    private FlowPane tags;
    @FXML
    private ImageView tickIcon;
    @FXML
    private ImageView overdueIcon;

    public EventListCard(ReadOnlyEvent event, int displayedIndex, boolean isOverdue) {
        super(FXML);
        showId(displayedIndex);
        showName(event);
        showLocationIfAny(event);
        showDescription(event);
        showPriority(event);
        showEventTimeSlots(event);
        showCompleteStatusIfApplicable(event);
        showOverdueStatusIfApplicable(event, isOverdue);

        initTags(event);
    }

    private void showId(int displayedIndex) {
        id.setText(displayedIndex + ". ");
    }

    private void showName(ReadOnlyEvent event) {
        name.setText(event.getName().name);
        name.setMinWidth(Region.USE_PREF_SIZE);
    }

    private void showCompleteStatusIfApplicable(ReadOnlyEvent event) {
        if (!event.isComplete()) {
            tickIcon.setVisible(false);
        }
    }

    private void showOverdueStatusIfApplicable(ReadOnlyEvent event, boolean isOverdue) {
        if (!event.isComplete() && isOverdue) {
            overdueIcon.setVisible(true);
            overdueIcon.setManaged(true);
            for (Node child: timeslots.getChildren()) {
                child.setStyle("-fx-text-fill: red"); //done this way to overwrite the CSS properties
            }
        } else {
            overdueIcon.setVisible(false);
            overdueIcon.setManaged(false);
        }
    }

    private void showDescription(ReadOnlyEvent event) {
        description.setText(event.getDescription().description);
        if (event.getDescription().hasDescription()) {
            description.setMinWidth(Region.USE_PREF_SIZE);
        } else {
            description.setVisible(false);
        }
    }

    private void showPriority(ReadOnlyEvent event) {
        priority.setText(UiDisplayUtil.priorityForUi(event.getPriority()));
        switch (event.getPriority().priority) {
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
        }
        priority.setMinWidth(Region.USE_PREF_SIZE);
    }

    private void showLocationIfAny(ReadOnlyEvent event) {
        eventLocation.setText(UiDisplayUtil.getLocationStringForUi(event.getLocation()));
        eventLocation.setMinWidth(Region.USE_PREF_SIZE);
    }

    private void showEventTimeSlots(ReadOnlyEvent event) {
        event.getTimeslots().forEach(timeslot -> timeslots.getChildren().add(new Label(
                UiDisplayUtil.renderTimeslotAsStringForUi(timeslot))));
    }

    private void initTags(ReadOnlyEvent event) {
        event.getTags().forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }
}

package project.taskcrusher.storage;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import project.taskcrusher.commons.exceptions.IllegalValueException;
import project.taskcrusher.model.event.Event;
import project.taskcrusher.model.event.Location;
import project.taskcrusher.model.event.ReadOnlyEvent;
import project.taskcrusher.model.event.Timeslot;
import project.taskcrusher.model.shared.Description;
import project.taskcrusher.model.shared.Name;
import project.taskcrusher.model.tag.Tag;
import project.taskcrusher.model.tag.UniqueTagList;

public class XmlAdaptedEvent {

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String location;
    @XmlElement(required = true)
    private String description;

    @XmlElement(required = true)
    private List<XmlAdaptedTimeslot> timeslots = new ArrayList<>();

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedTask.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedEvent() {}


    /**
     * Converts a given Event into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedEvent
     */
    public XmlAdaptedEvent(ReadOnlyEvent source) {
        name = source.getName().name;
        location = source.getLocation().location;
        description = source.getDescription().description;

        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }

        timeslots = new ArrayList<>();
        for (Timeslot timeslot: source.getTimeslots()) {
            timeslots.add(new XmlAdaptedTimeslot(timeslot));
        }
    }

    /**
     * Converts this jaxb-friendly adapted event object into the model's Event object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted event
     */
    public Event toModelType() throws IllegalValueException {
        final List<Tag> eventTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            eventTags.add(tag.toModelType());
        }

        final List<Timeslot> eventTimeslots = new ArrayList<>();
        for (XmlAdaptedTimeslot timeslot: timeslots) {
            eventTimeslots.add(timeslot.toModelType());
        }

        final Name name = new Name(this.name);
        final Location location = new Location(this.location);
        final Description description = new Description(this.description);
        final UniqueTagList tags = new UniqueTagList(eventTags);
        return new Event(name, eventTimeslots, location, description, tags);
    }

}
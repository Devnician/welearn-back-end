package uni.ruse.welearn.welearn.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.ruse.welearn.welearn.components.EmailService;
import uni.ruse.welearn.welearn.model.Discipline;
import uni.ruse.welearn.welearn.model.Event;
import uni.ruse.welearn.welearn.repository.EventRepository;
import uni.ruse.welearn.welearn.util.WeLearnException;

import java.util.List;
import java.util.Optional;

/**
 * @author ivelin.dimitrov
 */
@Service
@Slf4j
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private DisciplineService disciplineService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    public Event findById(String id) throws WeLearnException {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            throw new WeLearnException("Event with id " + id + " not found");
        }
        return optionalEvent.get();
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }


    public Event save(Event event) throws WeLearnException {
        Event savedEvent = saveEventInternal(event);
        determineRecipientsAndSendEmails(savedEvent, "New event has been added - Welearn");
        return savedEvent;
    }

    public Event saveWithoutEmail(Event event) throws WeLearnException {
        return saveEventInternal(event);
    }

    private Event saveEventInternal(Event event) throws WeLearnException {
        if (event.getEventId() == null || event.getEventId().isEmpty()) {
            return eventRepository.save(event);
        }
//        checkOverlappingTimeRange(event);
        throw new WeLearnException("New Event object cannot have an id");
    }

    public Event edit(Event event) throws WeLearnException {
        Event savedEvent = editEventInternal(event);
        determineRecipientsAndSendEmails(savedEvent, "Event has been modified - Welearn");
        return savedEvent;
    }

    public Event editWithoutEmail(Event event) throws WeLearnException {
        return editEventInternal(event);
    }

    private Event editEventInternal(Event event) throws WeLearnException {
        Event existingEvent = findById(event.getEventId());
//        checkOverlappingTimeRange(event);
        existingEvent.setDescription(event.getDescription());
        existingEvent.setDiscipline(event.getDiscipline());
        existingEvent.setGroup(event.getGroup());
        existingEvent.setStartDate(event.getStartDate());
        existingEvent.setEndDate(event.getEndDate());
        existingEvent.setBlacklist(event.getBlacklist());
        existingEvent.setType(event.getType());
        existingEvent.setName(event.getName());
        Event savedEvent = eventRepository.save(existingEvent);
        return savedEvent;
    }

    private void determineRecipientsAndSendEmails(Event savedEvent, String subject) {
        try {
            Discipline discipline = disciplineService.findOne(savedEvent.getDiscipline().getId());
            discipline.getGroup().forEach(it -> it.getUsers().forEach(user -> emailService.sendSimpleMessage(
                    user.getEmail(),
                    subject,
                    savedEvent.toString()
            )));
        } catch (WeLearnException ignored) {
            if (savedEvent.getGroup() != null) {
                savedEvent.getGroup().getUsers().forEach(user -> emailService.sendSimpleMessage(
                        user.getEmail(),
                        subject,
                        savedEvent.toString()
                ));
            } else {
                log.info("Event with id: " + savedEvent.getEventId() + " does not have valid email recipient candidates");
            }
        }
    }

    public void delete(String id) throws WeLearnException {
        determineRecipientsAndSendEmails(findById(id), "Event has been deleted - Welearn");
        deleteEventInternal(id);
    }

    public void deleteWithoutEmail(String id) throws WeLearnException {
        deleteEventInternal(id);
    }

    private void deleteEventInternal(String id) throws WeLearnException {
        Event existingEvent = findById(id);
        eventRepository.delete(existingEvent);
    }


    private void checkOverlappingTimeRange(Event event) throws WeLearnException {
        if (event.getStartDate() != null && event.getEndDate() != null && event.getGroup() != null) {
            if (!eventRepository.findByStartDateGreaterThanEqualAndEndDateLessThanEqualAndGroupEquals(
                    event.getStartDate(),
                    event.getEndDate(),
                    event.getGroup()
            ).isEmpty()) {
                throw new WeLearnException("Event cannot overlap in start and end time range for group "
                        + event.getGroup().getName()
                        + " with id: "
                        + event.getGroup().getGroupId());
            }
        }
    }
}

package uni.ruse.welearn.welearn.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    EventRepository eventRepository;

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
        if (event.getEventId() == null) {
            return eventRepository.save(event);
        }
        checkOverlappingTimeRange(event);
        throw new WeLearnException("New Event object cannot have an id");
    }

    public Event edit(Event event) throws WeLearnException {
        Event existingEvent = findById(event.getEventId());
        checkOverlappingTimeRange(event);
        existingEvent.setDescription(event.getDescription());
        existingEvent.setDiscipline(event.getDiscipline());
        existingEvent.setGroup(event.getGroup());
        existingEvent.setStartDate(event.getStartDate());
        existingEvent.setEndDate(event.getEndDate());
        existingEvent.setBlacklist(event.getBlacklist());
        existingEvent.setType(event.getType());
        existingEvent.setName(event.getName());
        return eventRepository.save(existingEvent);
    }

    public void delete(String id) throws WeLearnException {
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

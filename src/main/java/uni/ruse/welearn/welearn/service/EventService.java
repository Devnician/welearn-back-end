package uni.ruse.welearn.welearn.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.ruse.welearn.welearn.model.Event;
import uni.ruse.welearn.welearn.repository.EventRepository;
import uni.ruse.welearn.welearn.util.WeLearnException;

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
        if(optionalEvent.isEmpty()){
            throw new WeLearnException("Event with id " + id + " not found");
        }
        return optionalEvent.get();
    }
}

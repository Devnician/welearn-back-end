package uni.ruse.welearn.welearn.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uni.ruse.welearn.welearn.model.Event;
import uni.ruse.welearn.welearn.model.dto.EventDto;
import uni.ruse.welearn.welearn.service.DisciplineService;
import uni.ruse.welearn.welearn.service.EventService;
import uni.ruse.welearn.welearn.service.GroupService;
import uni.ruse.welearn.welearn.service.ResourceService;
import uni.ruse.welearn.welearn.service.UserService;
import uni.ruse.welearn.welearn.util.WeLearnException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ivelin.dimitrov
 */
@CrossOrigin(maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/event")
public class EventController {
    @Autowired
    private GroupService groupService;
    @Autowired
    private DisciplineService disciplineService;
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
    @Autowired
    private ResourceService resourceService;

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getById(
            @PathVariable String id
    ) throws WeLearnException {
        return new ResponseEntity<>(new EventDto(eventService.findById(id)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> findAll() {
        return new ResponseEntity<>(eventService.findAll().stream().map(EventDto::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EventDto> createEvent(
            @RequestBody @Valid EventDto eventDto
    ) throws WeLearnException {
        return new ResponseEntity<>(
                new EventDto(eventService.save(
                        new Event(eventDto, groupService, disciplineService, userService, eventService, resourceService)
                )), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<EventDto> editEvent(
            @RequestBody @Valid EventDto eventDto
    ) throws WeLearnException {
        return new ResponseEntity<>(
                new EventDto(eventService.edit(
                        new Event(eventDto, groupService, disciplineService, userService, eventService, resourceService)
                )), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteEvent(
            @PathVariable String id
    ) throws WeLearnException {
        eventService.delete(id);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}

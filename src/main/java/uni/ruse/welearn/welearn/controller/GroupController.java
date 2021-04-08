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
import uni.ruse.welearn.welearn.model.Group;
import uni.ruse.welearn.welearn.model.dto.GroupDto;
import uni.ruse.welearn.welearn.service.DisciplineService;
import uni.ruse.welearn.welearn.service.EventService;
import uni.ruse.welearn.welearn.service.GroupService;
import uni.ruse.welearn.welearn.service.ResourceService;
import uni.ruse.welearn.welearn.service.ScheduleService;
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
@RequestMapping("/api/group")
@Slf4j
public class GroupController {

    @Autowired
    private GroupService groupService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private DisciplineService disciplineService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<GroupDto>> findAll() {
        return new ResponseEntity<>(
                groupService.findAll().stream().map(GroupDto::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDto> findById(
            @PathVariable String id
    ) throws WeLearnException {
        return new ResponseEntity<>(
                new GroupDto(groupService.findOne(id)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GroupDto> saveGroup(
            @RequestBody @Valid GroupDto groupDto
    ) throws WeLearnException {
        return new ResponseEntity<>(
                new GroupDto(groupService.save(
                        new Group(groupDto, scheduleService, disciplineService, groupService, resourceService, userService, eventService)
                )), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<GroupDto> editGroup(
            @RequestBody @Valid GroupDto groupDto
    ) throws WeLearnException {
        return new ResponseEntity<>(
                new GroupDto(groupService.edit(
                        new Group(groupDto, scheduleService, disciplineService, groupService, resourceService, userService, eventService)
                )), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteGroup(
            @PathVariable String id
    ) throws WeLearnException {
        groupService.delete(id);
        return new ResponseEntity<>(
                true, HttpStatus.OK);
    }
}

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
import uni.ruse.welearn.welearn.model.Schedule;
import uni.ruse.welearn.welearn.model.dto.ScheduleDto;
import uni.ruse.welearn.welearn.service.DisciplineService;
import uni.ruse.welearn.welearn.service.GroupService;
import uni.ruse.welearn.welearn.service.ResourceService;
import uni.ruse.welearn.welearn.service.ScheduleService;
import uni.ruse.welearn.welearn.util.WeLearnException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ivelin.dimitrov
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/schedule")
@Slf4j
public class ScheduleController {
    @Autowired
    private GroupService groupService;
    @Autowired
    private DisciplineService disciplineService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<List<ScheduleDto>> findAll() {
        return new ResponseEntity<>(scheduleService.findAll().stream().map(ScheduleDto::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDto> findById(
            @PathVariable String id
    ) throws WeLearnException {
        return new ResponseEntity<>(new ScheduleDto(scheduleService.findById(id)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ScheduleDto> save(
            @RequestBody ScheduleDto scheduleDto
    ) throws WeLearnException {
        return new ResponseEntity<>(new ScheduleDto(scheduleService.save(new Schedule(scheduleDto, groupService, disciplineService, resourceService))), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ScheduleDto> edit(
            @RequestBody ScheduleDto scheduleDto
    ) throws WeLearnException {
        return new ResponseEntity<>(new ScheduleDto(scheduleService.edit(new Schedule(scheduleDto, groupService, disciplineService, resourceService))), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(
            @PathVariable String id
    ) throws WeLearnException {
        scheduleService.delete(id);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PostMapping("/generate/{id}")
    public ResponseEntity<Boolean> generateEvents(
            @PathVariable String id
    ) throws WeLearnException {
        scheduleService.generateEvents(id);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}

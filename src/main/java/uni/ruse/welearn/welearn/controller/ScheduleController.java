package uni.ruse.welearn.welearn.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uni.ruse.welearn.welearn.model.dto.ScheduleDto;
import uni.ruse.welearn.welearn.service.DisciplineService;
import uni.ruse.welearn.welearn.service.GroupService;
import uni.ruse.welearn.welearn.service.ResourceService;

/**
 * @author ivelin.dimitrov
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/schedule")
@Slf4j
public class ScheduleController {
    @Autowired
    private ScheduleDto scheduleDto;
    @Autowired
    private GroupService groupService;
    @Autowired
    private DisciplineService disciplineService;
    @Autowired
    private ResourceService resourceService;
}

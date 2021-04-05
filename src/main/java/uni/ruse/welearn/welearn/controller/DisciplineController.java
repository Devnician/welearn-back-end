package uni.ruse.welearn.welearn.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uni.ruse.welearn.welearn.model.Discipline;
import uni.ruse.welearn.welearn.model.auth.ApiResponse;
import uni.ruse.welearn.welearn.model.dto.DisciplineDto;
import uni.ruse.welearn.welearn.service.DisciplineService;
import uni.ruse.welearn.welearn.service.EventService;
import uni.ruse.welearn.welearn.service.GroupService;
import uni.ruse.welearn.welearn.service.ResourceService;
import uni.ruse.welearn.welearn.service.UserService;
import uni.ruse.welearn.welearn.util.WeLearnException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ivelin.dimitrov
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/discipline")
@Slf4j
public class DisciplineController {

    @Autowired
    private DisciplineService disciplineService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;

    @GetMapping
    public ApiResponse<List<DisciplineDto>> getDisciplines() {
        return new ApiResponse<>(HttpStatus.OK.value(), "All disciplines fetched successfully",
                disciplineService.getDisciplines().stream().map(DisciplineDto::new).collect(Collectors.toList()));
    }

    @GetMapping("/{disciplineId}")
    public ApiResponse<DisciplineDto> getDiscipline(@PathVariable String disciplineId) throws WeLearnException {
        return new ApiResponse<>(HttpStatus.OK.value(), "Discipline fetched successfully",
                new DisciplineDto(disciplineService.getDisciplineById(disciplineId)));
    }

    @PostMapping
    public ApiResponse<DisciplineDto> createDiscipline(@RequestBody DisciplineDto disciplineRequestDto) throws WeLearnException {
        return new ApiResponse<>(HttpStatus.OK.value(), "Discipline created successfully",
                new DisciplineDto(disciplineService.createDiscipline(
                        new Discipline(disciplineRequestDto, groupService, disciplineService, resourceService, userService, eventService)
                )));
    }

    @PutMapping
    public ApiResponse<DisciplineDto> editDiscipline(
            @RequestBody DisciplineDto disciplineResponseDto
    ) throws WeLearnException {
        return new ApiResponse<>(HttpStatus.OK.value(), "Group updated successfully",
                new DisciplineDto(disciplineService.editDiscipline(
                        new Discipline(disciplineResponseDto, groupService, disciplineService, resourceService, userService, eventService)
                )));
    }

    @DeleteMapping("/{disciplineId}")
    public ApiResponse<Boolean> removeDiscipline(
            @PathVariable String disciplineId
    ) throws WeLearnException {
        disciplineService.removeDiscipine(disciplineId);
        return new ApiResponse<>(HttpStatus.OK.value(), "Group removed successfully", true);
    }
}

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
import uni.ruse.welearn.welearn.model.EvaluationMark;
import uni.ruse.welearn.welearn.model.auth.ApiResponse;
import uni.ruse.welearn.welearn.model.dto.EvaluationMarkDto;
import uni.ruse.welearn.welearn.service.DisciplineService;
import uni.ruse.welearn.welearn.service.EvaluationMarkService;
import uni.ruse.welearn.welearn.service.GroupService;
import uni.ruse.welearn.welearn.service.UserService;
import uni.ruse.welearn.welearn.util.WeLearnException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ivelin.dimitrov
 */
@CrossOrigin(maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/evaluationMark")
public class EvaluationMarkController {

    @Autowired
    private EvaluationMarkService evaluationMarkService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private DisciplineService disciplineService;
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ApiResponse<EvaluationMarkDto> getById(
            @PathVariable String id
    ) throws WeLearnException {
        return new ApiResponse<>(HttpStatus.OK.value(), "Evaluation mark retrieved successfully", new EvaluationMarkDto(evaluationMarkService.findById(id)));
    }

    @GetMapping
    public ApiResponse<List<EvaluationMarkDto>> findAll() {
        return new ApiResponse<>(HttpStatus.OK.value(), "Marks list retrieved successfully", evaluationMarkService.findAll().stream().map(EvaluationMarkDto::new).collect(Collectors.toList()));
    }

    @PostMapping
    public ApiResponse<EvaluationMarkDto> createMark(
            @RequestBody EvaluationMarkDto evaluationMarkDto
    ) throws WeLearnException {
        return new ApiResponse<>(HttpStatus.OK.value(), "Mark saved successfully",
                new EvaluationMarkDto(evaluationMarkService.save(
                        new EvaluationMark(evaluationMarkDto, groupService, disciplineService, userService)
                )));
    }

    @PutMapping
    public ApiResponse<EvaluationMarkDto> editEvaluationMark(
            @RequestBody EvaluationMarkDto evaluationMarkDto
    ) throws WeLearnException {
        return new ApiResponse<>(HttpStatus.OK.value(), "Mark edited successfully",
                new EvaluationMarkDto(evaluationMarkService.edit(
                        new EvaluationMark(evaluationMarkDto, groupService, disciplineService, userService)
                )));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteMark(
            @PathVariable String id
    ) throws WeLearnException {
        evaluationMarkService.delete(id);
        return new ApiResponse<>(HttpStatus.OK.value(), "Mark deleted successfully", true);
    }
}

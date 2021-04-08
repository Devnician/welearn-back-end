package uni.ruse.welearn.welearn.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
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
import uni.ruse.welearn.welearn.model.EvaluationMark;
import uni.ruse.welearn.welearn.model.dto.EvaluationMarkDto;
import uni.ruse.welearn.welearn.service.DisciplineService;
import uni.ruse.welearn.welearn.service.EvaluationMarkService;
import uni.ruse.welearn.welearn.service.GroupService;
import uni.ruse.welearn.welearn.service.UserService;
import uni.ruse.welearn.welearn.util.WeLearnException;

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
    public ResponseEntity<EvaluationMarkDto> getById(
            @PathVariable String id
    ) throws WeLearnException {
        return new ResponseEntity<>(new EvaluationMarkDto(evaluationMarkService.findById(id)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EvaluationMarkDto>> findAll() {
        return new ResponseEntity<>(evaluationMarkService.findAll().stream().map(EvaluationMarkDto::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EvaluationMarkDto> createMark(
            @RequestBody @Valid EvaluationMarkDto evaluationMarkDto
    ) throws WeLearnException {
        return new ResponseEntity<>(
                new EvaluationMarkDto(evaluationMarkService.save(
                        new EvaluationMark(evaluationMarkDto, groupService, disciplineService, userService)
                )), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<EvaluationMarkDto> editEvaluationMark(
            @RequestBody @Valid EvaluationMarkDto evaluationMarkDto
    ) throws WeLearnException {
        return new ResponseEntity<>(
                new EvaluationMarkDto(evaluationMarkService.edit(
                        new EvaluationMark(evaluationMarkDto, groupService, disciplineService, userService)
                )), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteMark(
            @PathVariable String id
    ) throws WeLearnException {
        evaluationMarkService.delete(id);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}

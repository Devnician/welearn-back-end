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
import uni.ruse.welearn.welearn.model.Group;
import uni.ruse.welearn.welearn.model.auth.ApiResponse;
import uni.ruse.welearn.welearn.model.dto.GroupDto;
import uni.ruse.welearn.welearn.service.GroupService;
import uni.ruse.welearn.welearn.util.WeLearnException;

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


    @GetMapping
    public ApiResponse<List<GroupDto>> findAll() {
        return new ApiResponse<>(HttpStatus.OK.value(), "Groups successfully fetched",
                groupService.findAll().stream().map(GroupDto::new).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ApiResponse<GroupDto> findById(
            @PathVariable String id
    ) throws WeLearnException {
        return new ApiResponse<>(HttpStatus.OK.value(), "Group fetched successfully",
                new GroupDto(groupService.findOne(id)));
    }

    @PostMapping
    public ApiResponse<GroupDto> saveGroup(
            @RequestBody GroupDto groupDto
    ) throws WeLearnException {
        return new ApiResponse<>(HttpStatus.OK.value(), "Group saved successfully",
                new GroupDto(groupService.save(new Group(groupDto))));
    }

    @PutMapping
    public ApiResponse<GroupDto> editGroup(
            @RequestBody GroupDto groupDto
    ) throws WeLearnException {
        return new ApiResponse<>(HttpStatus.OK.value(), "Group edited successfully",
                new GroupDto(groupService.edit(new Group(groupDto))));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteGroup(
            @PathVariable String id
    ) throws WeLearnException {
        groupService.delete(id);
        return new ApiResponse<>(HttpStatus.OK.value(), "Group deleted successfully",
                true);
    }
}
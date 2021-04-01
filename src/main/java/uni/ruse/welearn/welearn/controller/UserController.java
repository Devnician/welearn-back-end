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
import uni.ruse.welearn.welearn.model.User;
import uni.ruse.welearn.welearn.model.auth.ApiResponse;
import uni.ruse.welearn.welearn.model.dto.UserDto;
import uni.ruse.welearn.welearn.service.DisciplineService;
import uni.ruse.welearn.welearn.service.EventService;
import uni.ruse.welearn.welearn.service.GroupService;
import uni.ruse.welearn.welearn.service.RoleService;
import uni.ruse.welearn.welearn.service.UserService;
import uni.ruse.welearn.welearn.util.WeLearnException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ivelin.dimitrov
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private EventService eventService;
    @Autowired
    private DisciplineService disciplineService;

    /**
     * Persist the user data
     *
     * @param user {@link User}
     * @return {@link User}
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ApiResponse<UserDto> saveUser(@RequestBody UserDto user) throws WeLearnException {
        return new ApiResponse<>(HttpStatus.OK.value(), "User added successfully",
                new UserDto(userService.addUser(new User(user, groupService, disciplineService, userService, eventService)))
        );
    }

    /**
     * Fetch all users
     *
     * @return {@link ApiResponse}
     */
    @GetMapping
    public ApiResponse<List<UserDto>> listUser() {
        return new ApiResponse<>(HttpStatus.OK.value(), "User list fetched successfully.",
                userService.findAllUsers().stream().map(UserDto::new).collect(Collectors.toList()));
    }

    /**
     * Fetch users by role id
     * @param id identifier of {@link uni.ruse.welearn.welearn.model.Role}
     * @return  {@link ApiResponse}
     */
    @GetMapping("/role/{id}")
    public ApiResponse<List<UserDto>> listUser(@PathVariable long id) {
        return new ApiResponse<>(HttpStatus.OK.value(), "User list fetched successfully.",
                userService.findAllUsersByRoleID(id).stream().map(UserDto::new).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDto> getUser(
            @PathVariable String id
    ) throws WeLearnException {
        return new ApiResponse<>(HttpStatus.OK.value(), "User retrieved successfully",
                new UserDto(userService.findUserById(id))
                );
    }

    /**
     * Saves information for logout
     *
     * @param id user id
     * @return {@link ApiResponse}
     */
    @GetMapping("/logout/{id}")
    public ApiResponse<UserDto> logout(
            @PathVariable String id
    ) throws WeLearnException {
        return new ApiResponse<>(HttpStatus.OK.value(), "Logged out successfully.", new UserDto(userService.logout(id)));
    }


    @PutMapping()
    public ApiResponse<UserDto> updateUser(
            @RequestBody UserDto userDto
    ) throws WeLearnException {
        return new ApiResponse<>(HttpStatus.OK.value(), "User updated successfully.", new UserDto(userService.updateUser(
                new User(userDto, groupService, disciplineService, userService, eventService)
        )));
    }

    @DeleteMapping("/{customer-id}")
    public ApiResponse<UserDto> deleteUser(
            @PathVariable String customerId
    ) throws WeLearnException {
        return new ApiResponse<>(HttpStatus.OK.value(), "User deleted successfully", new UserDto(userService.deleteUser(customerId)));
    }

}

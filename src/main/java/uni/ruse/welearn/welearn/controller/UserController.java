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
import uni.ruse.welearn.welearn.model.User;
import uni.ruse.welearn.welearn.model.dto.UserDto;
import uni.ruse.welearn.welearn.service.DisciplineService;
import uni.ruse.welearn.welearn.service.EventService;
import uni.ruse.welearn.welearn.service.GroupService;
import uni.ruse.welearn.welearn.service.RoleService;
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
    public ResponseEntity<UserDto> saveUser(@RequestBody @Valid UserDto user) throws WeLearnException {
        return new ResponseEntity<>(
                new UserDto(userService.addUser(new User(user, groupService, disciplineService, userService, eventService))), HttpStatus.OK
        );
    }

    /**
     * Fetch all users
     *
     * @return {@link ResponseEntity}
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> listUser() {
        return new ResponseEntity<>(
                userService.findAllUsers().stream().map(UserDto::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Fetch users by role id
     * @param id identifier of {@link uni.ruse.welearn.welearn.model.Role}
     * @return  {@link ResponseEntity}
     */
    @GetMapping("/role/{id}")
    public ResponseEntity<List<UserDto>> listUser(@PathVariable long id) {
        return new ResponseEntity<>(
                userService.findAllUsersByRoleID(id).stream().map(UserDto::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(
            @PathVariable String id
    ) throws WeLearnException {
        return new ResponseEntity<>(
                new UserDto(userService.findUserById(id)), HttpStatus.OK
                );
    }

    /**
     * Saves information for logout
     *
     * @param id user id
     * @return {@link ResponseEntity}
     */
    @GetMapping("/logout/{id}")
    public ResponseEntity<UserDto> logout(
            @PathVariable String id
    ) throws WeLearnException {
        return new ResponseEntity<>(new UserDto(userService.logout(id)), HttpStatus.OK);
    }


    @PutMapping()
    public ResponseEntity<UserDto> updateUser(
            @RequestBody @Valid UserDto userDto
    ) throws WeLearnException {
        return new ResponseEntity<>(new UserDto(userService.updateUser(
                new User(userDto, groupService, disciplineService, userService, eventService)
        )), HttpStatus.OK);
    }

    @DeleteMapping("/{customer-id}")
    public ResponseEntity<UserDto> deleteUser(
            @PathVariable String customerId
    ) throws WeLearnException {
        return new ResponseEntity<>(new UserDto(userService.deleteUser(customerId)), HttpStatus.OK);
    }

}

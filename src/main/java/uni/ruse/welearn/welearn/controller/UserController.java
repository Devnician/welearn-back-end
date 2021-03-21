package uni.ruse.welearn.welearn.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uni.ruse.welearn.welearn.model.Role;
import uni.ruse.welearn.welearn.model.User;
import uni.ruse.welearn.welearn.model.auth.ApiResponse;
import uni.ruse.welearn.welearn.model.dto.UserRequestDto;
import uni.ruse.welearn.welearn.model.dto.UserResponseDto;
import uni.ruse.welearn.welearn.services.RoleService;
import uni.ruse.welearn.welearn.services.UserService;
import uni.ruse.welearn.welearn.util.WeLearnException;

import java.util.List;

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

    /**
     * Persist the user data
     *
     * @param user {@link User}
     * @return {@link User}
     */
    @PostMapping
    public ApiResponse<UserResponseDto> saveUser(@RequestBody UserRequestDto user) throws WeLearnException {
        return new ApiResponse<>(HttpStatus.OK.value(), "User added successfully", userService.addUser(user));
    }

    /**
     * Fetch all users
     *
     * @return {@link ApiResponse}
     */
    @GetMapping
    public ApiResponse<List<UserResponseDto>> listUser() {
        return new ApiResponse<>(HttpStatus.OK.value(), "User list fetched successfully.", userService.findAllUsers());
    }

    /**
     * Saves information for logout
     *
     * @param id user id
     * @return {@link ApiResponse}
     */
    @GetMapping("/logout/{id}")
    public ApiResponse<User> logout(@PathVariable String id) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Logged out successfully.", userService.logout(id));
    }

    /**
     * Lists roles
     *
     * @return {@link ApiResponse}
     */
    @GetMapping("/roles")
    public ApiResponse<List<Role>> listRoles() {
        List<Role> roles = roleService.findAllRoles();
        return new ApiResponse<>(HttpStatus.OK.value(), "Role list fetched successfully.", roles);
    }

    /**
     * Adds new role into db
     *
     * @param role the {@link Role}
     * @return {@link ApiResponse}
     */
    @PostMapping("/roles/add")
    public ApiResponse<User> saveRole(@RequestBody Role role) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Role saved successfully.", roleService.saveRole(role));
    }
}

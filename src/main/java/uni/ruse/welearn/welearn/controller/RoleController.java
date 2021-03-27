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
import uni.ruse.welearn.welearn.model.Role;
import uni.ruse.welearn.welearn.model.auth.ApiResponse;
import uni.ruse.welearn.welearn.model.dto.RoleDto;
import uni.ruse.welearn.welearn.service.RoleService;
import uni.ruse.welearn.welearn.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ivelin.dimitrov
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/roles")
@Slf4j
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    /**
     * Lists roles
     *
     * @return {@link ApiResponse}
     */
    @GetMapping()
    public ApiResponse<List<RoleDto>> listRoles() {
        List<RoleDto> roles = roleService.findAllRoles().stream().map(RoleDto::new).collect(Collectors.toList());
        return new ApiResponse<>(HttpStatus.OK.value(), "Role list fetched successfully.", roles);
    }

    @GetMapping("/{id}")
    public ApiResponse<RoleDto> getRoleById(
            @PathVariable Long id
    ) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Role fetched succesfully",
                new RoleDto(roleService.findRoleById(id))
        );
    }

    /**
     * Adds new role into db
     *
     * @param role the {@link Role}
     * @return {@link ApiResponse}
     */
    @PostMapping("/add")
    public ApiResponse<RoleDto> saveRole(@RequestBody RoleDto role) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Role saved successfully.", new RoleDto(roleService.saveRole(
                new Role(role, userService)
        )));
    }

    @PutMapping
    public ApiResponse<RoleDto> updateRole(
            @RequestBody RoleDto roleDto
    ) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Role edited successfully", new RoleDto(roleService.updateRole(
                new Role(roleDto, userService)
        )));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteRole(
            @PathVariable Long id
    ) {
        roleService.deleteRoleById(id);
        return new ApiResponse<>(HttpStatus.OK.value(), "Role deleted successfully", true);
    }
}

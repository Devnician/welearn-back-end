package uni.ruse.welearn.welearn.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uni.ruse.welearn.welearn.model.Role;
import uni.ruse.welearn.welearn.model.auth.ApiResponse;
import uni.ruse.welearn.welearn.model.dto.RoleDto;
import uni.ruse.welearn.welearn.services.RoleService;

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

    /**
     * Adds new role into db
     *
     * @param role the {@link Role}
     * @return {@link ApiResponse}
     */
    @PostMapping("/add")
    public ApiResponse<RoleDto> saveRole(@RequestBody RoleDto role) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Role saved successfully.", new RoleDto(roleService.saveRole(new Role(role))));
    }
}

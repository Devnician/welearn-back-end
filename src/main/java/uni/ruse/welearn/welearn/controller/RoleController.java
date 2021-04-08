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
import uni.ruse.welearn.welearn.model.Role;
import uni.ruse.welearn.welearn.model.dto.RoleDto;
import uni.ruse.welearn.welearn.service.RoleService;
import uni.ruse.welearn.welearn.service.UserService;

import javax.validation.Valid;
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
     * @return {@link ResponseEntity}
     */
    @GetMapping()
    public ResponseEntity<List<RoleDto>> listRoles() {
        List<RoleDto> roles = roleService.findAllRoles().stream().map(RoleDto::new).collect(Collectors.toList());
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getRoleById(
            @PathVariable Long id
    ) {
        return new ResponseEntity<>(
                new RoleDto(roleService.findRoleById(id)), HttpStatus.OK
        );
    }

    /**
     * Adds new role into db
     *
     * @param role the {@link Role}
     * @return {@link ResponseEntity}
     */
    @PostMapping("/add")
    public ResponseEntity<RoleDto> saveRole(@RequestBody @Valid RoleDto role) {
        return new ResponseEntity<>(new RoleDto(roleService.saveRole(
                new Role(role, userService)
        )), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<RoleDto> updateRole(
            @RequestBody @Valid RoleDto roleDto
    ) {
        return new ResponseEntity<>(new RoleDto(roleService.updateRole(
                new Role(roleDto, userService)
        )), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteRole(
            @PathVariable Long id
    ) {
        roleService.deleteRoleById(id);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}

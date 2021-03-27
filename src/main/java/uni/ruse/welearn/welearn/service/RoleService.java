package uni.ruse.welearn.welearn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.ruse.welearn.welearn.model.Role;
import uni.ruse.welearn.welearn.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

/**
 * * Service that wires all related repositories and methods for processing and
 * retrieving information.
 *
 * @author petar ivanov
 */
@Service(value = "roleService")
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Persist role using {@link RoleRepository}
     *
     * @param role {@link Role}
     * @return {@link Role}
     */
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    /**
     * Fetches all {@link Role}
     *
     * @return Roles
     */
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * Delete row from db
     *
     * @param id the role id
     * @return 1 is succeed, 0 otherwise
     */
    public int deleteRoleById(long id) {
        if (id == 1) {
            return 0;
        }
        roleRepository.deleteById(id);
        return 1;
    }

    /**
     * Find by id
     *
     * @param id role id
     * @return {@link Role} or null
     */
    public Role findRoleById(long id) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        return optionalRole.orElse(null);
    }

    /**
     * Update {@link Role}
     *
     * @param role {@link Role}
     * @return {@link Role}
     */
    public Role updateRole(Role role) {
        return roleRepository.save(role);
    }

}

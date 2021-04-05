package uni.ruse.welearn.welearn.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import uni.ruse.welearn.welearn.model.Role;
import uni.ruse.welearn.welearn.model.User;
import uni.ruse.welearn.welearn.service.RoleService;
import uni.ruse.welearn.welearn.service.UserService;

/**
 * @author Ivelin Dimitrov
 */
@Configuration
@Slf4j
public class CreateAdmin {

    @Autowired
    UserService userService;
    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;
    @Autowired
    private RoleService roleService;

    @Bean
    public void createAdminUser() {
        if (roleService.findRoleByName("administrator") == null) {
            log.info("Creating administrator Role");
            Role role = new Role();
            role.setRole("administrator");
            role.setRoleBg("администратор");
            role.setDescription("administrator");
            role.setDescriptionBg("администрира системата");
            role.setPermissions("[[2,1,1,1,0]]");
            roleService.saveRole(role);
        } else {
            log.info("Role admin is already present");
        }
        if (userService.findOne("admin") == null) {
            log.info("Creating administrator User");
            User user = new User();
            user.setFirstName("Иван");
            user.setLastName("Иванов");
            user.setUsername("admin");
            user.setPassword(bcryptEncoder.encode("admin"));
            user.setRole(roleService.findRoleByName("administrator"));
            user.setEmail("example@email.com");
            userService.saveUser(user);
        } else {
            log.info("User admin is already present");
        }
    }
}

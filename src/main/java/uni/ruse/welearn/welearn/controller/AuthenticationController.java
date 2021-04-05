package uni.ruse.welearn.welearn.controller;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uni.ruse.welearn.welearn.model.Role;
import uni.ruse.welearn.welearn.model.User;
import uni.ruse.welearn.welearn.model.auth.ApiResponse;
import uni.ruse.welearn.welearn.model.auth.AuthToken;
import uni.ruse.welearn.welearn.model.auth.LoginUser;
import uni.ruse.welearn.welearn.service.RoleService;
import uni.ruse.welearn.welearn.service.UserService;
import uni.ruse.welearn.welearn.util.JwtTokenUtil;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/token")
@Slf4j
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    /**
     * Creates super user and gets administrator token.
     *
     * @return Token
     */
    @GetMapping("/test")
    public String getAdminToken() {
        // TODO comment this when finish!!
        return jwtTokenUtil.createAdminToken();
    }

    /**
     * Rest method for token generation
     *
     * @param loginUser {@link LoginUser}
     * @return {@link ApiResponse}
     * @throws AuthenticationException
     */
    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public ApiResponse<AuthToken> register(@RequestBody LoginUser loginUser) throws AuthenticationException {

        final User user = userService.findOne(loginUser.getUsername());

        if (user == null) {
            return new ApiResponse<>(HttpStatus.OK.value(), "wrong_user", null);
        } else {
            if (user.getDeleted() == 1) {
                log.info("Account is deleted. Please, contact with administrator.");
                return new ApiResponse<>(HttpStatus.OK.value(), "deleted", null);
            }

            String oldPass = user.getPassword();
            String loginPass = loginUser.getPassword();

            boolean isPassCorrect = bcryptEncoder.matches(loginPass, oldPass);
            if (!isPassCorrect) {
                return new ApiResponse<>(HttpStatus.OK.value(), "wrong_pass", null);
            }
        }

        Authentication re = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
        Role role = roleService.findRoleById(user.getRole().getId());

        final String token = jwtTokenUtil.generateToken(user, role);

        log.info("token sent for USER: " + user.getUserId() + " with role: " + role.getRole());

        userService.login(user);

        return new ApiResponse<>(200, "success", new AuthToken(
                token,
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getUserId(),
                user.getRole().getId()
        ));
    }

}

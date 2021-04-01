package uni.ruse.welearn.welearn.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import uni.ruse.welearn.welearn.model.Group;
import uni.ruse.welearn.welearn.model.Role;
import uni.ruse.welearn.welearn.model.User;
import uni.ruse.welearn.welearn.model.auth.ApiResponse;
import uni.ruse.welearn.welearn.repository.RoleRepository;
import uni.ruse.welearn.welearn.repository.UserRepository;
import uni.ruse.welearn.welearn.util.WeLearnException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service that wires all related repositories and methods for processing and
 * retrieving information.
 *
 * @author petar ivanov
 */
@Service(value = "userService")
@Slf4j
public class UserService implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private GroupService groupService;

    /**
     *
     */
    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new WeLearnException("Invalid username");
        }
        // String existPass = user.getPassword();
        // if(bcryptEncoder.matches(rawPassword, encodedPassword))
        return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(),
                getAuthority());
    }

    private List<SimpleGrantedAuthority> getAuthority() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    /**
     * Lists users
     *
     * @return List with users objects
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Lists users with given role id
     * @param roleId
     * @return List with users objects
     */
    public List<User> findAllUsersByRoleID(long roleId) {
        return userRepository.findAllByRoleId(roleId);
    }

    /**
     * Logical delete
     *
     * @param id which user
     * @return {@link User}
     */
    public User deleteUser(String id) throws WeLearnException {
        User user = findUserById(id);
        user.setDeleted(1);
        return userRepository.save(user);
    }

    /**
     * Finds user by username
     *
     * @param username criteria
     * @return {@link User}
     */
    public User findOne(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    /**
     * Finds user by id
     *
     * @param id
     * @return user or null
     */
    public User findUserById(String id) throws WeLearnException {
        Optional<User> optionalUser = userRepository.findByUserId(id);
        if (optionalUser.isEmpty()) {
            throw new WeLearnException("User not found");
        }
        return optionalUser.get();
    }

    /**
     * Saves {@link User} data
     *
     * @param user {@link User}
     */
    public void saveUser(User user) {
        userRepository.save(user);
    }

    /**
     * Updates user data
     *
     * @param user
     * @return
     */
    public User updateUser(User user) throws WeLearnException {
        User existingUser = findUserById(user.getUserId());
        if (existingUser != null) {
            if (user.getPassword() == null) {
                log.info("Password is not changed for " + user.getUserId());
            } else {
                log.info("Changed pass " + user.getPassword());
                existingUser.setPassword(bcryptEncoder.encode(user.getPassword()));
            }
            if (!user.getEmail().isBlank()) {
                existingUser.setEmail(user.getEmail());
            }
            if (!user.getAddress().isBlank()) {
                existingUser.setAddress(user.getAddress());
            }
            if (user.getBirthdate() != null) {
                existingUser.setBirthdate(user.getBirthdate());
            }
            if (!user.getPhoneNumber().isBlank()) {
                existingUser.setPhoneNumber(user.getPhoneNumber());
            }
            if (user.getRole() != null) {
                Optional<Role> role = roleRepository.findById(user.getRole().getId());
                if (role.isEmpty()) {
                    throw new WeLearnException("Role not found");
                } else {
                    existingUser.setRole(role.get());
                }
            }
            if(user.getGroup() != null){
                Group existingGroup = groupService.findOne(user.getGroup().getGroupId());
                existingUser.setGroup(existingGroup);
            }
            existingUser.setFirstName(user.getFirstName());
            existingUser.setMiddleName(user.getMiddleName());
            existingUser.setLastName(user.getLastName());

            existingUser = userRepository.save(existingUser);
        }
        return existingUser;
    }

    /**
     * Adds {@link User} and builds response for frontend
     *
     * @param user {@link User}
     * @return {@link ApiResponse}
     */
    public User addUser(User user) throws WeLearnException {
        if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
            user.setPassword(bcryptEncoder.encode(user.getPassword()));
            user.setDeleted(0);
            user.setLoggedIn(0);
            return userRepository.save(user);
        } else {
            throw new WeLearnException("User already exists");
        }
    }

    /**
     * Marks user as logged out.
     *
     * @param id user id
     * @return 1 if succesfull
     */
    public User logout(String id) throws WeLearnException {
        User user = findUserById(id);
        user.setLoggedIn(0);
        return userRepository.save(user);
    }

    /**
     * Saves log in data
     *
     * @param user
     */
    public void login(User user) {
        user.setLoggedIn(1);
        userRepository.save(user);

    }

}

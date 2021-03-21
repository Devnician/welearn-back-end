package uni.ruse.welearn.welearn.services;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import uni.ruse.welearn.welearn.model.User;
import uni.ruse.welearn.welearn.model.auth.ApiResponse;
import uni.ruse.welearn.welearn.model.dto.UserRequestDto;
import uni.ruse.welearn.welearn.model.dto.UserResponseDto;
import uni.ruse.welearn.welearn.repository.RoleRepository;
import uni.ruse.welearn.welearn.repository.UserRepository;
import uni.ruse.welearn.welearn.util.WeLearnException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service that wires all related repositories and methods for processing and
 * retrieving information.
 *
 * @author petar ivanov
 */
@Service(value = "userService")
public class UserService implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

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
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    /**
     * Lists users
     *
     * @return List with users objects
     */
    public List<UserResponseDto> findAllUsers() {
        return userRepository.findAll().stream().map(UserResponseDto::new).collect(Collectors.toList());
    }

    /**
     * Logical delete
     *
     * @param id which user
     * @return {@link User}
     */
    public User deleteUser(String id) {
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
    public User findUserById(String id) {
        Optional<User> optionalUser = userRepository.findByUserId(id);
        return optionalUser.orElse(null);
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
     * @param userDto
     * @return
     */
    public User updateUser(User userDto) {
        User user = findUserById(userDto.getUserId());
        if (user != null) {


            if (userDto.getPassword() == null) {
                System.out.println("Pass is the same..");
            } else {
                System.out.println("Changed pass " + userDto.getPassword());
                user.setPassword(bcryptEncoder.encode(userDto.getPassword()));
            }

            userRepository.save(user);
        }
        return userDto;
    }

    /**
     * Adds {@link User} and build sresponse for frontend
     *
     * @param userRequestDto {@link User}
     * @return {@link ApiResponse}
     */
    public UserResponseDto addUser(UserRequestDto userRequestDto) throws WeLearnException {
        if (userRepository.findByUsername(userRequestDto.getUsername()).isEmpty()) {
            User newUser = new User(userRequestDto, roleRepository);
            newUser.setPassword(bcryptEncoder.encode(userRequestDto.getPassword()));
            newUser.setDeleted(0);
            newUser.setLoggedIn(0);

            newUser = userRepository.save(newUser);

            return new UserResponseDto(newUser);
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
    public int logout(String id) {
        User u = findUserById(id);
        if (u == null) {
            return 0;
        } else {
            u.setLoggedIn(0);
            u = userRepository.save(u);
            return 1;
        }
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

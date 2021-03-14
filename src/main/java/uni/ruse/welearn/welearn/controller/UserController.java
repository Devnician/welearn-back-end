package uni.ruse.welearn.welearn.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uni.ruse.welearn.welearn.model.User;
import uni.ruse.welearn.welearn.repository.UserRepository;

import java.util.List;

/**
 * @author ivelin.dimitrov
 */
@Controller
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<User>> findAllUsers() {
        userRepository.findAll().forEach(it -> log.info(it.toString()));
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }
}

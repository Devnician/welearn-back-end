package uni.ruse.welearn.welearn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.ruse.welearn.welearn.repository.UserRepository;

import java.util.List;

/**
 * @author ivelin.dimitrov
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

}

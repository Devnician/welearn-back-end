package uni.ruse.welearn.welearn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.Discipline;
import uni.ruse.welearn.welearn.model.EvaluationMark;
import uni.ruse.welearn.welearn.model.Event;
import uni.ruse.welearn.welearn.model.Group;
import uni.ruse.welearn.welearn.model.Role;
import uni.ruse.welearn.welearn.model.User;

import java.util.Set;

/**
 * @author ivelin.dimitrov
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String address;
    private String birthdate;
    private String phoneNumber;
    private String middleName;
    private int loggedIn;
    private int deleted;
    private Group group;
    private Set<EvaluationMark> evaluationMarks;
    private Role role;
    private Set<Event> blacklistedEvents;
    private Discipline taughtDiscipline;
    private Discipline assistedDiscipline;

    public UserDto(User user) {
        BeanUtils.copyProperties(user, this);
    }
}

package uni.ruse.welearn.welearn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.Discipline;
import uni.ruse.welearn.welearn.model.Event;
import uni.ruse.welearn.welearn.model.User;

import java.util.Set;
import java.util.stream.Collectors;

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
    private String groupId;
    private Set<EvaluationMarkDto> evaluationMarks;
    private RoleDto role;
    private Set<String> taughtDisciplineIds;
    private Set<String> assistedDisciplineIds;
    private Set<String> blackListedEventIds;

    public UserDto(User user) {
        if (user != null) {
            BeanUtils.copyProperties(user, this);
            password = null;
            if (user.getGroup() != null) {
                groupId = user.getGroup().getGroupId();
            }
            if (user.getEvaluationMarks() != null) {
                evaluationMarks = user.getEvaluationMarks().stream().map(EvaluationMarkDto::new).collect(Collectors.toSet());
            }
            role = new RoleDto(user.getRole());
            if (user.getTaughtDiscipline() != null) {
                taughtDisciplineIds = user.getTaughtDiscipline().stream().map(Discipline::getId).collect(Collectors.toSet());
            }
            if (user.getAssistedDiscipline() != null) {
                assistedDisciplineIds = user.getAssistedDiscipline().stream().map(Discipline::getId).collect(Collectors.toSet());
            }
            if (user.getBlacklistedEvents() != null) {
                blackListedEventIds = user.getBlacklistedEvents().stream().map(Event::getEventId).collect(Collectors.toSet());
            }
        }
    }
}

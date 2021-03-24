package uni.ruse.welearn.welearn.model.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
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
    @JsonBackReference("user-group")
    private GroupDto group;
    @JsonManagedReference("user-mark")
    private Set<EvaluationMarkDto> evaluationMarks;
    @JsonBackReference("user-role")
    private RoleDto role;
    @JsonManagedReference("teacher-discipline")
    private Set<DisciplineDto> taughtDiscipline;
    @JsonManagedReference("assistant-discipline")
    private Set<DisciplineDto> assistedDiscipline;

    public UserDto(User user) {
        if (user != null) {
            BeanUtils.copyProperties(user, this);
            password = null;
//            group = new GroupDto(user.getGroup());
            evaluationMarks = user.getEvaluationMarks().stream().map(EvaluationMarkDto::new).collect(Collectors.toSet());
            role = new RoleDto(user.getRole());
//            taughtDiscipline = user.getTaughtDiscipline().stream().map(DisciplineDto::new).collect(Collectors.toSet());
//            assistedDiscipline = user.getAssistedDiscipline().stream().map(DisciplineDto::new).collect(Collectors.toSet());
        }
    }
}

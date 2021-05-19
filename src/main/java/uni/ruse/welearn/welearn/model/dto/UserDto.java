package uni.ruse.welearn.welearn.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.Discipline;
import uni.ruse.welearn.welearn.model.Event;
import uni.ruse.welearn.welearn.model.User;

/**
 * @author ivelin.dimitrov
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String userId;
    @NotBlank(message = "Email is mandatory")
    @Pattern(regexp = "^[^@\\s]+@[^@\\s\\.]+\\.[^@\\.\\s]+$", message = "Email is invalid")
    private String email;
    @NotBlank(message = "First name is mandatory")
    @Size(min = 2, max = 30, message = "First name must be between 2 and 30 symbols long")
    @Pattern(regexp = "([а-яА-Я]{2,})|([a-zA-Z]{2,})", message = "The name contains forbidden symbols")
    private String firstName;
    @NotBlank(message = "Last name is mandatory")
    @Size(min = 2, max = 30, message = "Last name must be between 2 and 30 symbols long")
    @Pattern(regexp = "([а-яА-Я]{2,})|([a-zA-Z]{2,})", message = "The name contains forbidden symbols")
    private String lastName;
    @NotBlank(message = "Username is mandatory")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 symbols long")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username field accepts only letters and numbers.")
    @Column(unique = true)
    private String username;
    private String password;
    @Size(min = 2, max = 45, message = "Address length must be between 2 and 45 symbols")
    private String address;
    @NotNull(message = "Birthdate is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp birthdate;
    @Pattern(regexp = "^(\\+?359\\d{9})|(0\\d{9})$", message = "Phone number is not correct")
    private String phoneNumber;
    @Size(min = 2, max = 30, message = "Middle name must be between 2 and 30 symbols long")
    @Pattern(regexp = "([а-яА-Я]{2,})|([a-zA-Z]{2,})", message = "The middle name contains forbidden symbols")
    private String middleName;
    @Column(columnDefinition = "integer default 0")
    private int loggedIn;
    private int deleted;
    private String groupId;
    private Set<EvaluationMarkDto> evaluationMarks;
    @NotNull(message = "Role is mandatory")
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

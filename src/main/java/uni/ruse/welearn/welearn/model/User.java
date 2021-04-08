package uni.ruse.welearn.welearn.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.dto.UserDto;
import uni.ruse.welearn.welearn.service.DisciplineService;
import uni.ruse.welearn.welearn.service.EventService;
import uni.ruse.welearn.welearn.service.GroupService;
import uni.ruse.welearn.welearn.service.UserService;
import uni.ruse.welearn.welearn.util.AuditedClass;
import uni.ruse.welearn.welearn.util.WeLearnException;

/**
 * @author Ivelin Dimitrov
 */
@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends AuditedClass {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "uuid",
            strategy = "uuid2"
    )
    private String userId;
    @Column(unique = true)
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
    @NotBlank(message = "Password is mandatory")
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

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonBackReference
    private Group group;

    @OneToMany(mappedBy = "user")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private Set<EvaluationMark> evaluationMarks;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonBackReference
    @NotNull(message = "Role is mandatory")
    @Valid
    private Role role;

    @ManyToMany(mappedBy = "blacklist")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Event> blacklistedEvents;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private Set<Discipline> taughtDiscipline;

    @OneToMany(mappedBy = "assistant", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private Set<Discipline> assistedDiscipline;

    public User(
            UserDto userDto,
            GroupService groupService,
            DisciplineService disciplineService,
            UserService userService,
            EventService eventService
    ) throws WeLearnException {
        if (userDto != null) {
            BeanUtils.copyProperties(userDto, this);
            if (userDto.getBlackListedEventIds() != null) {
                blacklistedEvents = userDto.getBlackListedEventIds().stream().map(it -> {
                    try {
                        return eventService.findById(it);
                    } catch (WeLearnException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toSet());
            }
            role = new Role(userDto.getRole(), userService);
            if (userDto.getGroupId() != null) {
                group = groupService.findOne(userDto.getGroupId());
            }
            if (userDto.getEvaluationMarks() != null) {
                evaluationMarks = userDto.getEvaluationMarks().stream().map(it -> {
                    try {
                        return new EvaluationMark(it, groupService, disciplineService, userService);
                    } catch (WeLearnException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toSet());
            }
            if (userDto.getTaughtDisciplineIds() != null) {
                taughtDiscipline = userDto.getTaughtDisciplineIds().stream().map(disciplineId -> {
                    try {
                        return disciplineService.getDisciplineById(disciplineId);
                    } catch (WeLearnException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toSet());
            }
            if (userDto.getAssistedDisciplineIds() != null) {
                assistedDiscipline = userDto.getAssistedDisciplineIds().stream().map(disciplineId -> {
                    try {
                        return disciplineService.getDisciplineById(disciplineId);
                    } catch (WeLearnException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toSet());
            }
        }
    }
}

package uni.ruse.welearn.welearn.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.stream.Collectors;

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
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String address;
    private String birthdate;
    private String phoneNumber;
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

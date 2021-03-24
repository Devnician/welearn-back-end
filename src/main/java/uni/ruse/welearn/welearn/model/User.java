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
import uni.ruse.welearn.welearn.util.AuditedClass;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Set;

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
    @JsonManagedReference
    private Group group;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<EvaluationMark> evaluationMarks;

    @OneToOne
    @JoinColumn(name = "role_id")
    @JsonManagedReference
    private Role role;

    @ManyToMany(mappedBy = "blacklist")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private Set<Event> blacklistedEvents;

    @OneToOne
    @JoinColumn(name = "teacher_id")
    @JsonBackReference
    private Discipline taughtDiscipline;

    @OneToOne
    @JoinColumn(name = "assistant_id")
    @JsonBackReference
    private Discipline assistedDiscipline;

    public User(UserDto userDto) {
        BeanUtils.copyProperties(userDto, this);
    }
}

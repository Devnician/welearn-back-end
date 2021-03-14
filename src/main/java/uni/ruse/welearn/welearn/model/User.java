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
import uni.ruse.welearn.welearn.enums.Role;
import uni.ruse.welearn.welearn.util.AuditedClass;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonManagedReference
    private Group group;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<EvaluationMark> evaluationMarks;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(mappedBy = "blacklist")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private Set<Event> event;

    @Override
    public String toString() {
        return "\nUser{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", evaluationMarks=" + evaluationMarks +
                ", role=" + role +
                '}';
    }
}

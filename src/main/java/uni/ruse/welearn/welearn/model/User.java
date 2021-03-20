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
import uni.ruse.welearn.welearn.model.dto.UserRequestDto;
import uni.ruse.welearn.welearn.repository.RoleRepository;
import uni.ruse.welearn.welearn.util.AuditedClass;
import uni.ruse.welearn.welearn.util.WeLearnException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Optional;
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

    public User(UserRequestDto userRequestDto, RoleRepository roleRepository) throws WeLearnException {
        Optional<Role> role = roleRepository.findById(userRequestDto.getRoleId());
        if (role.isPresent()) {
            BeanUtils.copyProperties(userRequestDto, this);
            this.setRole(role.get());
        } else {
            throw new WeLearnException("Role is not found");
        }
    }
}

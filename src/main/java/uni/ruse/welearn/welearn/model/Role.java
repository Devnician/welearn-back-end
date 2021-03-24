package uni.ruse.welearn.welearn.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.dto.RoleDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Model for role that conatins labels and transient List with permissions
 *
 * @author petar ivanov
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private long id;

    private String role;

    private String roleBg;

    private String description;

    private String descriptionBg;

    private String permissions;

    @OneToMany(mappedBy = "role")
    @JsonManagedReference
    private Set<User> user;

    public Role(RoleDto roleDto) {
        if (roleDto != null) {
            BeanUtils.copyProperties(roleDto, this);
            user = roleDto.getUser().stream().map(User::new).collect(Collectors.toSet());
        }
    }
}

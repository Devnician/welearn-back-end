package uni.ruse.welearn.welearn.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.dto.RoleDto;
import uni.ruse.welearn.welearn.service.UserService;
import uni.ruse.welearn.welearn.util.WeLearnException;

/**
 * Model for role that conatins labels and transient List with permissions
 *
 * @author petar ivanov
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
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

    @NotBlank(message = "Role is mandatory")
    @Size(min = 3, max = 30, message = "Role may be between 3 and 30 symbols long")
    @Column(unique = true)
    private String role;

    @Size(min = 3, max = 30, message = "RoleBg may be between 3 and 30 symbols long")
    private String roleBg;

    @NotBlank(message = "Description is mandatory")
    @Size(min = 3, max = 30, message = "Description may be between 3 and 30 symbols long")
    private String description;

    @Size(min = 3, max = 30, message = "DescriptionBg may be between 3 and 30 symbols long")
    private String descriptionBg;

    private String permissions;

    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<User> user;

    public Role(
            RoleDto roleDto,
            UserService userService
    ) {
        if (roleDto != null) {
            BeanUtils.copyProperties(roleDto, this);
            if (roleDto.getUserId() != null) {
                user = roleDto.getUserId().stream().map(it -> {
                    try {
                        return userService.findUserById(it);
                    } catch (WeLearnException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toSet());
            }
        }
    }
}

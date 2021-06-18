package uni.ruse.welearn.welearn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.Role;
import uni.ruse.welearn.welearn.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ivelin.dimitrov
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDto {
    private long id;
    @NotBlank(message = "Role is mandatory")
    @Size(min = 3, max = 30, message = "Role may be between 3 and 30 symbols long")
    private String role;
    @Size(min = 3, max = 30, message = "RoleBg may be between 3 and 30 symbols long")
    private String roleBg;
    @NotBlank(message = "Description is mandatory")
    @Size(min = 3, max = 30, message = "Description may be between 3 and 30 symbols long")
    private String description;
    @Size(min = 3, max = 30, message = "DescriptionBg may be between 3 and 30 symbols long")
    private String descriptionBg;
    private String permissions;
    private Set<String> userId;

    public RoleDto(Role role) {
        if (role != null) {
            BeanUtils.copyProperties(role, this);
            if (role.getUser() != null) {
                userId = role.getUser().stream().map(User::getUserId).collect(Collectors.toSet());
            }
        }
    }
}

package uni.ruse.welearn.welearn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.Role;
import uni.ruse.welearn.welearn.model.User;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ivelin.dimitrov
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    private long id;
    private String role;
    private String roleBg;
    private String description;
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

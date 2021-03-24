package uni.ruse.welearn.welearn.model.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.Role;

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
    @JsonManagedReference("user-role")
    private Set<UserDto> user;

    public RoleDto(Role role) {
        if (role != null) {
            BeanUtils.copyProperties(role, this);
//            user = role.getUser().stream().map(UserDto::new).collect(Collectors.toSet());
        }
    }
}

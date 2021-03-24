package uni.ruse.welearn.welearn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.Role;

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

    public RoleDto(Role role) {
        BeanUtils.copyProperties(role, this);
    }
}

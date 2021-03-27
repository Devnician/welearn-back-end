package uni.ruse.welearn.welearn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.Resource;

/**
 * @author ivelin.dimitrov
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResourceDto {
    private String resourceId;
    private String name;
    private String type;
    private String dirPath;
    private Boolean accessibleAll;
    private String groupId;
    private String disciplineId;
    private String scheduleId;

    public ResourceDto(Resource resource) {
        if (resource != null) {
            BeanUtils.copyProperties(resource, this);
            if (resource.getGroup() != null) {
                groupId = resource.getGroup().getGroupId();
            }
            if (resource.getDiscipline() != null) {
                disciplineId = resource.getDiscipline().getId();
            }
            if (resource.getSchedule() != null) {
                scheduleId = resource.getSchedule().getId();
            }
        }
    }
}

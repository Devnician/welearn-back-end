package uni.ruse.welearn.welearn.model.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference("resource-group")
    private GroupDto group;
    @JsonBackReference("resource-discipline")
    private DisciplineDto discipline;
    @JsonBackReference("schedule-resource")
    private ScheduleDto schedule;

    public ResourceDto(Resource resource) {
        if (resource != null) {
            BeanUtils.copyProperties(resource, this);
//            group = new GroupDto(resource.getGroup());
//            discipline = new DisciplineDto(resource.getDiscipline());
//            schedule = new ScheduleDto(resource.getSchedule());
        }
    }
}

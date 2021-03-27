package uni.ruse.welearn.welearn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.Resource;
import uni.ruse.welearn.welearn.model.Schedule;

import java.sql.Time;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ivelin.dimitrov
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDto {
    private String id;
    private Time startTime;
    private Time endTime;
    private String groupId;
    private String disciplineId;
    private Set<String> resourceIds;

    public ScheduleDto(Schedule schedule) {
        if (schedule != null) {
            BeanUtils.copyProperties(schedule, this);
            if (schedule.getGroup() != null) {
                groupId = schedule.getGroup().getGroupId();
            }
            if (schedule.getDiscipline() != null) {
                disciplineId = schedule.getDiscipline().getId();
            }
            if (schedule.getResources() != null) {
                resourceIds = schedule.getResources().stream().map(Resource::getResourceId).collect(Collectors.toSet());
            }
        }
    }
}

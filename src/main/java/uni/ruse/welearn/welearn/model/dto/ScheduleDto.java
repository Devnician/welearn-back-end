package uni.ruse.welearn.welearn.model.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
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
    @JsonBackReference("schedule-group")
    private GroupDto group;
    private DisciplineDto discipline;
    @JsonManagedReference("schedule-resource")
    private Set<ResourceDto> resources;

    public ScheduleDto(Schedule schedule) {
        if (schedule != null) {
            BeanUtils.copyProperties(schedule, this);
//            group = new GroupDto(schedule.getGroup());
//            discipline = new DisciplineDto(schedule.getDiscipline());
//            resources = schedule.getResources().stream().map(ResourceDto::new).collect(Collectors.toSet());
        }
    }
}

package uni.ruse.welearn.welearn.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.Resource;
import uni.ruse.welearn.welearn.model.Schedule;
import uni.ruse.welearn.welearn.util.TimeDeserializer;

/**
 * @author ivelin.dimitrov
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDto {
    private String id;
    @JsonDeserialize(using = TimeDeserializer.class)
    @JsonFormat(pattern = "HH:mm")
    private Time startHour;
    @JsonDeserialize(using = TimeDeserializer.class)
    @JsonFormat(pattern = "HH:mm")
    private Time endHour;
    @NotBlank(message = "Group is mandatory")
    private String groupId;
    @NotBlank(message = "Discipline is mandatory")
    private String disciplineId;
    private Set<String> resourceIds;
    @NotNull(message = "Days is mandatory")
    private String dayOfWeek;
    @NotNull(message = "start date is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp startDate;
    @NotNull(message = "start date is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp endDate;

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

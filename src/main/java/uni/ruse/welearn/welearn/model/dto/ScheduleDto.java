package uni.ruse.welearn.welearn.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.Schedule;
import uni.ruse.welearn.welearn.util.TimeDeserializer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * @author ivelin.dimitrov
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
        }
    }
}

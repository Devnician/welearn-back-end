package uni.ruse.welearn.welearn.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.Group;

/**
 * @author ivelin.dimitrov
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {
    private Set<UserDto> users;
    private Set<ScheduleDto> schedules;
    private Set<EventDto> events;
    private Set<DisciplineDto> disciplines;
    private Set<ResourceDto> resources;
    private String groupId;
    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 45, message = "Name may be between 2 and 45 symbols long")
    @Pattern(regexp = "([а-яА-Я\\s]{2,})|([a-zA-Z\\s]{2,})", message = "Name is invalid, it may contain only letters")
    private String name;
    @Size(min = 2, max = 45, message = "Description may be between 2 and 45 symbols long")
    @Pattern(regexp = "([а-яА-Я\\s]{2,})|([a-zA-Z\\s]{2,})", message = "Description is invalid, it may contain only letters")
    private String description;
    @NotNull(message = "Max resources is mandatory")
    private Integer maxResourcesMb;
    @NotNull(message = "start date is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp startDate;
    @NotNull(message = "start date is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp endDate;

    public GroupDto(Group group) {
        if (group != null) {
            BeanUtils.copyProperties(group, this);
            if (group.getUsers() != null) {
                users = group.getUsers().stream().map(UserDto::new).collect(Collectors.toSet());
            }
            if (group.getSchedules() != null) {
                schedules = group.getSchedules().stream().map(ScheduleDto::new).collect(Collectors.toSet());
            }
            if (group.getEvents() != null) {
                events = group.getEvents().stream().map(EventDto::new).collect(Collectors.toSet());
            }
            if (group.getDisciplines() != null) {
                disciplines = group.getDisciplines().stream().map(DisciplineDto::new).collect(Collectors.toSet());
            }
            if (group.getResources() != null) {
                resources = group.getResources().stream().map(ResourceDto::new).collect(Collectors.toSet());
            }
        }
    }
}

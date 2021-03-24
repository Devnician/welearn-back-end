package uni.ruse.welearn.welearn.model.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.Group;

import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ivelin.dimitrov
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {
    @JsonManagedReference("user-group")
    private Set<UserDto> users;
    @JsonManagedReference("schedule-group")
    private Set<ScheduleDto> schedules;
    @JsonManagedReference("event-group")
    private Set<EventDto> events;
    private Set<DisciplineDto> disciplines;
    @JsonManagedReference("resource-group")
    private Set<ResourceDto> resources;
    private String groupId;
    private String name;
    private String description;
    private Integer maxResourcesMb;
    private Timestamp startDate;
    private Timestamp endDate;

    public GroupDto(Group group) {
        if(group != null) {
            BeanUtils.copyProperties(group, this);
            users = group.getUsers().stream().map(UserDto::new).collect(Collectors.toSet());
            schedules = group.getSchedules().stream().map(ScheduleDto::new).collect(Collectors.toSet());
            events = group.getEvents().stream().map(EventDto::new).collect(Collectors.toSet());
            disciplines = group.getDisciplines().stream().map(DisciplineDto::new).collect(Collectors.toSet());
            resources = group.getResources().stream().map(ResourceDto::new).collect(Collectors.toSet());
        }
    }
}

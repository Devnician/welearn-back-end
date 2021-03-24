package uni.ruse.welearn.welearn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.Discipline;
import uni.ruse.welearn.welearn.model.Event;
import uni.ruse.welearn.welearn.model.Group;
import uni.ruse.welearn.welearn.model.Resource;
import uni.ruse.welearn.welearn.model.Schedule;
import uni.ruse.welearn.welearn.model.User;

import java.sql.Timestamp;
import java.util.Set;

/**
 * @author ivelin.dimitrov
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {
    private Set<User> users;
    private Set<Schedule> schedules;
    private Set<Event> events;
    private Set<Discipline> disciplines;
    private Set<Resource> resources;
    private String groupId;
    private String name;
    private String description;
    private Integer maxResourcesMb;
    private Timestamp startDate;
    private Timestamp endDate;

    public GroupDto(Group group) {
        BeanUtils.copyProperties(group, this);
    }
}

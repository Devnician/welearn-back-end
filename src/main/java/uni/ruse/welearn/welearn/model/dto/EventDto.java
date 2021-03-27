package uni.ruse.welearn.welearn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.Event;

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
public class EventDto {
    private String eventId;
    private String name;
    private Timestamp startDate;
    private Timestamp endDate;
    private String type;
    private String groupId;
    private Set<UserDto> blacklist;

    public EventDto(Event event) {
        if (event != null) {
            BeanUtils.copyProperties(event, this);
            if (event.getGroup() != null) {
                groupId = event.getGroup().getGroupId();
            }
            if (event.getBlacklist() != null) {
                blacklist = event.getBlacklist().stream().map(UserDto::new).collect(Collectors.toSet());
            }
        }
    }
}

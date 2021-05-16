package uni.ruse.welearn.welearn.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.Event;

/**
 * @author ivelin.dimitrov
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private String eventId;
    @NotBlank
    @Size(min = 2, max = 30, message = "Name field may be between 2 and 30 symbols long")
    private String name;
    @NotNull(message = "Start date is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp startDate;
    @NotNull(message = "Start date is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp endDate;
    @NotNull(message = "Type is mandatory")
    private String type;
    private String description;
    private String groupId;
    private DisciplineDto discipline;
    private Set<UserDto> blacklist;

    public EventDto(Event event) {
        if (event != null) {
            BeanUtils.copyProperties(event, this);
            if (event.getGroup() != null) {
                groupId = event.getGroup().getGroupId();
            }
            if (event.getDiscipline() != null) {
                discipline = new DisciplineDto(event.getDiscipline());
            }
            if (event.getBlacklist() != null) {
                blacklist = event.getBlacklist().stream().map(UserDto::new).collect(Collectors.toSet());
            }
        }
    }
}

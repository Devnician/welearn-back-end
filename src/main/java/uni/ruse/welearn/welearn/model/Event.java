package uni.ruse.welearn.welearn.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.dto.EventDto;
import uni.ruse.welearn.welearn.service.DisciplineService;
import uni.ruse.welearn.welearn.service.EventService;
import uni.ruse.welearn.welearn.service.GroupService;
import uni.ruse.welearn.welearn.service.ResourceService;
import uni.ruse.welearn.welearn.service.UserService;
import uni.ruse.welearn.welearn.util.AuditedClass;
import uni.ruse.welearn.welearn.util.WeLearnException;

/**
 * @author Ivelin Dimitrov
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event extends AuditedClass {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "uuid",
            strategy = "uuid2"
    )
    private String eventId;
    @NotBlank
    @Size(min = 2, max = 30, message = "Name field may be between 2 and 30 symbols long")
    private String name;
    @NotNull(message = "Start date is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Timestamp startDate;
    @NotNull(message = "Start date is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Timestamp endDate;
    @NotNull(message = "Type is mandatory")
    private String type;
    private String description;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonBackReference
    private Group group;

    @OneToOne
    @JoinColumn(name = "discipline_id", referencedColumnName = "id")
    @NotNull(message = "Discipline is mandatory")
    private Discipline discipline;

    @ManyToMany
    @JoinTable(
            name = "blacklist",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private Set<User> blacklist;

    public Event(
            EventDto eventDto,
            GroupService groupService,
            DisciplineService disciplineService,
            UserService userService,
            EventService eventService,
            ResourceService resourceService
    ) throws WeLearnException {
        if (eventDto != null) {
            BeanUtils.copyProperties(eventDto, this);
            if (eventDto.getGroupId() != null) {
                group = groupService.findOne(eventDto.getGroupId());
            }
            if (eventDto.getDiscipline() != null) {
                discipline = new Discipline(eventDto.getDiscipline(), groupService, disciplineService, resourceService, userService, eventService);
            }
            if (eventDto.getBlacklist() != null) {
                blacklist = eventDto.getBlacklist().stream().map(it -> {
                    try {
                        return new User(it, groupService, disciplineService, userService, eventService);
                    } catch (WeLearnException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toSet());
            }
        }
    }
}

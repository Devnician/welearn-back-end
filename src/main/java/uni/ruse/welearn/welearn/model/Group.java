package uni.ruse.welearn.welearn.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.dto.GroupDto;
import uni.ruse.welearn.welearn.util.AuditedClass;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Ivelin Dimitrov
 */
@Entity(name = "groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group extends AuditedClass {
    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private Set<User> users;

    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private Set<Schedule> schedules;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private Set<Event> events;

    @ManyToMany(mappedBy = "group", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private Set<Discipline> disciplines;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private Set<Resource> resources;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "uuid",
            strategy = "uuid2"
    )
    private String groupId;
    private String name;
    private String description;
    private Integer maxResourcesMb;
    private Timestamp startDate;
    private Timestamp endDate;

    public Group(GroupDto groupDto) {
        if (groupDto != null) {
            BeanUtils.copyProperties(groupDto, this);
            resources = groupDto.getResources().stream().map(Resource::new).collect(Collectors.toSet());
            disciplines = groupDto.getDisciplines().stream().map(Discipline::new).collect(Collectors.toSet());
            events = groupDto.getEvents().stream().map(Event::new).collect(Collectors.toSet());
            schedules = groupDto.getSchedules().stream().map(Schedule::new).collect(Collectors.toSet());
            users = groupDto.getUsers().stream().map(User::new).collect(Collectors.toSet());
        }
    }
}

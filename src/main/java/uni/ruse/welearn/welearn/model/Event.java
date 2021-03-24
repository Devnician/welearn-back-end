package uni.ruse.welearn.welearn.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import uni.ruse.welearn.welearn.model.dto.EventDto;
import uni.ruse.welearn.welearn.util.AuditedClass;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

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
    private String name;
    private Timestamp startDate;
    private Timestamp endDate;
    private String type;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonBackReference
    private Group group;

    @ManyToMany
    @JoinTable(
            name = "blacklist",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private Set<User> blacklist;

    public Event(EventDto eventDto) {
        if (eventDto != null) {
            BeanUtils.copyProperties(eventDto, this);
            group = new Group(eventDto.getGroup());
            blacklist = eventDto.getBlacklist().stream().map(User::new).collect(Collectors.toSet());
        }
    }
}

package uni.ruse.welearn.welearn.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import uni.ruse.welearn.welearn.util.AuditedClass;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.Set;

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
    @JsonBackReference
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<User> users;

    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
    @JsonBackReference
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Schedule> schedules;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    @JsonBackReference
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Event> events;

    @ManyToMany(mappedBy = "group", fetch = FetchType.LAZY)
    @JsonBackReference
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Discipline> disciplines;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    @JsonBackReference
    @LazyCollection(LazyCollectionOption.FALSE)
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

    @Override
    public String toString() {
        return "Group{" +
                "users=" + users +
                ", schedules=" + schedules +
                ", events=" + events +
                ", groupId='" + groupId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", maxResourcesMb=" + maxResourcesMb +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}

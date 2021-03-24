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
import uni.ruse.welearn.welearn.model.dto.ScheduleDto;
import uni.ruse.welearn.welearn.util.AuditedClass;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.sql.Time;
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
public class Schedule extends AuditedClass {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "uuid",
            strategy = "uuid2"
    )
    private String id;
    private Time startTime;
    private Time endTime;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonBackReference
    private Group group;

    @OneToOne(cascade = CascadeType.DETACH)
    @JsonBackReference
    @JoinColumn(name = "discipline_id", referencedColumnName = "id")
    private Discipline discipline;

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private Set<Resource> resources;

    public Schedule(ScheduleDto scheduleDto) {
        if (scheduleDto != null) {
            BeanUtils.copyProperties(scheduleDto, this);
            group = new Group(scheduleDto.getGroup());
            discipline = new Discipline(scheduleDto.getDiscipline());
            resources = scheduleDto.getResources().stream().map(Resource::new).collect(Collectors.toSet());
        }
    }
}

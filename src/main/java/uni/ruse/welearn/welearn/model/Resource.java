package uni.ruse.welearn.welearn.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.dto.ResourceDto;
import uni.ruse.welearn.welearn.util.AuditedClass;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Ivelin Dimitrov
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resource extends AuditedClass {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "uuid",
            strategy = "uuid2"
    )
    private String resourceId;
    private String name;
    private String type;
    private String dirPath;
    private Boolean accessibleAll;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonBackReference
    private Group group;

    @ManyToOne
    @JoinColumn(name = "discipline_id")
    @JsonBackReference
    private Discipline discipline;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    @JsonBackReference
    private Schedule schedule;

    public Resource(ResourceDto resourceDto) {
        if (resourceDto != null) {
            BeanUtils.copyProperties(resourceDto, this);
            schedule = new Schedule(resourceDto.getSchedule());
            discipline = new Discipline(resourceDto.getDiscipline());
            group = new Group(resourceDto.getGroup());
        }
    }
}

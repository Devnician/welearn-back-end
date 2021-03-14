package uni.ruse.welearn.welearn.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import uni.ruse.welearn.welearn.util.AuditedClass;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.sql.Time;

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
    @JsonManagedReference
    private Group group;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "day_id", referencedColumnName = "id")
    private Day day;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "discipline_id", referencedColumnName = "id")
    private Discipline discipline;

    @Override
    public String toString() {
        return "\nSchedule{" +
                "id='" + id + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", dayMapping=" + day +
                ", disciplineMapping=" + discipline +
                '}';
    }
}

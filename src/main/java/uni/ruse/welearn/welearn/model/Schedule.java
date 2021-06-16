package uni.ruse.welearn.welearn.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.dto.ScheduleDto;
import uni.ruse.welearn.welearn.service.DisciplineService;
import uni.ruse.welearn.welearn.service.GroupService;
import uni.ruse.welearn.welearn.service.ResourceService;
import uni.ruse.welearn.welearn.util.AuditedClass;
import uni.ruse.welearn.welearn.util.WeLearnException;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.sql.Time;
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
public class Schedule extends AuditedClass {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "uuid",
            strategy = "uuid2"
    )
    private String id;

    @JsonFormat(pattern = "HH:mm")
    @Column(name="start_time")
    private Time startHour;
    @JsonFormat(pattern = "HH:mm")
    @Column(name="end_time")
    private Time endHour;

    @NotNull(message = "start date is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp startDate;
    @NotNull(message = "start date is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp endDate;

    @NotNull(message = "Days is mandatory")
    @Column(name="days")
    private String dayOfWeek;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonBackReference
    @NotNull(message = "Group is mandatory")
    private Group group;

    @OneToOne(cascade = CascadeType.DETACH)
    @JsonBackReference
    @JoinColumn(name = "discipline_id", referencedColumnName = "id")
    @NotNull(message = "Discipline is mandatory")
    private Discipline discipline;


    public Schedule(
            ScheduleDto scheduleDto,
            GroupService groupService,
            DisciplineService disciplineService,
            ResourceService resourceService
    ) throws WeLearnException {
        if (scheduleDto != null) {
            BeanUtils.copyProperties(scheduleDto, this);
            if (scheduleDto.getGroupId() != null) {
                group = groupService.findOne(scheduleDto.getGroupId());
            }
            if (scheduleDto.getDisciplineId() != null) {
                discipline = disciplineService.getDisciplineById(scheduleDto.getDisciplineId());
            }
        }
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id='" + id + '\'' + "\n" +
                ", startHour=" + startHour + "\n" +
                ", endHour=" + endHour + "\n" +
                ", startDate=" + startDate + "\n" +
                ", endDate=" + endDate + "\n" +
                ", dayOfWeek='" + dayOfWeek + '\'' + "\n" +
                ", group=" + group.getName() + "\n" +
                ", discipline=" + discipline.getName() + "\n" +
                '}';
    }
}

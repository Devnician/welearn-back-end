package uni.ruse.welearn.welearn.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.sql.Time;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
import uni.ruse.welearn.welearn.service.DisciplineService;
import uni.ruse.welearn.welearn.service.GroupService;
import uni.ruse.welearn.welearn.service.ResourceService;
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
public class Schedule extends AuditedClass {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "uuid",
            strategy = "uuid2"
    )
    private String id;
    @NotBlank(message = "Start time is mandatory")
    @JsonFormat(pattern = "HH:mm")
    private Time startTime;
    @NotBlank(message = "End time is mandatory")
    @JsonFormat(pattern = "HH:mm")
    private Time endTime;

    @NotNull(message = "Days is mandatory")
    private String days;

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

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private Set<Resource> resources;

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
            if (scheduleDto.getResourceIds() != null) {
                resources = scheduleDto.getResourceIds().stream().map(id1 -> {
                    try {
                        return resourceService.findById(id1);
                    } catch (WeLearnException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toSet());
            }
        }
    }
}

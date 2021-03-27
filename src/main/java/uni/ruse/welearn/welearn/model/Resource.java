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
import uni.ruse.welearn.welearn.service.DisciplineService;
import uni.ruse.welearn.welearn.service.GroupService;
import uni.ruse.welearn.welearn.service.ScheduleService;
import uni.ruse.welearn.welearn.util.AuditedClass;
import uni.ruse.welearn.welearn.util.WeLearnException;

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

    public Resource(
            ResourceDto resourceDto,
            ScheduleService scheduleService,
            DisciplineService disciplineService,
            GroupService groupService
    ) throws WeLearnException {
        if (resourceDto != null) {
            BeanUtils.copyProperties(resourceDto, this);
            if (resourceDto.getScheduleId() != null) {
                schedule = scheduleService.findById(resourceDto.getScheduleId());
            }
            if (resourceDto.getDisciplineId() != null) {
                discipline = disciplineService.getDisciplineById(resourceDto.getDisciplineId());
            }
            if (resourceDto.getGroupId() != null) {
                group = groupService.findOne(resourceDto.getGroupId());
            }
        }
    }
}

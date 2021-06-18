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
import uni.ruse.welearn.welearn.service.EventService;
import uni.ruse.welearn.welearn.service.GroupService;
import uni.ruse.welearn.welearn.service.ScheduleService;
import uni.ruse.welearn.welearn.util.AuditedClass;
import uni.ruse.welearn.welearn.util.WeLearnException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 256, message = "Name may be between 2 and 256 symbols long")
    @Pattern(regexp = "^[a-zA-Z0-9-_.\\s]*$", message = "File name my contain only letters, numbers, dash, underscore and dot")
    private String name;
    @NotBlank(message = "Type is mandatory")
    @Pattern(regexp = "^(application/msword)|" +
            "(application/vnd.openxmlformats-officedocument.wordprocessingml.document)|" +
            "(application/pdf)|" +
            "(image/png)|" +
            "(image/jpeg)|" +
            "(application/vnd.openxmlformats-officedocument.spreadsheetml.sheet)|" +
            "(application/vnd.ms-excel)|" +
            "(text/csv)|" +
            "(application/vnd.oasis.opendocument.spreadsheet)|" +
            "(text/plain)$", message = "Type is invalid")
    private String type;
//    @NotBlank(message = "Dir path is mandatory")
//    @Column(unique = true)
    private String dirPath;
    @NotNull(message = "AccessibleAll is mandatory")
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
    @JoinColumn(name = "event_id")
    @JsonBackReference
    private Event event;

    public Resource(
            ResourceDto resourceDto,
            EventService eventService,
            DisciplineService disciplineService,
            GroupService groupService
    ) throws WeLearnException {
        if (resourceDto != null) {
            BeanUtils.copyProperties(resourceDto, this);
            if (resourceDto.getEventId() != null) {
                event = eventService.findById(resourceDto.getEventId());
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

package uni.ruse.welearn.welearn.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
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
import uni.ruse.welearn.welearn.model.dto.DisciplineDto;
import uni.ruse.welearn.welearn.service.DisciplineService;
import uni.ruse.welearn.welearn.service.EventService;
import uni.ruse.welearn.welearn.service.GroupService;
import uni.ruse.welearn.welearn.service.ResourceService;
import uni.ruse.welearn.welearn.service.UserService;
import uni.ruse.welearn.welearn.util.AuditedClass;
import uni.ruse.welearn.welearn.util.WeLearnException;

/**
 * @author Ivelin Dimitrov
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Discipline extends AuditedClass {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "uuid",
            strategy = "uuid2"
    )
    private String id;

    @NotBlank(message = "Name is mandatory")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9 -]*$", message = "Name may contain only letters and numbers")
    private String name;

    @OneToMany(mappedBy = "discipline", fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private Set<Resource> resources;

    @ManyToMany(mappedBy = "disciplines", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private Set<Group> group;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "assistant_id")
    private User assistant;

    public Discipline(
            DisciplineDto disciplineDto,
            GroupService groupService,
            DisciplineService disciplineService,
            ResourceService resourceService,
            UserService userService,
            EventService eventService
    ) throws WeLearnException {
        if (disciplineDto != null) {
            BeanUtils.copyProperties(disciplineDto, this);
            if (disciplineDto.getResourceIds() != null) {
                resources = disciplineDto.getResourceIds().stream().map(it -> {
                    try {
                        return resourceService.findById(it);
                    } catch (WeLearnException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toSet());
            }
            if (disciplineDto.getTeacher() != null) {
                teacher = new User(disciplineDto.getTeacher(), groupService, disciplineService, userService, eventService);
            } else if (disciplineDto.getTeacherId() != null) {
                teacher = userService.findUserById(disciplineDto.getTeacherId());
            }


            if (disciplineDto.getAssistant() != null) {
                assistant = new User(disciplineDto.getAssistant(), groupService, disciplineService, userService, eventService);
            } else if (disciplineDto.getAssistantId() != null) {
                assistant = userService.findUserById(disciplineDto.getAssistantId());
            }
        }
    }
}

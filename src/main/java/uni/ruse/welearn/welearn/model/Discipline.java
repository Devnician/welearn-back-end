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
import uni.ruse.welearn.welearn.model.dto.DisciplineDto;
import uni.ruse.welearn.welearn.util.AuditedClass;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
public class Discipline extends AuditedClass {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "uuid",
            strategy = "uuid2"
    )
    private String id;

    private String name;

    @OneToMany(mappedBy = "discipline", fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private Set<Resource> resources;

    @ManyToMany
    @JsonManagedReference
    @JoinColumn(name = "group_id")
    private Set<Group> group;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "assistant_id")
    private User assistant;

    public Discipline(DisciplineDto disciplineResponseDto) {
        if (disciplineResponseDto != null) {
            BeanUtils.copyProperties(disciplineResponseDto, this);
            resources = disciplineResponseDto.getResources().stream().map(Resource::new).collect(Collectors.toSet());
            teacher = new User(disciplineResponseDto.getTeacher());
            assistant = new User(disciplineResponseDto.getAssistant());
        }
    }
}

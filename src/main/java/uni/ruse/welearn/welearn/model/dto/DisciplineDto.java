package uni.ruse.welearn.welearn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.Discipline;
import uni.ruse.welearn.welearn.model.Resource;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ivelin.dimitrov
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DisciplineDto {
    private String id;
    @NotBlank(message = "Name is mandatory")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9 -]*$", message = "Name may contain only letters and numbers")
    private String name;
    private Set<String> resourceIds;
    private UserDto teacher;
    private UserDto assistant;
    private String teacherId;
    private String assistantId;

    public DisciplineDto(Discipline discipline) {
        if (discipline != null) {
            BeanUtils.copyProperties(discipline, this);
            if (discipline.getResources() != null) {
                resourceIds = discipline.getResources().stream().map(Resource::getResourceId).collect(Collectors.toSet());
            }
            if (discipline.getTeacher() != null) {
                teacher = new UserDto(discipline.getTeacher());
            }
            if (discipline.getAssistant() != null) {
                assistant = new UserDto(discipline.getAssistant());
            }
        }
    }
}

package uni.ruse.welearn.welearn.model.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.Discipline;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ivelin.dimitrov
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DisciplineDto {
    private String id;
    private String name;
    @JsonManagedReference("resource-discipline")
    private Set<ResourceDto> resources;
    @JsonBackReference("teacher-discipline")
    private UserDto teacher;
    @JsonBackReference("assistant-discipline")
    private UserDto assistant;

    public DisciplineDto(Discipline discipline) {
        if (discipline != null) {
            BeanUtils.copyProperties(discipline, this);
//            resources = discipline.getResources().stream().map(ResourceDto::new).collect(Collectors.toSet());
            teacher = new UserDto(discipline.getTeacher());
            assistant = new UserDto(discipline.getAssistant());
        }
    }
}

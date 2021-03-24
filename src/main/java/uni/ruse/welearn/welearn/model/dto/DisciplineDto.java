package uni.ruse.welearn.welearn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.Discipline;
import uni.ruse.welearn.welearn.model.Group;
import uni.ruse.welearn.welearn.model.Resource;
import uni.ruse.welearn.welearn.model.User;

import java.util.Set;

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
    private Set<Resource> resources;
    private Set<Group> group;
    private User teacher;
    private User assistant;

    public DisciplineDto(Discipline discipline) {
        BeanUtils.copyProperties(discipline, this);
    }
}

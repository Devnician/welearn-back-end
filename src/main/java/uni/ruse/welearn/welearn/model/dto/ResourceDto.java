package uni.ruse.welearn.welearn.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.Resource;

/**
 * @author ivelin.dimitrov
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResourceDto {
    private String resourceId;
    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 256, message = "Name may be between 2 and 256 symbols long")
    @Pattern(regexp = "^[a-zA-Z0-9-_.\\s]*$", message = "File name my contain only letters, numbers, dash, underscore and dot")
    private String name;
    @NotBlank(message = "Type is mandatory")
    @Pattern(regexp = "^(doc)|(docx)|(pdf)|(png)|(jpeg)|(jpg)|(xlsx)|(xls)|(csv)|(ods)|(txt)$", message = "Type is invalid")
    private String type;
    @NotBlank(message = "Dir path is mandatory")
    private String dirPath;
    @NotNull(message = "AccessibleAll is mandatory")
    private Boolean accessibleAll;
    private String groupId;
    private String disciplineId;
    private String scheduleId;

    public ResourceDto(Resource resource) {
        if (resource != null) {
            BeanUtils.copyProperties(resource, this);
            if (resource.getGroup() != null) {
                groupId = resource.getGroup().getGroupId();
            }
            if (resource.getDiscipline() != null) {
                disciplineId = resource.getDiscipline().getId();
            }
            if (resource.getSchedule() != null) {
                scheduleId = resource.getSchedule().getId();
            }
        }
    }
}

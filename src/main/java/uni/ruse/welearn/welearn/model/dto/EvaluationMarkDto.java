package uni.ruse.welearn.welearn.model.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.EvaluationMark;

import java.math.BigDecimal;

/**
 * @author ivelin.dimitrov
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationMarkDto {
    private String id;
    private BigDecimal markValue;
    @JsonBackReference("mark-group")
    private GroupDto group;
    @JsonBackReference("mark-discipline")
    private DisciplineDto discipline;
    @JsonBackReference("user-mark")
    private UserDto user;

    public EvaluationMarkDto(EvaluationMark evaluationMark) {
        if (evaluationMark != null) {
            BeanUtils.copyProperties(evaluationMark, this);
//            group = new GroupDto(evaluationMark.getGroup());
//            discipline = new DisciplineDto(evaluationMark.getDiscipline());
//            user = new UserDto(evaluationMark.getUser());
        }
    }
}

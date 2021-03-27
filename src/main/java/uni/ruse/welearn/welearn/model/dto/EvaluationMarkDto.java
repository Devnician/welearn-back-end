package uni.ruse.welearn.welearn.model.dto;

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
    private String groupId;
    private String disciplineId;
    private String userId;

    public EvaluationMarkDto(EvaluationMark evaluationMark) {
        if (evaluationMark != null) {
            BeanUtils.copyProperties(evaluationMark, this);
            if (evaluationMark.getGroup() != null) {
                groupId = evaluationMark.getGroup().getGroupId();
            }
            if (evaluationMark.getDiscipline() != null) {
                disciplineId = evaluationMark.getDiscipline().getId();
            }
            if (evaluationMark.getUser() != null) {
                userId = evaluationMark.getUser().getUserId();
            }
        }
    }
}

package uni.ruse.welearn.welearn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.EvaluationMark;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author ivelin.dimitrov
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvaluationMarkDto {
    private String id;
    @NotNull(message = "Mark Value is mandatory")
    @DecimalMin(value = "2.0", message = "Mark value may not be lower than 2.0")
    @DecimalMax(value = "6.0", message = "Mark value may not be higher than 6.0")
    @Digits(integer=1, fraction=2)
    private BigDecimal markValue;
    private String groupId;
    @NotBlank(message = "Discipline is mandatory")
    private String disciplineId;
    @NotBlank(message = "User is mandatory")
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

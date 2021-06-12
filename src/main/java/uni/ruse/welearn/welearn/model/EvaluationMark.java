package uni.ruse.welearn.welearn.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.dto.EvaluationMarkDto;
import uni.ruse.welearn.welearn.service.DisciplineService;
import uni.ruse.welearn.welearn.service.GroupService;
import uni.ruse.welearn.welearn.service.UserService;
import uni.ruse.welearn.welearn.util.AuditedClass;
import uni.ruse.welearn.welearn.util.WeLearnException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Ivelin Dimitrov
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class EvaluationMark extends AuditedClass {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "uuid",
            strategy = "uuid2"
    )
    private String id;
    @NotNull(message = "Mark Value is mandatory")
    @DecimalMin(value = "2.0", message = "Mark value may not be lower than 2.0")
    @DecimalMax(value = "6.0", message = "Mark value may not be higher than 6.0")
    @Digits(integer=1, fraction=2)
    private BigDecimal markValue;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonBackReference
    private Group group;

    @ManyToOne
    @JoinColumn(name = "discipline_id")
    @JsonBackReference
    @NotNull(message = "Discipline is mandatory")
    private Discipline discipline;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    @NotNull(message = "User is mandatory")
    private User user;

    public EvaluationMark(
            EvaluationMarkDto evaluationMarkDto,
            GroupService groupService,
            DisciplineService disciplineService,
            UserService userService
    ) throws WeLearnException {
        if (evaluationMarkDto != null) {
            BeanUtils.copyProperties(evaluationMarkDto, this);
            if (evaluationMarkDto.getGroupId() != null) {
                group = groupService.findOne(evaluationMarkDto.getGroupId());
            }
            if (evaluationMarkDto.getDisciplineId() != null) {
                discipline = disciplineService.getDisciplineById(evaluationMarkDto.getDisciplineId());
            }
            if (evaluationMarkDto.getUserId() != null) {
                user = userService.findUserById(evaluationMarkDto.getUserId());
            }
        }
    }
}

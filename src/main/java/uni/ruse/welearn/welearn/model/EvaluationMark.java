package uni.ruse.welearn.welearn.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.dto.EvaluationMarkDto;
import uni.ruse.welearn.welearn.util.AuditedClass;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

/**
 * @author Ivelin Dimitrov
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationMark extends AuditedClass {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "uuid",
            strategy = "uuid2"
    )
    private String id;
    private BigDecimal markValue;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonBackReference
    private Group group;

    @ManyToOne
    @JoinColumn(name = "discipline_id")
    @JsonBackReference
    private Discipline discipline;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public EvaluationMark(EvaluationMarkDto evaluationMarkDto) {
        if (evaluationMarkDto != null) {
            BeanUtils.copyProperties(evaluationMarkDto, this);
            group = new Group(evaluationMarkDto.getGroup());
            discipline = new Discipline(evaluationMarkDto.getDiscipline());
            user = new User(evaluationMarkDto.getUser());
        }
    }
}

package uni.ruse.welearn.welearn.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import uni.ruse.welearn.welearn.util.AuditedClass;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Ivelin Dimitrov
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resource extends AuditedClass {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "uuid",
            strategy = "uuid2"
    )
    private String resourceId;
    private String name;
    private String type;
    private String dirPath;
    private Boolean accessibleAll;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonManagedReference
    private Group group;

    @Override
    public String toString() {
        return "\nResource{" +
                "resourceId='" + resourceId + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", dirPath='" + dirPath + '\'' +
                ", accessibleAll=" + accessibleAll +
                '}';
    }
}

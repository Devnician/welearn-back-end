package uni.ruse.welearn.welearn.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import uni.ruse.welearn.welearn.util.AuditedClass;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * @author Ivelin Dimitrov
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Discipline extends AuditedClass {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "uuid",
            strategy = "uuid2"
    )
    private String id;

    private String name;

    @OneToMany(mappedBy = "discipline", fetch = FetchType.EAGER)
    @JsonManagedReference
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Resource> resources;

    @ManyToMany
    @JoinColumn(name = "group_id")
    @JsonManagedReference
    private Set<Group> group;

    @Override
    public String toString() {
        return "Dsicipline{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

package uni.ruse.welearn.welearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.ruse.welearn.welearn.model.Group;

/**
 * @author Ivelin Dimitrov
 */
@Repository
public interface GroupRepository extends JpaRepository<Group, String> {
}

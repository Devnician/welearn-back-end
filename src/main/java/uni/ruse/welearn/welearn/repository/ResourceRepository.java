package uni.ruse.welearn.welearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.ruse.welearn.welearn.model.Resource;

/**
 * @author Ivelin Dimitrov
 */
@Repository
public interface ResourceRepository extends JpaRepository<Resource, String> {
}

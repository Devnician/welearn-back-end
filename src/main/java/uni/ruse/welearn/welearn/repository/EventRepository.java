package uni.ruse.welearn.welearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.ruse.welearn.welearn.model.Event;

/**
 * @author Ivelin Dimitrov
 */
@Repository
public interface EventRepository extends JpaRepository<Event, String> {
}

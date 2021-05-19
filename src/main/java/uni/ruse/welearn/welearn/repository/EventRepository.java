package uni.ruse.welearn.welearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.ruse.welearn.welearn.model.Event;
import uni.ruse.welearn.welearn.model.Group;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Ivelin Dimitrov
 */
@Repository
public interface EventRepository extends JpaRepository<Event, String> {

    List<Event> findByStartDateGreaterThanEqualAndEndDateLessThanEqualAndGroupEquals(Timestamp startDate, Timestamp endDate, Group group);
}

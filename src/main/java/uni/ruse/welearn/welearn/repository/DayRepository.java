package uni.ruse.welearn.welearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.ruse.welearn.welearn.model.Day;

import java.util.Optional;

/**
 * @author Ivelin Dimitrov
 */
@Repository
public interface DayRepository extends JpaRepository<Day, String> {
    Optional<Day> findByName(String name);
}

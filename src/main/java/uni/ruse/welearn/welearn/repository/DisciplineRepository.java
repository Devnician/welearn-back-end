package uni.ruse.welearn.welearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.ruse.welearn.welearn.model.Discipline;

import java.util.Optional;

/**
 * @author Ivelin Dimitrov
 */
@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, String> {
    Optional<Discipline> findByName(String disciplineName);
}

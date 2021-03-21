package uni.ruse.welearn.welearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.ruse.welearn.welearn.model.Role;

/**
 * extends {@link JpaRepository}
 *
 * @author petar ivanov
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRole(String string);
}

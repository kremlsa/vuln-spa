package vulnspa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vulnspa.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Находит пользователя по имени.
     * Нужно для UserService.getCurrentUser().
     */
    Optional<User> findByUsername(String username);
}
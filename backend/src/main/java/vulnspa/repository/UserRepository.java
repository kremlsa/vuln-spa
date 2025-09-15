package vulnspa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vulnspa.model.User;

import java.util.Optional;

/**
 * Репозиторий пользователей.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Находит пользователя по имени.
     *
     * @param username логин пользователя.
     * @return опционал с найденным пользователем.
     */
    Optional<User> findByUsername(String username);
}
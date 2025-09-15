package vulnspa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vulnspa.model.Note;

/**
 * Репозиторий Spring Data для управления заметками.
 */
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
}

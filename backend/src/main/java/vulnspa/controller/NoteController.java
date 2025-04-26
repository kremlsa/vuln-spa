package vulnspa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vulnspa.model.Note;
import vulnspa.repository.NoteRepository;

import java.util.List;


@RestController
@RequestMapping("/api/notes")
public class NoteController {
    @Autowired
    private NoteRepository noteRepository;

    @GetMapping
    public List<Note> getNotes() {
        return noteRepository.findAll();
    }

    /* Broken Access Control Не проверяем автора заметки, можно создавать заметки от чужого имени */
    @PostMapping
    public Note createNote(@RequestBody Note note) {
        return noteRepository.save(note);
    }
    /* Broken Access Control Не проверяем автора заметки, можно удалять любые заметки */
    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Удаляет пользователь: " + (auth != null ? auth.getName() : "Аноним"));
        noteRepository.deleteById(id);
    }
}
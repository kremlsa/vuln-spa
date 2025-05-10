package vulnspa.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import vulnspa.model.Note;
import vulnspa.repository.NoteRepository;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;


@RestController
@RequestMapping("/api/notes")
public class NoteController {
    @Autowired
    private NoteRepository noteRepository;

    private String renderNoteContent(Note note) {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
            cfg.setFallbackOnNullLoopVariable(false);
            cfg.setClassicCompatible(true);

            Template template = new Template("note-" + note.getId(), new StringReader(note.getContent()), cfg);

            Map<String, Object> dataModel = new HashMap<>();
            dataModel.put("author", note.getAuthor());

            StringWriter out = new StringWriter();
            template.process(dataModel, out);

            return out.toString();

        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            return "[rendering error]";
        }
    }

    @GetMapping
    public List<Map<String, Object>> getNotes() {
        List<Note> notes = noteRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Note note : notes) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", note.getId());
            item.put("author", note.getAuthor());
            item.put("title", note.getTitle());
            item.put("content", renderNoteContent(note));

            result.add(item);
        }

        return result;
    }

//    ${"freemarker.template.utility.Execute"?new()("whoami")}
//${"freemarker.template.utility.Execute"?new()("id")}

    /* Broken Access Control Не проверяем автора заметки, можно создавать заметки от чужого имени */
//    @PostMapping
//    public Note createNote(@RequestBody Note note) {
//        return noteRepository.save(note);
//    }
    @PostMapping
    public Note createRawNote(@RequestBody String rawJson) {
        // 💀 Уязвимая обработка JSON
        JSONObject json = new JSONObject(rawJson);
        System.out.println(json.toString());

        // Попробуем извлечь "content" и "author"
        String content = json.optString("content", "(пусто)");
        String author = json.optString("author", "неизвестно");
        Note note = new Note();
        note.setAuthor(author);
        note.setContent(content);
        return noteRepository.save(note);
    }

    /* Broken Access Control Не проверяем автора заметки, можно удалять любые заметки */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        Optional<Note> noteOpt = noteRepository.findById(id);
        if (noteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Note note = noteOpt.get();

        // Пасхалка: Чака удалять нельзя
        if ("Чак".equalsIgnoreCase(note.getAuthor())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Нельзя удалять заметки Чака Норриса. Это небезопасно. Он уже выехал."));
        }

        // Дополнительно: проверка авторства
//        if (!note.getAuthor().equals(user.getUsername())) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body(Map.of("error", "Вы можете удалять только свои заметки."));
//        }

        noteRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
package vulnspa.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import vulnspa.model.Note;
import vulnspa.repository.NoteRepository;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/templates")
public class SstiNoteController {

    private final NoteRepository noteRepository;

    public SstiNoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @GetMapping("/preview/{id}")
    @ResponseBody // обязательно, чтобы вернуть HTML как строку, а не искать шаблон
    public ResponseEntity<String> preview(@PathVariable Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
            cfg.setFallbackOnNullLoopVariable(false);
            cfg.setClassicCompatible(true);

            Template dynamicTemplate = new Template("ssti-dynamic", new StringReader(note.getContent()), cfg);

            Map<String, Object> dataModel = new HashMap<>();
            dataModel.put("author", note.getAuthor());

            StringWriter out = new StringWriter();
            dynamicTemplate.process(dataModel, out);

            return ResponseEntity.ok(out.toString());

        } catch (IOException | TemplateException e) {
            e.printStackTrace(); // печать в консоль для отладки
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Template rendering failed: " + e.getMessage());
        }
    }
}

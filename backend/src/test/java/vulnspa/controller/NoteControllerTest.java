package vulnspa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import vulnspa.model.Note;
import vulnspa.repository.NoteRepository;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
@AutoConfigureMockMvc(addFilters = false) // Отключает Security фильтры
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteRepository noteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateNote() throws Exception {
        // Создаем Note, который вернет фейковый репозиторий
        Note savedNote = new Note();
        savedNote.setAuthor("Captain Nemo");
        savedNote.setContent("Nautilus Pompilius");
        savedNote.setTitle("Submarine");
        savedNote.setId(1L);

        // Заглушка репозитория: сохраняем любую Note -> возвращаем savedNote
        when(noteRepository.save(any(Note.class))).thenReturn(savedNote);

        Note requestNote = new Note();
        requestNote.setAuthor("Captain Nemo");
        requestNote.setContent("Nautilus Pompilius");
        requestNote.setTitle("Submarine");
        requestNote.setId(1L);

        mockMvc.perform(post("/api/notes")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestNote)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Submarine"))
                .andExpect(jsonPath("$.content").value("Nautilus Pompilius"))
                .andExpect(jsonPath("$.author").value("Captain Nemo"));
    }

/*
Поднимается только NoteController, остальные бины мокаются	@WebMvcTest(NoteController.class)
NoteRepository подменяется мок-объектом	@MockBean
Любой noteRepository.save() возвращает заготовленный savedNote	when(noteRepository.save(any())).thenReturn(savedNote)
Используется MockMvc, чтобы симулировать HTTP-запрос	mockMvc.perform(post(...))
Проверяется: статус 200 и содержимое JSON ответа	.andExpect(jsonPath(...))
 */

    @Test
    void testDeleteNote() throws Exception {
        Long noteId = 1L;

        // ОБЯЗАТЕЛЬНО замокать existsById
        when(noteRepository.existsById(noteId)).thenReturn(true);

        mockMvc.perform(delete("/api/notes/{id}", noteId))
                .andExpect(status().isOk());

        // Проверяем, что метод deleteById был вызван ровно один раз с нужным ID
        verify(noteRepository, times(1)).deleteById(noteId);
    }


    @Test
    void testGetNotes() throws Exception {
        // Готовим список заметок
        Note note1 = new Note();
        note1.setId(1L);
        note1.setTitle("First Note");
        note1.setContent("Content 1");
        note1.setAuthor("Author1");

        Note note2 = new Note();
        note2.setId(2L);
        note2.setTitle("Second Note");
        note2.setContent("Content 2");
        note2.setAuthor("Author2");

        List<Note> notes = Arrays.asList(note1, note2);

        // Мокаем поведение репозитория
        when(noteRepository.findAll()).thenReturn(notes);

        // Делаем запрос и проверяем ответ
        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("First Note"))
                .andExpect(jsonPath("$[1].title").value("Second Note"))
                .andExpect(jsonPath("$[0].author").value("Author1"))
                .andExpect(jsonPath("$[1].author").value("Author2"));
    }
}
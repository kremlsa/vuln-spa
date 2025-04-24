package vulnspa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import vulnspa.dto.NoteDTO;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class VulnerableController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Уязвимый метод поиска заметок
    @GetMapping("/search")
    public List<NoteDTO> searchNotes(@RequestParam String query) {
        // 🧨 ВНИМАНИЕ 🧨 — здесь создается SQL Injection
        String sql = "SELECT ID, TITLE, CONTENT FROM note WHERE content LIKE '%" + query + "%'";
        System.out.println(sql);
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            NoteDTO note = new NoteDTO();
            note.setId(rs.getInt("ID"));
            note.setTitle(rs.getString("TITLE"));
            note.setContent(rs.getString("CONTENT"));
            return note;
        });
    }
}


/*
 * ' UNION SELECT NULL, NULL, NULL --
 * ' UNION SELECT 1, username, password FROM users --
 * ' OR '1'='1
 */
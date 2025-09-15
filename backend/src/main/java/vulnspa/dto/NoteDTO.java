package vulnspa.dto;

/**
 * DTO для передачи заметок из SQL-запросов.
 */
public class NoteDTO {
    private int id;
    private String title;
    private String content;
    private String author;

    /**
     * Возвращает идентификатор заметки.
     *
     * @return числовой идентификатор.
     */
    public int getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор заметки.
     *
     * @param id уникальный идентификатор.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Возвращает заголовок заметки.
     *
     * @return текст заголовка.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Устанавливает заголовок заметки.
     *
     * @param title новый заголовок.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Возвращает содержимое заметки.
     *
     * @return текст заметки.
     */
    public String getContent() {
        return content;
    }

    /**
     * Устанавливает содержимое заметки.
     *
     * @param content новый текст заметки.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Возвращает автора заметки.
     *
     * @return имя автора.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Устанавливает автора заметки.
     *
     * @param author имя автора.
     */
    public void setAuthor(String author) {
        this.author = author;
    }
}
package vulnspa.model;

import jakarta.persistence.*;

/**
 * Сущность заметки, хранящаяся в базе данных.
 */
@Entity
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(length = 5000)
    private String content;

    private String author;

    /**
     * Устанавливает идентификатор заметки.
     *
     * @param id первичный ключ.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Возвращает идентификатор заметки.
     *
     * @return первичный ключ.
     */
    public Long getId() {
        return id;
    }

    /**
     * Возвращает заголовок заметки.
     *
     * @return строка заголовка.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Устанавливает заголовок заметки.
     *
     * @param title строка заголовка.
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
     * @param content текст заметки.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Возвращает имя автора.
     *
     * @return автор заметки.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Устанавливает автора заметки.
     *
     * @param author автор заметки.
     */
    public void setAuthor(String author) {
        this.author = author;
    }
}
package vulnspa.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер загрузки файлов из classpath.
 */
@RestController
@RequestMapping("/api/files")
public class FileController {

    /**
     * Возвращает файл из папки {@code static/files} внутри classpath.
     *
     * @param filename имя файла, который нужно скачать.
     * @return HTTP-ответ с ресурсом или соответствующим кодом ошибки.
     */
    @GetMapping
    public ResponseEntity<Resource> downloadFile(@RequestParam String filename) {
        try {
            // ❗ Уязвимая загрузка ресурса из classpath (без фильтрации "../")
            ClassLoader classLoader = getClass().getClassLoader();
            java.net.URL resourceUrl = classLoader.getResource("static/files/" + filename); // может содержать ../
            if (resourceUrl == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Resource resource = new UrlResource(resourceUrl);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace(); // для отладки
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

/*
/api/files?filename=../../application.properties ⚠️ возможно
 */
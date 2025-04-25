package vulnspa.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.*;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @GetMapping
    public ResponseEntity<Resource> downloadFile(@RequestParam String filename, HttpServletResponse response) {
        try {
            // ❗ БЕЗ фильтрации — уязвимость Directory Traversal
            Path filePath = Paths.get("").toAbsolutePath().resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            System.out.println(filePath);

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}


// http://127.0.0.1:8080/api/files?filename=backend/src/main/resources/application.properties
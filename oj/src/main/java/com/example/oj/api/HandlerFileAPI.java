package com.example.oj.api;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.tree.DefaultTreeCellEditor;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class HandlerFileAPI {

    @PostMapping("/upload-file")
    public ResponseEntity<List<String>> uploadFile(@RequestParam("files")List<MultipartFile> files, @RequestParam String folder, @RequestParam String type) throws IOException {
        List<String> filesResponse = new ArrayList<>();
        for (MultipartFile file : files) {
            byte[] bytes = file.getBytes();
            String filename = file.getOriginalFilename();
            Path directory = Paths.get("./testcase", folder, type);
            Files.createDirectories(directory);
            Path filePath = directory.resolve(filename);
            Files.write(filePath, bytes);
            filesResponse.add(filePath.toString());
        }
        return ResponseEntity.ok(filesResponse);
    }

    @DeleteMapping("/delete-folder/{folder}")
    public void deleteFile(@PathVariable String folder) {
        String folderPath = "./testcase/" + folder;
        Path directory = Paths.get(folderPath);
        try {
            if(Files.exists(directory)) {
                Files.walk(directory)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

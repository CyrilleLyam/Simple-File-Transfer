package com.seanglay.simple_file_transfer.controller;

import com.seanglay.simple_file_transfer.model.StoredFile;
import com.seanglay.simple_file_transfer.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/")
    public String uploadPage() {
        return "upload";
    }

    @PostMapping("/upload")
    public String handleUpload(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        StoredFile savedFile = fileService.storeAndRecord(file);
        model.addAttribute("url", savedFile.getDownloadUrl());
        return "success";
    }

    @GetMapping("/download/{storedName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String storedName) throws Exception {
        StoredFile fileMeta = fileService.getFileByStoredName(storedName);
        Resource file = fileService.loadFile(storedName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileMeta.getOriginalName() + "\"")
                .body(file);
    }
}

package com.seanglay.simple_file_transfer.service;

import com.seanglay.simple_file_transfer.model.StoredFile;
import com.seanglay.simple_file_transfer.repository.FileRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private final Path UPLOADED_FOLDER = Paths.get("uploads");
    private final FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) throws IOException {
        this.fileRepository = fileRepository;
        Files.createDirectories(UPLOADED_FOLDER);
    }

    @Override
    public StoredFile storeAndRecord(MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();

        if (originalName == null || originalName.isBlank()) {
            throw new IllegalArgumentException("Invalid file name");
        }

        String storedName = UUID.randomUUID() + "_" + originalName;
        Path filePath = UPLOADED_FOLDER.resolve(storedName);
        Files.copy(file.getInputStream(), filePath);

        String downloadUrl = "/download/" + storedName;

        StoredFile metadata = new StoredFile(null, originalName, storedName, downloadUrl, LocalDateTime.now());
        return fileRepository.save(metadata);
    }

    @Override
    public Resource loadFile(String storedName) throws Exception {
        Path filePath = UPLOADED_FOLDER.resolve(storedName).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("Could not read the file: " + storedName);
        }

        return resource;
    }

    @Override
    public StoredFile getFileByStoredName(String storedName) {
        return fileRepository.findByStoredName(storedName)
                .orElseThrow(() -> new RuntimeException("File not found"));
    }
}

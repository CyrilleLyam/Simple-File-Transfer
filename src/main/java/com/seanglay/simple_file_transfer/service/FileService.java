package com.seanglay.simple_file_transfer.service;

import com.seanglay.simple_file_transfer.model.StoredFile;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    StoredFile storeAndRecord(MultipartFile file) throws IOException;
    Resource loadFile(String storedName) throws Exception;
    StoredFile getFileByStoredName(String storedName);
}

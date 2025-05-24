package com.seanglay.simple_file_transfer.repository;

import com.seanglay.simple_file_transfer.model.StoredFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<StoredFile, Long> {
    Optional<StoredFile> findByStoredName(String storedName);
}

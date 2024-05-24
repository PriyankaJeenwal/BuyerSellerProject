package com.user.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.service.entity.FileData;
import com.user.service.entity.User;

public interface FileDataRepository extends JpaRepository<FileData, Long> {
Optional<FileData> findByNameAndUserId(String fileName,Long userId);
Optional<FileData> findByUserId(Long userId);

}

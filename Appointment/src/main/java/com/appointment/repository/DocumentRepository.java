package com.appointment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appointment.entity.Appointment;
import com.appointment.entity.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
	//public Optional<Document> findDocumentByNameAndUserId(Long userId,String name);
}

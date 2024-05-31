package com.appointment.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointment_table")
public class Appointment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	@Column(name = "id")
	private Long id;

	@Column(name = "title")
	private String title;

	@Column(name = "appointmentDate")
	private Date appointmentDate;

	@Column(name = "userId")
	private Long userId;

	@Column(name = "agentId")
	private Long agentId;

	@Column(name = "description")
	private String description;
	


	/*
	 * @Column(name = "previousMedicalDocument") private String
	 * previousMedicalDocument;
	 */
	//private MultipartFile[] previousMedicalDocument;
	

	 
	@ElementCollection
	@CollectionTable(name = "CollectionHistory", joinColumns = @JoinColumn(name = "appointmentId"))
	private List<Date> appointmentDateHistory;

	
}

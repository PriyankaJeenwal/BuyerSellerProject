package com.appointment.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "Document_table")
public class Document {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	@Column(name = "id")
	private Long id;
	
	@Column(name = "name")
	private String name;

	@Column(name = "url")
	private String url;

	/*
	 * @Column(name = "appointmentId") private Date appointmentDate;
	 */
	@Column(name = "userId")
	private Long userId;

}

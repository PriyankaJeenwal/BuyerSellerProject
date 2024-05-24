package com.user.service.entity;



import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Appointment {

	private Long id;

	private String title;

	private LocalDate appintmentDate;

	private Long userId;

	private Long doctorId;

	private String description;

	private String previousMedicalDocument;

}

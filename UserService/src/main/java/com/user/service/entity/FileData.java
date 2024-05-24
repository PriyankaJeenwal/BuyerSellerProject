package com.user.service.entity;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "FileData")
@Entity
@Builder

public class FileData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "type")
	private String type;
	
	@Column(name = "filePath")
	private String filePath;
	
//	@Column(name = "userId")
//	private Long userId;
	
//	@OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//	private User user;

	@JsonIgnore
	@OneToOne(mappedBy = "fileData")
	   private User user;

}

package com.appointment.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.appointment.entity.Appointment;
import com.appointment.entity.Document;
import com.appointment.response.AppointmentResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface AppointmentController {
	@GetMapping("/user/{userId}")
	public ResponseEntity<AppointmentResponse<List<Appointment>>> getByUserId(@PathVariable Long userId);

	@PostMapping("/addAppointment")
	public ResponseEntity<AppointmentResponse<Appointment>> addAppointment(@RequestBody Appointment appointment);

	@GetMapping("/getAll")
	public ResponseEntity<AppointmentResponse<List<Appointment>>> findAllAppointment(
			@RequestParam(defaultValue = "5", required = false) Integer pageSize,

			@RequestParam(defaultValue = "0", required = false) Integer page);

	@GetMapping("getAppointmentById/{id}")
	public ResponseEntity<AppointmentResponse<Appointment>> getAppointmentById(@PathVariable Long id);

	@GetMapping("/getByTitle/{title}")
	public ResponseEntity<AppointmentResponse<List<Appointment>>> getAppointmentByTitle(@PathVariable String title);

	@PostMapping("/delete/{id}")
	public ResponseEntity<String> deleteAppointmentById(@PathVariable Long id);

	@PostMapping("/update/{id}")
	public ResponseEntity<AppointmentResponse<Appointment>> updateAppointment(@RequestBody Appointment appointment,
			@PathVariable Long id);

	@PostMapping("/upload")
	public ResponseEntity<Appointment> fileUpload(@RequestParam("image") MultipartFile image);

	@PostMapping(value = "/uploads", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public AppointmentResponse<Appointment> Upload(@RequestPart("appointments") String appointments,
			@RequestPart("file") List<MultipartFile> file) throws JsonMappingException, JsonProcessingException;
	
	 @GetMapping("/getFile")
	 public ResponseEntity<?> getImage(@RequestParam("userId") Long userId, @RequestParam("imageName") String imageName) throws IOException;

}

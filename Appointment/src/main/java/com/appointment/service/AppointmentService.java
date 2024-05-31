package com.appointment.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.appointment.entity.Appointment;
import com.appointment.response.AppointmentResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface AppointmentService {

	public AppointmentResponse<Appointment> getAppointmentById(Long id);

	public AppointmentResponse<List<Appointment>> getAllAppointment(org.springframework.data.domain.Pageable paging);

	public boolean deleteAppointmentById(Long id);

	public AppointmentResponse<List<Appointment>> getAppointmentByTitle(String title);

	public AppointmentResponse<List<Appointment>> getByUserId(Long userId);

	public String uploadImage(String path, MultipartFile file);

	public AppointmentResponse<Appointment> updateAppointment(Appointment newAppointment, Long id);

	public AppointmentResponse<Appointment> addAppointment(Appointment appointment);

	public AppointmentResponse<Appointment> getJson(String appointments,String path, List<MultipartFile> file) throws JsonMappingException, JsonProcessingException;


	public byte[] getImage(Long userId, String path, String imageName) throws IOException;

	

}

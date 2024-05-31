package com.appointment.controller;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import com.appointment.entity.Appointment;
import com.appointment.repository.AppointmentRepository;
import com.appointment.response.AppointmentResponse;
import com.appointment.service.AppointmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/appointment")
public class AppointmentControllerImpl implements AppointmentController {

	@Autowired
	private AppointmentService appointmentService;

	@Autowired
	AppointmentRepository appointmentRepository;

	//@Value("${project.image}")
	
	private String path="C:\\Users\\User\\Documents\\AppointmentDocuments\\documents";
	static final Logger log = LogManager.getLogger(AppointmentControllerImpl.class.getName());

	@Override
	public ResponseEntity<AppointmentResponse<Appointment>> addAppointment(Appointment appointment) {
		try {
			if (!(appointment.getAppointmentDate() == null && appointment.getTitle() == null
					&& appointment.getAgentId() == null && appointment.getUserId() == null
					&& appointment.getTitle() == " ")) {

				log.info("adding new appointment : {}", appointment);

				AppointmentResponse<Appointment> appointmentResponse = appointmentService.addAppointment(appointment);
				return new ResponseEntity<>(appointmentResponse, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(new AppointmentResponse<Appointment>(
						"Appointment not added. Required fields are missing.", false), HttpStatus.BAD_REQUEST);

			}
		} catch (Exception e) {
			log.info("Exception while creating appointment: {}", e.getMessage());
			return new ResponseEntity<>(
					new AppointmentResponse<>("Appointment not added. Internal Server Error", false),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	public ResponseEntity<AppointmentResponse<List<Appointment>>> findAllAppointment(Integer pageSize, Integer page) {

		try {
			log.info("fetching all appointments");
			Pageable paging = PageRequest.of(page, pageSize);
			AppointmentResponse<List<Appointment>> appointmentResponse = appointmentService.getAllAppointment(paging);
			log.info("appointmentList :{} ", appointmentResponse.getData());

			return new ResponseEntity<>(appointmentResponse, HttpStatus.FOUND);

		} catch (Exception e) {
			log.info("Exception occur while fetching appointment: {}", e.getMessage());
			return new ResponseEntity<>(
					new AppointmentResponse<>("Appointment not added. Internal Server Error", false),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	public ResponseEntity<AppointmentResponse<List<Appointment>>> getAppointmentByTitle(@PathVariable String title) {

		try {
			AppointmentResponse<List<Appointment>> appointmentResponse = appointmentService
					.getAppointmentByTitle(title);
			if (appointmentResponse.isStatus()) {
				return new ResponseEntity<>(appointmentResponse, HttpStatus.FOUND);
			} else {
				return new ResponseEntity<>(appointmentResponse, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			log.error("Eeception occur: {}", e.getMessage());
			return new ResponseEntity<>(
					new AppointmentResponse<>("Appointment not added. Internal Server Error", false),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	public ResponseEntity<String> deleteAppointmentById(Long id) {
		try {
			log.info("deleting appointment by id : {} ", id);
			if (appointmentService.deleteAppointmentById(id)) {
				return new ResponseEntity<String>("Appointment deleted successfully", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Appointment not with given id not exist ", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error("Exception occur: {}", e.getMessage());
		}
		return new ResponseEntity<String>("Appointment  not exist  with given id", HttpStatus.NOT_FOUND);

	}

	@Override
	public ResponseEntity<AppointmentResponse<List<Appointment>>> getByUserId(Long userId) {
		try {
			AppointmentResponse<List<Appointment>> appointmentResponse = appointmentService.getByUserId(userId);
			if (appointmentResponse.isStatus())
				return new ResponseEntity<AppointmentResponse<List<Appointment>>>(appointmentResponse, HttpStatus.OK);
			else
				return new ResponseEntity<AppointmentResponse<List<Appointment>>>(appointmentResponse,
						HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			log.info("Exception occur: {}", e.getMessage());
		}

		return new ResponseEntity<AppointmentResponse<List<Appointment>>>(HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<AppointmentResponse<Appointment>> getAppointmentById(@PathVariable Long id) {
		try {
			if (id != 0 && id != null) {
				log.info("get Appointment By Id : {}", id);
				AppointmentResponse<Appointment> appointmentResponse = appointmentService.getAppointmentById(id);
				log.info("appointment :{}", appointmentResponse.getData());
				return new ResponseEntity<>(appointmentResponse, HttpStatus.FOUND);
			}
		} catch (Exception e) {
			log.info("exception : {}", e);
			return new ResponseEntity<>(
					new AppointmentResponse<>("Appointment not added. Internal Server Error", false),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<AppointmentResponse<Appointment>>(HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<AppointmentResponse<Appointment>> updateAppointment(Appointment appointment, Long id) {
		try {
			log.info("updating the appointment :{}", appointment);
			if (id != null && appointment != null) {

				AppointmentResponse<Appointment> appointmentResponse = appointmentService.updateAppointment(appointment,
						id);

				return new ResponseEntity<>(appointmentResponse, HttpStatus.OK);

			} else {
				return new ResponseEntity<>(new AppointmentResponse<>("Appointment id can not be null", false),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			log.info("exception : {}", e);
			return new ResponseEntity<>(
					new AppointmentResponse<>("Appointment not added. Internal Server Error", false),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	public AppointmentResponse<Appointment> Upload(String appointment, List<MultipartFile> file) throws JsonMappingException, JsonProcessingException {
		
		try {
		Appointment appointmentResponse = new ObjectMapper().readValue(appointment, Appointment.class);  
		AppointmentResponse<Appointment> appointmentJson = appointmentService.getJson(appointment, path, file);
		return appointmentJson;
		}catch(Exception e)
		{
            log.error("Error processing appointment: {}", e.getMessage());

            return new AppointmentResponse<>("Invalid appointment data", false);
		}
		
	}

	@Override
	public ResponseEntity<Appointment> fileUpload(MultipartFile image) {
		try {
			appointmentService.uploadImage(path, image);
		} catch (Exception e) {
			log.info("Exception occur: {}", e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity<>(HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> getImage(Long userId, String imageName) throws IOException {
		
		try {
			byte[] data= appointmentService.getImage(userId,path, imageName);
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("file/jpg")).body(data);
		}catch (Exception e) {
			log.info("Exception occur: {}", e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		
		}

	}

}

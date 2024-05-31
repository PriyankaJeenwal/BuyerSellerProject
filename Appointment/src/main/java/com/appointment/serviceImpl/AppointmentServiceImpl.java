package com.appointment.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.appointment.entity.Appointment;
import com.appointment.entity.Document;
import com.appointment.repository.AppointmentDao;
import com.appointment.repository.AppointmentRepository;
import com.appointment.repository.DocumentRepository;
import com.appointment.response.AppointmentResponse;
import com.appointment.service.AppointmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AppointmentServiceImpl implements AppointmentService {

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	AppointmentDao appointmentDao;

	@Autowired
	DocumentRepository documentRepository;

	private static final String ERROR_MESSAGE = "Something went wrong";
	private static final Logger log = LoggerFactory.getLogger(AppointmentServiceImpl.class);

	@Override
	public AppointmentResponse<List<Appointment>> getByUserId(Long userId) {
		AppointmentResponse<List<Appointment>> appointmentResponse = new AppointmentResponse<List<Appointment>>();
		try {
			log.info("get appointment by user id");
			List<Appointment> appointmentList = appointmentRepository.findByuserId(userId);
			if (!appointmentList.isEmpty()) {
				appointmentResponse.setData(appointmentList);
				appointmentResponse.setMessage("success");
				appointmentResponse.setStatus(true);
				return appointmentResponse;
			}
		} catch (Exception e) {
			log.error("exception " + e.toString());
		}
		appointmentResponse.setMessage("no data found");
		appointmentResponse.setStatus(false);
		return appointmentResponse;

	}

	@Override
	public AppointmentResponse<Appointment> addAppointment(Appointment appointment) {
		AppointmentResponse<Appointment> appointmentResponse = new AppointmentResponse<Appointment>();
		log.info("appointment :{}", appointment);
		try {
			if (appointment.getTitle() != null && appointment.getAppointmentDate() != null) {

				Optional<Appointment> existingAppointment = appointmentRepository
						.findAppointmentByTitleAndUserId(appointment.getTitle().toLowerCase(), appointment.getUserId());

				log.info("existingAppointment :{}", existingAppointment);
				if (existingAppointment.isEmpty()) {
					log.info("existingAppointment is empty we can add new appointment");
					Appointment newAppointment = new Appointment();

					newAppointment.setTitle(appointment.getTitle().toLowerCase());
					newAppointment.setUserId(appointment.getUserId());
					newAppointment.setAgentId(appointment.getAgentId());
					newAppointment.setDescription(appointment.getDescription());
					newAppointment.setAppointmentDate(appointment.getAppointmentDate());

					appointmentRepository.save(newAppointment);
					List<Appointment> appointmentList = new ArrayList<Appointment>();
					appointmentList.add(newAppointment);
					appointmentResponse.setData(appointmentList);
					appointmentResponse.setMessage("appointment.getDisease().toLowerCase()success");
					appointmentResponse.setStatus(true);
					return appointmentResponse;
				}
			}

		} catch (Exception e) {
			log.error("exception in addAppointment : {}", e);
		}
		log.info("appointment already exist");
		appointmentResponse.setMessage("appointment not saved");
		appointmentResponse.setStatus(false);
		return appointmentResponse;
	}

	@Override
	public String uploadImage(String path, MultipartFile file) {
		// TODO Auto-generated method stub

		// File name

		String name = file.getOriginalFilename();
		// File Path

		String filePath = path + File.separator + name;

		// create folder if not created

		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		}

		// file copy

		try {

			File files = new File("images/" + file.getOriginalFilename());
			if (files.exists()) {
				System.out.println("file already exist");
			} else {
				Files.copy(file.getInputStream(), Paths.get(filePath));
			}
		} catch (Exception e) {
			log.info("Exception occur {}:", e.getMessage());

		}
		return filePath;
	}

	@Override
	public AppointmentResponse<List<Appointment>> getAllAppointment(Pageable paging) {

		AppointmentResponse<List<Appointment>> appointmentResponse = new AppointmentResponse<>();
		try {
			Page<Appointment> appointmentList = appointmentRepository.findAll(paging);
			List<Appointment> list = appointmentList.getContent();
			log.info("list of appointments:{} ", appointmentList);
			if (!list.isEmpty()) {
				log.info("appointmentList is not empty");
				appointmentResponse.setData(appointmentList);
				appointmentResponse.setMessage("success");
				appointmentResponse.setStatus(true);
				return appointmentResponse;
			}

		} catch (Exception e) {
			log.info("exception in getAllAppointment :{} ", e.getMessage());
		}

		appointmentResponse.setMessage("appointment not saved");
		appointmentResponse.setStatus(false);
		return appointmentResponse;

	}

	@Override
	public AppointmentResponse<List<Appointment>> getAppointmentByTitle(String title) {

		AppointmentResponse<List<Appointment>> appointmentResponse = new AppointmentResponse<>();
		try {
			List<Appointment> list = appointmentDao.findAppointmentByTitle(title);
			if (!list.isEmpty()) {
				appointmentResponse.setData(list);
				appointmentResponse.setMessage("appointment found");
				appointmentResponse.setStatus(true);
				return appointmentResponse;
			}

		} catch (Exception e) {
			log.error("exception {}", e);
		}
		appointmentResponse.setMessage("appointment not found");
		appointmentResponse.setStatus(false);
		return appointmentResponse;

	}

	@Override
	public boolean deleteAppointmentById(Long id) {

		try {
			log.info("id : {}", id);
			Optional<Appointment> appointment = appointmentRepository.findById(id);
			if (appointment.isPresent()) {
				log.info("appointment is not empty ; {}", appointment);
				appointmentRepository.deleteById(id);
				return true;
			}
		} catch (Exception e) {
			log.error("exception :{} ", e);
		}
		log.info("appointment not found with id : {}", id);
		return false;

	}

	@Override
	public AppointmentResponse<Appointment> getAppointmentById(Long id) {
		log.info("Id : {}", id);
		AppointmentResponse<Appointment> appointmentResponse = new AppointmentResponse<Appointment>();
		try {
			Optional<Appointment> appointment = appointmentRepository.findById(id);
			if (!appointment.isEmpty()) {
				Appointment appointmentObj = appointment.get();
				log.info("appointment is not empty");
				List<Appointment> appointmentList = new ArrayList<Appointment>();
				appointmentList.add(appointmentObj);
				appointmentResponse.setData(appointmentList);
				appointmentResponse.setMessage("appointment found");
				appointmentResponse.setStatus(true);
				return appointmentResponse;
			}
		} catch (Exception e) {
			log.error("exception in getAppointmentById : {}", e);
		}

		appointmentResponse.setMessage("appointment not found");
		appointmentResponse.setStatus(false);
		return appointmentResponse;

	}

	public AppointmentResponse<List<Appointment>> findAppointmentByAppointmentDate(LocalDate appointmentDate) {

		AppointmentResponse<List<Appointment>> appointmentResponse = new AppointmentResponse<>();
		try {
			List<Appointment> appointment = appointmentRepository.findAppointmentByAppointmentDate(appointmentDate);
			log.info("appointmentList:{} ", appointment);
			if (!appointment.isEmpty()) {
				log.info("appointmentList:{} ", appointment);
				appointmentResponse.setData(appointment);
				appointmentResponse.setMessage("appointment found");
				appointmentResponse.setStatus(true);
				return appointmentResponse;

			}
		} catch (Exception e) {
			log.error("exception in getTaskByCompletionDate:{} ", e);
		}
		log.info("appointment not found");
		appointmentResponse.setMessage("appointment not found");
		appointmentResponse.setStatus(false);
		return appointmentResponse;
	}

	@Override
	public AppointmentResponse<Appointment> updateAppointment(Appointment newAppointment, Long id) {
		AppointmentResponse<Appointment> appointmentResponse = new AppointmentResponse<Appointment>();
		try {
			if (id != null) {
				Optional<Appointment> appointment = appointmentRepository.findById(id);
				log.info("update appointment :{}", appointment);

				if (appointment.isPresent()) {
					log.info(" appointment is present :{}", appointment);
					Appointment appointmentObj = appointment.get();
					List<Appointment> appointmentList = new ArrayList<Appointment>();
					if (newAppointment.getTitle() != null) {
						log.info(" disease is not null :{}", newAppointment.getDescription());
						appointmentObj.setTitle(newAppointment.getTitle());
					}
					if (newAppointment.getDescription() != null) {
						log.info(" description is not null :{}", newAppointment.getDescription());
						appointmentObj.setDescription(newAppointment.getDescription());
					}
					if (newAppointment.getAppointmentDate() != null) {
						log.info(" appointmentDate is not null :{}", newAppointment.getAppointmentDate());
						appointmentObj.getAppointmentDateHistory().add(appointment.get().getAppointmentDate());
						appointmentObj.setAppointmentDate(newAppointment.getAppointmentDate());
					}
					if (newAppointment.getAgentId() != 0) {
						appointmentObj.setAgentId(newAppointment.getAgentId());
					}
					if (newAppointment.getUserId() != 0) {
						appointmentObj.setUserId(newAppointment.getUserId());
					}
					appointmentRepository.save(appointmentObj);
					appointmentList.add(appointmentObj);
					appointmentResponse.setData(appointmentList);
					appointmentResponse.setMessage("appointment updated");
					appointmentResponse.setStatus(true);
					return appointmentResponse;

				}
			}

		} catch (Exception e) {
			log.error("exception in update appointment :{} ", e);
		}
		log.info("appointment not found with id : {}", id);
		appointmentResponse.setMessage("appointment not updated");
		appointmentResponse.setStatus(false);
		return appointmentResponse;

	}

	@Override
	public AppointmentResponse<Appointment> getJson(String appointments, String path, List<MultipartFile> files)
			throws JsonMappingException, JsonProcessingException {
		AppointmentResponse<Appointment> appointmentResponse = new AppointmentResponse<Appointment>();

		ObjectMapper objectMapper = new ObjectMapper();
		Appointment appointmentobj = objectMapper.readValue(appointments, Appointment.class);
		for (MultipartFile file : files) {
			String name = file.getOriginalFilename();

			String filePath = path + File.separator + appointmentobj.getUserId() + File.separator + name;
			File f = new File(path + File.separator + appointmentobj.getUserId());
			if (!f.exists()) {
				f.mkdir();
			}

			try {
				File existFiles = new File(path + appointmentobj.getUserId() + "/" + file.getOriginalFilename());
				if (existFiles.exists()) {
					System.out.println("file already exist");
				} else {
					Files.copy(file.getInputStream(), Paths.get(filePath));
					Document document = new Document();
					document.setName(name);
					document.setUrl(filePath);
					document.setUserId(appointmentobj.getUserId());
					documentRepository.save(document);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.info("appointment :{}", appointmentobj);
		try {
			if (appointmentobj.getTitle() != null && appointmentobj.getAppointmentDate() != null) {

				Optional<Appointment> existingAppointment = appointmentRepository.findAppointmentByTitleAndUserId(
						appointmentobj.getTitle().toLowerCase(), appointmentobj.getUserId());

				log.info("existingAppointment :{}", existingAppointment);
				if (existingAppointment.isEmpty()) {
					log.info("existingAppointment is empty we can add new appointment");
					Appointment newAppointment = new Appointment();

					newAppointment.setTitle(appointmentobj.getTitle().toLowerCase());
					newAppointment.setUserId(appointmentobj.getUserId());
					newAppointment.setAgentId(appointmentobj.getAgentId());
					newAppointment.setDescription(appointmentobj.getDescription());
					newAppointment.setAppointmentDate(appointmentobj.getAppointmentDate());

					appointmentRepository.save(newAppointment);
					List<Appointment> appointmentList = new ArrayList<Appointment>();
					appointmentList.add(newAppointment);
					appointmentResponse.setData(appointmentList);
					appointmentResponse.setMessage("appointment book succesfully");
					appointmentResponse.setStatus(true);
					return appointmentResponse;
				}
			}

		} catch (Exception e) {
			log.info("exception in addAppointment : {}", e.getMessage());
		}
		log.info("appointment already exist");
		appointmentResponse.setMessage("appointment not saved");
		appointmentResponse.setStatus(false);
		return null;
	}

	@Override
	public byte[] getImage(Long userId, String path, String imageName) throws IOException {
		path = "C:\\Users\\User\\Documents\\AppointmentDocuments\\documents\\";
		String filePath = path + userId;
		// Optional<Document>
		// existDocument=documentRepository.findDocumentByNameAndUserId(userId,
		// imageName);
		// if(existDocument.isPresent()) {

		Path imagePath = Path.of(filePath, imageName);
		String getByPath = imagePath.toString();
		if (Files.exists(imagePath)) {
			// File existFiles = new
			// File(path+File.separator+userId+File.separator+imageName);

			byte[] imageBytes = Files.readAllBytes(new File(getByPath).toPath());
			return imageBytes;

		}

		// if (existFiles.exists()) {

		return null;

	}

}

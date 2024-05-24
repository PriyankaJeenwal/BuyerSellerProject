package com.user.service.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.service.entity.Appointment;
import com.user.service.entity.FileData;
import com.user.service.entity.Role;
import com.user.service.entity.User;
import com.user.service.feignClient.AppointmentClient;
import com.user.service.repository.FileDataRepository;
import com.user.service.repository.RoleRepository;
import com.user.service.repository.UserRepository;
import com.user.service.response.UserResponse;
import com.user.service.responseEntity.AppointmentResponse;
import com.user.service.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private FileDataRepository fileDataRepository;

	@Autowired
	private AppointmentClient appointmentClient;

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	private static final String ERROR_MESSAGE = "Something went wrong";
	private final String path = "C:\\Users\\User\\Documents\\AppointmentDocuments\\profile\\";

	@Override
	public UserResponse<User> createUser(User user) {
		List<User> userList = new ArrayList<User>();

		try {
			if (!Objects.equals(user.getEmail(), "") && !Objects.equals(user.getName(), "")
					&& !Objects.equals(user.getPassword(), "") && !Objects.equals(user.getRole(), "")) {
				if (user.getEmail() != null &&  user.getName() != null && user.getRole() != null
						&& user.getPassword() != null) {
					Optional<User> existUser = userRepository.findByEmail(user.getEmail());

					if (existUser.isPresent()) {

						return new UserResponse<>("User already exists", null, false);
					} else {
						Role role = user.getRole();
						Optional<Role> existRole = roleRepository.findByName(role.getName());

						if (existRole.isPresent()) {
							Role newRole = existRole.get();
							if (user.getPassword().matches("[a-zA-Z0-9@$]{8,}+")) {
								String encoded = new BCryptPasswordEncoder().encode(user.getPassword());
								user.setPassword(encoded);
								user.setRole(newRole);
								userRepository.save(user);
								return new UserResponse<>("User saved successfully", userList, true);
							} else {
								return new UserResponse<>(
										"Password should contain uppercase, lowercase, digit, and special character and length must be 8 characters",
										userList, false);
							}
						} else {
							logger.info("role is not present: {}", user.getRole());
							return new UserResponse<>("role not present", userList, false);
						}
					}
				} else {
					logger.info("user can not be null: {}", user);
					return new UserResponse<>("User cannot be null", userList, false);
				}
			} else {
				logger.info("user can not empty: {}", user);

				return new UserResponse<>("User cannot be empty", null, false);
			}
		} catch (Exception e) {
			logger.error("Error processing user data: {}", e.getMessage());
			return new UserResponse<>(ERROR_MESSAGE, null, false);
		}
	}

	@Override
	public UserResponse<User> getUserById(Long userId) {
		List<User> userList = new ArrayList<User>();
		if (userId != 0 && userId != null) {
			try {
				Optional<User> getUser = userRepository.findById(userId);
				if (getUser.isPresent()) {
					User user = getUser.get();

					// fetch task by userId
					ResponseEntity<AppointmentResponse<List<Appointment>>> appointmentByUserId = appointmentClient
							.getByUserId(user.getId());
					AppointmentResponse<List<Appointment>> body = appointmentByUserId.getBody();
					List<Appointment> appointmentList = body.getData();
					System.out.println(appointmentList);
					user.setAppointment(appointmentList);
					userList.add(user);

					return new UserResponse<User>("Users fetched successfully", userList, true);
				}

			} catch (Exception e) {
				return new UserResponse<User>(ERROR_MESSAGE, userList, false);
			}
		} else {
			logger.info("invalid userID: {}", userId);
			return new UserResponse<User>("plase assign correct userId", null, false);

		}
		logger.error("Error fetching user data for userId: {}", userId);
		return new UserResponse<User>("something went wrong", null, false);

	}

	@Override
	public UserResponse<List<User>> updateUser(Long id, User updateUser) {

		List<User> userList = new ArrayList<User>();
		try {
			if (id == null || updateUser == null) {

				throw new IllegalArgumentException("User id and updated user cannot be null");
			}

			User user = userRepository.findById(id).orElse(null);
			if (user == null) {
				return (new UserResponse<>("User not found with id: " + id, userList, false));
			}

			if (updateUser.getName() != null && !updateUser.getName().isEmpty()) {
				user.setName(updateUser.getName());
			}
			if (updateUser.getEmail() != null && !updateUser.getEmail().isEmpty()) {
				user.setEmail(updateUser.getEmail());
			}
			userList.add(user);
			userRepository.save(user);
			return new UserResponse<>("User updated successfully", userList, false);
		} catch (Exception e) {
			logger.info("Error updating user with id: {}", id, e);

			return (new UserResponse<>(ERROR_MESSAGE, userList, false));
		}
	}

	@Override
	public String deleteUser(Long id) {
		try {
		logger.info("finding the id of the User for getUserById");
		Optional<User> userOptional = userRepository.findById(id);
		if (!(userOptional.isPresent())) {
			logger.error("User ID is  not present=" + id);
			return "user is not present";
		} else {
			userRepository.deleteById(id);
			logger.info("Uesr with ID " + id + " deleted successfully.");
			return "user deleted sucessfully";
		}
		}catch (Exception e) {
			logger.info("exception occur:{}",e.getMessage());
		}
		return path;
	}

	@Override
	public UserResponse<List<User>> getAllUsers(Pageable paging) {
		List<User> newUserList = new ArrayList<User>();
		try {
			Page<User> userPage = userRepository.findAll(paging);
			List<User> userList = userPage.getContent();

			for (User user : userList) {
				ResponseEntity<AppointmentResponse<List<Appointment>>> appointmentByUserId = appointmentClient
						.getByUserId(user.getId());
				System.out.println(appointmentByUserId);
				AppointmentResponse<List<Appointment>> body = appointmentByUserId.getBody();
				List<Appointment> appointmentList = body.getData();
				System.out.println(appointmentList);
				user.setAppointment(appointmentList);
				newUserList.add(user);
			}
			System.out.println(newUserList);
			return new UserResponse<List<User>>("Users fetched successfully", newUserList, true);
		} catch (Exception e) {
			logger.info("exception occur {}", e.getMessage());
			return new UserResponse<List<User>>(ERROR_MESSAGE, null, false);
		}
	}

	@Override
	public UserResponse<List<User>> getUserByRole(String role) {
		try {
			Optional<Role> getRole = roleRepository.findByName(role);
			if (getRole.isPresent()) {
				List<User> newUserList = userRepository.findUsersByRoleName(role);
				return new UserResponse<List<User>>("Users fetched successfully", newUserList, true);

			} else {
				logger.info("role not present {}",role);
				return new UserResponse<List<User>>("Role not present", null, true);
			}

		} catch (Exception e) {
			logger.info("exception occur {}", e.getMessage());

			return new UserResponse<List<User>>("Something went wrong", null, false);
		}

	}

	@Override
	public String uploadProfile(Long userId, MultipartFile file) throws IOException {
		String filePath = path + file.getOriginalFilename();
		FileData fileData = new FileData();
		User getUser = userRepository.findById(userId).get();
		if (getUser.getId() == userId) {
			Optional<FileData> presentFile = fileDataRepository.findByUserId(userId);

			if (presentFile.isPresent()) {
				return "profile already exist";
			} else {
				fileData.setName(file.getOriginalFilename());
				fileData.setType(file.getContentType());
				fileData.setFilePath(filePath);
				fileData.setUser(getUser);
				fileDataRepository.save(fileData);

				file.transferTo(new File(filePath));
				if (fileData != null) {
					return "file uploaded successfully : " + filePath;
				}

			}
		} else {
			
			return "user is not present";
		}
		return filePath;

	}

	@Override
	public byte[] getProfile(Long userId) throws IOException {
		try {
		Optional<FileData> filedata = fileDataRepository.findByUserId(userId);
		String filepath = filedata.get().getFilePath();
		// byte[] image =Files.readAllBytes(new File(filepath).toPath());
		File file = new File(filepath);
		byte[] imageget = null;
		
			imageget = Files.readAllBytes(file.toPath());
			return imageget;

		} catch (Exception e) {
			logger.error("Error processing user data: {}", e.getMessage());
			e.printStackTrace();
		}
		return null;
		
		}
		

	@Override
	public UserResponse<User> getJson(String user, String path, MultipartFile file)
			throws IllegalStateException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		User userobj = objectMapper.readValue(user, User.class);

		String name = file.getOriginalFilename();

		String filePath = path + File.separator + file.getOriginalFilename();
		FileData fileData = new FileData();
		Optional<User> getUser = userRepository.findById(userobj.getId());
		List<User> userList = new ArrayList<User>();

		try {
			if (!Objects.equals(userobj.getEmail(), "") && !Objects.equals(userobj.getName(), "")
					&& !Objects.equals(userobj.getPassword(), "") && !Objects.equals(userobj.getRole(), "")) {
				if (!(userobj.getEmail() == null) && !(userobj.getName() == null) && !(userobj.getRole() == null)
						&& !(userobj.getPassword() == null)) {
					Optional<User> existUser = userRepository.findByEmail(userobj.getEmail());

					if (existUser.isPresent()) {

						return new UserResponse<>("User already exists", null, false);
					} else {
						Role role = userobj.getRole();
						Optional<Role> existRole = roleRepository.findByName(role.getName());

						if (existRole.isPresent()) {
							Role newRole = existRole.get();
							if (userobj.getPassword().matches("[a-zA-Z0-9@$]{8,}+")) {
								String encoded = new BCryptPasswordEncoder().encode(userobj.getPassword());

								// Optional<FileData> presentFile =
								// fileDataRepository.findByUserId(userobj.getId());

								// if (presentFile.isPresent()) {
								// } else {

								userobj.setPassword(encoded);
								userobj.setRole(newRole);

								fileData.setName(file.getOriginalFilename());
								fileData.setType(file.getContentType());
								fileData.setFilePath(filePath);
								fileData.setUser(userobj);
								file.transferTo(new File(filePath));

								userobj.setFileData(fileData);
								fileData.setUser(userobj);

								userRepository.save(userobj);
								return new UserResponse<>("User saved successfully", userList, true);
							} else {
								return new UserResponse<>(
										"Password should contain uppercase, lowercase, digit, and special character and length must be 8 characters",
										userList, false);
							}
						} else {
							logger.info("role is not present: {}", user);
							return new UserResponse<>("role not present", userList, false);
						}
					}
				} else {
					logger.info("user can not be null: {}", user);

					return new UserResponse<>("User cannot be null", userList, false);
				}
			} else {
				logger.info("user can not be empty: {}", user);

				return new UserResponse<>("User cannot be empty", userList, false);
			}
		} catch (Exception e) {
			logger.error("Error processing user data: {}", e.getMessage());

			return new UserResponse<>(ERROR_MESSAGE, userList, false);
		}
	}

//	@Override
//	public MultiValueMap<byte[], User> getUserById(Long id)throws IOException
//	{
//		try {
//			
//			Optional<User> getUser = userRepository.findById(id);
//			if (getUser.isPresent()) {
//				User user = getUser.get();
//
//
//			Optional<FileData> filedata = fileDataRepository.findByNameAndUserId(user.getFileData().getName(), id);
//			String filepath = filedata.get().getFilePath();
//			// byte[] image =Files.readAllBytes(new File(filepath).toPath());
//			Optional<FileData> image = fileDataRepository.findByNameAndUserId(user.getFileData().getName(), id);
//			String filePath2 = image.get().getFilePath();
//			File file = new File(filePath2);
//			byte[] imageget = null;
//			try {
//				imageget = Files.readAllBytes(file.toPath());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			
//			
//			// fetch appointment by userId
//				ResponseEntity<AppointmentResponse<List<Appointment>>> appointmentByUserId = appointmentClient
//						.getByUserId(user.getId());
//				AppointmentResponse<List<Appointment>> body = appointmentByUserId.getBody();
//				List<Appointment> appointmentList = body.getData();
//				System.out.println(appointmentList);
//				user.setAppointment(appointmentList);
//
//			    MultiValueMap<byte[],User> multiPartData = new LinkedMultiValueMap<>();
//			    multiPartData.add(imageget, user);
//				
//
//				return  multiPartData;
//				
//				
//				
//				//ByteArrayResource jsonResource = new ByteArrayResource(jsonData.getBytes()); 
//				//ByteArrayResource imageResource = new ByteArrayResource(imageBytes);
//				
//				}
//		} catch (Exception e) {
//				// Handle the case where the image could not be read
//				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading image file", e); 
//	}
//		
//return null;
//	}

	// compress the image bytes before storing it in the database
//	public static byte[] compressBytes(byte[] data) {
//		Deflater deflater = new Deflater();
//		deflater.setInput(data);
//		deflater.finish();
//
//		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
//		byte[] buffer = new byte[1024];
//		while (!deflater.finished()) {
//			int count = deflater.deflate(buffer);
//			outputStream.write(buffer, 0, count);
//		}
//		try {
//			outputStream.close();
//		} catch (IOException e) {
//		}
//		System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
//
//		return outputStream.toByteArray();
//	}

//	public static byte[] compressImage(byte[] data, int targetSizeInBytes) {
//        Deflater deflater = new Deflater();
//        deflater.setInput(data);
//        deflater.finish();
//
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
//        byte[] buffer = new byte[1024];
//        while (!deflater.finished()) {
//            int count = deflater.deflate(buffer);
//            outputStream.write(buffer, 0, count);
//        }
//
//        // Check if the compressed size is within the target limit
//        if (outputStream.size() > targetSizeInBytes) {
//            // Implement additional logic to further reduce the size if needed
//            // e.g., reduce image quality or dimensions
//            // ...
//        }
//
//        return outputStream.toByteArray();
//    }
//
//	// uncompress the image bytes before returning it to the angular application
//	public static byte[] decompressBytes(byte[] data) {
//		Inflater inflater = new Inflater();
//		inflater.setInput(data);
//		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
//		byte[] buffer = new byte[1024];
//		try {
//			while (!inflater.finished()) {
//				int count = inflater.inflate(buffer);
//				outputStream.write(buffer, 0, count);
//			}
//			outputStream.close();
//		} catch (IOException ioe) {
//		} catch (DataFormatException e) {
//		}
//		return outputStream.toByteArray();
//	}

}

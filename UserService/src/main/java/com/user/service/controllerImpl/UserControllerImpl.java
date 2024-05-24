package com.user.service.controllerImpl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.service.controller.UserController;
import com.user.service.entity.User;
import com.user.service.response.UserImageResponse;
import com.user.service.response.UserResponse;
import com.user.service.service.UserService;

@RestController
@RequestMapping("/user")
public class UserControllerImpl implements UserController {

	static final Logger logger = LogManager.getLogger(UserControllerImpl.class.getName());

	@Autowired
	private UserService userService;

	private static final String ERROR_MESSAGE = "Something went wrong";
	private String path = "C:\\Users\\User\\Documents\\AppointmentDocuments\\profile";

	@Override
	public ResponseEntity<UserResponse<User>> createUser(User user) {
		UserResponse<User> userResponse = new UserResponse<User>();
		try {
				userResponse = userService.createUser(user);
				return new ResponseEntity<UserResponse<User>>(userResponse, HttpStatus.CREATED);		
		} catch (Exception e) {
            logger.warn("Received null user data{}",user);
            return new ResponseEntity<UserResponse<User>>(HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<UserResponse<List<User>>> getAllUsers(Integer pageSize, Integer page) {
		UserResponse<List<User>> userResponse = new UserResponse<List<User>>();
		try {
			Pageable paging = PageRequest.of(page, pageSize);
			userResponse = userService.getAllUsers(paging);

			return new ResponseEntity<UserResponse<List<User>>>(userResponse, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.info("exception occur {}",e.getMessage());
			return new ResponseEntity<UserResponse<List<User>>>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<UserResponse<User>> getUserById(Long userId) {
		UserResponse<User> userResponse = new UserResponse<User>();
		try {
			if (userId != 0) {
				userResponse = userService.getUserById(userId);

				return new ResponseEntity<UserResponse<User>>(userResponse, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<UserResponse<User>>(HttpStatus.BAD_REQUEST);

			}
		} catch (Exception e) {
			logger.info("exception occur {}",e.getMessage());
			return new ResponseEntity<UserResponse<User>>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<UserResponse<List<User>>> updateUser(Long id, User updateUser) {
		UserResponse<List<User>> userResponse = new UserResponse<List<User>>();
		try {
			if (id != 0 && updateUser != null) {
				userResponse = userService.updateUser(id, updateUser);

				return new ResponseEntity<UserResponse<List<User>>>(userResponse, HttpStatus.OK);
			} else {
				return new ResponseEntity<UserResponse<List<User>>>(HttpStatus.BAD_REQUEST);

			}
		} catch (Exception e) {
			logger.info("Exception occur :- "+e.getMessage());
			return new ResponseEntity<UserResponse<List<User>>>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<String> deleteUser(Long id) {
		try {
			if (id != 0) {
				logger.info("Deleting User with id: {}", id);
				String existEmployee = userService.deleteUser(id);
				return ResponseEntity.status(HttpStatus.OK).body(existEmployee);
			} else {
				logger.warn("please enter the id");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("please give correct userID");

			}
		} catch (Exception e) {
			logger.info("User not deleted with id: {}", id);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public ResponseEntity<UserResponse<List<User>>> getUserByRole(String role) {
		try {
			if (role == null) {
				return new ResponseEntity<UserResponse<List<User>>>(HttpStatus.NOT_FOUND);
			} else {
				UserResponse<List<User>> userResponse = userService.getUserByRole(role);
				return new ResponseEntity<UserResponse<List<User>>>(userResponse, HttpStatus.CREATED);

			}
		} catch (Exception e) {
			logger.info("exception occur {}",e.getMessage());
			return new ResponseEntity<UserResponse<List<User>>>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<?> fileUpload(Long userId, MultipartFile image) throws IOException {
		try {
			if (userId != 0) {
				String data = userService.uploadProfile(userId, image);
				return ResponseEntity.status(HttpStatus.OK).body(data);
			} else {
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

			}
		} catch (IOException e) {
			logger.info("exception occur {}",e.getMessage());
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);

		}
	}

	@Override
	public ResponseEntity<byte[]> getProfile(Long userId) throws IOException {
		try {
			byte[] data = userService.getProfile(userId);
			System.out.println(new String(data, StandardCharsets.US_ASCII));
			logger.info("{}->{}", "FILES_GET_BY_ID:", userId);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(data);
		} catch (IOException e) {
			
			logger.info("exception occur {}",e.getMessage());
			return (ResponseEntity<byte[]>) ResponseEntity.notFound();
		}
	}

	@Override
	public UserResponse<User> Upload(String user, MultipartFile file) throws IllegalStateException, IOException {
	try {
		if(user!=null)
		{
		User appointmentResponse = new ObjectMapper().readValue(user, User.class);

		System.out.println(appointmentResponse);

		UserResponse<User> appointmentJson = userService.getJson(user, path, file);
		return appointmentJson;
		}
			//return new  UserResponse<User>("");
	
		}catch(Exception e) {
			return new  UserResponse<User>("Exception occur :- "+e.getMessage(),null,false);
		
	}
	return null;
	
	}

//	
////	 image and data in response old one
//	@Override
//	public ResponseEntity<MultiValueMap<byte[], UserResponse<List<User>>>> getUserByIdAndImage(Long userId)
//			throws IOException {
//		try {
//			if (userId != 0) {
//				UserResponse<List<User>> user = userService.getUserById(userId);
//				byte[] data = userService.getProfile(userId);
//				logger.info("{}->{}", "FILES_GET_BY_ID:", userId);
//
//
//				HttpHeaders userHeaders = new HttpHeaders();
//				userHeaders.setContentType(MediaType.APPLICATION_JSON);
//				HttpEntity<UserResponse<List<User>>> userEntity = new HttpEntity<>(user, userHeaders);
//				
//				// Add image data
//				HttpHeaders imageHeaders = new HttpHeaders();
//				imageHeaders.setContentType(MediaType.IMAGE_JPEG);
//				HttpEntity<byte[]> imageEntity = new HttpEntity<>(data, imageHeaders);
//			//map.add(data, user);
////////map.addAll(map);
//      //MultiValueMap<byte[], UserResponse<List<User>>> map = new LinkedMultiValueMap<>();
//				MultiValueMap<byte[], UserResponse<List<User>>> map = new LinkedMultiValueMap<>();
//				map.add(data, user);
//				return new ResponseEntity<>(map,HttpStatus.OK);
//
////	        return ResponseEntity
////	        		.ok()
////	        		.contentType(MediaType.APPLICATION_JSON)
////	        		.body(map);
//
//			} else {
//				return ResponseEntity.notFound().build();
//			}
//
//		} catch (Exception e) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading image file", e);
//		}
//
//	}
//
//	
	// working code
	@Override
	public ResponseEntity<UserImageResponse> getUserByIdAndImage(Long userId) throws IOException {
		try {
			if (userId != 0) {
				UserResponse<User> user = userService.getUserById(userId);
				byte[] data = userService.getProfile(userId);
				String imageData = Base64.getEncoder().encodeToString(data);

				System.out.println(new String(data, StandardCharsets.US_ASCII));
				UserImageResponse response = new UserImageResponse(user.getMessage(), user.getUserData(), data,

						user.isStatus(), imageData);
				System.out.println("-------------------------im");
				System.out.println(imageData);
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);

				return new ResponseEntity<>(response, headers, HttpStatus.OK);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading image file", e);
		}
	}

}

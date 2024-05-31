package com.user.service.controller;

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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.user.service.entity.User;
import com.user.service.response.UserImageResponse;
import com.user.service.response.UserResponse;

public interface UserController {

	@PostMapping("/createUser")
	public ResponseEntity<UserResponse<User>> createUser(@RequestBody User user);

	@GetMapping("/getUsers")
	public ResponseEntity<UserResponse<List<User>>> getAllUsers(
			@RequestParam(defaultValue = "5", required = false) Integer pageSize,
			@RequestParam(defaultValue = "0", required = false) Integer page);

	@GetMapping("/{userId}")
	public ResponseEntity<UserResponse<User>> getUserById(@PathVariable Long userId);

	@PostMapping("/updateUser")
	public ResponseEntity<UserResponse<List<User>>> updateUser(@RequestParam Long id, @RequestBody User updateUser)
			throws Exception;

	@PostMapping("/deleteUser")
	public ResponseEntity<String> deleteUser(@RequestParam Long id);

	@GetMapping("/getUsersByRole")
	public ResponseEntity<UserResponse<List<User>>> getUserByRole(String role);

	@PostMapping("/upload")
	public ResponseEntity<?> fileUpload(@RequestParam("userId") Long userId, @RequestParam("image") MultipartFile image)
			throws IOException;

	@GetMapping("/getProfile")
	public ResponseEntity<byte[]> getProfile(@RequestParam("userId") Long userId) throws IOException;

	@PostMapping(value = "/uploads", consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE })
	public UserResponse<User> Upload(@RequestPart("user") String user, @RequestPart("file") MultipartFile file) 
			throws JsonMappingException, JsonProcessingException, IllegalStateException, IOException;

	@GetMapping("/image/{userId}")
	public ResponseEntity<UserImageResponse> getUserByIdAndImage(@PathVariable("userId") Long userId)throws IOException;
}

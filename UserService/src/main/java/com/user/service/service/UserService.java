package com.user.service.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.user.service.entity.User;
import com.user.service.response.UserResponse;

import jakarta.ws.rs.core.MultivaluedMap;

@Service
public interface UserService {

	public UserResponse<User> createUser(User user);

	public UserResponse<List<User>> getAllUsers(Pageable paging);

	public UserResponse<User> getUserById(Long id);

	public UserResponse<List<User>> updateUser(Long id, User updateUser) throws Exception;

	public String deleteUser(Long id);

	public UserResponse<List<User>> getUserByRole(String role);

	public String uploadProfile(Long userId, MultipartFile file) throws IOException;

	public byte[] getProfile(Long userId) throws IOException;
	
	public UserResponse<User> getJson(String user,String path, MultipartFile file) throws JsonMappingException, JsonProcessingException, IllegalStateException, IOException;

	//public MultiValueMap<byte[], User> getUserById(Long id) throws IOException;


}

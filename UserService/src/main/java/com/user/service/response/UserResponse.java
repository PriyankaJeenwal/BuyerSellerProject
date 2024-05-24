package com.user.service.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponse<T> {

	private String message;
	
	private Object userData;
	private boolean status;

	public UserResponse(String message, Object userData, boolean status) {
		this.message = message;
		this.userData = userData;
		this.status = status;
	}
}
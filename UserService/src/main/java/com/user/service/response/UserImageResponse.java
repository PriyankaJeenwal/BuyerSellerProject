package com.user.service.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserImageResponse {
	private String message;
	private Object userData;
	private byte[] imageData;
	private boolean status;
	private String userImage;

	public UserImageResponse(String message, Object userData, byte[] imageData, boolean status,String userImage) {
		this.message = message;
		this.userData = userData;
		this.imageData = imageData;
		this.status = status;
	
	this.userImage = userImage;
	}
}
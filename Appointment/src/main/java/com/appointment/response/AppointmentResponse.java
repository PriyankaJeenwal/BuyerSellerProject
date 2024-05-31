package com.appointment.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class AppointmentResponse<T> {
	private Object data;

	private String message;

	private boolean status;

	public AppointmentResponse(String message, boolean status) {
		super();

		this.message = message;
		this.status = status;
	}

}

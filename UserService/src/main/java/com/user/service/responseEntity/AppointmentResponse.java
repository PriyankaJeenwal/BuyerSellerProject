package com.user.service.responseEntity;



import java.util.List;

import com.user.service.entity.Appointment;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class AppointmentResponse<T> {

	private List<Appointment> data;

	private String message;

	private boolean status;

	public AppointmentResponse(String message, boolean status) {
		super();
		this.message = message;
		this.status = status;
	}
}
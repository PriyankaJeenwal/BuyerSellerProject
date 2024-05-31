package com.user.service.feignClient;



import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import com.user.service.entity.Appointment;
import com.user.service.responseEntity.AppointmentResponse;



@FeignClient(url = "http://localhost:9091", value = "AppointmentClient")

public interface AppointmentClient {
	@GetMapping("/appointment/user/{userId}")
	public ResponseEntity<AppointmentResponse<List<Appointment>>> getByUserId(@PathVariable Long userId);

}

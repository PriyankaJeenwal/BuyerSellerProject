package com.appointment.schedular;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.appointment.entity.Appointment;
import com.appointment.repository.AppointmentRepository;

@Component
public class Scheduler {

	@Autowired
	AppointmentRepository appointmentRepository;

	@Scheduled(fixedRate = 60000)
	public void scheduleNotification() {
		System.out.println("scheduler");
		LocalDateTime currentTime = LocalDateTime.now();

		List<Appointment> appointmentList = appointmentRepository.findAll();
		for (Appointment appointment : appointmentList) {

			System.out.println(currentTime);
			Date appointmentDate = appointment.getAppointmentDate();
			System.out.println(appointmentDate);
			System.out.println(appointment.getAppointmentDate());
			LocalDateTime ldt = LocalDateTime.ofInstant(appointmentDate.toInstant(),
                    ZoneId.systemDefault());
			if (currentTime.withSecond(0).withNano(0).equals(ldt.minusHours(1).withSecond(0).withNano(0))) {
				System.out.println("your appointment" + appointment.getTitle() + " is near complition date");
			}
		}
	}

}

package com.appointment.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.appointment.entity.Appointment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class AppointmentDao {

	@Autowired
	EntityManager em;

	public List<Appointment> findAppointmentByTitle(String title) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Appointment> cq = cb.createQuery(Appointment.class);

		Root<Appointment> appointment = cq.from(Appointment.class);
		Predicate diseasePredicate = cb.equal(appointment.get("title"), title);
		cq.where(diseasePredicate);
		TypedQuery<Appointment> query = em.createQuery(cq);
		return query.getResultList();
	}
}

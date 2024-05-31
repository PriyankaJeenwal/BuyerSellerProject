package com.user.service.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.user.service.entity.Role;



@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	public Optional<Role> findByName(String name);

}


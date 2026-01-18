package com.example.CommunityClinicDemo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CommunityClinicDemo.Entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor,Long> {
	public List<Doctor> findByCommunityClinicId(Long id);

}

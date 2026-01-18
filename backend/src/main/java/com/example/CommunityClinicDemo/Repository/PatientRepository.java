package com.example.CommunityClinicDemo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CommunityClinicDemo.Entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
   public List<Patient> findByDoctorId(Long id);
   public List<Patient> findByCommunityClinicId(Long id);
   public Optional<Patient> findByTC(String tc);
}

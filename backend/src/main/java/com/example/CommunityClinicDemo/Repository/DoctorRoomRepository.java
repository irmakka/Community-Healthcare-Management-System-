package com.example.CommunityClinicDemo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CommunityClinicDemo.Entity.DoctorRoom;

public interface DoctorRoomRepository extends JpaRepository<DoctorRoom, Long> {
   public Optional<DoctorRoom> findByDoctorId(Long id);
   public List<DoctorRoom> findByFloor(int floor);
   public List<DoctorRoom> findByCommunityClinicId(Long id);
   
}

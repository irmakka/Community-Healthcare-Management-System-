package com.example.CommunityClinicDemo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CommunityClinicDemo.Entity.CommunityClinic;

public interface CommunityClinicRepository extends JpaRepository<CommunityClinic,Long> {
   public List<CommunityClinic> findByCity(String city);
}

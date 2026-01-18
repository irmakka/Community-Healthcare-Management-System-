package com.example.CommunityClinicDemo.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
@Entity
public class DoctorRoom {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	int floor;
    int roomNumber;
    
    @OneToOne(mappedBy = "doctorRoom",orphanRemoval = false)
    @JsonManagedReference("doctor-room")
    Doctor doctor;
    
    @ManyToOne
	@JsonBackReference("clinic-rooms")
	CommunityClinic communityClinic;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getFloor() {
		return floor;
	}
	public void setFloor(int floor) {
		this.floor = floor;
	}
	public int getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}
	public Doctor getDoctor() {
		return doctor;
	}
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
	public CommunityClinic getCommunityClinic() {
		return communityClinic;
	}
	public void setCommunityClinic(CommunityClinic communityClinic) {
		this.communityClinic = communityClinic;
	}
	
}

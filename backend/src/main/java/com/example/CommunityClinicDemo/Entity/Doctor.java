package com.example.CommunityClinicDemo.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Doctor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String name,surname;
	
	@ManyToOne
	@JsonBackReference("clinic-doctors")
	CommunityClinic communityClinic;
	
	@OneToMany(mappedBy="doctor",orphanRemoval =false)
	@JsonManagedReference("doctor-patients")
	List<Patient> patients;
	
	//I didn't make orphanRemoval true to assign it other doctor later.
	@OneToOne
	@JsonBackReference("doctor-room")
	DoctorRoom doctorRoom;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public CommunityClinic getCommunityClinic() {
		return communityClinic;
	}
	public void setCommunityClinic(CommunityClinic communityClinic) {
		this.communityClinic = communityClinic;
	}

	public List<Patient> getPatients() {
		return patients;
	}

	public void setPatients(List<Patient> patients) {
		this.patients = patients;
	}	
	
	public void addPatient(Patient p) {
	 this.patients.add(p);
	 p.setDoctor(this);
	}
	public DoctorRoom getDoctorRoom() {
		return doctorRoom;
	}
	public void setDoctorRoom(DoctorRoom doctorRoom) {
		this.doctorRoom = doctorRoom;
	}
	public void deletePatient(Patient p) {
		this.patients.remove(p);
	}

	
	
}

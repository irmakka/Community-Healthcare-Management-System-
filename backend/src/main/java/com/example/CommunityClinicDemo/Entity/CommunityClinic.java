package com.example.CommunityClinicDemo.Entity;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class CommunityClinic{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String name;
	String phoneNumber;
	String city;
	String address;
	
	//I want to hold the patient and doctor records even clinic is deleted.
	@JsonIgnore
	@OneToMany(mappedBy="communityClinic",orphanRemoval = false)
	@JsonManagedReference("clinic-doctors")
	List<Doctor> doctors;
	
	@JsonIgnore
	@OneToMany(mappedBy="communityClinic",orphanRemoval =false)
	@JsonManagedReference("clinic-patients")
	List<Patient> patients;
	
	@JsonIgnore
	@OneToMany(mappedBy="communityClinic",cascade=CascadeType.ALL,orphanRemoval = true)
	@JsonManagedReference("clinic-rooms")
	List<DoctorRoom> rooms;
	
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

	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public List<Doctor> getDoctors() {
		return doctors;
	}
	public void setDoctors(List<Doctor> doctors) {
		this.doctors = doctors;
	}
	public List<Patient> getPatients() {
		return patients;
	}
	public void setPatients(List<Patient> patients) {
		this.patients = patients;
	}
	
	public void addDoctor(Doctor d) {
		this.doctors.add(d);
		d.setCommunityClinic(this);
	}
	public void addPatient(Patient p) {
		this.patients.add(p);
	    p.setCommunityClinic(this);
	}
	public void removeDoctor(Doctor d) {
		this.doctors.remove(d);
	}
	public void removePatient(Patient p) {
		this.patients.remove(p);
	}
	public List<DoctorRoom> getRooms() {
		return rooms;
	}
	public void setRooms(List<DoctorRoom> rooms) {
		this.rooms = rooms;
	}
	
	public void addRoom(DoctorRoom room) {
      this.rooms.add(room);
        room.setCommunityClinic(this);
    }
	public void removeRoom(DoctorRoom room) {
		this.rooms.remove(room);
	}
	
}

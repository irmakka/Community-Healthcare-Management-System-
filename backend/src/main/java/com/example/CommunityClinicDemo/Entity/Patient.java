package com.example.CommunityClinicDemo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Patient {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
Long id;

String name,surname;
String phoneNumber;
int age;
String TC;

@ManyToOne
@JsonBackReference("clinic-patients")
CommunityClinic communityClinic;

@ManyToOne
@JsonBackReference("doctor-patients")
Doctor doctor;

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

public String getPhoneNumber() {
	return phoneNumber;
}

public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
}

public int getAge() {
	return age;
}

public void setAge(int age) {
	this.age = age;
}

public String getTC() {
	return TC;
}

public void setTC(String tC) {
	this.TC = tC;
}

public CommunityClinic getCommunityClinic() {
	return communityClinic;
}

public void setCommunityClinic(CommunityClinic communityClinic) {
	this.communityClinic = communityClinic;
}

public Doctor getDoctor() {
	return doctor;
}

public void setDoctor(Doctor doctor) {
	this.doctor = doctor;
}

}

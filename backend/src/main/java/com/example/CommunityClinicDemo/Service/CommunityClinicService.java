package com.example.CommunityClinicDemo.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.CommunityClinicDemo.Entity.CommunityClinic;
import com.example.CommunityClinicDemo.Entity.Doctor;
import com.example.CommunityClinicDemo.Entity.DoctorRoom;
import com.example.CommunityClinicDemo.Entity.Patient;
import com.example.CommunityClinicDemo.Repository.CommunityClinicRepository;
import com.example.CommunityClinicDemo.Repository.DoctorRepository;
import com.example.CommunityClinicDemo.Repository.DoctorRoomRepository;
import com.example.CommunityClinicDemo.Repository.PatientRepository;

@Service
public class CommunityClinicService {
	@Autowired
    CommunityClinicRepository clinicRep;
	
	@Autowired
    DoctorRepository docRep;
	
	@Autowired
    PatientRepository patientRep;
	
	@Autowired
	DoctorRoomRepository roomRep;
	
	
	public List<CommunityClinic> getClinics(){
		 return clinicRep.findAll();
	}
	public Optional <CommunityClinic> getAClinic(Long id){
		return clinicRep.findById(id);
	}
	public List<CommunityClinic> getClinicsByCity( String city){
		return clinicRep.findByCity(city);
	}
	public String saveClinic( CommunityClinic clinic) {
		clinicRep.save(clinic);
		return "Clinic Saved";
	}
	
	public String deleteClinic(Long id) {
		CommunityClinic clinic= clinicRep.findById(id).get();
		clinic.getDoctors().stream().forEach(d->{d.setCommunityClinic(null);
		 d.setDoctorRoom(null); 
		 docRep.save(d);
		});
		clinic.getPatients().stream().forEach(p->{p.setCommunityClinic(null);
		patientRep.save(p);
		}
		);
		
		clinicRep.deleteById(id);
		return "Deleted";
	}
	
	
	public List<Doctor> getClinicDoctors( Long id){
		 return docRep.findByCommunityClinicId(id);
	}
	public List<Patient> getClinicPatients( Long id){
		 return patientRep.findByCommunityClinicId(id);
	}
	public List<DoctorRoom> getClinicRooms(Long id){
		 return  roomRep.findByCommunityClinicId(id);
	}
	
	public String assignDoctor( Long clinicId, Long doctorId) {
		CommunityClinic clinic= clinicRep.findById(clinicId).get();
		Doctor doctor=docRep.findById(doctorId).get();
		clinic.addDoctor(doctor);
		docRep.save(doctor);
		return "Doctor Assigned";
	}
	public String assignRoom( Long clinicId,Long  roomId) {
		CommunityClinic clinic= clinicRep.findById(clinicId).get();
		DoctorRoom  room=roomRep.findById(roomId).get();
		clinic.addRoom(room);
		roomRep.save(room);
		return "Room Assigned";
	}
	
	public String removePatientFromClinic( Long clinicId, Long patientId) {
		CommunityClinic clinic= clinicRep.findById(clinicId).get();
		Patient patient=patientRep.findById(patientId).get();
		clinic.removePatient(patient);
		patient.getDoctor().deletePatient(patient);
		patient.setDoctor(null);
		patient.setCommunityClinic(null);
		patientRep.save(patient);
		return "Patient Deleted From This Clinic";
	}
	
	public String removeDoctorFromClinic( Long clinicId, Long doctorId) {
	
		Optional<CommunityClinic> clinicOpt = clinicRep.findById(clinicId);
		Optional<Doctor> doctorOpt = docRep.findById(doctorId);
		
		if (clinicOpt.isEmpty()) {
			return "Clinic is  not found";
		}
		if (doctorOpt.isEmpty()) {
			return "Doctor is  not found";
		}
		
		CommunityClinic clinic = clinicOpt.get();
		Doctor doctor = doctorOpt.get();

		if (clinic.getDoctors() != null) {
			clinic.getDoctors().remove(doctor);
		}
		
		if (doctor.getPatients() != null && !doctor.getPatients().isEmpty()) {
			for(Patient p : doctor.getPatients()) {
				if (p != null) {
					p.setDoctor(null);
					patientRep.save(p);
				}
			}
		}
		
		if (doctor.getDoctorRoom() != null) {
			doctor.setDoctorRoom(null);
		}
		
		doctor.setCommunityClinic(null);
	
		docRep.save(doctor);
		
		return "Doctor Deleted From This Clinic";
	}
	
	
	public String assignPatient( Long clinicId, Long patientId) {
		CommunityClinic clinic= clinicRep.findById(clinicId).get();
		Patient patient=patientRep.findById(patientId).get();
		clinic.addPatient(patient);
		patientRep.save(patient);
		return "Patient Assigned";
	}
	
	
	
    public String updateClicic( CommunityClinic c,Long id) {
     CommunityClinic clinic = clinicRep.findById(id).get();
   	        clinic.setName(c.getName());
   	        clinic.setPhoneNumber(c.getPhoneNumber());
   	        clinic.setCity(c.getCity());
   	        clinic.setAddress(c.getAddress());
   	        clinicRep.save(clinic);
   	        return "Clinic updated";
	}
    
	  public List<DoctorRoom> GetEmptyRooms(Long id){
		  List<DoctorRoom> temp_rooms= new ArrayList<DoctorRoom>();
		  for(DoctorRoom room : clinicRep.findById(id).get().getRooms().stream().filter(r->r.getDoctor()==null).toList()) {
			  temp_rooms.add(room);
		  }
			 return temp_rooms; 
			  
		  }
}


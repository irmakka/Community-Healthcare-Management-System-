package com.example.CommunityClinicDemo.Service;

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
public class DoctorService {
	@Autowired
	DoctorRepository doctorRep;
	@Autowired
	CommunityClinicRepository comRep;
	@Autowired
	PatientRepository patientRep;
	@Autowired
	DoctorRoomRepository roomRep;

	
	public List<Doctor> getDoctors(){
		 return doctorRep.findAll();
	}

	public Optional <Doctor> getDoctor( Long id){
		return doctorRep.findById(id);
	}
    public String addDoctor(Doctor d) {
       doctorRep.save(d);
       return "Doctor Saved";
    }
    
    public String deleteDoctor(Long id) {
    	Optional<Doctor> doctorOpt = doctorRep.findById(id);
    	if (!doctorOpt.isPresent()) {
    		return "Doctor not found";
    	}
    	Doctor doctor = doctorOpt.get();
    	if (doctor.getCommunityClinic() != null) {
    		CommunityClinic clinic = doctor.getCommunityClinic();
    		if (clinic.getDoctors() != null) {
    			clinic.getDoctors().remove(doctor);
    		}
    		
    		doctor.setCommunityClinic(null);
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
    		DoctorRoom room = doctor.getDoctorRoom();
    		if (room != null) {
    			room.setDoctor(null);
    			roomRep.save(room);
    		}
    	}
    	doctor.setDoctorRoom(null);
    	doctorRep.save(doctor);
    	doctorRep.deleteById(id);
    	
		return "Doctor Deleted";
    }
    
    public List<Patient> getPatients( Long id){
    	Doctor doctor= doctorRep.findById(id).get();
    	 return doctor.getPatients();
    	
    }
    public String assignPatient(Long doctorId, Long patientId) {
    	Doctor doctor=doctorRep.findById(doctorId).get();
    	Patient patient= patientRep.findById(patientId).get();
    	doctor.addPatient(patient);
    	patientRep.save(patient);
		return "Assigned Patient";
    	
    }
    public String assignDoctorRoom(Long doctorId, Long roomId) {
    	Doctor doctor=doctorRep.findById(doctorId).get();
    	DoctorRoom  doctorRoom=roomRep.findById(roomId).get();
    	doctor.setDoctorRoom(doctorRoom);
    	doctorRoom.setDoctor(doctor);
    	roomRep.save(doctorRoom);
    	doctorRep.save(doctor);
		return "Assigned Room";
    	
    }
    public String removePatient (Long id, Long patientId) {
    	 Doctor doctor=doctorRep.findById(id).get();
    	 Patient p= patientRep.findById(patientId).get();
    	 doctor.deletePatient(p);
    	 p.setDoctor(null);
    	 patientRep.save(p);
		return "Deleted From Doctor";
    }
    public String removeRoom( Long id,Long roomId) {
    	 Doctor doctor=doctorRep.findById(id).get();
    	 DoctorRoom room=roomRep.findById(roomId).get();
    	 doctor.setDoctorRoom(null);
    	 room.setDoctor(null);
    	 doctorRep.save(doctor);
    	 roomRep.save(room);
   	     return "room is deleted from doctor";
   }
    public String updateDoctor(Doctor d,Long id) {
   	 Doctor doctor = doctorRep.findById(id).get();
   	        doctor.setName(d.getName());
   	        doctor.setSurname(d.getSurname());
   	        doctorRep.save(doctor);
   	        return "Doctor updated";
	}

}
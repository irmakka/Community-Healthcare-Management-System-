package com.example.CommunityClinicDemo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.CommunityClinicDemo.Entity.Patient;
import com.example.CommunityClinicDemo.Repository.PatientRepository;

@Service
public class PatientService {
	@Autowired
    PatientRepository patientRep;


	public List<Patient> getPatients(){
		 return patientRep.findAll();
	}
	
	public Optional <Patient> getPatient( Long id){
		return patientRep.findById(id);
	}
    
    public List<Patient> getPatientsByDoctorId( Long id){
    	return patientRep.findByDoctorId(id);
    }
     public Optional<Patient> getPatientByTC(String tc){
    	 return patientRep.findByTC(tc);
     }
     
     public String addPatient( Patient p) {
    	 patientRep.save(p);
    	 return "Patient Created";
     }
    public String deletePatient(Long id) {
    	Patient p= patientRep.findById(id).get();
    	p.getDoctor().getPatients().remove(p);
    	p.getCommunityClinic().getPatients().remove(p);
    	patientRep.deleteById(id);
		return  "Patient deleted";
    }
    public String updatePatient( Patient p, Long id) {
    	 Patient optionalPatient = patientRep.findById(id).get();
    	        optionalPatient.setName(p.getName());
    	        optionalPatient.setTC(p.getTC());
    	        optionalPatient.setAge(p.getAge());
    	        patientRep.save(optionalPatient);
    	        return "Patient updated";
	}
    
}

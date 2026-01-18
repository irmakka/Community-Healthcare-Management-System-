package com.example.CommunityClinicDemo.Controller;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.CommunityClinicDemo.Entity.Patient;
import com.example.CommunityClinicDemo.Service.PatientService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("patients")
public class PatientController {
	@Autowired
    PatientService patientServ;

	@GetMapping
	public List<Patient> getAllPatients(){
		 return patientServ.getPatients();
	}
	@GetMapping("{id}")
	public Optional <Patient> getAPatient(@PathVariable Long id){
		return patientServ.getPatient(id);
	}
    @GetMapping("doctorId/{id}")
    public List<Patient> getAllPatientsByDoctorId(@PathVariable Long id){
    	return patientServ.getPatientsByDoctorId(id);
    }
    
    @GetMapping("tc/{tc}")
    public Optional<Patient> getAPatientByTC(@PathVariable String tc){
   	 return patientServ.getPatientByTC(tc);
    }
    
    @PostMapping("save")
    public String addAPatient(@RequestBody Patient p) {
        patientServ.addPatient(p);
        return "Patient Created";
    }

    @DeleteMapping("{id}")
    public String deleteAPatient(@PathVariable Long id) {
        patientServ.deletePatient(id);
        return "Patient deleted";
    }
	
    @PutMapping("update/{id}")
    public String updateAPatient( @RequestBody Patient p,@PathVariable Long id) {
   	   patientServ.updatePatient(p,id);
   	        return "Patient updated";
	}
	
	
	
}

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

import com.example.CommunityClinicDemo.Entity.Doctor;
import com.example.CommunityClinicDemo.Entity.Patient;
import com.example.CommunityClinicDemo.Service.DoctorService;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("doctors")
public class DoctorController{
@Autowired
DoctorService docServ;

@GetMapping
public List<Doctor> getAllDoctors(){
	 return docServ.getDoctors();
}
@GetMapping("{id}")
public Optional <Doctor> getADoctor(@PathVariable Long id){
	return docServ.getDoctor(id);
}

@PostMapping("save")
public String addADoctor(@RequestBody Doctor d) {
	docServ.addDoctor(d);
	return "Doctor Added";
}
@DeleteMapping("{id}")
public String deleteADoctor(@PathVariable Long id) {
	docServ.deleteDoctor(id);
	return "deleted";
}
@GetMapping("{id}/patients")
public List<Patient> getPatients(@PathVariable Long id){
	return docServ.getPatients(id);
	
}

@GetMapping("{doctorId}/assignPatient/{patientId}")
public String assignAPatient(@PathVariable Long doctorId,@PathVariable Long patientId) {
   docServ.assignPatient(doctorId, patientId);
	return "Assigned Patient";
}
@GetMapping("{doctorId}/assignRoom/{roomId}")
public String assignADoctorRoom( @PathVariable Long doctorId,@PathVariable  Long roomId) {
	docServ.assignDoctorRoom(doctorId, roomId);
	return "Assigned Room";
	
}
@DeleteMapping("{id}/deletePatient/{patientId}")
public String removeAPatient(@PathVariable Long id,@PathVariable Long patientId) {
	 docServ.removePatient(id, patientId);
	 return "deleted from doctor";
}
@DeleteMapping("{id}/deleteRoom/{roomId}")
public String removeARoom(@PathVariable Long id,@PathVariable Long roomId) {
	 docServ.removeRoom(id, roomId);
	 return "room is deleted from doctor";
}
@PutMapping("update/{id}")
public String updateADoctor(@RequestBody  Doctor d,@PathVariable Long id) {
  	        docServ.updateDoctor(d,id);
  	        return "Doctor updated";
	}

}

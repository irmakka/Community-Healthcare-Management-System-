package  com.example.CommunityClinicDemo.Controller;

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
import com.example.CommunityClinicDemo.Entity.CommunityClinic;
import com.example.CommunityClinicDemo.Entity.Doctor;
import com.example.CommunityClinicDemo.Entity.DoctorRoom;
import com.example.CommunityClinicDemo.Entity.Patient;
import com.example.CommunityClinicDemo.Service.CommunityClinicService;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("clinics")
public class CommunityClinicController {
	@Autowired
    CommunityClinicService clinicServ;
  
	@GetMapping
	public List<CommunityClinic> getAllClinics(){
		 return clinicServ.getClinics();
	}
	@GetMapping("{id}")
	public Optional <CommunityClinic> getAClinic(@PathVariable Long id){
		return clinicServ.getAClinic(id);
	}
	@GetMapping("city/{city}")
	public List<CommunityClinic> getClinicsByACity(@PathVariable String city){
		return clinicServ.getClinicsByCity(city);
	}
	@PostMapping("save")
	public String saveAClinic(@RequestBody CommunityClinic clinic) {
		 return clinicServ.saveClinic(clinic);
	}
	
	@DeleteMapping("{id}")
	public String deleteAClinic(@PathVariable Long id) {
		return clinicServ.deleteClinic(id);
	}
	
	@GetMapping("{id}/doctors")
	public List<Doctor> getDoctorsByClinic(@PathVariable Long id){
		 return clinicServ.getClinicDoctors(id);
	}
	@GetMapping("{id}/patients")
	public List<Patient> getPatientsByClinic(@PathVariable Long id){
		 return clinicServ.getClinicPatients(id);
	}
	
	@GetMapping("{id}/rooms")
	public List<DoctorRoom> getRoomsByClinic(@PathVariable Long id){
		 return clinicServ.getClinicRooms(id);
	}
	

	@DeleteMapping("{clinicId}/removePatient/{patientId}")
	public String removeAPatientFromClinic(@PathVariable Long clinicId,@PathVariable Long patientId) {
	    clinicServ.removePatientFromClinic(clinicId, patientId);
		return "Patient Deleted From This Clinic";
	}

	@DeleteMapping("{clinicId}/removeDoctor/{doctorId}")
	public String removeADoctorFromClinic(@PathVariable Long clinicId,@PathVariable Long doctorId) {
		clinicServ.removeDoctorFromClinic(clinicId, doctorId);
		return "Doctor Deleted From This Clinic";
	}
	
	@GetMapping("{clinicId}/assignDoctor/{doctorId}")
	public String assignClinicADoctor(@PathVariable Long clinicId,@PathVariable Long doctorId) {
		return clinicServ.assignDoctor(clinicId, doctorId);
	}
	
	@GetMapping("{clinicId}/assignPatient/{patientId}")
	public String assignClinicAPatient(@PathVariable Long clinicId,@PathVariable Long patientId) {
		return clinicServ.assignPatient(clinicId, patientId);
	}
	
	@GetMapping("{clinicId}/assignRoom/{roomId}")
	public String assignClinicARoom(@PathVariable Long clinicId,@PathVariable Long  roomId) {
		return clinicServ.assignRoom(clinicId, roomId);
	}
	@PutMapping("update/{id}")
	public String updateAClinic(@RequestBody  CommunityClinic c,@PathVariable Long  id) {
	  	        clinicServ.updateClicic(c,id);
	  	        return "Clinic  updated";
		}

	 @GetMapping("{id}/emptyRooms")
	  public List<DoctorRoom> GetEmptyRooms(@PathVariable Long  id){
		 return  clinicServ.GetEmptyRooms(id);
		  
	  }
	

}

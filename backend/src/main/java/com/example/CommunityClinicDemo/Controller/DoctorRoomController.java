package com.example.CommunityClinicDemo.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.CommunityClinicDemo.Entity.DoctorRoom;
import com.example.CommunityClinicDemo.Service.DoctorRoomService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("rooms")
public class DoctorRoomController {
	@Autowired
    DoctorRoomService roomServ;
    
	@GetMapping
	public List<DoctorRoom> getAllRooms(){
		 return roomServ.getRooms();
	}
	@GetMapping("{id}")
	public Optional <DoctorRoom> getARoom(@PathVariable Long id){
		 return roomServ.getRoom(id);
	}
	
	
	@GetMapping("doctor/{id}")
	public Optional <DoctorRoom> getARoomByDoctor(@PathVariable Long id){
		return roomServ.getRoomByDoctor(id);
	}

	@GetMapping("floor/{floor}")
	public List<DoctorRoom> getRoomsByAFloor(@PathVariable int floor){
		return roomServ.getRoomsByFloor(floor);
	}
	@PostMapping("save")
	public String addARoom(@RequestBody   DoctorRoom r) {
	    roomServ.addRoom(r);
		return " room is created";
	}
	  @PutMapping("update/{id}")
	  public String updateARoom(@RequestBody   DoctorRoom r ,@PathVariable  Long id) {
		   	 roomServ.updateRoom(r,id);
		   	        return "Room updated";
			}
	  

}

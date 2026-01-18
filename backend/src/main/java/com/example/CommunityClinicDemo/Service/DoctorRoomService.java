package com.example.CommunityClinicDemo.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.CommunityClinicDemo.Entity.DoctorRoom;
import com.example.CommunityClinicDemo.Repository.DoctorRepository;
import com.example.CommunityClinicDemo.Repository.DoctorRoomRepository;

@Service
public class DoctorRoomService {
	
	@Autowired
    DoctorRoomRepository roomRep;
    @Autowired
    DoctorRepository doctorRep;
	
	public List<DoctorRoom> getRooms(){
		 return roomRep.findAll();
	}
	public Optional <DoctorRoom> getRoom(Long id){
		 return roomRep.findById(id);
	}
	
	public Optional <DoctorRoom> getRoomByDoctor(Long id){
		return roomRep.findByDoctorId(id);
	}
	
	public List<DoctorRoom> getRoomsByFloor(int floor){
		return roomRep.findByFloor(floor);
	}
	public String addRoom(DoctorRoom r) {
	    roomRep.save(r);
		return " room is created";
	}
	  public String updateRoom(DoctorRoom r,Long id) {
		   	 DoctorRoom room = roomRep.findById(id).get();
		   	        room.setFloor(r.getFloor());
		   	        room.setRoomNumber(r.getRoomNumber());
		   	        roomRep.save(room);
		   	        return "Room updated";
			}
	 
	
}

package com.example.hotel.room.controller;

import com.example.hotel.room.model.Room;
import com.example.hotel.room.repo.RoomRepository;
import com.example.hotel.room.service.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//This marks the class as a REST controller, meaning it handles Http api requests
@RestController
//All routes in this controller will start with "/api/rooms"
@RequestMapping("/api/rooms")
public class RoomController {

	
	// Injects the RoomRepository for direct DB access if needed
	@Autowired
    private final RoomRepository roomRepository;

	
	// Injects the RoomService to handle business logic
    @Autowired
    private RoomService roomService;

    // Constructor injection for the repository
    RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
    
    
    // It creates a new room using the details provided in the request body
    @PostMapping("/add")
    public Room createRoom(@RequestBody Room room) {
        return roomService.createRoom(room);
        
    }
    
    // It returns a list of all rooms (booked and available)
    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    // It returns only rooms that are currently available (not booked)
    @GetMapping("/available")
    public List<Room> getAvailableRooms() {
        return roomService.getAvailableRooms();
    }
    // to update a room's booking status
    @PutMapping("/{roomNumber}/status")
    public Room updateStatus(@PathVariable int roomNumber, @RequestParam boolean status) {
        return roomService.updateRoomStatus(roomNumber, status);
    }

}

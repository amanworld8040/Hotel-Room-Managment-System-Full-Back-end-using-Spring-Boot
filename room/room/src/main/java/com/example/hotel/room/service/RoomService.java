package com.example.hotel.room.service;

import com.example.hotel.room.model.Room;
import com.example.hotel.room.repo.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;
    
    public Optional<Room> getRoomByRoomNumber(int roomNumber) {
        return roomRepository.findByRoomNumber(roomNumber);
    }

    
    public boolean existsByRoomNumber(int roomNumber) {
        return roomRepository.existsByRoomNumber(roomNumber);
    }
    public Room addRoom(Room room) {
        if (roomRepository.findByRoomNumber(room.getRoomNumber()).isPresent()) {
            return null; // Duplicate room number
        }
        return roomRepository.save(room);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Optional<Room> findAvailableRoom(String roomType, int persons) {
        return roomRepository.findAll().stream()
                .filter(r -> !r.isBooked() && r.getRoomType().equalsIgnoreCase(roomType) && r.getNumberOfPersons() == persons)
                .findFirst();
    }

    public void updateRoom(Room room) {
        roomRepository.save(room);
    }
    public void deleteRoom(Room room) {
        roomRepository.delete(room);
    }

    public Optional<Room> getRoomByNumber(int roomNumber) {
    	
        return roomRepository.findByRoomNumber(roomNumber);
        
    }
    public Room createRoom(Room room) {
        return addRoom(room); // or your logic for creating a room
    }

    public List<Room> getAllAvailableRooms() {
        return roomRepository.findAll()
                .stream()
                .filter(room -> !room.isBooked())
                .toList();
    }


    public Room updateRoomStatus(int roomNumber, boolean status) {
        Optional<Room> optionalRoom = roomRepository.findByRoomNumber(roomNumber);
        if (optionalRoom.isEmpty()) {
            throw new RuntimeException("Room not found");
        }
        Room room = optionalRoom.get();
        room.setBooked(status);
        return roomRepository.save(room);
    }

	public Optional<Room> findClosestRoomByPersons(int persons) {
	    List<Room> availableRooms = roomRepository.findAll()
	        .stream()
	        .filter(r -> !r.isBooked())
	        .collect(Collectors.toList());

	    // Find room with equal or next higher capacity
	    return availableRooms.stream()
	        .filter(r -> r.getNumberOfPersons() >= persons)
	        .sorted(Comparator.comparingInt(Room::getNumberOfPersons))
	        .findFirst();
	}
	public List<Room> getAvailableRooms() {
	    return roomRepository.findAll()
	            .stream()
	            .filter(room -> !room.isBooked())
	            .toList();
	}
	public boolean isRoomAvailable(String roomType, int persons) {
	    List<Room> availableRooms = getAllAvailableRooms();

	    return availableRooms.stream()
	            .anyMatch(r -> r.getRoomType().equalsIgnoreCase(roomType) &&
	                           r.getNumberOfPersons() >= persons);
	}



}

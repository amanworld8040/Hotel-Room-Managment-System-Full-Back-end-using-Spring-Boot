package com.example.hotel.room.repo;

import com.example.hotel.room.model.Booking;
import com.example.hotel.room.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomNumber(int roomNumber);
    
    boolean existsByRoomNumber(int roomNumber);
    List<Room> findByBookedFalse();
}


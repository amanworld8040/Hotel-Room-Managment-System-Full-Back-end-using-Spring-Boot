package com.example.hotel.room.repo;

import com.example.hotel.room.model.Booking;
import com.example.hotel.room.model.User;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByRoomId(Long roomId);
	List<Booking> findByUser(User user);
   
}

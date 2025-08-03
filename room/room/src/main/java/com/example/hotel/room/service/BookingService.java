package com.example.hotel.room.service;

import com.example.hotel.room.model.Booking;
import com.example.hotel.room.model.Room;
import com.example.hotel.room.model.User;
import com.example.hotel.room.repo.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.sql.Date;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public Booking bookRoom(User user, Room room, Date checkIn, Date checkOut, int days, int persons) {
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setCheckIn(checkIn);
        booking.setCheckOut(checkOut);
        booking.setNumberOfDays(days);
        booking.setNumberOfPersons(persons);
        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findByUser(user);
    }


    public List<Booking> getBookingsByRoom(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
 
}

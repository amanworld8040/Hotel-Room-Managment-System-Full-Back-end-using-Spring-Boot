package com.example.hotel.room.model;

import jakarta.persistence.*;

import java.sql.Date;

import java.util.List;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;// Primary key, auto-generated

    @Column(unique = true)
    private int roomNumber;

    public int getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	private String roomType;

    private boolean booked;

    private int numberOfPersons;

    private int numberOfDays;

    private Date checkIn;

    private Date checkOut;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Booking> bookings;

	public boolean isBooked() {
		return booked;
	}

	public void setBooked(boolean booked) {
		this.booked = booked;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
	    roomType = roomType.trim().toLowerCase();
	    if (roomType.equals("suite") || roomType.equals("deluxe") || roomType.equals("standard")) {
	        this.roomType = roomType;
	    } else {
	        throw new IllegalArgumentException("❌ Invalid room type. Please enter Suite, Deluxe, or Standard.");
	    }
	}

	public int getNumberOfPersons() {
		return numberOfPersons;
	}

	public void setNumberOfPersons(int numberOfPersons) {
		this.numberOfPersons = numberOfPersons;
	}

	public int getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(int numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	public Date getCheckOut() {
		return checkOut;
	}

	public void setCheckOut(Date checkOut) {
		this.checkOut = checkOut;
	}

	public Date getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(Date checkIn) {
		this.checkIn = checkIn;
	}
}

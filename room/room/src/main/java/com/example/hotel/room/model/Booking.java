package com.example.hotel.room.model;

import jakarta.persistence.*;

import java.sql.Date;

import com.example.hotel.room.RoomApplication.RoomTypeLimits;



@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Primary key, auto-generated

 

	@ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	private Date checkIn;

    private Date checkOut;

    private int numberOfDays;

    private int numberOfPersons;

	public Date getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(Date checkIn) {
		this.checkIn = checkIn;
	}

	public Date getCheckOut() {
		return checkOut;
	}

	public void setCheckOut(Date checkOut) {
		this.checkOut = checkOut;
	}

	public int getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(int numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	public int getNumberOfPersons() {
		return numberOfPersons;
	}

	public void setNumberOfPersons(int numberOfPersons) {
	    if (this.room == null || this.room.getRoomType() == null) {
	        throw new IllegalArgumentException("❌ Room must be set and have a valid type before setting number of persons.");
	    }

	    String roomType = this.room.getRoomType().toLowerCase();
	    int allowed = RoomTypeLimits.getMaxPersons(roomType);

	    if (numberOfPersons < 1 || numberOfPersons > allowed) {
	        throw new IllegalArgumentException("❌ " + roomType + " allows max " + allowed + " person(s).");
	    }

	    this.numberOfPersons = numberOfPersons;
	}


	   public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}
    
}

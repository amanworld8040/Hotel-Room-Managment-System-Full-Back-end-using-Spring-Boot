package com.example.hotel.room.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key, auto-generated

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;
    
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    public String getName() {
		return name;
	}

    public void setName(String name) {
        if (name == null || !name.matches("^[A-Za-z ]+$")) {
            throw new IllegalArgumentException("❌ Invalid name. Name should only contain alphabets.");
        }
        this.name = name;
    }


	public String getEmail() {
		return email;
	}

	public void setEmail(String email){
	    if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
	        throw new IllegalArgumentException("❌ Invalid email format.");
	    }
	    this.email = email;
	}

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("❌ Username cannot be empty.");
        }
        this.username = username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 5) {
            throw new IllegalArgumentException("❌ Password must be at least 5 characters long.");
        }
        this.password = password;
    }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if (role == null || 
           (!role.equalsIgnoreCase("admin") && !role.equalsIgnoreCase("customer"))) {
            throw new IllegalArgumentException("❌ Role must be 'admin' or 'customer'. Please try again.");
        }
        this.role = role.toLowerCase();
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}

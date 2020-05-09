package com.techelevator.jdbc;

import java.util.List;

import com.techelevator.dataclass.Reservation;
import com.techelevator.dataclass.Venue;

public interface VenueDAO {
	
	List<Venue> selectAllVenues();
	Reservation addVenueToReservation(Reservation reservation, Venue venue);
	List<String> getVenueCategories(int venueId);
	List<Integer> getVenuesByCategoryIdAndAccessibility(Reservation reservation);
	void getVenueNameFromSpaceId(Reservation reservation);
}

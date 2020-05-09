package com.techelevator.jdbc;

import java.util.List;

import com.techelevator.dataclass.Reservation;
import com.techelevator.dataclass.Space;
import com.techelevator.dataclass.Venue;

public interface SpaceDAO {
	
	List<Space> viewSpacesForSelectedVenue(Venue venue);
	List<Space> getAvailableSpaces(Reservation reservation);
	List<Space> getAdvancedSearchSpaces(Reservation reservation);

}

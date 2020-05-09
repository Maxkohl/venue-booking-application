package com.techelevator.jdbc;

import java.util.List;

import com.techelevator.dataclass.Reservation;
import com.techelevator.dataclass.Space;

public interface ReservationDAO {
	
	List<Reservation> selectAllReservations();
	void submitReservation(Reservation reservation);
	List<Reservation> viewThirtyDayReservations();


}

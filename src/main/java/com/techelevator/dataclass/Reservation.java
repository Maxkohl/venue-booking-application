package com.techelevator.dataclass;

import java.time.LocalDate;

public class Reservation  extends Space{
	
	private LocalDate reservationStartDate;
	private LocalDate reservationEndDate;
	private int reservationId;
	private String reserveredFor;
	private int numberOfAttendees;
	
	
	public int getReservationId() {
		return reservationId;
	}
	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
	}
	public LocalDate getReservationStartDate() {
		return reservationStartDate;
	}
	public void setReservationStartDate(LocalDate reservationStartDate) {
		this.reservationStartDate = reservationStartDate;
	}
	public LocalDate getReservationEndDate() {
		return reservationEndDate;
	}
	public void setReservationEndDate(LocalDate reservationEndDate) {
		this.reservationEndDate = reservationEndDate;
	}
	
	
	public String getReserveredFor() {
		return reserveredFor;
	}
	public void setReserveredFor(String reserveredFor) {
		this.reserveredFor = reserveredFor;
	}
	public int getNumberOfAttendees() {
		return numberOfAttendees;
	}
	public void setNumberOfAttendees(int numberOfAttendees) {
		this.numberOfAttendees = numberOfAttendees;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Reservation other = (Reservation) obj;
		if (reservationStartDate == null) {
			if (other.reservationStartDate == null) {
				return false;
			}
		} else if (!reservationStartDate.equals(other.reservationStartDate)) {
			return false;
		}
		if (reservationEndDate == null) {
			if (other.reservationEndDate == null) {
				return false;
			}
		} else if (!reservationEndDate.equals(other.reservationEndDate)) {
			return false;
		}
		if (reservationId != other.reservationId) {
			return false;
		}
		return true;
	}
	
	

}

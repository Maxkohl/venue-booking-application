package com.techelevator;

import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.jdbc.JDBCReservationDAO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.dataclass.City;
import com.techelevator.dataclass.Reservation;
import com.techelevator.dataclass.State;
import com.techelevator.dataclass.Venue;
import com.techelevator.jdbc.JDBCReservationDAO;
import com.techelevator.jdbc.JDBCVenueDAO;

public class JDBCReservationDAOIntegrationTest extends DAOIntegrationTest {

	private JDBCReservationDAO dao;
	private JdbcTemplate jdbcTemplate;

	@Before
	public void setup() {
		dao = new JDBCReservationDAO(getDataSource());
		jdbcTemplate = new JdbcTemplate(getDataSource());
	}

	@Test
	public void select_all_reservations_size_check() {
		Reservation reservation = makeReservation();
		List<Reservation> reservations = dao.selectAllReservations();

		String sql = "INSERT INTO reservation VALUES (?, ?, ?, ?, ?, ?) ";
		jdbcTemplate.update(sql, reservation.getReservationId(), reservation.getSpaceId(),
				reservation.getNumberOfAttendees(), reservation.getReservationStartDate(),
				reservation.getReservationEndDate(), reservation.getReserveredFor());
		List<Reservation> results = dao.selectAllReservations();

		Assert.assertNotNull(results);
		Assert.assertTrue(reservations.size() + 1 == results.size());
	}

	@Test
	public void get_all_reservations_check_added_reservation() {
		Reservation reservation = makeReservation();
		List<Reservation> reservations = dao.selectAllReservations();

		String sql = "INSERT INTO reservation VALUES (?, ?, ?, ?, ?, ?) ";
		jdbcTemplate.update(sql, reservation.getReservationId(), reservation.getSpaceId(),
				reservation.getNumberOfAttendees(), reservation.getReservationStartDate(),
				reservation.getReservationEndDate(), reservation.getReserveredFor());

		List<Reservation> results = dao.selectAllReservations();
		Reservation answer = new Reservation();
		for (Reservation result : results) {
			if (result.equals(reservation)) {
				answer = result;
			}
		}

		Assert.assertNotNull(results);
		Assert.assertEquals(reservation, answer);

	}

	@Test
	public void select_all_reservations_within_thirty_days_size_check() {
		Reservation reservation = makeReservation();
		List<Reservation> reservations = dao.viewThirtyDayReservations();

		String sql = "INSERT INTO reservation VALUES (?, ?, ?, ?, ?, ?) ";
		jdbcTemplate.update(sql, reservation.getReservationId(), reservation.getSpaceId(),
				reservation.getNumberOfAttendees(), reservation.getReservationStartDate(),
				reservation.getReservationEndDate(), reservation.getReserveredFor());
		List<Reservation> results = dao.viewThirtyDayReservations();

		Assert.assertNotNull(results);
		Assert.assertTrue(reservations.size() + 1 == results.size());
	}
	
	@Test
	public void select_all_reservations_within_thirty_days_contents_check() {
		Reservation reservation = makeReservation();
		List<Reservation> reservations = dao.viewThirtyDayReservations();

		dao.submitReservation(reservation);

		List<Reservation> results = dao.viewThirtyDayReservations();
		Reservation answer = new Reservation();
		for (Reservation result : results) {
			if (result.equals(reservation)) {
				answer = result;
			} 
		}
		Assert.assertNotNull(results);
		Assert.assertEquals(reservation, answer);
	}

	@Test
	public void succesfully_add_reservation_size_check() {
		Reservation reservation = makeReservation();
		List<Reservation> reservations = dao.selectAllReservations();

		dao.submitReservation(reservation);

		List<Reservation> results = dao.selectAllReservations();

		Assert.assertNotNull(results);
		Assert.assertTrue(reservations.size() + 1 == results.size());
	}

	@Test
	public void succesfully_add_reservation_reservation_id_check() {
		Reservation reservation = makeReservation();
		List<Reservation> reservations = dao.selectAllReservations();

		dao.submitReservation(reservation);

		List<Reservation> results = dao.selectAllReservations();
		Reservation answer = new Reservation();
		for (Reservation result : results) {
			if (result.equals(reservation)) {
				answer = result;
			}
		}
		Assert.assertNotNull(results);
		Assert.assertEquals(reservation, answer);
	}

	private Reservation makeReservation() {
		Reservation reservation = new Reservation();
		reservation.setReservationId(999);
		reservation.setSpaceId(3);
		reservation.setVenueName("The Bird House");
		reservation.setSpaceName("Room Full of Birds");
		reservation.setReservationStartDate(LocalDate.of(2020, 03, 11));
		reservation.setReservationEndDate(LocalDate.of(2020, 03, 14));
		reservation.setReserveredFor("Bobby Birdboy");
		reservation.setNumberOfAttendees(500);
		reservation.setOpenMonth(3);
		reservation.setCloseMonth(8);
		reservation.setMaxOccupancy(999);
		reservation.setAccessible(true);
		reservation.setDailyRate(new BigDecimal(9999));
		reservation.setVenueId(99);
		reservation.setDescription("So many birds please someone send help or at least a bird wrangler.");

		return reservation;

	}

}

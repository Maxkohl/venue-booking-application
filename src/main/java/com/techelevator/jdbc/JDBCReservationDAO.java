package com.techelevator.jdbc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.dataclass.Reservation;
import com.techelevator.dataclass.Space;
import com.techelevator.dataclass.Venue;

public class JDBCReservationDAO implements ReservationDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Reservation> selectAllReservations() {
		List<Reservation> reservations = new ArrayList<Reservation>();
		String sql = "SELECT reservation_id, space.id AS space_id, number_of_attendees, space.name AS space_name, venue.name AS venue_name, start_date, end_date, reserved_for "
				+ "FROM reservation " + "JOIN space ON reservation.space_id = space.id "
				+ "JOIN venue on venue.id = space.venue_id ";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
		while (rows.next()) {
			reservations.add(mapRowToReservation(rows));
		}
		return reservations;
	}

	@Override
	public List<Reservation> viewThirtyDayReservations() {
		List<Reservation> reservations = new ArrayList<Reservation>();
		String sql = "SELECT reservation_id, space.id AS space_id, number_of_attendees, space.name AS space_name, venue.name AS venue_name, start_date, end_date, reserved_for "
				+ "FROM reservation " + "JOIN space ON reservation.space_id = space.id "
				+ "JOIN venue on venue.id = space.venue_id "
				+ "WHERE start_date BETWEEN CURRENT_DATE AND (CURRENT_DATE + 30) " + "ORDER BY start_date ";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
		while (rows.next()) {
			reservations.add(mapRowToReservation(rows));
		}
		return reservations;
	}

	@Override
	public void submitReservation(Reservation reservation) {

		String sql = "INSERT INTO reservation VALUES(DEFAULT, ?, ?, ?, ?, ?) ";

		jdbcTemplate.update(sql, reservation.getSpaceId(),
				reservation.getNumberOfAttendees(), reservation.getReservationStartDate(), reservation.getReservationEndDate(),
				reservation.getReserveredFor());
		
		String selectSql = "SELECT MAX(reservation_id) AS reservation_id FROM reservation ";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(selectSql);
		rows.next();
		reservation.setReservationId(rows.getInt( "reservation_id" ));
	}

	private Reservation mapRowToReservation(SqlRowSet rows) {

		Reservation reservation = new Reservation();
		reservation.setReservationId(rows.getInt("reservation_id"));
		reservation.setSpaceId(rows.getInt("space_id"));
		reservation.setNumberOfAttendees(rows.getInt("number_of_attendees"));
		reservation.setReservationStartDate(LocalDate.parse(rows.getString("start_date")));
		reservation.setReservationEndDate(LocalDate.parse(rows.getString("end_date")));
		reservation.setReserveredFor(rows.getString("reserved_for"));
		reservation.setVenueName(rows.getString("venue_name"));
		reservation.setSpaceName(rows.getString("space_name"));

		return reservation;
	}

}

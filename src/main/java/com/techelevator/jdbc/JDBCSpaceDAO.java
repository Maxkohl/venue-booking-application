package com.techelevator.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.dataclass.Reservation;
import com.techelevator.dataclass.Space;
import com.techelevator.dataclass.Venue;

public class JDBCSpaceDAO implements SpaceDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCSpaceDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Space> viewSpacesForSelectedVenue(Venue venue) {
		List<Space> spaces = new ArrayList<Space>();
		String sql = "SELECT space.id AS space_id, venue_id, space.name AS space_name, is_accessible, open_from, open_to, CAST(daily_rate AS decimal(8,2)), max_occupancy, venue.name AS venue_name " + 
				"FROM space " + 
				"JOIN venue ON space.venue_id = venue.id " + 
				"WHERE venue_id = ?ORDER BY venue_id, space.name ";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, venue.getVenueId());
		while (rows.next()) {
			spaces.add(mapRowToSpace(rows));
		}
		return spaces;
	}

	@Override
	public List<Space> getAvailableSpaces(Reservation reservation) {
		List<Space> availableSpaces = new ArrayList<Space>();
		
		String sql = "SELECT space.id AS space_id, space.name AS space_name, open_from, open_to, max_occupancy, is_accessible, daily_rate::decimal, venue.name AS venue_name, venue_id "
				+ "FROM space "
				+"JOIN venue ON space.venue_id = venue.id "
				+ "WHERE space.id NOT IN ( SELECT space.id FROM space FULL OUTER JOIN reservation ON space.id = reservation.space_id "
				+ "FULL OUTER JOIN venue ON space.venue_id = venue.id WHERE (((open_from IS NOT NULL) AND  ((? NOT BETWEEN open_from AND open_to) OR (? NOT BETWEEN open_from AND open_to))) "
				+ "OR ((((? BETWEEN start_date AND end_date) OR (? BETWEEN start_date AND end_date)) AND (start_date IS NOT NULL)) OR (? > max_occupancy)) OR (? != venue.id))) ";

		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, reservation.getOpenMonth(), reservation.getCloseMonth(),
				reservation.getReservationStartDate(), reservation.getReservationEndDate(),
				reservation.getNumberOfAttendees(), reservation.getVenueId());

		while (rows.next()) {
			availableSpaces.add(mapRowToSpace(rows));
		}

		return availableSpaces;

	}
	
	@Override
	public List<Space> getAdvancedSearchSpaces(Reservation reservation) {
		List<Space> availableSpaces = new ArrayList<Space>();
		String sql = "SELECT space.id AS space_id, space.name AS space_name, open_from, open_to, max_occupancy, is_accessible, daily_rate::decimal, venue.name AS venue_name, venue_id " + 
				"FROM space " + 
				"JOIN venue ON space.venue_id = venue.id " + 
				"WHERE space.id NOT IN ( SELECT space.id FROM space FULL OUTER JOIN reservation ON space.id = reservation.space_id " + 
				"FULL OUTER JOIN venue ON space.venue_id = venue.id WHERE (((open_from IS NOT NULL) AND ((? NOT BETWEEN open_from AND open_to) OR (? NOT BETWEEN open_from AND open_to))) " + 
				"OR ((((? BETWEEN start_date AND end_date) OR (? BETWEEN start_date AND end_date)) AND (start_date IS NOT NULL)) OR (? > max_occupancy)) OR (? <= daily_rate::decimal))) "
				+ "ORDER BY venue.name, space.name ";

		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, reservation.getOpenMonth(), reservation.getCloseMonth(),
				reservation.getReservationStartDate(), reservation.getReservationEndDate(),
				reservation.getNumberOfAttendees(), reservation.getDailyRate());

		while (rows.next()) {
			availableSpaces.add(mapRowToSpace(rows));
		}

		return availableSpaces;

	}

	private Space mapRowToSpace(SqlRowSet rows) {
		Space space = new Space();
		space.setSpaceId(rows.getInt("space_id"));
		space.setSpaceName(rows.getString("space_name"));
		space.setOpenMonth(rows.getInt("open_from"));
		space.setCloseMonth(rows.getInt("open_to"));
		space.setMaxOccupancy(rows.getInt("max_occupancy"));
		space.setAccessible(rows.getBoolean("is_accessible"));
		space.setDailyRate(rows.getBigDecimal("daily_rate"));
		space.setVenueName(rows.getString("venue_name"));
		space.setVenueId(rows.getInt("venue_id"));

		return space;

	}
	
	public void reserveSpace(Space space, Reservation reservation) {
		reservation.setSpaceId(space.getSpaceId());
		reservation.setSpaceName(space.getSpaceName());
		reservation.setOpenMonth(space.getOpenMonth());
		reservation.setCloseMonth(space.getCloseMonth());
		reservation.setMaxOccupancy(space.getMaxOccupancy());
		reservation.setAccessible(space.isAccessible());
		reservation.setDailyRate(space.getDailyRate());
	}
	

}

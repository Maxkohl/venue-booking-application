package com.techelevator.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.dataclass.Reservation;
import com.techelevator.dataclass.Venue;

public class JDBCVenueDAO implements VenueDAO{
	private JdbcTemplate jdbcTemplate;
	
	public JDBCVenueDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Venue> selectAllVenues() {
		List<Venue> venues = new ArrayList<Venue>();
		String sql = "SELECT venue.id, venue.name, venue.description, city.name AS city_name, city.state_abbreviation AS state_code "
				+ "FROM venue JOIN city ON venue.city_id = city.id "
				+ " ORDER BY venue.name ";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
		while (rows.next()) {
			venues.add(mapRowToVenue(rows));
		}
		return venues;
	}
	
	@Override
	public Reservation addVenueToReservation(Reservation reservation, Venue venue) {
		reservation.setCityId(venue.getCityId());
		reservation.setVenueId(venue.getVenueId());
		reservation.setVenueName(venue.getVenueName());
		reservation.setDescription(venue.getDescription());
		reservation.setCityName(venue.getCityName());
		reservation.setStateCode(venue.getStateCode());
		reservation.setCategories(getVenueCategories(venue.getVenueId()));
		
		return reservation;
		
	}
	
	@Override
	public List<String> getVenueCategories(int venueId) {
		List<String> categories = new ArrayList<String>();
		String sql = "SELECT category.name AS category_name FROM venue " 
				+ "JOIN category_venue ON category_venue.venue_id = id "
				+ "JOIN category ON category.id = category_venue.category_id "
				+ "WHERE venue.id = ? ";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, venueId);
		while (rows.next()) {
			categories.add(mapCategoryToVenue(rows));
		}
		return categories;
	}
	
	@Override
	public void getVenueNameFromSpaceId(Reservation reservation) {
		String sql = "SELECT venue.name AS venue_name FROM venue JOIN space ON venue.id = space.venue_id WHERE space.id = ?";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, reservation.getSpaceId());
		rows.next();
		reservation.setVenueName(rows.getString("venue_name"));
	}
	
	
	@Override
	public List<Integer> getVenuesByCategoryIdAndAccessibility(Reservation reservation) {
		List<Integer> venueIDList = new ArrayList<Integer>();
		String sql = "SELECT category_venue.venue_id FROM category_venue " + 
				"JOIN venue on venue.id = category_venue.venue_id " + 
				"JOIn space on space.venue_id = venue.id " + 
				"WHERE category_id = ? AND is_accessible = ?;";
		
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, reservation.getCategoryId(), reservation.isAccessible());
		while (rows.next()) {
			venueIDList.add(rows.getInt("venue_id"));
		}
		return venueIDList;
		
	}
	
	private Venue mapRowToVenue(SqlRowSet rows) {
		Venue venue = new Venue();
		venue.setVenueId(rows.getInt("id"));
		venue.setVenueName(rows.getString("name"));
		venue.setDescription(rows.getString("description"));
		venue.setCityName(rows.getString("city_name"));
		venue.setStateCode(rows.getString("state_code"));
		
		return venue;
		
	}
	
	private String mapCategoryToVenue(SqlRowSet rows) {
		String result = rows.getString("category_name");
		return result;
		
	}

}

package com.techelevator;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.dataclass.City;
import com.techelevator.dataclass.Reservation;
import com.techelevator.dataclass.State;
import com.techelevator.dataclass.Venue;
import com.techelevator.jdbc.JDBCVenueDAO;

public class JDBCVenueDAOIntegrationTest extends DAOIntegrationTest{

	private JDBCVenueDAO dao;
	private JdbcTemplate jdbcTemplate;
	
	@Before
	public void setup() {
		dao = new JDBCVenueDAO(getDataSource());
		jdbcTemplate = new JdbcTemplate(getDataSource());
	}
	
	@Test
	public void get_all_venues_size_check() {
		Venue venue = makeVenue();
		City city = makeCity();
		State state = makeState();
		List<Venue> venues = dao.selectAllVenues();
		
		String sqlState = "INSERT INTO state VALUES(?, ?) ";
		jdbcTemplate.update(sqlState, state.getStateCode(), state.getStateName());
		
		String sqlCity = "INSERT INTO city VALUES(?, ?, ?) ";
		jdbcTemplate.update(sqlCity, city.getCityId(), city.getCityName(), city.getStateCode());
		
		String sqlVenue = "INSERT INTO venue VALUES(?, ?, ?, ?) ";
		jdbcTemplate.update(sqlVenue, venue.getVenueId(), venue.getVenueName(), venue.getCityId(), venue.getDescription());
		List<Venue> results = dao.selectAllVenues();
		
		Assert.assertNotNull(results);
		Assert.assertTrue(venues.size() + 1 == results.size());
		
	}
	
	@Test
	public void get_all_venues_check_added_venue() {
		Venue venue = makeVenue();
		City city = makeCity();
		State state = makeState();
		List<Venue> venues = dao.selectAllVenues();
		
		String sqlState = "INSERT INTO state VALUES(?, ?) ";
		jdbcTemplate.update(sqlState, state.getStateCode(), state.getStateName());
		
		String sqlCity = "INSERT INTO city VALUES(?, ?, ?) ";
		jdbcTemplate.update(sqlCity, city.getCityId(), city.getCityName(), city.getStateCode());
		
		String sqlVenue = "INSERT INTO venue VALUES(?, ?, ?, ?) ";
		jdbcTemplate.update(sqlVenue, venue.getVenueId(), venue.getVenueName(), venue.getCityId(), venue.getDescription());
		List<Venue> results = dao.selectAllVenues();
		Venue answer = new Venue();
		for ( Venue result : results) {
			if (result.equals(venue)) {
				answer = result;
			}
		}
		
		Assert.assertNotNull(results);
		Assert.assertEquals(venue, answer);
		
	}
	
	@Test 
	public void succesfully_add_venue_to_reservation() {
		Venue venue = makeVenue();
		City city = makeCity();
		State state = makeState();
		Reservation reservation = new Reservation();
		dao.addVenueToReservation(reservation, venue);
	
		Assert.assertTrue(venue.getVenueName() == reservation.getVenueName());
		Assert.assertTrue(venue.getVenueId() == reservation.getVenueId());
		Assert.assertTrue(venue.getCityId() == reservation.getCityId());
		Assert.assertTrue(venue.getCityName() == reservation.getCityName());
		Assert.assertTrue(venue.getDescription() == reservation.getDescription());
		Assert.assertTrue(venue.getStateCode() == reservation.getStateCode());
		Assert.assertEquals(venue.getCategories(), reservation.getCategories());
	}
	
	@Test
	public void get_categories_by_venue_id_size_check() {
		Venue venue = makeVenue();
		City city = makeCity();
		State state = makeState();
		
		String sqlVenue = "INSERT INTO venue VALUES(?, ?, ?, ?) ";
		jdbcTemplate.update(sqlVenue, venue.getVenueId(), venue.getVenueName(), venue.getCityId(), venue.getDescription());
		
		String sql = "INSERT INTO category_venue VALUES(?,?) ";
		jdbcTemplate.update(sql,venue.getVenueId(), venue.getCategoryId());
		
		List<String> categoryList = dao.getVenueCategories(venue.getVenueId());

		Assert.assertNotNull(categoryList);
		Assert.assertTrue(categoryList.size() == 1);
	}
	
	@Test
	public void get_categories_by_venue_id_content_check() {
		Venue venue = makeVenue();
		venue.setCategories(makeCategoryList());
		
		String sqlVenue = "INSERT INTO venue VALUES(?, ?, ?, ?) ";
		jdbcTemplate.update(sqlVenue, venue.getVenueId(), venue.getVenueName(), venue.getCityId(), venue.getDescription());
		
		String sql = "INSERT INTO category_venue VALUES(?,?) ";
		jdbcTemplate.update(sql,venue.getVenueId(), venue.getCategoryId());
		
		List<String> results = dao.getVenueCategories(venue.getVenueId());
		
		
		Assert.assertNotNull(results);
		Assert.assertEquals(venue.getCategories().get(0), results.get(0));
	}
	
	@Test
	public void get_venue_name_from_space_id_content_check() {
		Reservation reservation = new Reservation();

		reservation.setSpaceId(20);
		
		dao.getVenueNameFromSpaceId(reservation);
		String result = reservation.getVenueName();
		
		Assert.assertNotNull(result);
		Assert.assertEquals("Smirking Stone Bistro", result);
		
	}
	
	
	private Venue makeVenue() {
		Venue venue = new Venue();
		venue.setVenueName("The Bird House");
		venue.setVenueId(17000);
		venue.setCityId(3);
		venue.setDescription("There are alot of birds in a house");
		venue.setCategoryId(1);

		return venue;
	}
	
	private City makeCity() {
		City city = new City();
		city.setCityId(27000);
		city.setCityName("The city of America");
		city.setStateCode("xy");
		return city;
	}
	
	private State makeState() {
		State state = new State();
		state.setStateCode("xy");
		state.setStateName("Bird Town");
		return state;
	}
	
	private List<String> makeCategoryList() {
		List<String> list = new ArrayList<String>();
		list.add("Family Friendly");
		list.add("Rustic");
		list.add("Historic");
		
		return list;
	}
	
	
}

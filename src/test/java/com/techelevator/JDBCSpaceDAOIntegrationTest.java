package com.techelevator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.dataclass.City;
import com.techelevator.dataclass.Reservation;
import com.techelevator.dataclass.Space;
import com.techelevator.dataclass.State;
import com.techelevator.dataclass.Venue;
import com.techelevator.jdbc.JDBCSpaceDAO;
import com.techelevator.jdbc.JDBCVenueDAO;

public class JDBCSpaceDAOIntegrationTest extends DAOIntegrationTest {

	private JDBCSpaceDAO dao;
	private JdbcTemplate jdbcTemplate;

	@Before
	public void setup() {
		dao = new JDBCSpaceDAO(getDataSource());
		jdbcTemplate = new JdbcTemplate(getDataSource());
	}

	@Test
	public void get_all_spaces_for_venue_select() {
		Space space = makeSpace();
		Venue venue = new Venue();
		venue.setVenueName("The Bird House");
		venue.setVenueId(3);
		venue.setCityId(27000);
		venue.setDescription("There are alot of birds in a house");

		List<Space> spaces = dao.viewSpacesForSelectedVenue(venue);

		String sql = "INSERT INTO Space VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, space.getSpaceId(), venue.getVenueId(), space.getSpaceName(), space.isAccessible(),
				space.getOpenMonth(), space.getCloseMonth(), space.getDailyRate(), space.getMaxOccupancy());

		List<Space> result = dao.viewSpacesForSelectedVenue(venue);

		Assert.assertNotNull(result);
		Assert.assertTrue(spaces.size() + 1 == result.size());

	}
	
	
	
	@Test
	public void get_all_available_spaces() {
		Venue venue = makeVenue();
		City city = makeCity();
		State state = makeState();
		Reservation reservation = makeReservation();
		
		List<Space> spaces = dao.getAvailableSpaces(reservation);
		
		String sqlState = "INSERT INTO state VALUES(?, ?) ";
		jdbcTemplate.update(sqlState, state.getStateCode(), state.getStateName());
		
		String sqlCity = "INSERT INTO city VALUES(?, ?, ?) ";
		jdbcTemplate.update(sqlCity, city.getCityId(), city.getCityName(), city.getStateCode());
		
		String sqlVenue = "INSERT INTO venue VALUES(?, ?, ?, ?) ";
		jdbcTemplate.update(sqlVenue, venue.getVenueId(), venue.getVenueName(), venue.getCityId(), venue.getDescription());
		
		Space space = makeSpace();
		Space space2 = makeSpace2();
		
		String sqlSpace = "INSERT INTO space VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sqlSpace, space.getSpaceId(), venue.getVenueId(), space.getSpaceName(), space.isAccessible(), space.getOpenMonth(), space.getCloseMonth(),
				space.getDailyRate(), space.getMaxOccupancy());
		
		String sqlSpace2 = "INSERT INTO space VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sqlSpace2, space2.getSpaceId(), venue.getVenueId(), space2.getSpaceName(), space2.isAccessible(), space2.getOpenMonth(), space2.getCloseMonth(),
				space2.getDailyRate(), space2.getMaxOccupancy());
		
		String sqlReservation = "INSERT INTO reservation VALUES (100000, 100001, 100, '4087-10-03', '4087-10-03', 'TEST') ";
		jdbcTemplate.update(sqlReservation);
		
		List<Space> spaces2 = dao.getAvailableSpaces(reservation);
		
		Assert.assertNotNull(spaces2);
		Assert.assertTrue(spaces.size() + 1 == spaces2.size());
		
		
		
		
	}
	
	@Test
	public void view_spaces_for_selected_venue() {
		Venue venue = makeVenue();
		City city = makeCity();
		State state = makeState();
		String sqlState = "INSERT INTO state VALUES(?, ?) ";
		jdbcTemplate.update(sqlState, state.getStateCode(), state.getStateName());
		
		String sqlCity = "INSERT INTO city VALUES(?, ?, ?) ";
		jdbcTemplate.update(sqlCity, city.getCityId(), city.getCityName(), city.getStateCode());
		
		String sqlVenue = "INSERT INTO venue VALUES(?, ?, ?, ?) ";
		jdbcTemplate.update(sqlVenue, venue.getVenueId(), venue.getVenueName(), venue.getCityId(), venue.getDescription());
		
		Space space = makeSpace();
		Space space2 = makeSpace2();
		
		String sqlSpace = "INSERT INTO Space VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sqlSpace, space.getSpaceId(), venue.getVenueId(), space.getSpaceName(), space.isAccessible(), space.getOpenMonth(), space.getCloseMonth(),
				space.getDailyRate(), space.getMaxOccupancy());
		
		String sqlSpace2 = "INSERT INTO Space VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sqlSpace2, space2.getSpaceId(), venue.getVenueId(), space2.getSpaceName(), space2.isAccessible(), space2.getOpenMonth(), space2.getCloseMonth(),
				space2.getDailyRate(), space2.getMaxOccupancy());
		
		List<Space>spaces = dao.viewSpacesForSelectedVenue(venue);
		Assert.assertNotNull(spaces);
		Assert.assertEquals(2, spaces.size());
		
	}

	private Space makeSpace() {
		Space space = new Space();
		space.setSpaceId(100000);
		space.setSpaceName("Room Full of Birds");
		space.setOpenMonth(3);
		space.setCloseMonth(7);
		space.setMaxOccupancy(999);
		space.setAccessible(true);
		space.setDailyRate(new BigDecimal(550));
		space.setVenueId(17000);

		return space;
	}

	private Space makeSpace2() {
		Space space2 = new Space();
		space2.setSpaceId(100001);
		space2.setSpaceName("Small Bird Cage");
		space2.setOpenMonth(1);
		space2.setCloseMonth(11);
		space2.setMaxOccupancy(150);
		space2.setAccessible(false);
		space2.setDailyRate(new BigDecimal(700));
		space2.setVenueId(17000);
		return space2;
	}
	
	private Venue makeVenue() {
		Venue venue = new Venue();
		venue.setVenueName("The Bird House");
		venue.setVenueId(17000);
		venue.setCityId(27000);
		venue.setDescription("There are alot of birds in a house");
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
	
	private Reservation makeReservation() {
		Reservation reservation = new Reservation();
		reservation.setVenueName("The Bird House");
		reservation.setVenueId(17000);
		reservation.setCityId(27000);
		reservation.setDescription("There are alot of birds in a house");
		reservation.setSpaceId(100001);
		reservation.setSpaceName("Small Bird Cage");
		reservation.setOpenMonth(3);
		reservation.setCloseMonth(3);
		reservation.setMaxOccupancy(150);
		reservation.setAccessible(false);
		reservation.setDailyRate(new BigDecimal(700));
		reservation.setVenueId(17000);
		reservation.setReservationStartDate(LocalDate.of(4087, 10, 03));
		reservation.setReservationEndDate(LocalDate.of(4087, 10, 04));
		reservation.setReservationId(100000);
		reservation.setReserveredFor("Test");
		reservation.setNumberOfAttendees(30);
		
		return reservation;
	}
}

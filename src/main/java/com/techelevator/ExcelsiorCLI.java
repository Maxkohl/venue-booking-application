package com.techelevator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.dataclass.Space;

import com.techelevator.dataclass.Reservation;

import com.techelevator.dataclass.Venue;
import com.techelevator.jdbc.JDBCReservationDAO;
import com.techelevator.jdbc.JDBCSpaceDAO;
import com.techelevator.jdbc.JDBCVenueDAO;
import com.techelevator.view.Menu;

public class ExcelsiorCLI {

	private final static String MAIN_MENU_LIST_VENUES = "1";
	private final static String MAIN_MENU_QUIT = "Q";
	private final static String MAIN_MENU_DISPLAY_RESERVATIONS = "D";
	private final static String MAIN_MENU_SEARCH_FOR_SPACE = "S";
	private final static String[] MAIN_MENU_OPTIONS = { "1) List Venues", "D) Display Reservations",
			"S) Search for Space", "Q) Quit" };

	private final static String VENUE_DETAILS_VIEW_SPACES = "1";
	private final static String VENUE_DETAILS_SEARCH_FOR_RESERVATION = "2";
	private final static String OPTION_RETURN = "R";
	private final static String[] VENUE_DETAILS_OPTIONS = { "1) View Spaces", "R) Return to Previous Screen" };

	private final static String OPTION_ONE = "1";
	private final static String OPTION_TWO = "2";
	private final static String OPTION_THREE = "3";
	private final static String OPTION_FOUR = "4";
	private final static String INVALID_OPTION = "!=== Invalid option. Please try again ===!"
			+ System.getProperty("line.separator");

	private static Menu menu = new Menu();

	private JDBCVenueDAO venueDAO;
	private JDBCSpaceDAO spaceDAO;
	private JDBCReservationDAO reservationDAO;
	private Reservation reservation;

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/excelsior-venues");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		ExcelsiorCLI application = new ExcelsiorCLI(dataSource, menu);

		application.run();
	}

	public ExcelsiorCLI(DataSource datasource, Menu menu) {
		this.menu = menu;
		this.venueDAO = new JDBCVenueDAO(datasource);
		this.spaceDAO = new JDBCSpaceDAO(datasource);
		this.reservationDAO = new JDBCReservationDAO(datasource);
	}

	public void run() {

		reservation = new Reservation();
		boolean run = true;
		while (run) {
			String choice = menu.displayMenuOptions(MAIN_MENU_OPTIONS);
			switch (choice) {
			case MAIN_MENU_LIST_VENUES:
				getVenueChoice();
				break;
			case MAIN_MENU_DISPLAY_RESERVATIONS:
				menu.displayThirtyDayReservations(reservationDAO.viewThirtyDayReservations());
				break;
			case MAIN_MENU_SEARCH_FOR_SPACE:
				advancedSearchMenu();
				break;
			case MAIN_MENU_QUIT:
				menu.getMessage("Thank you for using the Excelsior - Venue Booking Application.");
				run = false;
				break;
			default:
				menu.getMessage(INVALID_OPTION);
			}

		}

	}

	public void getVenueChoice() {
		int venueIndex = 0;
		boolean run = true;
		while (run) {
			String choice = menu.getVenueFromUser(venueDAO.selectAllVenues(), venueIndex);
			venueIndex += 3;
			Venue venue;
			switch (choice) {
			case OPTION_ONE:
				if (venueIndex > venueDAO.selectAllVenues().size() + 3) {
					venueIndex -= 3;
					menu.getMessage(INVALID_OPTION);
					break;
				}
				venueIndex -= 3;
				venue = venueDAO.selectAllVenues().get(venueIndex);
				venueDAO.addVenueToReservation(reservation, venue);
				venueDetailsMenu();
				break;
			case OPTION_TWO:
				if (venueIndex > venueDAO.selectAllVenues().size() + 2) {
					venueIndex -= 3;
					menu.getMessage(INVALID_OPTION);
					break;
				}
				venueIndex -= 2;
				venue = venueDAO.selectAllVenues().get(venueIndex);
				venueDAO.addVenueToReservation(reservation, venue);
				venueDetailsMenu();
				break;
			case OPTION_THREE:
				if (venueIndex > venueDAO.selectAllVenues().size() + 1) {
					venueIndex -= 3;
					menu.getMessage(INVALID_OPTION);
					break;
				}
				venueIndex -= 1;
				venue = venueDAO.selectAllVenues().get(venueIndex);
				venueDAO.addVenueToReservation(reservation, venue);
				venueDetailsMenu();
				break;
			case OPTION_FOUR:
				if (venueIndex > venueDAO.selectAllVenues().size() - 3) {
					venueIndex -= 3;
					menu.getMessage(INVALID_OPTION);
					break;
				}
				break;
			case OPTION_RETURN:
				run = false;
				break;
			default:
				venueIndex -= 3;
				menu.getMessage(INVALID_OPTION);
				break;
			}
		}

	}

	public void venueDetailsMenu() {
		List<Space> spaces = spaceDAO.viewSpacesForSelectedVenue(reservation);
		menu.showVenueDetails(reservation);
		boolean run = true;
		while (run) {
			String choice = menu.displayMenuOptions(VENUE_DETAILS_OPTIONS);
			switch (choice) {
			case VENUE_DETAILS_VIEW_SPACES:
				menu.showVenueSpaces(spaces, reservation);
				reserveSpaceMenu();
				run = false;
				break;
			case OPTION_RETURN:
				run = false;
				break;
			default:
				menu.getMessage(INVALID_OPTION);
				break;
			}
		}
	}

	public void reserveSpaceMenu() {
		boolean run = true;
		while (run) {
			menu.getReservationInfo(reservation);
			List<Space> availableSpaces = spaceDAO.getAvailableSpaces(reservation);
			if (availableSpaces.size() == 0) {
				boolean quit = menu.noAvailableSpacesMenu();
				if (quit) {
					System.exit(0);
				}
			} else {
				List<Integer> spaceIds = menu.displayAvailableSpaces(availableSpaces, reservation);
				int choice = menu.getReservationSpaceNumAndName(reservation);
				if (choice == 0) {
					run = false;
				} else if (spaceIds.contains(choice)) {
					spaceDAO.reserveSpace(availableSpaces.get(spaceIds.indexOf(choice)), reservation);
					reservationDAO.submitReservation(reservation);
					menu.displayConfirmation(reservation);
					System.exit(0);
				} else {
					menu.getMessage(INVALID_OPTION);
				}
			}
		}
	}

	public void advancedSearchMenu() {
		boolean run = true;
		while (run) {
			menu.getReservationInfo(reservation);
			BigDecimal userDailyBudget = menu.getAdvancedReservationInfo(reservation);
			List<Space> availableSpaces = spaceDAO.getAdvancedSearchSpaces(reservation);
			List<Integer> venuesByCategory = venueDAO.getVenuesByCategoryIdAndAccessibility(reservation);
			List<Space> availableSpacesByCategory = new ArrayList<Space>();

			for (Integer venueID : venuesByCategory) {
				for (Space space : availableSpaces) {
					if (space.isAccessible() == reservation.isAccessible()
							&& (venueID == space.getVenueId() || reservation.getVenueId() == 0)) {
						availableSpacesByCategory.add(space);
					}
				}
			}
			if (availableSpaces.size() == 0) {
				boolean quit = menu.noAvailableSpacesMenu();
				if (quit) {
					System.exit(0);
				}
			} else {

				System.getProperty("line.separator");
				menu.getMessage("The following spaces are available based on your needs: ");
				System.getProperty("line.separator");

				List<Integer> spaceIds = menu.displayAvailableSpaces(availableSpacesByCategory, reservation);

				int choice = menu.getReservationSpaceNumAndName(reservation);
				venueDAO.getVenueNameFromSpaceId(reservation);
				if (choice == 0) {
					run = false;
				} else if (spaceIds.contains(choice)) {
					spaceDAO.reserveSpace(availableSpacesByCategory.get(spaceIds.indexOf(choice)), reservation);
					reservationDAO.submitReservation(reservation);
					menu.displayConfirmation(reservation);
					System.exit(0);
				} else {
					menu.getMessage(INVALID_OPTION);
				}
			}
		}
	}

}

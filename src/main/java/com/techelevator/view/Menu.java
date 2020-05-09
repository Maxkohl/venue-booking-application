package com.techelevator.view;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.techelevator.dataclass.Reservation;
import com.techelevator.dataclass.Space;
import com.techelevator.dataclass.Venue;

public class Menu {

	private Scanner in = new Scanner(System.in);

	public void getMessage(String message) {
		System.out.println(message);
	}

	public String displayMenuOptions(String[] options) {
		String userChoice = "";
		while (true) {
			for (int i = 0; i < options.length; i++) {
				System.out.println(options[i]);
			}
			userChoice = getChoiceFromUser(options).toUpperCase();
			break;
		}
		return userChoice;
	}

	public String getChoiceFromUser(String[] options) {
		System.out.println("Please enter choice >>> ");
		String userChoice = in.nextLine();
		return userChoice;
	}

	public String getVenueFromUser(List<Venue> venueList, int venueIndex) {
		System.out.println("Which venue would you like to view? ");
		String nextChoice = "";
		int optionNum = 1;
		int index = venueIndex;
		for (; venueIndex < index + 3; venueIndex++) {
			System.out.println(optionNum + ") " + venueList.get(venueIndex).getVenueName());
			optionNum++;
			if (venueIndex + 1 == venueList.size()) {
				break;
			}
		}
		if (venueIndex + 3 <= venueList.size()) {
			System.out.println("4) ...");
		}
		System.out.println("R) Return To Previous Screen \n");
		System.out.println("Select choice >>> ");
		nextChoice = in.nextLine().toUpperCase();

		return nextChoice;
	}

	public void showVenueDetails(Reservation reservation) {
		System.out.println(reservation.getVenueName());

		System.out.println("Location: " + reservation.getCityName() + ", " + reservation.getStateCode());
		System.out.println("Categories: " + reservation.getCategories().toString().replace("[", " ").replace("]", " "));
		System.out.println(System.getProperty("line.separator"));
		System.out.println(reservation.getDescription());

		System.out.println("What would you like to do? ");
		System.out.println("Select choice >>> ");

	}

	public void showVenueSpaces(List<Space> spaces, Reservation reservation) {
		System.getProperty("line.separator");
		String[] months = Space.getMonths();
		System.out.println(reservation.getVenueName() + " Spaces");
		System.getProperty("line.separator");
		System.out.printf("%2s %-32s  %-6s  %-7s   %-16s %-5s%n", "", "Name", "Open", "Close", "Daily Rate",
				"Max. Occupancy");
		System.out.println("----------------------------------------------------------------------------------");
		int spaceNum = 1;
		for (Space space : spaces) {
			System.out.printf("#%d %-32s  %-6s  %-7s   $%-15.2f %-5d%n", spaceNum, space.getSpaceName(),
					months[space.getOpenMonth()], months[space.getCloseMonth()],
					space.getDailyRate().setScale(2, RoundingMode.FLOOR), space.getMaxOccupancy());
			spaceNum++;
		}
		System.getProperty("line.separator");
		System.out.flush();
	}

	public void displayThirtyDayReservations(List<Reservation> reservations) {
		System.out.println();
		System.out.println("The following reservations are coming up in the next 30 days: ");
		System.out.println();
		System.getProperty("line.separator");
		System.out.printf("%-30s %-28s  %-30s  %-13s   %s %n", "Venue", "Space", "Reserved For", "From", "To");
		System.out.println(
				"----------------------------------------------------------------------------------------------------------------");

		for (Reservation reservation : reservations) {

			System.out.printf("%-31s %-27s  %-30s  %-15s %s %n", reservation.getVenueName(), reservation.getSpaceName(),
					reservation.getReserveredFor(), reservation.getReservationStartDate(),
					reservation.getReservationEndDate());

		}

	}

	public void getReservationInfo(Reservation reservation) {

		boolean runOne = true;
		while (runOne) {
			System.out.println("When do you need the space? (yyyy-mm-dd) >>>");
			try {
				reservation.setReservationStartDate(LocalDate.parse(in.nextLine()));
				if (reservation.getReservationStartDate().compareTo(LocalDate.now()) < 0) {
					throw new Exception();
				}
				runOne = false;
			} catch (Exception e) {
				System.out.println("!=== Invalid input. Please retry and insert a valid date ===!");
			}
		}
		reservation.setOpenMonth(reservation.getReservationStartDate().getMonthValue());
		boolean runTwo = true;
		while (runTwo) {
			System.out.println("How many days will you need the space? >>>");
			try {
				reservation.setReservationEndDate(reservation.getReservationStartDate().plusDays(in.nextInt()));
				runTwo = false;
			} catch (Exception e) {
				System.out.println("!=== Invalid input. Please retry and insert a number of days ===!");
				in.nextLine();
			}
		}
		reservation.setCloseMonth(reservation.getReservationEndDate().getMonthValue());
		in.nextLine();
		boolean runThree = true;
		while (runThree) {
			System.out.println("How many people will be in attendance? >>>");
			try {
				reservation.setNumberOfAttendees(in.nextInt());
				runThree = false;
			} catch (Exception e) {
				System.out.println("!=== Invalid input. Please retry and insert a number of attendees===!");
				in.nextLine();
			}
		}
		in.nextLine();

	}

	public List<Integer> displayAvailableSpaces(List<Space> spaces, Reservation reservation) {

		Set<String> uniqueVenueNameSet = uniqueVenueName(spaces);
		List<Integer> spaceIDs = new ArrayList<Integer>();
		int i = 0;
		int venueNameCount = 0;
		for (String venueName : uniqueVenueNameSet) {
			System.out.println();
			System.out.println(venueName);
			System.out.println();
			System.out.printf("%-6s %-30s  %-15s  %-15s   %-16s %-15s%n", "Space #", "Name", "Daily Rate", "Max Occup.",
					"Accessible?", "Total Cost");
			System.out.println(
					"---------------------------------------------------------------------------------------------------------");
			while (i < spaces.size()) {
				System.out.printf("#%-6d %-30s  $%-15.2f  %-15d   %-16s $%-15.2f%n", spaces.get(i).getSpaceId(),
						spaces.get(i).getSpaceName(), spaces.get(i).getDailyRate().setScale(2, RoundingMode.FLOOR),
						spaces.get(i).getMaxOccupancy(), spaces.get(i).isAccessible() ? "Yes" : "No",
						spaces.get(i).getDailyRate().multiply(new BigDecimal(Period
								.between(reservation.getReservationStartDate(), reservation.getReservationEndDate())
								.getDays())));
				spaceIDs.add(spaces.get(i).getSpaceId());
				if (i != 0 && venueNameCount <= 5) {
					venueNameCount++;
				}
				i++;
				if (venueNameCount == 5) {
					break;
				} else if (i >= spaces.size() || !spaces.get(i).getVenueName().equals(venueName)) {
					venueNameCount = 0;
					break;
				}

			}
		}
		System.getProperty("line.separator");
		System.out.flush();
		return spaceIDs;

	}

	public BigDecimal getAdvancedReservationInfo(Reservation reservation) {
		BigDecimal userDailyBudget = new BigDecimal(0);
		boolean runOne = true;
		while (runOne) {
			System.out.println("Does the space require accessibility accommodations (Y/N)? >>> ");
			try {
				String choice = in.nextLine();
				if (choice.toUpperCase().equals("Y")) {
					reservation.setAccessible(true);
					runOne = false;
				} else if (choice.toUpperCase().equals("N")) {
					reservation.setAccessible(false);
					runOne = false;
				}
			} catch (Exception e) {
				System.out.println("!=== Invalid input. Please choose (Y)es or (N)o ===!");
				in.nextLine();
			}
		}
		boolean runTwo = true;
		while (runTwo) {
			System.out.println("What is your daily budget? >>> ");
			try {
				userDailyBudget = new BigDecimal(in.nextInt());
				in.nextLine();
				runTwo = false;
			} catch (Exception e) {
				System.out.println("!=== Invalid input. Please enter valid Daily Rate ===!");
				in.nextLine();
			}
		}
		boolean runThree = true;
		while (runThree) {
			System.out.println("Which of the categories would you like to include?");
			getCategoryChoice(reservation);
			runThree = false;
			break;
		}
		return userDailyBudget;

	}

	public int getReservationSpaceNumAndName(Reservation reservation) {
		System.out.println("Which space would you like to reserve (enter 0 to cancel)? ");
		int userChoice = in.nextInt();
		in.nextLine();
		System.out.println("Who is this reservation for? ");
		reservation.setReserveredFor(in.nextLine());
		reservation.setSpaceId(userChoice);
		return userChoice;
	}

	public boolean noAvailableSpacesMenu() {
		while (true) {
			System.out.println("There are no available spaces that fulfill your request would"
					+ " you like try another reservation? Yes(Y) No(N) ");
			String choice = in.nextLine();
			if (choice.toUpperCase().equals("N")) {
				return true;
			} else if (choice.toUpperCase().equals("Y")) {
				return false;
			}
			System.out.println("Not a valid option");
		}
	}

	public void displayConfirmation(Reservation reservation) {

		System.out.printf("%6s %s", "Confirmation #:",
				reservation.getReservationId() + System.getProperty("line.separator"));
		System.out.printf("%15s %s", "Venue:", reservation.getVenueName() + System.getProperty("line.separator"));
		System.out.printf("%15s %s", "Space:", reservation.getSpaceName() + System.getProperty("line.separator"));
		System.out.printf("%15s %s", "Reserved For:",
				reservation.getReserveredFor() + System.getProperty("line.separator"));
		System.out.printf("%15s %s", "Attendees:",
				reservation.getNumberOfAttendees() + System.getProperty("line.separator"));
		System.out.printf("%15s %s", "Arrival Date:",
				reservation.getReservationStartDate() + System.getProperty("line.separator"));
		System.out.printf("%15s %s", "Depart Date:",
				reservation.getReservationEndDate() + System.getProperty("line.separator"));
		System.out.printf("%15s $%-15s", "Total Cost:", reservation.getDailyRate().multiply(new BigDecimal(
				Period.between(reservation.getReservationStartDate(), reservation.getReservationEndDate()).getDays())));

	}

	public void getCategoryChoice(Reservation reservation) {
		String[] categories = { "1) Family Friendly", "2) Outdoors", "3) Historic", "4) Rustic", "5) Luxury",
				"6) Modern", "N) None" };
		for (int i = 0; i < categories.length; i++) {
			System.out.println(categories[i]);
		}
		boolean run = true;
		while (run) {
			String choice = in.nextLine().toUpperCase();
			switch (choice) {
			case "1":
				reservation.setCategoryId(1);
				run = false;
				break;
			case "2":
				reservation.setCategoryId(2);
				run = false;
				break;
			case "3":
				reservation.setCategoryId(3);
				run = false;
				break;
			case "4":
				reservation.setCategoryId(4);
				run = false;
				break;
			case "5":
				reservation.setCategoryId(5);
				run = false;
				break;
			case "6":
				reservation.setCategoryId(6);
				run = false;
				break;
			case "N":
				System.out.println("No category");
				run = false;
				break;
			default:
				System.out.println("!=== Invalid input. Please enter valid Number of Category ===!");
				break;
			}
		}
	}

	private Set<String> uniqueVenueName(List<Space> spaces) {
		Set<String> uniqueNameSet = new LinkedHashSet<String>();
		for (Space space : spaces) {
			uniqueNameSet.add(space.getVenueName());
		}

		return uniqueNameSet;
	}

}

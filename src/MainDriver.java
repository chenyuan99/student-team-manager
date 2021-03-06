import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.runners.JUnit4;

/**
 * Runs the Student Team Manager program from the command line.
 * 
 * @author Rae Bouldin
 * @author Yuan Chen
 * @author Saunders Cox
 * @author Stephen Franklin
 * @author Chaipakon Luangprasert
 * 
 * @version 2020.04.23
 */
public class MainDriver {

	private static Database<String, StudentUser> database;
	private static User currentUser;

	public static void main(String[] args) {
		if (args != null) {
		    // java MainDriver -d
			if (args.length > 0 && args[0].equals("-d")) {
				org.junit.runner.JUnitCore.runClasses(ValidationTest.class, DocumentTest.class, MainTesting.class, ReviewTest.class,
						SkillTest.class, StudentUserTest.class, UserTest.class);
				return;
			}
		}

		database = new Database<String, StudentUser>("data.db");
		currentUser = null;

		System.out.println("==================== STUDENT TEAM MANAGER ====================");

		Scanner scan = new Scanner(System.in);
		int userInput = -1;
		while (userInput != NavigationMenu.QUIT) {
			userInput = NavigationMenu.displayMainMenu(scan, currentUser);
			scan.nextLine();
			switch (userInput) {
			case NavigationMenu.CREATE_NEW_PROFILE:
				createNewProfile(scan);
				break;
			case NavigationMenu.SIGN_IN:
				signIn(scan);
				break;
			case NavigationMenu.VIEW_PROFILE:
				viewProfile();
				break;
			case NavigationMenu.EDIT_PROFILE:
				editProfile(scan);
				break;
			case NavigationMenu.SIGN_OUT:
				signOut(scan);
				break;
			case NavigationMenu.REMOVE_PROFILE:
				removeProfile(scan);
				break;
			default:
				break;
			}
		}

		System.out.println("\nGoodbye!");
		System.out.println("==============================================================");

	}

	private static void createNewProfile(Scanner scan) {
		System.out.println("===== CREATE NEW PROFILE =====");

		// Create a completely random user id (collision probability is very
		// low).
		long userID = Instant.now().toEpochMilli() + ((long) Math.random() % 10000);

		System.out.print("Enter your email: ");
		String email = scan.nextLine();

		System.out.print("Enter your password: ");
		String password = scan.nextLine();

		System.out.print("Enter your first name: ");
		String fName = scan.nextLine();

		System.out.print("Enter your last name: ");
		String lName = scan.nextLine();

		System.out.print("Enter your phone number: ");
		String phone = scan.nextLine();

		System.out.print("Enter your major: ");
		String major = scan.nextLine();

		System.out.println("Select your year: ");
		System.out.println("1. Freshman\n2. Sophomore\n3. Junior\n4. Senior");
		int selectedYear = scan.nextInt();
		scan.nextLine();
		while (selectedYear < 1 || selectedYear > 4) {
			System.out.printf("'%d' is an invalid selection.\n", selectedYear);
			System.out.println("Select your year: ");
			System.out.println("1. Freshman\n2. Sophomore\n3. Junior\n4. Senior");
			selectedYear = scan.nextInt();
			scan.nextLine();
		}
		String year = "";
		switch (selectedYear) {
		case 1:
			year = "Freshman";
			break;
		case 2:
			year = "Sophomore";
			break;
		case 3:
			year = "Junior";
			break;
		case 4:
			year = "Senior";
			break;
		default:
			break;
		}

		database.store(email, new StudentUser(userID, fName, lName, email, phone, major, year, password));

		System.out.println("Your profile has been successfully created!");
	}

	private static void signIn(Scanner scan) {
		System.out.print("Enter your email: ");
		String email = scan.nextLine();
		StudentUser user = database.retrieve(email);

		// case: no user found
		if (user == null) {
			System.out.println("A user with that email doesn't exist.");
			return;
		}

		System.out.print("Enter your password: ");
		String password = scan.nextLine();
		// case: password incorrect
		if (!user.checkPassword(password)) {
			System.out.println("Incorrect password.");
			return;
		}

		currentUser = user;

	}

	private static void viewProfile() {
		System.out.println("===== VIEW PROFILE =====");

		System.out.printf("Name:\t%s %s\n", currentUser.getFirstName(), currentUser.getLastName());
		System.out.printf("Email:\t%s\n", currentUser.getEmail());
		System.out.printf("Phone:\t%s\n", currentUser.getPhoneNumber());

		if (currentUser.getClass() == StudentUser.class) {
			StudentUser castCurrentUser = (StudentUser) currentUser;
			System.out.printf("Major:\t%s\n", castCurrentUser.getMajor());
			System.out.printf("Year:\t%s\n", castCurrentUser.getYear());
		}

		System.out.println();
	}

	private static void editProfile(Scanner scan) {
		System.out.println("===== EDIT PROFILE =====");

		int userSelection = NavigationMenu.displayEditProfile(scan, currentUser);
		scan.nextLine();
		switch (userSelection) {
		case NavigationMenu.CANCEL:
			return;
		case NavigationMenu.EDIT_EMAIL:
			System.out.print("Enter a new email: ");
			currentUser.setEmail(scan.nextLine());
			break;
		case NavigationMenu.EDIT_PASSWORD:
			System.out.println("Enter a new password: ");
			currentUser.setPassword(scan.nextLine());
			break;
		case NavigationMenu.EDIT_FIRST_NAME:
			System.out.print("Enter a new first name: ");
			currentUser.setFirstName(scan.nextLine());
			break;
		case NavigationMenu.EDIT_LAST_NAME:
			System.out.print("Enter a new last name: ");
			currentUser.setLastName(scan.nextLine());
			break;
		case NavigationMenu.EDIT_PHONE_NUMBER:
			System.out.print("Enter a new phone number: ");
			currentUser.setPhoneNumber(scan.nextLine());
			break;
		case NavigationMenu.EDIT_MAJOR:
			System.out.print("Enter a new major: ");
			StudentUser currentUserCast = (StudentUser) currentUser;
			currentUserCast.setMajor(scan.nextLine());
			break;
		case NavigationMenu.EDIT_YEAR:
			StudentUser currentUserCast1 = (StudentUser) currentUser;
			System.out.println("Enter a new year: ");
			System.out.println("1. Freshman\n2. Sophomore\n3. Junior\n4. Senior");
			int selectedYear = scan.nextInt();
			scan.nextLine();
			while (selectedYear < 1 || selectedYear > 4) {
				System.out.printf("'%d' is an invalid selection.\n", selectedYear);
				System.out.println("Select your year: ");
				System.out.println("1. Freshman\n2. Sophomore\n3. Junior\n4. Senior");
				selectedYear = scan.nextInt();
				scan.nextLine();
			}
			String year = "";
			switch (selectedYear) {
			case 1:
				year = "Freshman";
				break;
			case 2:
				year = "Sophomore";
				break;
			case 3:
				year = "Junior";
				break;
			case 4:
				year = "Senior";
				break;
			default:
				break;
			}
			currentUserCast1.setYear(year);
			break;
		default:
			break;
		}

		database.store(currentUser.getEmail(), (StudentUser) currentUser);
		System.out.println("Your profile has been successfully updated!");
	}

	private static void signOut(Scanner scan) {
		currentUser = null;
	}

	private static void removeProfile(Scanner scan) {
		System.out.println("Enter your password to remove your profile:");
		if (currentUser.checkPassword(scan.nextLine())) {
			database.remove(currentUser.getEmail());
			currentUser = null;
			System.out.println("Profile Removed");
		} else {
			System.out.println("Error: unsuccessful removal");
		}
	}

} // end of MainDriver class

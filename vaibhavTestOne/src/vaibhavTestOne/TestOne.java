package vaibhavTestOne;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TestOne {

	private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/test";
	// Database credentials
	private static final String USER = "root";
	private static final String PASS = "1432";

	private static final int DUPLICATE_ENTRY_ERROR_CODE = 1062;

	public static void main(String[] args) throws IOException {
		Connection connection = null;
		Statement statement = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);
			// STEP 3: Open a connection
			// System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		}
		int choice = 0;
		Scanner scanner = new Scanner(System.in);
		do {

			System.out.print("\033[H\033[2J");
			System.out.println("################################################################");
			System.out.println("1 Insert Name And City ");
			System.out.println("2 Modified  City");
			System.out.println("3 Remove");
			System.out.println("4 Remove All");
			System.out.println("5 Search Name");
			System.out.println("6 Show All");
			System.out.println("7 Exit");
			System.out.println("################################################################");
			try {
				System.out.print("Please choose an option : ");
				choice = scanner.nextInt();
			} catch (InputMismatchException e) {
				scanner.nextLine();
				System.out.println(choice);
			}

			switch (choice) {
			case 1:
				System.out.print("Enter a name to be added : ");
				String name = scanner.next();
				System.out.print("Enter the city to be added : ");
				String city = scanner.next();
				String sql = " INSERT INTO testone (name, city) VALUES('" + name + "','" + city + "')";
				// INSERT INTO testone (name) SELECT * FROM (SELECT '" + name + "') AS tmp WHERE
				// NOT EXISTS ( SELECT name FROM testone WHERE name = '" + name + "') ;";
				try {
					statement.executeUpdate(sql);
					System.out.println("'" + name + "' has been added successfully.\n\n");
				} catch (SQLTimeoutException e) {
					System.out.println("Timeout error occured while executing the SQL query : " + sql);
				} catch (SQLException e) {
					if (e.getErrorCode() == DUPLICATE_ENTRY_ERROR_CODE) {
						System.out.println("'" + name + "' is already present in table\n\n");
					}
				}
				break;
			case 2:
				System.out.print("Enter a Name To Be Modify City :");
				name = scanner.next();
				System.out.print("\nEnter the city name : ");
				city = scanner.next();
				sql = "UPDATE TESTONE SET CITY='" + city + "'WHERE NAME='" + name + "' ;";
				try {
					statement.executeUpdate(sql);
					System.out.println("'" + city + "'City has been added successfully.\n\n");

				} catch (SQLTimeoutException e) {
					System.out.println("Timeout error occured while executing the SQL query : " + sql);

				} catch (SQLException e) {
					System.out.println("ERROR CODE: " + e.getErrorCode() + ", ERROR MESSAGE : " + e.getMessage());
				}

				break;

			case 3:

				System.out.print("Enter a name to be removed : ");
				name = scanner.next();
				sql = "DELETE FROM testone WHERE name = '" + name + "';";
				try {
					int result = statement.executeUpdate(sql);
					if (result == 1) {
						System.out.println("'" + name + "' has been removed successfully.\n\n");
					} else {

						System.out.println("'" + name + "' is not present in table.\n\n");
					}
				} catch (SQLTimeoutException e) {
					System.out.println("Timeout error occured while executing the SQL query : " + sql);
				} catch (SQLException e) {
					System.out.println("ERROR CODE: " + e.getErrorCode() + ", ERROR MESSAGE : " + e.getMessage());
				}
				break;
			case 4:

				System.out.print("Do you really want to remove all (y/n) : ");
				String option = scanner.next();
				if (option.equalsIgnoreCase("y")) {
					sql = "DELETE FROM testone;";
					try {
						statement.executeUpdate(sql);
						System.out.println("All data deleted from table");
					} catch (SQLTimeoutException e) {
						System.out.println("Timeout error occured while executing the SQL query : " + sql);
					} catch (SQLException e) {
						System.out.println("ERROR CODE: " + e.getErrorCode() + ", ERROR MESSAGE : " + e.getMessage());
					}
				} else {
					System.out.println("No data removed");
				}

				break;

			case 5:
				ResultSet rs;
				System.out.print("Enter a name or City be Searched :");
				name = scanner.next();

				System.out.println("\n Your Search result...");
				sql = "SELECT * FROM testone WHERE name = '" + name + "' OR  city ='" + name + " ';";
				try {
					rs = statement.executeQuery(sql);
					if (rs.next() == false) {
						System.out.println("\n No Data found in Table \n\n");
					} else {
						System.out.println("\nSearch Result Given Below\n\n");
						System.out.println("\tId \t Name \t City");
						System.out.println("----------------------------");
						int id = 0;
						while (rs.next()) {
							System.out.println("\t" + ++id + "    " + rs.getString("name") + "      "
									+ rs.getString("city") + " ");
						}
						// System.out.println("\t" + rs.getInt("id") + "\t" + rs.getString("name") + "
						// "+ rs.getString("city")+" ");
						System.out.println("----------------------------");
						System.out.println(id + " row(s) returned");
						System.out.println("----------------------------");
					}
					rs.close();

				} catch (SQLTimeoutException e) {
					System.out.println("Timeout error occured while executing the SQL query : " + sql);
				} catch (SQLException e) {
					System.out.println("ERROR CODE: " + e.getErrorCode() + ", ERROR MESSAGE : " + e.getMessage());
				}
				break;
			case 6:
				sql = "SELECT name, city FROM testone";
				try {

					rs = statement.executeQuery(sql);
					if (rs.next() == false) {
						System.out.println("\n No Data found in Table \n\n");

					} else {

						System.out.println("\n    LIST OF DATA IN TABLE ");
						System.out.println("----------------------------");
						System.out.println("\tId \t Name \t City");
						System.out.println("----------------------------");
						int id = 0;
						do {
							System.out.println(
									"\t" + ++id + "\t" + rs.getString("name") + "\t" + rs.getString("city") + " ");
						} while (rs.next());
						System.out.println("----------------------------");
						System.out.println(id + " row(s) returned");
						System.out.println("----------------------------");
						rs.close();
					}
				} catch (SQLTimeoutException e) {
					System.out.println("Timeout error occured while executing the SQL query : " + sql);
				} catch (SQLException e) {
					System.out.println("ERROR CODE: " + e.getErrorCode() + ", ERROR MESSAGE : " + e.getMessage());
				}
				break;
			case 7:
				System.out.println("Exiting from application... Good bye!!!");
				break;
			default:
				System.out.println("\n\nInvaild Option, please try again!!!\n");
			}
		} while (choice != 7);
		scanner.close();
	}
}

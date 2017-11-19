package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import model.User;

public class UserApp {

	public static void main(String[] args) {

		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/warsztat2?useSSL=false", "root",
					"coderslab");
			User[] users = User.loadAllUsers(conn);
			System.out.println("Lista wszystkich użytkowników");
			for (int i = 0; i < users.length; i++) {
				User newUser = users[i];
				System.out.println(newUser.getUsername());
			}
			System.out.println();
			Scanner sc = new Scanner(System.in);
			String answer;
			
			System.out.println("Wybierz jedną z opcji: add, edit, delete, quit.");

			while (sc.hasNext()) {
				answer = sc.next();
				if (answer.equals("add")) {
					System.out.println("Podaj nazwę użytkownika");
					String username = sc.next();
					System.out.println("Podaj email");
					String email = sc.next();
					System.out.println("Podaj hasło");
					String password = sc.next();

					User user = new User(username, email, password);
					user.setUserGroupId(1);
					user.saveToDB(conn);
					System.out.println("Dodano użytkownika do bazy danych.");
					continue;

				} else if (answer.equals("edit")) {

					System.out.println("Wpisz id użytkownika do edycji");
					int id = sc.nextInt();
					User user = User.loadUserById(conn, id);
					System.out.println("Podaj nową nazwę użytkownika");
					String username = sc.next();
					user.setUsername(username);
					System.out.println("Podaj nowy email");
					String email = sc.next();
					user.setEmail(email);
					System.out.println("Podaj nowe hasło");
					String password = sc.next();
					user.setPassword(password);

					user.saveToDB(conn);

					System.out.println("Edytowano użytkownika w bazie danych.");
					continue;

				} else if (answer.equals("delete")) {

					System.out.println("Podaj id użytkownika do usunięcia");
					int id = sc.nextInt();

					User user = User.loadUserById(conn, id);
					try {
						user.deleteUser(conn);
						System.out.println("Usunięto użytkownika z bazy danych.");
					} catch (Exception e) {
						System.out.println("Nie można usunąć użytkownika mającego przypisane zadania!");
						continue;
					}
					continue;

				} else if (answer.equals("quit")) {
					System.out.println("Koniec programu");
					conn.close();
					break;

				} else {
					System.out.println("Niepoprawne wprowadzenie! Spróbuj jeszcze raz!");
					sc.nextLine();
				}
			}
			sc.close();

			// conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}

package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

import model.Exercise;
import model.Solution;
import model.User;

public class AssignExerciseApp {

	public static void main(String[] args) {

		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/warsztat2?useSSL=false", "root",
					"coderslab");

			Scanner sc = new Scanner(System.in);
			String answer;

			do {

				System.out.println("Wybierz jedną z opcji: add, view, quit.");
				answer = sc.next();

				if (answer.equals("add")) {

					User[] users = User.loadAllUsers(conn);
					System.out.println("Lista wszystkich użytkowników");
					for (int i = 0; i < users.length; i++) {
						User newUser = users[i];
						System.out.println(newUser.getId() + ": " + newUser.getUsername());
					}
					System.out.println();
					System.out.println("Wpisz id użytkownika, któremu chcesz przypisać zadanie");

					long userId = sc.nextLong();

					Exercise[] exercises = Exercise.loadAllExercises(conn);
					System.out.println("Lista wszystkich zadań");
					for (int i = 0; i < exercises.length; i++) {
						Exercise newEx = exercises[i];
						System.out.println(newEx.getId() + ": " + newEx.getName() + ": " + newEx.getDescription());
					}
					System.out.println();
					System.out.println("Wpisz id zadania, które chcesz przypisać wybranemu użytkownikowi");

					int exerciseId = sc.nextInt();

					Date date = new Date();
					Solution solution = new Solution(date, null, null);
					solution.setUsersId(userId);
					solution.setExerciseId(exerciseId);
					solution.saveToDB(conn);
					System.out.println("Dodano rozwiązanie do bazy danych");

					continue;

				}
				if (answer.equals("view")) {

					User[] users = User.loadAllUsers(conn);
					System.out.println("Lista wszystkich użytkowników");
					for (int i = 0; i < users.length; i++) {
						User newUser = users[i];
						System.out.println(newUser.getId() + ": " + newUser.getUsername());
					}
					System.out.println();
					System.out.println("Wpisz id użytkownika, którego rozwiązania chcesz obejrzeć");
					long userId = sc.nextLong();

					Exercise.loadAllByUserId(conn, userId);
					continue;
				}

				if (!"add".equals(answer) && (!"view".equals(answer)) && (!"quit".equals(answer))) {
					System.out.println("Niepoprawne wprowadzenie! Spróbuj jeszcze raz!");
					sc.nextLine();
					continue;
				}

			} while (!"quit".equals(answer)); {

				System.out.println("Koniec programu");
				conn.close();
			}
			sc.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}

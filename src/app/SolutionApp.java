package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

import model.Exercise;
import model.Solution;
import model.User;

public class SolutionApp {

	public static void main(String[] args) {

		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/warsztat2?useSSL=false", "root",
					"coderslab");

			Scanner sc = new Scanner(System.in);
			System.out.println("Witaj w programie użytkownika, wpisz swoje id");
			long userId = sc.nextLong();
			String answer;

			do {

				System.out.println("Wybierz jedną z opcji: add, view, quit.");
				answer = sc.next();

				if (answer.equals("add")) {

					// lista zadan ktorych uzytkownik jeszcze nie wykonal
					User.loadNotDoneById(conn, userId);
					
					//edycja
					System.out.println("Podaj id zadania, do którego chcesz dodać rozwiązanie");
					int exerciseId = sc.nextInt();
					System.out.println("Podaj rozwiązanie zadania");
					String description = sc.next();
					Solution solution = new Solution(new Date(), new Date(), description);
					solution.setExerciseId(exerciseId);
					solution.setUsersId(userId);

					// sprawdzenie czy dodano juz rozwiazanie tego zadania
					boolean check = Solution.checkIfExists(conn, exerciseId, userId);
					if (check == false) {
						solution.saveToDB(conn);
						System.out.println("Dodano rozwiązanie do bazy danych!");
					} else {
					System.out.println("Już dodano rozwiązanie do tego zadania!");
					}
				}
				if (answer.equals("view")) {

					try {

						Exercise.loadAllByUserId(conn, userId);
					} catch (Exception f) {
						System.out.println("Nie znaleziono rekordu o podanym id!");
					}

				}
				if (!"edit".equals(answer) && (!"add".equals(answer)) && (!"delete".equals(answer))
						&& (!"quit".equals(answer))) {
					System.out.println("Niepoprawne wprowadzenie! Spróbuj jeszcze raz!");
					sc.nextLine();
					continue;
				}

			} while (!"quit".equals(answer));
			{

				System.out.println("Koniec programu");
				conn.close();
			}
			sc.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}

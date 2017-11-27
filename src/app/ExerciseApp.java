package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import model.Exercise;

public class ExerciseApp {

	public static void main(String[] args) {

		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/warsztat2?useSSL=false", "root",
					"coderslab");
			Exercise[] exercises = Exercise.loadAllExercises(conn);
			System.out.println("Lista wszystkich zadań");
			for (int i = 0; i < exercises.length; i++) {
				Exercise newEx = exercises[i];
				System.out.println(newEx.getName() + ": " + newEx.getDescription());
			}
			System.out.println();
			Scanner sc = new Scanner(System.in);
			String answer;

			do {

				System.out.println("Wybierz jedną z opcji: add, edit, delete, quit.");
				answer = sc.next();

				if (answer.equals("add")) {

					System.out.println("Podaj tytuł zadania");
					String name = sc.nextLine();
					System.out.println("Podaj treść zadania");
					String description = sc.nextLine();
					
					Exercise exercise = new Exercise(name, description);
					exercise.saveToDB(conn);
					
					System.out.println("Dodano zadanie do bazy danych.");
					continue;

				}
				if (answer.equals("edit")) {

					System.out.println("Wpisz id zadania do edycji");
					int id = sc.nextInt();
					Exercise exercise = Exercise.loadExerciseById(conn, id);
					System.out.println("Podaj nowy tytuł zadania");
					String title = sc.next();
					exercise.setName(title);
					System.out.println("Podaj nową treść zadania");
					String description = sc.next();
					exercise.setDescription(description);
					
					exercise.saveToDB(conn);

					System.out.println("Edytowano zadanie w bazie danych.");
					continue;

				}
				if (answer.equals("delete")) {

					System.out.println("Podaj id zadania do usunięcia");
					int id = sc.nextInt();

					Exercise exercise = Exercise.loadExerciseById(conn, id);
					try {
						exercise.deleteExercise(conn);
						System.out.println("Usunięto zadanie z bazy danych.");
					} catch (Exception e) {
						System.out.println("Nie można usunąć zadań mających przypisane rozwiązania!");
						continue;
					}
					continue;

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

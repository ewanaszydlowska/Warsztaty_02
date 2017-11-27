package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import model.Group;

public class GroupApp {
	
	public static void main(String[] args) {

		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/warsztat2?useSSL=false", "root",
					"coderslab");
			Group[] groups = Group.loadAllGroups(conn);
			System.out.println("Lista wszystkich grup");
			for (int i = 0; i < groups.length; i++) {
				Group newGroup = groups[i];
				System.out.println(newGroup.getName());
			}
			System.out.println();
			Scanner sc = new Scanner(System.in);
			String answer;

			do {

				System.out.println("Wybierz jedną z opcji: add, edit, delete, quit.");
				answer = sc.next();

				if (answer.equals("add")) {
					
					System.out.println("Podaj nazwę grupy");
					String name = sc.next();
					
					Group group = new Group(name);
					group.saveToDB(conn);
					System.out.println("Dodano grupę do bazy danych.");
					continue;

				}
				if (answer.equals("edit")) {

					System.out.println("Wpisz id grupy do edycji");
					int id = sc.nextInt();
					Group group = Group.loadGroupById(conn, id);
					System.out.println("Podaj nową nazwę grupy");
					String name = sc.next();
					group.setName(name);
					
					group.saveToDB(conn);
					
					System.out.println("Edytowano grupę w bazie danych.");
					continue;

				}
				if (answer.equals("delete")) {

					System.out.println("Podaj id grupy do usunięcia");
					int id = sc.nextInt();
					
					Group group = Group.loadGroupById(conn, id);

					try {
						group.deleteGroup(conn);
						System.out.println("Usunięto grupę z bazy danych.");
					} catch (Exception e) {
						System.out.println("Nie można usunąć grupy mającej przypisanych użytkowników!");
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

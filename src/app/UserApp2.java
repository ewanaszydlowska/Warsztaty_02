package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import model.Exercise;

public class UserApp2 {
	public static void main(String[] args) {

		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/warsztat2?useSSL=false", "root",
					"coderslab");
			Scanner sc = new Scanner(System.in);
//		System.out.println("Podaj id zadania");
//		String id = sc.next();
			
			String title = "lzadanie 5";
			String description= "oblicz x";
			
			Exercise ex = new Exercise(title, description);
			ex.saveToDB(conn);
			
			System.out.println("dodano");
//			System.out.println("Podaj tytuł zadania");
//			String title = sc.next();
//			System.out.println("Podaj treść zadania");
//			String description = sc.next();
//			
//			Exercise exercise = new Exercise(title, description);
//			exercise.saveToDB(conn);
//			
//			System.out.println("Dodano zadanie do bazy danych.");
//			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

}

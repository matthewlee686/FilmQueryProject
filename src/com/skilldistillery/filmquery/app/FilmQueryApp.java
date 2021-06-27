package com.skilldistillery.filmquery.app;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {
  
  DatabaseAccessor db = new DatabaseAccessorObject();

  public static void main(String[] args) throws SQLException {
    FilmQueryApp app = new FilmQueryApp();
    app.launch();
  }

  private void launch() throws SQLException {
    Scanner input = new Scanner(System.in);
    
    startUserInterface(input);
    
    input.close();
  }

  private void startUserInterface(Scanner input) throws SQLException {
	  
	  
	  int choice;
	  int filmId;
    
	   do {
		   
		   System.out.println("           Menu");
		   System.out.println("---------------------------");
		   System.out.println("1.) Look up Film by ID");
		   System.out.println("2.) Look up Film by Search Keyword");
		   System.out.println("3.) Exit");
		   System.out.print("\nEnter choice here: ");
		   choice = input.nextInt();
		   

		   //Search by Film ID
		   if(choice == 1) {
			   
			   System.out.println("\nPlease enter the film id you would like to view: ");
			   filmId = input.nextInt();

			   Film film = db.findFilmById(filmId);
			   
			   if(film == null) {
				   System.out.println("\nNo Film Found\n"); //Will notify if no Film ID found
			   } else {
				   System.out.println(film);
			   }
		   } 
		   
		   //Search for Film by Search Keyword
		   else if (choice == 2) {
			   
			   System.out.println("Please enter your Search Query: ");
			   String searchQuery = input.next();
			   List<Film> film = db.findFilmBySearchQuery(searchQuery);
			   
			   if(film.size() == 0) {
				   System.out.println("\nYour Search has: " + film.size() + " results\n"); //Will notify if no Film ID found
			   } else {
				   System.out.println(film);
				   System.out.println("\nYour Search has: " + film.size() + " results\n"); //Search Result Size
			   }
		   }
		   
		   //Exit the Menu
		   else if (choice == 3) {
			   System.out.println("\nBreak Loop\n");
			   break;
		   }
		   
		   //Input Validation
		   else {
			   System.out.println("\nInvalid Choice\n");
		   }
		   
	   } while(choice != 3);
	   
	   System.out.println("\nEnd Program");
	 
  }

}

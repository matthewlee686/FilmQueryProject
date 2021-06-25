package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
	
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	

  @Override
  public Film findFilmById(int filmId) throws SQLException{
	  
	  Film film = null; 
	  
	  String user = "student";
	  String pass = "student";
		
	  Connection conn = DriverManager.getConnection(URL, user, pass); 
	  
	  String sql = "SELECT * FROM film WHERE id = ?";
	  PreparedStatement stmt = conn.prepareStatement(sql);
	  stmt.setInt(1, filmId);
	  ResultSet filmResult = stmt.executeQuery();
	  
	  while(filmResult.next()) {
		  
		  filmId = filmResult.getInt(filmId);
	      String title = filmResult.getString("title");
	      String desc = filmResult.getString("description");
	      short releaseYear = filmResult.getShort("release_year");
	      int langId = filmResult.getInt("language_id");
	      int rentDur = filmResult.getInt("rental_duration");
	      double rate = filmResult.getDouble("rental_rate");
	      int length = filmResult.getInt("length");
	      double repCost = filmResult.getDouble("replacement_cost");
	      String rating = filmResult.getString("rating");
	      String features = filmResult.getString("special_features");
	      
	      film = new Film(filmId, title, desc, releaseYear, langId,
	                           rentDur, rate, length, repCost, rating, features);
		  
	  }
	  
	  //Close
	  stmt.close();
	  filmResult.close();
	  conn.close();
	  
	  
    return film;
  }


	@Override
	public Actor findActorById(int actorId) throws SQLException {
		
		Actor actor = null; 
		  
		String user = "student";
		String pass = "student";
			
		Connection conn = DriverManager.getConnection(URL, user, pass); 
		
		String sql = "SELECT * FROM actor WHERE id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, actorId);
		ResultSet actorResult = stmt.executeQuery();
		
		while(actorResult.next()) {
			
			actorId = actorResult.getInt(actorId);
			String firstName = actorResult.getString("first_name");
			String lastName = actorResult.getString("last_name");
			
			actor = new Actor(actorId, firstName, lastName);
	
		}
		
		//Close
		stmt.close();
		actorResult.close();
		conn.close();
		
		return actor;
	}


	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> actors = new ArrayList<>(); 
		
		try {
		
		String user = "student";
		String pass = "student";
			
		Connection conn = DriverManager.getConnection(URL, user, pass); 
		
		String sql = "SELECT actor.id, actor.first_name, actor.last_name\n"
				+ "FROM actor JOIN film_actor ON actor.id = film_actor.actor_id\n"
				+ "JOIN film ON film_actor.film_id = film.id\n"
				+ "WHERE film.id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, filmId);
		ResultSet result = stmt.executeQuery();
		
		while(result.next()) {
			
			int actorId = result.getInt("actor.id");
			String firstName = result.getString("actor.first_name");
			String lastName = result.getString("actor.last_name");
			
			Actor actor = new Actor(actorId, firstName, lastName);
			
			actors.add(actor);
			
		}
		
		//Close
		stmt.close();
		result.close();
		conn.close();
		
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return actors;
	}
	
	
}//Class

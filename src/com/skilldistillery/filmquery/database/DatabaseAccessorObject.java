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
	public Film findFilmById(int filmId) throws SQLException {

		Film film = null;
		List<Actor> actors = new ArrayList<>();

		String user = "student";
		String pass = "student";

		Connection conn = DriverManager.getConnection(URL, user, pass);

		String sql = "SELECT *, actor.first_name, actor.last_name\n" + "FROM film\n"
				+ "JOIN language ON film.language_id = language.id\n"
				+ "JOIN film_actor ON film.id = film_actor.film_id\n" + "JOIN actor ON actor.id = film_actor.actor_id\n"
				+ "WHERE film.id = ?";

		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, filmId);

//	  System.out.println(stmt);

		ResultSet filmResult = stmt.executeQuery();

		while (filmResult.next()) {

			String title = filmResult.getString("title");
			short releaseYear = filmResult.getShort("release_year");
			String rating = filmResult.getString("rating");
			String desc = filmResult.getString("description");
			String lang = filmResult.getString("language.name");

			film = new Film(title, releaseYear, rating, desc, lang, actors);

			String actorFirstName = filmResult.getString("actor.first_name");
			String actorLastName = filmResult.getString("actor.last_name");

			Actor actor = new Actor(actorFirstName, actorLastName);
			actors.add(actor);
//			System.out.println(actors);

		}

		// Close
		stmt.close();
		filmResult.close();
		conn.close();

		return film;

	}

	// Function to look up Film by Search Query
	public List<Film> findFilmBySearchQuery(String search) throws SQLException {

		List<Film> films = new ArrayList<>();
		List<Actor> actors = new ArrayList<>();

		String user = "student";
		String pass = "student";

		Connection conn = DriverManager.getConnection(URL, user, pass);

		String sql = "SELECT *, language.name FROM film JOIN language ON film.language_id = language.id WHERE film.title LIKE ? OR film.description LIKE ?";

		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, "%" + search + "%");
		stmt.setString(2, "%" + search + "%");

//		System.out.println(stmt);

		ResultSet filmResult = stmt.executeQuery();

		while (filmResult.next()) {

			String title = filmResult.getString("title");
			short releaseYear = filmResult.getShort("release_year");
			String rating = filmResult.getString("rating");
			String desc = filmResult.getString("description");
			String lang = filmResult.getString("language.name");
			
			
			//Will return a separate list of Actors 
			//so the query will not return multiple results of the same Film
			actors = findActorsByFilmId(filmResult.getInt("film.id")); 

			Film film = new Film(title, releaseYear, rating, desc, lang, actors);

			films.add(film);
			
		}

		return films;
	}
	

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		
		List<Actor> actors = new ArrayList<>(); 
		
		try {
		
		String user = "student";
		String pass = "student";
			
		Connection conn = DriverManager.getConnection(URL, user, pass); 
		
		String sql = "SELECT actor.first_name, actor.last_name FROM actor JOIN film_actor ON actor.id = film_actor.actor_id JOIN film ON film_actor.film_id = film.id WHERE film.id = ?";
		
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, filmId);
		ResultSet result = stmt.executeQuery();
		
//		System.out.println(stmt);
		
		while(result.next()) {
			
			String firstName = result.getString("actor.first_name");
			String lastName = result.getString("actor.last_name");
			
			Actor actor = new Actor(firstName, lastName);
			
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


}// Class

package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

		String user = "student";
		String pass = "student";

		Connection conn = DriverManager.getConnection(URL, user, pass);

		String sql = "SELECT * FROM film JOIN language ON film.language_id = language.id WHERE film.id = ?";
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

			film = new Film(title, releaseYear, rating, desc, lang);

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

		String user = "student";
		String pass = "student";

		Connection conn = DriverManager.getConnection(URL, user, pass);

		String sql = "SELECT *, language.name FROM film JOIN language ON film.language_id = language.id WHERE film.title LIKE '%" + search + "%' OR film.description LIKE '%" + search + "%'";

		PreparedStatement stmt = conn.prepareStatement(sql);

//		System.out.println(stmt);

		ResultSet filmResult = stmt.executeQuery();

		while (filmResult.next()) {

			String title = filmResult.getString("title");
			short releaseYear = filmResult.getShort("release_year");
			String rating = filmResult.getString("rating");
			String desc = filmResult.getString("description");
			String lang = filmResult.getString("language.name");

			Film film = new Film(title, releaseYear, rating, desc, lang);

			films.add(film);

		}

		return films;
	}

}// Class

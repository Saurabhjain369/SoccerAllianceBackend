package com.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Dataconnection {

	private static final String url1 = "jdbc:mysql://ujjxirs2mutw76um:6MTQ28uIk1EnJUdYQ7QH@bofoxytoicrjg3ht0q2t-mysql.services.clever-cloud.com:3306/bofoxytoicrjg3ht0q2t";
	private static final String user = "ujjxirs2mutw76um";
	private static final String password = "6MTQ28uIk1EnJUdYQ7QH";

	Connection conn = null;

	public Connection getCon() throws SQLException {

		try {

			System.out.println("Connecting to database...");

			 Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url1, user, password);


		} catch (SQLException e) {
			System.out.println("SQL Error : " + e.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return conn;

	}

}
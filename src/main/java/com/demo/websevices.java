package com.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Path("api")

public class websevices {

	JSONObject main = new JSONObject();
	JSONArray ja = new JSONArray();
	JSONObject jb = new JSONObject();
	String sql = "";
	Statement stmt = null;
	ResultSet rs;
	PreparedStatement pstmt = null;

	Connection conn = null;
	Dataconnection con = new Dataconnection();

	// Add parameter in function argument to reduce use of conn object
	private void createStatement() {
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// rs = stmt.executeQuery(sql);
	}

	private void closeConn() {
		
		try {
			
			if(rs != null) {
				
				rs.close();
			}
			if(stmt != null) {
				
				stmt.close();
			}
			if(pstmt != null) {
				pstmt.close();
			}
			if(conn != null) {
				
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@GET
	@Path("test")
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {

		try {

			conn = con.getCon();
			if(conn != null) {
			
			
				
				main.accumulate("Status", 200);
				main.put("TimeStamp", System.currentTimeMillis() / 1000);
				main.accumulate("message", "db connected.");

			

			
		}else {
				
				main.accumulate("Status", "Error");
				main.put("TimeStamp", System.currentTimeMillis() / 1000);
				main.accumulate("message", "Connection error!!!!");
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			closeConn();
		}

		return main.toString();

	}

	

	//working
	//https://soccerallianceapp.appspot.com/rest/api/registerUser&4&John&john1@gmail.com&5147788987&0&India&22&1&noPhoto
	@POST
	@Path("registerUser")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String registerUser(User user) {

		System.out.println(user);
		try {

			conn = con.getCon();
			if (conn != null) {

				System.out.println("Connected to the database test1");
				sql = "insert into users(user_id,full_name,email,phone,gender,country, age, user_type,user_photo) values('" + user.getUser_id() + "','"
						+ user.getFull_name() + "','" + user.getEmail() + "','" + user.getPhone() + "','" + user.getGender() + "','" + user.getCountry() + "','" + user.getAge()
						+ "','" + user.getUser_type() + "','" + user.getUser_photo() + "')";
				createStatement();
				int i = stmt.executeUpdate(sql);
				System.out.println(i);
				if (i > 0) {

					main.accumulate("Status", 200);
					main.put("TimeStamp", System.currentTimeMillis() / 1000);
					main.accumulate("message", "User registered..");

				} else {
					main.accumulate("Status", "Warning");
					main.put("TimeStamp", System.currentTimeMillis() / 1000);
					main.accumulate("message", "User not registered..");

				}
			} else {

				main.accumulate("Status", "Warning");
				main.put("TimeStamp", System.currentTimeMillis() / 1000);
				main.accumulate("message", "Connection error!!!!");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeConn();
		}

		return main.toString();

	}

	/*http://localhost:8080/rest/api/listOfLeague_guestDashboard*/
	//working
	@GET
	@Path("listOfLeague_guestDashboard")
	@Produces(MediaType.TEXT_PLAIN)
	public String listOfLeague_guestDashboard() {

		try {

			conn = con.getCon();

			if(conn != null) {
			System.out.println("Connected to the database test1");
			sql = "SELECT league_id,Name,logo  from leagues";
			createStatement();
			rs = stmt.executeQuery(sql);

			main.put("Status", 200);
			main.put("TimeStamp", System.currentTimeMillis() / 1000);

			if (rs.next() == false) {
				main.put("Status", "Error");
				main.put("TimeStamp", System.currentTimeMillis() / 1000);
				main.put("message","Something went wrong !!!! No league found");
			} else {

				do {
					int league_id= rs.getInt("league_id");
					String name = rs.getString("name");
					String logo = rs.getString("logo");
					
					jb.accumulate("league_id", league_id);
					jb.accumulate("name", name);
					jb.accumulate("logo", logo);
					ja.add(jb);
					jb.clear();
				} while (rs.next());
			}
			main.accumulate("Leagues", ja);

			}else {
				
				main.put("Status", "Error");
				main.put("TimeStamp", System.currentTimeMillis() / 1000);
				main.put("Message", "Connection error");
				
			}	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			closeConn();
		}

		return main.toString();

	}


	/*http://localhost:8080/rest/api/upcomingMatches_guestDashboard*/
	//working
	@GET
	@Path("upcomingMatches_guestDashboard")
	@Produces(MediaType.TEXT_PLAIN)
	public String upcomingMatches_guestDashboard() {


		try {

			conn = con.getCon();
			
			System.out.println("Connected to the database test1");
			if(conn != null) {
			
			sql = "select t1.name as team1,t1.logo as team1_logo,t2.name as team2,t2.logo as team2_logo,date_of_match from schedules inner join teams as t1 on schedules.team1_id = t1.team_id inner join teams as t2 on schedules.team2_id = t2.team_id WHERE schedules.Date_of_match > CURDATE()";
			createStatement();
			rs = stmt.executeQuery(sql);

			main.put("Status", 200);
			main.put("TimeStamp", System.currentTimeMillis() / 1000);

			if (rs.next() == false) {
				main.put("Status", "Error");
				main.put("TimeStamp", System.currentTimeMillis() / 1000);
				main.put("mesage", "Details not found");
			} else {

				do {
					String team1 = rs.getString("team1");
					String team2 = rs.getString("team2");
					String team1_logo = rs.getString("team1_logo");
					String team2_logo = rs.getString("team2_logo");
					String date_of_match = rs.getString("Date_of_match");
					jb.accumulate("team1", team1);
					jb.accumulate("team1_logo", team1_logo);
					jb.accumulate("team2", team2);
					jb.accumulate("team2_logo", team2_logo);
					jb.accumulate("date_of_match", date_of_match);
					ja.add(jb);
					jb.clear();
				} while (rs.next());
			}
			main.accumulate("UpcomingMatchList", ja);

			}else
			{
				main.put("Status", 200);
				main.put("TimeStamp", System.currentTimeMillis() / 1000);
                main.put("message", "connection error!!!");
					
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			closeConn();
		}

		return main.toString();
	}
	
	/*http://localhost:8080/rest/api/playedMatches_guestDashboard*/
	@GET
	@Path("playedMatches_guestDashboard")
	@Produces(MediaType.TEXT_PLAIN)
	public String playedMatches_guestDashboard() {

		try {

			conn = con.getCon();

			System.out.println("Connected to the database test1");
			if(conn != null) {
			
			sql = "select t1.name as team1,t1.logo as team1_logo,t2.name as team2,t2.logo as team2_logo,date_of_match from schedules inner join teams as t1 on schedules.team1_id = t1.team_id inner join teams as t2 on schedules.team2_id = t2.team_id WHERE schedules.Date_of_match <= CURDATE()";
			createStatement();
			rs = stmt.executeQuery(sql);

			main.put("Status", 200);
			main.put("TimeStamp", System.currentTimeMillis() / 1000);

			if (rs.next() == false) {
				main.put("Status", "Error");
				main.put("TimeStamp", System.currentTimeMillis() / 1000);
				main.put("mesage", "Details not found");
			} else {

				do {
					
					String team1 = rs.getString("team1");
					String team2 = rs.getString("team2");
					String team1_logo = rs.getString("team1_logo");
					String team2_logo = rs.getString("team2_logo");
					String date_of_match = rs.getString("Date_of_match");
					jb.accumulate("team1", team1);
					jb.accumulate("team1_logo", team1_logo);
					jb.accumulate("team2", team2);
					jb.accumulate("team2_logo", team2_logo);
					jb.accumulate("date_of_match", date_of_match);
					ja.add(jb);
					jb.clear();
				} while (rs.next());
			}
			main.accumulate("PlayedMatchList", ja);

			}else
			{
				main.put("Status", 200);
				main.put("TimeStamp", System.currentTimeMillis() / 1000);
                main.put("message", "connection error!!!");
					
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			closeConn();
		}

		return main.toString();

	}
	
	//working	
	/*http://localhost:8080/rest/api/AddPlayerInTeam&Jiny&nophoto&goalkeeper&79&2*/
	@POST
	@Path("AddPlayerInTeam")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String AddPlayerInTeam(Player player) {

		int team_id, player_id;
		String sql1, sql2, sql3;
		Statement stmt1 = null;
		Statement stmt2 = null;
		ResultSet rs1 = null;

		try {

			conn = con.getCon();
			System.out.println("Connected to the database test1");
			if (conn != null) {

				sql = "select team_id from teams where user_id= '" + player.getUser_id()+ "' ";

				stmt = conn.createStatement();

				rs = stmt.executeQuery(sql);

				main.accumulate("Status", 200);
				main.put("TimeStamp", System.currentTimeMillis() / 1000);

				while (rs.next()) {

					team_id = rs.getInt("team_id");
					System.out.println("Team id: " + team_id);
					sql1 = "insert into players(full_name,player_photo,position,strength) values('" +player.getFull_name() + "','"
							+ player.getPlayer_photo() + "','" + player.getPosition() + "','" + player.getStrength() + "')";
					stmt1 = conn.createStatement();
					System.out.println("SQL Query: " + sql1);
					int i = stmt1.executeUpdate(sql1);

					if (i > 0) {

						sql2 = "select player_id from players order by player_id DESC LIMIT 1";
						stmt2 = conn.createStatement();
						rs1 = stmt2.executeQuery(sql2);

						while (rs1.next()) {

							player_id = rs1.getInt("player_id");

							System.out.println("Player id: " + player_id);
							System.out.println();

							sql3 = "insert into players_in_team(player_id , team_id)values(" + player_id+ ","
									+ team_id + ")";

							int k = stmt2.executeUpdate(sql3);

							if (k > 0) {

								main.accumulate("message", "Player added to the team!!!!");
							} else {

								main.accumulate("message", "Player not added to the team!!!!");
							}

						}
					} else {

						main.accumulate("Status", "Warning");
						main.put("TimeStamp", System.currentTimeMillis() / 1000);
						main.accumulate("message", "Player not added to the team!!!!");
					}
				}

			} else {
				main.accumulate("Status", "Warning");
				main.put("TimeStamp", System.currentTimeMillis() / 1000);
				main.accumulate("message", "Connection not Found!!!!");

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {

			closeConn();
		}
		return main.toString();
	}


	//working
	/*http://localhost:8080/rest/api/ModifyPlayerDetails&8&shibit&Nophoto&fielder&55*/
	@POST
	@Path("ModifyPlayerDetails")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String ModifyPlayerDetails(Player player) {
		try {

			conn = con.getCon();

			System.out.println("Connected to the database test1");

			sql = "update players set full_name='" + player.getFull_name() + "' ,player_photo ='" + player.getPlayer_photo() + "' , position ='"
					+ player.getPosition() + "',strength ='" + player.getStrength() + "' where player_id= "+ player.getPlayer_id() +" ";
			
			createStatement();
			int i = stmt.executeUpdate(sql);
			System.out.println(sql);
			if (i>0) {

				main.accumulate("Status", 200);
				main.put("TimeStamp", System.currentTimeMillis() / 1000);
				main.accumulate("message", "player detail modified..");

			} else {
				main.accumulate("Status", "Warning");
				main.put("TimeStamp", System.currentTimeMillis() / 1000);
				main.accumulate("message", "player detail not modified..");

			}

			conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return main.toString();
	}
	//working
	
	@GET
	@Path("leagueDetailsByleagueID&{league_id}")
	@Produces(MediaType.TEXT_PLAIN)
	public String leagueDetailsByleagueID(@PathParam("league_id") int league_id) {

		try {

			conn = con.getCon();

			System.out.println("Connected to the database test1");
			if(conn != null) {
			sql = "SELECT Name,logo from leagues where league_id= '"+league_id+"'";
			createStatement();
			rs = stmt.executeQuery(sql);

			main.put("Status", 200);
			main.put("TimeStamp", System.currentTimeMillis() / 1000);

			if (rs.next() == false) {
				main.put("Status", "Warning");
				main.put("TimeStamp", System.currentTimeMillis() / 1000);
				main.put("message", "league not found");
			} else {

				do {
					league_id= rs.getInt("league_id");
					String name = rs.getString("name");
					String logo = rs.getString("logo");
					
					jb.accumulate("leagueId", league_id);
					jb.accumulate("name", name);
					jb.accumulate("logo", logo);
				
				} while (rs.next());
				main.accumulate("league", jb);
				
			}
		}
			else {

				main.put("Status", 200);
				main.put("TimeStamp", System.currentTimeMillis() / 1000);
				main.put("message", "connection error");
				
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			closeConn();
		}

		return main.toString();

	}
	
	//working
	/* http://localhost:8080/rest/api/createLeague&KLG&NoPhoto&6&3 */
	@POST
	@Path("createLeague")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)

	public String createLeague(League league) {
		try {
			conn = con.getCon();
		
		if (conn != null) {

			System.out.println("Connected to the database (in CreateLeague Method)");

			String sql = "INSERT INTO leagues(name,logo,no_of_teams,user_id) VALUES ('" + league.getName() + "', '" + league.getLogo() + "',"
					+ league.getNo_of_teams() + "," + league.getUser_id() + ")";

			stmt = conn.createStatement();
				int i = stmt.executeUpdate(sql);

				if (i > 0) {
					main.accumulate("Status", 200);
					main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
					main.accumulate("message", "League suceesfully Created.");

				} else {
					main.accumulate("Status", "Error");
					main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
					main.accumulate("message", "Something went wrong! League not Created");

				}

		}
		else {
			main.accumulate("Status", "Error");
			main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
			main.accumulate("message", "ConnectionError");
		}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("S Error  : " + e.getMessage());
		} finally {
			closeConn();
		}
		return main.toString();
	}
	//working
	/*http://localhost:8080/rest/api/ListOfCountries*/
	
	@GET
	@Path("ListOfCountries")
	@Produces(MediaType.TEXT_PLAIN)
	public String CountryList() {

		try {

			conn = con.getCon();

			if (conn != null) {
				System.out.println("Connected to the database test1");
				sql = "SELECT DISTINCT Country from users;";
				createStatement();
				rs = stmt.executeQuery(sql);
				
				main.put("Status",200);
				main.put("Timestamp",System.currentTimeMillis()/1000);
				
				if(rs.next() ==  false) {
					main.put("Status", "Warning");
					main.put("Timestamp",System.currentTimeMillis()/1000);
					main.put("message", "Something went wrong!!! Countries not found.");
				}else {
					
					do {
						String Country = rs.getString("Country");
						ja.add(Country);
						
					}	while (rs.next()) ;
				}
				
				main.accumulate("Countries", ja);
			}
			else {
				
				main.put("Status", "Warning");
				main.put("Timestamp",System.currentTimeMillis()/1000);
				main.put("message", "Connection error!!!");	
			
			}

		} catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("S Error  : " + e.getMessage());

		}
		finally {
      	  closeConn();
			
		}
		return main.toString();
		}
	
	//working
	//http://localhost:8080/rest/api/CreateSchedule&Torronto&2020-05-27&15:00PM&1&2&2
	@POST
	@Path("CreateSchedule")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String CreateSchedule(Schedule schedule) {

		try {
			conn = con.getCon();
			System.out.println("Connected to the database test1");

			if (conn != null) {
				String sql = "INSERT into schedules(Location,date_of_match,Time_of_match,Team1_id,Team2_id,League_id)VALUES ('"
						+ schedule.getLocation() + "','" + schedule.getDate_of_match() + "','" + schedule.getTime_of_match() + "','" + schedule.getTeam1_id() + "','" + schedule.getTeam2_id()
						+ "','" + schedule.getLeague_id() + "')";
				createStatement();

				int i = stmt.executeUpdate(sql);
				System.out.println(i);

				if (i > 0) {
					main.accumulate("Status", "OK");
					main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
					main.accumulate("Message", "Schedule for The Match Is Fixed!!!");

				} else {
					main.accumulate("Status", "Error");
					main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
					main.accumulate("Message", "Something went wrong!!! Schedule not fixed..");

				}

			} else {
				main.accumulate("Status", "Error");
				main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
				main.accumulate("Message", "COnnection error");

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("S Error  : " + e.getMessage());

		} finally {
			closeConn();

		}

		return main.toString();

	}

	//working
	//http://localhost:8080/rest/api/ReSchedule&USA&2020-05-27&15:00PM&3&5&8&7
	@POST
	@Path("ReSchedule")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String ReSchedule(Schedule schedule) {

		try {
			conn = con.getCon();
			System.out.println("Connected to the database test1");

			if (conn != null) {
				String sql = "UPDATE schedules SET Location = '" + schedule.getLocation() + "',date_of_match = '" + schedule.getDate_of_match()
						+ "',time_of_match = '" + schedule.getTime_of_match() + "', team1_id = '" + schedule.getTeam1_id() + "',team2_id = '"
						+ schedule.getTeam2_id() + "', league_id ='" + schedule.getLeague_id() + "'  WHERE schedule_id='" + schedule.getSchedule_id() + "'";

				createStatement();

				int i = stmt.executeUpdate(sql);
				System.out.println(i);

				if (i > 0) {
					main.accumulate("Status", "OK");
					main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
					main.accumulate("Message", "Match is Reschduled!!!");

				} else {
					main.accumulate("Status", "Error");
					main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
					main.accumulate("Message", "Something went wrong!!!");

				}

			} else {
				main.accumulate("Status", "Error");
				main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
				main.accumulate("Message", "Connection Error!!!");

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(" Error  : " + e.getMessage());

		} finally {
			closeConn();

		}

		return main.toString();
	}

	 	    //working
	    /*http://localhost:8080/rest/api/ListOfLeaguesByCountry&CANADA*/
	    @GET
		@Path("ListOfLeaguesByCountry&{Country}")
		@Produces(MediaType.APPLICATION_JSON)
		public String ListOfLeaguesByCountry(@PathParam("Country") String Country) {
	    	
			try {

				conn = con.getCon();

				if (conn != null) {
					System.out.println("Connected to the database test1");
					sql = "select name,league_id,logo from leagues INNER JOIN users ON leagues.user_id = users.user_id WHERE users.Country = '"+Country+"' ";
					createStatement();
					rs = stmt.executeQuery(sql);
					
					main.put("Status",200);
					main.put("Timestamp",System.currentTimeMillis()/1000);
					
					
					if(rs.next() ==  false) {
						
						main.put("Status", "Error");
						main.put("Timestamp",System.currentTimeMillis()/1000);
						main.put("message", "fail to get country...");
						
					}else {
						do {
							int league_id = rs.getInt("league_id");							
							String name = rs.getString("name");
							String logo = rs.getString("logo");
							jb.accumulate("id", league_id);
							jb.accumulate("name", name);
							jb.accumulate("logo", logo);
							ja.add(jb);
							jb.clear();
							
						}	while (rs.next()) ;
					}
					
					main.accumulate("Leagues: ", ja);
				
				}
				else {
					main.put("Status", "Error");
					main.put("Timestamp",System.currentTimeMillis()/1000);
					main.put("message", "Connection error...");
				}

			} catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("S Error  : " + e.getMessage());

			}finally {
	        	  closeConn();
					
			}

			return main.toString();

		}
	    
	  	    

	    /*farhin updateprofile and upadteplayerINfo not done 404 error*/
	    //working
	    //CreateTeam&jjl&nophoto&jl&1
	    @POST
		@Path("CreateTeam")
		@Produces(MediaType.TEXT_PLAIN)
		@Consumes(MediaType.APPLICATION_JSON)
		public String CreateTeam(Team team) {

			try {
				conn = con.getCon();
				if (conn != null) {
					sql = "insert into teams(name,logo,team_label,user_id)values('" + team.getName() + "', '" + team.getLogo()
							+ "' ,'" + team.getTeam_label() + "','" + team.getUser_id() + "')";

					createStatement();
					int i = stmt.executeUpdate(sql);
					if (i > 0) {

						main.accumulate("status", 200);
						main.accumulate("Timestamp", System.currentTimeMillis() / 1000);
						main.accumulate("message", "Team created");

					} else {
						main.accumulate("status", "Error");
						main.accumulate("Timestamp", System.currentTimeMillis() / 1000);
						main.accumulate("message", "Team not created");

					}
				} else {
					main.accumulate("status", "Error");
					main.accumulate("Timestamp", System.currentTimeMillis() / 1000);
					main.accumulate("message", "Connection not done!!!");

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				closeConn();

			}

			return main.toString();

		}
	    

		@POST
		@Path("UpdateTeam")
		@Produces(MediaType.TEXT_PLAIN)
		@Consumes(MediaType.APPLICATION_JSON)
		public String UpdateTeam(Team team) {

			try {
				conn = con.getCon();
				if (conn != null) {
					sql = "update teams SET name = '" + team.getName() + "', logo ='" + team.getLogo() + "', team_label = '"
							+ team.getTeam_label() + "', user_id = '" + team.getUser_id() + "' WHERE team_id = '" + team.getTeam_id() + "' ";

					createStatement();
					int i = stmt.executeUpdate(sql);
					if (i > 0) {

						main.accumulate("status", 200);
						main.accumulate("Timestamp", System.currentTimeMillis() / 1000);
						main.accumulate("message", "Team detailes updated");

					} else {
						main.accumulate("status", "Error");
						main.accumulate("Timestamp", System.currentTimeMillis() / 1000);
						main.accumulate("message", "Team detailes not updated");

					}
				} else {
					main.accumulate("status", "Error");
					main.accumulate("Timestamp", System.currentTimeMillis() / 1000);
					main.accumulate("message", "Connection not done!!!");

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				closeConn();

			}

			return main.toString();

		}

		


	    //working
		@GET
		@Path("viewPlayerListFromTeam&{team_id}")
		@Produces(MediaType.TEXT_PLAIN)
		public String viewPlayerListFromTeam(@PathParam("team_id") int team_id_number) {

			try {
				conn = con.getCon();
				if (conn != null) {

					sql = "select players.player_id,full_name,player_photo,strength from players inner join players_in_team ON players.player_id = players_in_team.player_id AND team_id = "
							+ team_id_number;

					createStatement();
					rs = stmt.executeQuery(sql);

					if (rs.next() == true) {
						main.accumulate("status", 200);
						main.accumulate("Timestamp", System.currentTimeMillis() / 1000);
						do {
							String FullName, PlayerPhoto, Strength;
							int player_id;
							

							player_id = rs.getInt("player_id");
							FullName = rs.getString("full_name");
							PlayerPhoto = rs.getString("player_photo");
							Strength = rs.getString("strength");

							jb.accumulate("Playerid", player_id);
							jb.accumulate("FullName", FullName);
							jb.accumulate("PlayerPhoto", PlayerPhoto);
							jb.accumulate("Strength", Strength);

							ja.add(jb);
							jb.clear();

						} while (rs.next());

						main.accumulate("Player List", ja);
					} else {

						main.accumulate("status", "Error");
						main.accumulate("Timestamp", System.currentTimeMillis() / 1000);
						main.accumulate("message", "Record of players not found");
					}
				} else {

					main.accumulate("status", "Error");
					main.accumulate("Timestamp", System.currentTimeMillis() / 1000);
					main.accumulate("message", "Connection not done!!!");
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				closeConn();

			}

			return main.toString();

		}

		//working
		@GET
		@Path("viewTeamListFromLeagueId&{league_id}")
		@Produces(MediaType.TEXT_PLAIN)
		public String viewTeamListFromLeagueId(@PathParam("league_id") int league_id_number) {

			try {
				conn = con.getCon();
				if (conn != null) {
					sql = "select teams.team_id,name,logo from teams inner join league_team ON teams.team_id=league_team.team_id AND league_id="
							+ league_id_number;

					createStatement();
					rs = stmt.executeQuery(sql);

					if (rs.next() == true) {
						main.accumulate("status", 200);
						main.accumulate("Timestamp", System.currentTimeMillis() / 1000);
						do {
							String TeamName, Logo;
							int teamid;

							
							teamid = rs.getInt("team_id");
							TeamName = rs.getString("name");
							Logo = rs.getString("logo");

							jb.accumulate("Teamid", teamid);
							jb.accumulate("TeamName", TeamName);
							jb.accumulate("Logo", Logo);

							ja.add(jb);
							jb.clear();

						} while (rs.next());

						main.accumulate("Team List", ja);
					} else {

						main.accumulate("status", "Error");
						main.accumulate("Timestamp", System.currentTimeMillis() / 1000);
						main.accumulate("message", "Record of Teams using League_id not found");
					}
				} else {

					main.accumulate("status", "Error");
					main.accumulate("Timestamp", System.currentTimeMillis() / 1000);
					main.accumulate("message", "Connection not done!!!");
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				closeConn();

			}

			return main.toString();

		}

		//working
		@GET
		@Path("viewTeamList")
		@Produces(MediaType.TEXT_PLAIN)
		public String viewTeamsList() {

			try {
				conn = con.getCon();
				if (conn != null) {
					sql = "select * from bofoxytoicrjg3ht0q2t.teams";

					createStatement();
					rs = stmt.executeQuery(sql);

					if (rs.next() == true) {
						main.accumulate("status", 200);
						main.accumulate("Timestamp", System.currentTimeMillis() / 1000);
						do {
							int TeamId;
							String UserId,TeamName, Logo, TeamLabel;

							TeamId = rs.getInt("team_id");
							TeamName = rs.getString("name");
							Logo = rs.getString("logo");
							TeamLabel = rs.getString("team_label");
							UserId = rs.getString("user_id");

							jb.accumulate("Team Id", TeamId);
							jb.accumulate("Team Name", TeamName);
							jb.accumulate("Logo", Logo);
							jb.accumulate("Team Label", TeamLabel);
							jb.accumulate("User Id", UserId);

							System.out.println("user"+UserId);
							ja.add(jb);
							jb.clear();

						} while (rs.next());

						main.accumulate("Team List", ja);
					} else {

						main.accumulate("status", "Error");
						main.accumulate("Timestamp", System.currentTimeMillis() / 1000);
						main.accumulate("message", "Record of Teams not found");
					}
				} else {

					main.accumulate("status", "Error");
					main.accumulate("Timestamp", System.currentTimeMillis() / 1000);
					main.accumulate("message", "Connection not done!!!");
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				closeConn();

			}

			return main.toString();

		}

		//working
		//AddMatchScore&1&1&2&2&3&4&1&2&3&4&5&6&7&2&2&2&3&4&1&2&3&4&5&6&7
		@GET
		@Path("AddMatchScore&{match_id}&{team_id1}&{goal1}&{shots1}&{shots_on_target1}&{possession1}&{passes1}&{pass_accuracy1}&{fouls1}&{yellow_cards1}&{red_cards1}&{offsides1}&{corners1}&{team_id2}&{goal2}&{shots2}&{shots_on_target2}&{possession2}&{passes2}&{pass_accuracy2}&{fouls2}&{yellow_cards2}&{red_cards2}&{offsides2}&{corners2}")
		@Produces(MediaType.TEXT_PLAIN)
		public String AddMatchScore(@PathParam("match_id") int Match_Id, @PathParam("team_id1") int Team_Id1,
				@PathParam("goal1") int Goal1, @PathParam("shots1") int Shots1,
				@PathParam("shots_on_target1") int Shots_On_Target1, @PathParam("possession1") int Possession1,
				@PathParam("passes1") int Passes1, @PathParam("pass_accuracy1") int Pass_Accuracy1,
				@PathParam("fouls1") int Fouls1, @PathParam("yellow_cards1") int Yellow_Cards1,
				@PathParam("red_cards1") int Red_Cards1, @PathParam("offsides1") int Offsides1,
				@PathParam("corners1") int Corners1, @PathParam("team_id2") int Team_Id2, @PathParam("goal2") int Goal2,
				@PathParam("shots2") int Shots2, @PathParam("shots_on_target2") int Shots_On_Target2,
				@PathParam("possession2") int Possession2, @PathParam("passes2") int Passes2,
				@PathParam("pass_accuracy2") int Pass_Accuracy2, @PathParam("fouls2") int Fouls2,
				@PathParam("yellow_cards2") int Yellow_Cards2, @PathParam("red_cards2") int Red_Cards2,
				@PathParam("offsides2") int Offsides2, @PathParam("corners2") int Corners2) {
			try {
				conn = con.getCon();
				if (conn != null) {

					sql = "insert into scores (match_id,team_id,goal,shots,shots_on_target,possession,passes,pass_accuracy,fouls,yellow_cards,red_cards,offsides,corners)values('"
							+ Match_Id + "', '" + Team_Id1 + "' ,'" + Goal1 + "','" + Shots1 + "','" + Shots_On_Target1
							+ "','" + Possession1 + "','" + Passes1 + "','" + Pass_Accuracy1 + "','" + Fouls1 + "','"
							+ Yellow_Cards1 + "','" + Red_Cards1 + "','" + Offsides1 + "','" + Corners1 + "')";
					String sql2 = "insert into scores (match_id,team_id,goal,shots,shots_on_target,possession,passes,pass_accuracy,fouls,yellow_cards,red_cards,offsides,corners)values('"
							+ Match_Id + "', '" + Team_Id2 + "' ,'" + Goal2 + "','" + Shots2 + "','" + Shots_On_Target2
							+ "','" + Possession2 + "','" + Passes2 + "','" + Pass_Accuracy2 + "','" + Fouls2 + "','"
							+ Yellow_Cards2 + "','" + Red_Cards2 + "','" + Offsides2 + "','" + Corners2 + "')";

					createStatement();
					int i = stmt.executeUpdate(sql);
					if (i > 0) {

						createStatement();
						int j = stmt.executeUpdate(sql2);

						if (j > 0) {

							main.accumulate("status", 200);
							main.accumulate("Timestamp", System.currentTimeMillis() / 1000);
							main.accumulate("message", "Score is inserted");

						} else {
							main.accumulate("status", "Error");
							main.accumulate("Timestamp", System.currentTimeMillis() / 1000);
							main.accumulate("message", "Score is not inserted");
						}

					} else {
						main.accumulate("status", "Error");
						main.accumulate("Timestamp", System.currentTimeMillis() / 1000);
						main.accumulate("message", "Score is not inserted");

					}
				} else {

					main.accumulate("status", "Error");
					main.accumulate("Timestamp", System.currentTimeMillis() / 1000);
					main.accumulate("message", "Connection not done!!!");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				closeConn();

			}

			return main.toString();

		}

		//working
		//UpdateMatchScore&1&1&2&2&3&4&1&2&3&4&5&6&7&2&2&2&3&4&1&2&3&4&5&6&7
		@GET
		@Path("UpdateMatchScore&{match_id}&{team_id1}&{goal1}&{shots1}&{shots_on_target1}&{possession1}&{passes1}&{pass_accuracy1}&{fouls1}&{yellow_cards1}&{red_cards1}&{offsides1}&{corners1}&"
				+ "{team_id2}&{goal2}&{shots2}&{shots_on_target2}&{possession2}&{passes2}&{pass_accuracy2}&{fouls2}&{yellow_cards2}&{red_cards2}&{offsides2}&{corners2}")
		@Produces(MediaType.TEXT_PLAIN)
		public String UpdateMatchScore(@PathParam("match_id") int Match_Id, @PathParam("team_id1") int Team_Id1,
				@PathParam("goal1") int Goal1, @PathParam("shots1") int Shots1,
				@PathParam("shots_on_target1") int Shots_On_Target1, @PathParam("possession1") int Possession1,
				@PathParam("passes1") int Passes1, @PathParam("pass_accuracy1") int Pass_Accuracy1,
				@PathParam("fouls1") int Fouls1, @PathParam("yellow_cards1") int Yellow_Cards1,
				@PathParam("red_cards1") int Red_Cards1, @PathParam("offsides1") int Offsides1,
				@PathParam("corners1") int Corners1, @PathParam("team_id2") int Team_Id2, @PathParam("goal2") int Goal2,
				@PathParam("shots2") int Shots2, @PathParam("shots_on_target2") int Shots_On_Target2,
				@PathParam("possession2") int Possession2, @PathParam("passes2") int Passes2,
				@PathParam("pass_accuracy2") int Pass_Accuracy2, @PathParam("fouls2") int Fouls2,
				@PathParam("yellow_cards2") int Yellow_Cards2, @PathParam("red_cards2") int Red_Cards2,
				@PathParam("offsides2") int Offsides2, @PathParam("corners2") int Corners2) {

			try {
				conn = con.getCon();
				if (conn != null) {

					sql = "Update scores SET goal = " + Goal1 + ",shots =" + Shots1 + ",shots_on_target=" + Shots_On_Target1
							+ ",possession=" + Possession1 + ",passes=" + Passes1 + ",pass_accuracy=" + Pass_Accuracy1
							+ ",fouls=" + Fouls1 + ",yellow_cards=" + Yellow_Cards1 + ",red_cards=" + Red_Cards1
							+ ",offsides=" + Offsides1 + ",corners=" + Corners1 + "  where match_id = " + Match_Id
							+ " AND team_id = " + Team_Id1 + "";

					String sql2 = "Update scores SET goal = " + Goal2 + ",shots =" + Shots2 + ",shots_on_target="
							+ Shots_On_Target2 + ",possession=" + Possession2 + ",passes=" + Passes2 + ",pass_accuracy="
							+ Pass_Accuracy2 + ",fouls=" + Fouls2 + ",yellow_cards=" + Yellow_Cards2 + ",red_cards="
							+ Red_Cards2 + ",offsides=" + Offsides2 + ",corners=" + Corners2 + "  where match_id = "
							+ Match_Id + " AND team_id = " + Team_Id2 + "";

					System.out.println("SQL Query1: " + sql2);
					createStatement();
					int i = stmt.executeUpdate(sql);
					if (i > 0) {

						int j = stmt.executeUpdate(sql2);

						if (j > 0) {

							main.accumulate("status", 200);
							main.accumulate("Timestamp", System.currentTimeMillis() / 1000);
							main.accumulate("message", "Score is updated");

						} else {
							main.accumulate("status", "Error");
							main.accumulate("Timestamp", System.currentTimeMillis() / 1000);
							main.accumulate("message", "Team 2 Score is not updated");
						}

					} else {
						main.accumulate("status", "Error");
						main.accumulate("Timestamp", System.currentTimeMillis() / 1000);
						main.accumulate("message", "Team 1 Score is not updated");

					}
				} else {
					main.accumulate("status", "Error");
					main.accumulate("Timestamp", System.currentTimeMillis() / 1000);
					main.accumulate("message", "Connection not done!!!");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				closeConn();

			}

			return main.toString();

		}
		
		// http://localhost:8080/rest/api/LeagueListByUser&2

	    //working
		@GET
		@Path("LeagueListByUser&{userId}")
		@Produces(MediaType.TEXT_PLAIN)
		public String getleague(@PathParam("userId") String userid) {

			try {

				String name, logo,userId;
				int leagueId, noOfteams;

				main.clear();
				JSONArray ja = new JSONArray();
				JSONObject jb = new JSONObject();

				conn = con.getCon();

				if (conn != null) {

					String sql = "SELECT league_id,name,logo,no_of_teams,user_id FROM leagues where user_id = '" + userid
							+ "' ";

					stmt = conn.createStatement();
					rs = stmt.executeQuery(sql);

					if (rs.next() == true) {
						main.accumulate("Status", 200);
						main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);

						do {

							leagueId = rs.getInt("league_id");
							name = rs.getString("name");
							logo = rs.getString("logo");
							noOfteams = rs.getInt("no_of_teams");
							userId = rs.getString("user_id");

							jb.accumulate("LeagueId", leagueId);
							jb.accumulate("Name", name);
							jb.accumulate("logo", logo);
							jb.accumulate("noOfTeams", noOfteams);
							jb.accumulate("UserID", userId);

							ja.add(jb);
							jb.clear();

						} while (rs.next());
						main.accumulate("LeagueList", ja);
					} else {

						main.accumulate("Status", "Error");
						main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
						main.accumulate("Message", "No Records Found");

					}
				} else {
					main.accumulate("Status", "Error");
					main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
					main.accumulate("Message", "Connection Error");
				}
			} catch (SQLException e) {
				
				e.printStackTrace();
			} finally {
				closeConn();
			}

			return main.toString();
		}

		// http://localhost:8080/rest/api/AddTeamInLeague&8&2

		//working
		//http://localhost:8080/rest/api/AddTeamInLeague&2&1
		@GET
		@Path("AddTeamInLeague&{LeagueId}&{teamId}")
		@Produces(MediaType.TEXT_PLAIN)

		public String addteam(@PathParam("LeagueId") int leagueid, @PathParam("teamId") String teamid) {

			try {

				conn = con.getCon();

				if (conn != null) {

					System.out.println("Connected to the database (in if else part)");

					String sql = "INSERT INTO league_team VALUES ('" + leagueid + "','" + teamid + "')";
					stmt = conn.createStatement();

					int i = stmt.executeUpdate(sql);

					if (i > 0) {

						main.accumulate("Status", 200);
						main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
						main.accumulate("message", "Team suceesfully added in league.");
						System.out.println(main);

					} else {
						main.accumulate("Status", "Error");
						main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
						main.accumulate("message", "Something went wrong! team is not added in League.");
						System.out.println(main);
					}

				} else {
					main.accumulate("Status", "Error");
					main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
					main.accumulate("message", "ConnectionError");
				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("S Error  : " + e.getMessage());
			} finally {
				closeConn();
			}

			return main.toString();
		}

		// http://localhost:8080/rest/api/RemoveTeamFromLeague&8&3

		//working
		@GET
		@Path("RemoveTeamFromLeague&{LeagueId}&{teamId}")
		@Produces(MediaType.TEXT_PLAIN)

		public String removeteam(@PathParam("LeagueId") int leagueid, @PathParam("teamId") String teamid) {

			try {
				conn = con.getCon();

				if (conn != null) {

					System.out.println("Connected to the database (in if else part)");

					String sql = " DELETE FROM league_team WHERE league_id= '" + leagueid + "' AND  team_id= '" + teamid + "' ";

					stmt = conn.createStatement();
					int i = stmt.executeUpdate(sql);

					if (i > 0) {

						main.accumulate("Status", 200);
						main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
						main.accumulate("message", "Team suceesfully deleted from league.");
						System.out.println(main);

					} else {
						main.accumulate("Status", "Error");
						main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
						main.accumulate("message", "Something went wrong! team is not deleted from League.");
						System.out.println(main);
					}

				} else {
					main.accumulate("Status", "Error");
					main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);

					main.accumulate("message", "ConnectionError");
				}

			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("S Error  : " + e.getMessage());
			} finally {

				closeConn();
			}
			return main.toString();
		}

		// http://localhost:8080/rest/api/updateLeague&1&NPL&NoPhoto&12&1

		//working
		@POST
		@Path("updateLeague")
		@Produces(MediaType.TEXT_PLAIN)
		@Consumes(MediaType.APPLICATION_JSON)

		public String updateLeague(League league) {

			try {
			conn = con.getCon();

			
			if (conn != null) {

				System.out.println("Connected to the database (in if else part)");

				String sql = "update leagues SET name = '" + league.getName() + "', logo ='" + league.getLogo() + "', no_of_teams = '"
						+ league.getNo_of_teams() + "', user_id = '" + league.getUser_id() + "' WHERE league_id = '" + league.getLeague_id() + "' ";

					stmt = conn.createStatement();
					int i = stmt.executeUpdate(sql);
					System.out.println("sql"+sql);
					System.out.println("i"+i);
					if (i > 0) {

						main.accumulate("Status", "OK");
						main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
						main.accumulate("message", "League suceesfully updated.");
						//System.out.println(main);

					} else {
						main.accumulate("Status", "OK");
						main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
						main.accumulate("message", "Something went wrong! League not updated");
						//System.out.println(main);
					}

			} else {
				main.accumulate("Status", "Error");
				main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);

				main.accumulate("message", "ConnectionError");
			}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("S Error  : " + e.getMessage());
			} finally {

				closeConn();
			}
			return main.toString();
		}

		/*
		 * 
		 * @GET
		 * 
		 * @Path("PlayedMatchesByLeagueId&{leagueId}")
		 * 
		 * @Produces(MediaType.TEXT_PLAIN) public String
		 * playedmatches(@PathParam("leagueId") int leagueid) throws SQLException{
		 * 
		 * String name, logo; int leagueId,team1id,team2id,goalsTeam1,goalsTeam2;
		 * 
		 * 
		 * main.clear(); JSONArray ja = new JSONArray(); JSONObject main = new
		 * JSONObject();
		 * 
		 * conn = con.getCon();
		 * 
		 * if (conn != null) { try {
		 * 
		 * String sql = "\r\n" +
		 * "SELECT schedules.team1_id,schedules.team2_id,schedules.league_id,schedules.date_of_match,schedules.time_of_match,teams.team_id,teams.name,teams.logo,teams.team_label,scores.score_id,schedules.schedule_id,scores.team_id,scores.goal\r\n"
		 * + "FROM  (((schedules\r\n" + "INNER JOIN matches\r\n" +
		 * "ON schedules.schedule_id = matches.schedule_id)\r\n" +
		 * "INNER JOIN scores\r\n" + "ON matches.match_id = scores.match_id)\r\n" +
		 * "INNER JOIN teams\r\n" + "ON scores.team_id = teams.team_id)\r\n" +
		 * "where schedules.league_id = '"+leagueid+ "' ";
		 * 
		 * stmt = conn.createStatement(); rs = stmt.executeQuery(sql);
		 * 
		 * if (rs.next() == true) { main.accumulate("Status", "OK");
		 * main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
		 * 
		 * do { leagueId = rs.getInt("league_id"); name = rs.getString("name"); logo =
		 * rs.getString("logo"); goalsTeam1 = rs.getInt("goal"); team1id =
		 * rs.getInt("team1_id"); team2id = rs.getInt("team2_id");
		 * 
		 * 
		 * 
		 * main.accumulate("League Id : ", leagueId); main.accumulate("Name : ", name);
		 * 
		 * main.accumulate("logo :", logo); main.accumulate("goal : ", goalsTeam1);
		 * main.accumulate("team1 id :", team1id); main.accumulate("team2 id :",
		 * team2id);
		 * 
		 * 
		 * 
		 * ja.add(main); main.clear();
		 * 
		 * } while (rs.next()); main.accumulate("LeagueList", ja); } else {
		 * 
		 * main.accumulate("Status", "Error"); main.accumulate("TimeStamp",
		 * System.currentTimeMillis() / 1000); main.accumulate("Message",
		 * "No Records Found");
		 * 
		 * } } catch (SQLException ex) {
		 * Logger.getLogger(websevices.class.getName()).log(Level.SEVERE, null, ex); }
		 * finally { closeConn(); } } else { main.accumulate("Status", "Error");
		 * main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
		 * main.accumulate("Message", "Connection Error"); } return main.toString(); }
		 * 
		 */

		// http://localhost:8080/rest/api/PlayedMatchesByLeagueId&2

		//not done yet
		@GET
		@Path("PlayedMatchesByLeagueId&{leagueId}")
		@Produces(MediaType.TEXT_PLAIN)
		public String playedmatches(@PathParam("leagueId") int leagueid) {

			try {

				String dateofmatch, timeofmatch, location;
				int leagueId, team1id, team2id, scheduleid, matchid;

				conn = con.getCon();

				if (conn != null) {
					String sql = "\r\n"
							+ "SELECT schedules.team1_id,schedules.team2_id,schedules.league_id,schedules.date_of_match,schedules.time_of_match,schedules.location,schedules.schedule_id,matches.match_id\r\n"
							+ "FROM  (schedules\r\n" + "INNER JOIN matches\r\n"
							+ "ON schedules.schedule_id = matches.schedule_id)\r\n" + "where schedules.league_id = '"
							+ leagueid + "' \r\n" + "";
					stmt = conn.createStatement();
					rs = stmt.executeQuery(sql);

					if (rs.next() == true) {
						main.accumulate("Status", 200);
						main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);

						do {
							leagueId = rs.getInt("league_id");
							matchid = rs.getInt("match_id");
							scheduleid = rs.getInt("schedule_id");
							team1id = rs.getInt("team1_id");
							team2id = rs.getInt("team2_id");
							dateofmatch = rs.getString("date_of_match");
							timeofmatch = rs.getNString("time_of_match");
							location = rs.getString("location");

							main.accumulate("LeagueId", leagueId);
							main.accumulate("MatchId", matchid);
							main.accumulate("ScheduleId", scheduleid);
							main.accumulate("Team1Id", team1id);
							main.accumulate("Team2Id", team2id);
							main.accumulate("DateofMatch", dateofmatch);
							main.accumulate("TimeOfMatch", timeofmatch);
							main.accumulate("Location", location);

							ja.add(main);
							main.clear();

						} while (rs.next());
						main.accumulate("LeagueList", ja);
					} else {

						main.accumulate("Status", "Error");
						main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
						main.accumulate("Message", "No Records Found");

					}
				} else {
					main.accumulate("Status", "Error");
					main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
					main.accumulate("Message", "Connection Error");
				}

			}

			catch (SQLException ex) {
				ex.printStackTrace();
			} finally {
				closeConn();
			}
			return main.toString();
		}

		// http://localhost:8080/rest/api/matchScore&2&1
		//working
		@GET
	    @Path("matchScore&{matchid}&{teamid}")               
	    @Produces(MediaType.TEXT_PLAIN)
	    public String matchscore(@PathParam("matchid") int matchId, @PathParam("teamid") int teamId) 
		{

	    try {    
	         int scoreid,goal,shots,shotsOnTarget,possesssion,passes,passAccuracy,fouls,yellowCards,redCards,offsides,corners,teamid,matchid;
	        
	        
	        conn = con.getCon();

	        if (conn != null) {
	            
	            	
	                String sql ="SELECT score_id,match_id,team_id,goal,shots,shots_on_target,possession,passes,pass_accuracy,fouls,yellow_cards,red_cards,offsides,corners FROM scores\r\n" + 
	                		"where match_id = '"+matchId+"' AND team_id = '" + teamId+"'  ;\r\n" + "";
	                
	                stmt = conn.createStatement();
	                rs = stmt.executeQuery(sql);

	                if (rs.next() == true) {
	                	main.accumulate("Status", 200);
	                	main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
	                	
	                    do {
	                    	
	                    	
	                    	scoreid = rs.getInt("score_id");
	                        matchid = rs.getInt("match_id");
	                        teamid = rs.getInt("team_id");
	                        goal = rs.getInt("goal");
	                    	shots = rs.getInt("shots");
	                    	shotsOnTarget = rs.getInt("shots_on_target");
	                    	possesssion = rs.getInt("possession");
	                    	passes = rs.getInt("passes");
	                    	passAccuracy = rs.getInt("pass_accuracy");
	                    	fouls = rs.getInt("fouls");
	                    	yellowCards = rs.getInt("yellow_cards");
	                    	redCards = rs.getInt("red_cards");
	                    	offsides = rs.getInt("offsides");
	                    	corners = rs.getInt("corners");
	                    	
	                    
	                    	jb.accumulate("ScoreId",scoreid);
	                        jb.accumulate("MatchId",matchid);
	                    	jb.accumulate("TeamId",teamid);
	                    	jb.accumulate("goal",goal);
	                    	jb.accumulate("shots",shots);
	                    	jb.accumulate("shotsOnTarget",shotsOnTarget);
	                    	jb.accumulate("possession",possesssion);
	                    	jb.accumulate("passes",passes);
	                    	jb.accumulate("passAccuracy",passAccuracy);
	                    	jb.accumulate("fouls",fouls);
	                    	jb.accumulate("yellowCards",yellowCards);
	                    	jb.accumulate("redCards",redCards);
	                    	jb.accumulate("offsides",offsides);
	                    	jb.accumulate("corners", corners);

	                    	ja.add(jb);
	                        jb.clear();

	                    } while (rs.next());
	                    main.accumulate("MatchScores", ja);
	                } else {

	                	main.accumulate("Status", "Error");
	                	main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
	                	main.accumulate("Message", "No Records Found");

	                }
	            } 
	            else {
	            	main.accumulate("Status", "Error");
	            	main.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
	            	main.accumulate("Message", "Connection Error");
	            }

	        }
	            catch (SQLException ex) {
	            	ex.printStackTrace();
	            } finally {
	                closeConn();
	            }
	                return main.toString();
	    }
		//working
		@GET
		@Path("ViewregisterUserDetail&{user_id}")
		@Produces(MediaType.TEXT_PLAIN)
		public String ViewregisterUserDetail(@PathParam("user_id") String user_id) {

			try {

				conn = con.getCon();
				if(conn != null) {
				
					sql = "SELECT full_name,email,phone,gender,country,age,user_type,user_photo from users WHERE user_id = '"+user_id +"'";
					createStatement();
					rs = stmt.executeQuery(sql);

					main.put("Status", 200);
					main.put("TimeStamp", System.currentTimeMillis() / 1000);

					if (rs.next() == false) {
						main.put("Status", "Error");
						main.put("TimeStamp", System.currentTimeMillis() / 1000);
						main.put("mesage", "Details not found");
					} else {

						do {
							String full_name = rs.getString("full_name");
							String email = rs.getString("email");
							String phone = rs.getString("phone");
							String gender = rs.getString("gender");
							String country = rs.getString("country");
							int age = rs.getInt("age");
							String user_type = rs.getString("user_type");
							String user_photo = rs.getString("user_photo");
							jb.accumulate("fullName", full_name);
							jb.accumulate("email", email);
							jb.accumulate("phone", phone);
							jb.accumulate("gender", gender);
							jb.accumulate("country", country);
							jb.accumulate("age", age);
							jb.accumulate("userType", user_type);
							jb.accumulate("userPhoto", user_photo);
							
							
						} while (rs.next());
					}
					main.accumulate("UserDetails", jb);
			}else {
					
					main.accumulate("Status", "Error");
					main.put("TimeStamp", System.currentTimeMillis() / 1000);
					main.accumulate("message", "Connection error!!!!");
					}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				closeConn();
			}

			return main.toString();

		}
	    //working
		@GET
	    @Path("CancelMatch&{match_id}")                
	    @Produces(MediaType.TEXT_PLAIN)
	    public String CancelMatch(@PathParam("match_id") int match_id)
	    		{

	        try {
				conn = con.getCon();
				System.out.println("Connected to the database test1");

				String sql ="DELETE from matches WHERE match_id = '" + match_id + "'";
				createStatement();
	        
					int i = stmt.executeUpdate(sql);
					System.out.println(i);
        
	                if (i > 0) {
	                    main.accumulate("Status", 200);
	                    main.accumulate("TimeStamp", System.currentTimeMillis()/1000);
	                    main.accumulate("Message", "Match has Been Cancelled Due to Bad Weather!!!");
	                     

	                } else {
	                    main.accumulate("Status", "Error");
	                    main.accumulate("TimeStamp", System.currentTimeMillis()/1000);
	                    main.accumulate("Message", "Something went wrong!!!");
	                    
	                }                    
	                

	            }  catch (SQLException e) 
	    		{
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    			System.out.println("S Error  : " + e.getMessage());

	    		}finally {
		        	  closeConn();
						
				
	    			
	    		}

	    		return main.toString();
	    	
	        }
		
		//not working
		
		@GET
		@Path("removePlayerFromTeam&{player_id}")
		@Produces(MediaType.TEXT_PLAIN)
		public String removePlayerFromTeam(@PathParam("player_id") int player_id) {
			try {

				conn = con.getCon();

				System.out.println("Connected to the database test1");
				if(conn!= null) {
				
				sql ="Delete from players where player_id= '"+player_id+"'";
				createStatement();
				int i = stmt.executeUpdate(sql);
				System.out.println("delete query :"+sql);
				if(i > 0) {
				
					main.accumulate("Status", 200);
					main.put("TimeStamp", System.currentTimeMillis() / 1000);
					main.accumulate("result", "player removed from team");
					
				}
				else {
					
					main.accumulate("Status", "Error");
					main.put("TimeStamp", System.currentTimeMillis() / 1000);
					main.accumulate("result", "System Wont be able to remove player!!!");
					
				} 	
				
				}else {
					main.accumulate("Status", "Error");
					main.put("TimeStamp", System.currentTimeMillis() / 1000);
					main.accumulate("result", "Connection error!!!!!");
					
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				
				closeConn();
			}

			return main.toString();
		}
		//working
		@POST
		@Path("UpdateUserProfile")
		@Produces(MediaType.TEXT_PLAIN)
		@Consumes(MediaType.APPLICATION_JSON)
		public String UpdateUserProfile(User user) {
			try {

				conn = con.getCon();

				System.out.println("Connected to the database test1");

				sql = "UPDATE users SET full_name = '" + user.getFull_name() + "',email = '" + user.getEmail()
						+ "', phone = '" + user.getPhone() + "', gender='" + user.getGender() + "' , country = '" + user.getCountry() + "',age='" + user.getAge()
						+ "', user_type='" + user.getUser_type() + "',user_photo = '" + user.getUser_photo() + "' WHERE user_id='" + user.getUser_id()
						+ "'";

				createStatement();
				int i = stmt.executeUpdate(sql);
				System.out.println(i);
				if (i > 0) {

					main.accumulate("Status", 200);
					main.put("TimeStamp", System.currentTimeMillis() / 1000);
					main.accumulate("message", "Your Profile Has Been Updated");

				} else {
					main.accumulate("Status", "Warning");
					main.put("TimeStamp", System.currentTimeMillis() / 1000);
					main.accumulate("message", "There is some problem!!!");

				}

				conn.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return main.toString();
		}
		
		
		@GET
		@Path("ViewTeamDetail&{user_id}")
		@Produces(MediaType.TEXT_PLAIN)
		public String ViewTeamDetail(@PathParam("user_id") String user_id) {

			try {

				conn = con.getCon();
				if (conn != null) {

					sql = "SELECT team_id, name, logo, team_label from teams WHERE user_id = '" + user_id + "'";
					createStatement();
					rs = stmt.executeQuery(sql);

					main.put("Status", 200);
					main.put("TimeStamp", System.currentTimeMillis() / 1000);

					if (rs.next() == false) {
						main.put("Status", "Error");
						main.put("TimeStamp", System.currentTimeMillis() / 1000);
						main.put("mesage", "Details not found");
					} else {

						do {
							int team_id = rs.getInt("team_id");
							String name = rs.getString("name");
							String logo = rs.getString("logo");
							String team_label = rs.getString("team_label");

							jb.accumulate("team_id", team_id);
							jb.accumulate("name", name);
							jb.accumulate("logo", logo);
							jb.accumulate("team_label", team_label);

						} while (rs.next());
					}
					main.accumulate("TeamDetails", jb);
				} else {

					main.accumulate("Status", "Error");
					main.put("TimeStamp", System.currentTimeMillis() / 1000);
					main.accumulate("message", "Connection error!!!!");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				closeConn();
			}

			return main.toString();

		}
}

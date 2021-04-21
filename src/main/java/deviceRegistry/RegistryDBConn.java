package deviceRegistry;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class RegistryDBConn {
	private static Connection con;
	private static boolean hasData = false;

	
	public void getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:DeviceRegistry.db");
		initialise();
	}

	public void initialise() throws SQLException {
		// TODO Auto-generated method stub
		if (!hasData) {
			hasData = true;
			
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='registry'");
			if (!res.next()) {
				System.out.println("Building the User table");
				//need to build table
				Statement state2 = con.createStatement();
				Statement state3 = con.createStatement();
				Statement state4 = con.createStatement();
				Statement state5 = con.createStatement();
				state2.execute("CREATE TABLE devices(id integer, deviceName varchar(100), deviceAddress varchar(500), primary key(id));");
				state3.execute("CREATE TABLE states(deviceID integer, state text NOT NULL, value text NOT NULL, primary key(deviceID, state), foreign key(deviceID) references devices(id) on delete cascade)");
				state4.execute("CREATE TABLE groups(groupID integer primary key not null, name varchar(200))");
				state5.execute("CREATE TABLE deviceGroup(groupID integer not null, deviceID integer not null, foreign key(groupID) references groups(groupID) on delete cascade, foreign key(deviceID) references devices(id) on delete cascade, primary key(groupID, deviceID))");
			}
		}
	}
	
	public String createDevice(int id, String name, String address) {
		String output;
		try {
			PreparedStatement prep = con.prepareStatement("INSERT INTO devices VALUES(?, ?, ?);");
			prep.setInt(1, id);
			prep.setString(2, name);
			prep.setString(3, address);
			
			int rowsAffected = prep.executeUpdate();
			
			output = String.valueOf(rowsAffected) + " rows affected. DeviceID = " + id + " DeviceName = " + name;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			output = e.getSQLState();
		}
		return output;
	}
	
	public String[] getDevices() throws SQLException {
		ArrayList<String> output = new ArrayList<String>();
		
		
		Statement s = con.createStatement();
		ResultSet rs = s.executeQuery("SELECT id FROM device");
		
		while (rs.next()) {
			output.add(String.valueOf(rs.getInt("id")));
		}
		
		String[] array = output.toArray(new String[0]);
		return array;
	}
	
	public String deleteDevices(int[] ids) throws SQLException {
		
		int rows = 0;
		for (int i = 0; i < ids.length; i++) {
			PreparedStatement prep = con.prepareStatement("DELETE FROM devices WHERE id=? ;");
			prep.setInt(1, ids[i]);
			rows = rows + prep.executeUpdate();
		}

		
		String output = String.valueOf(rows) + " rows affected.";
		
		return output;
	}
	
	public String writeState(int id, String key, String value) throws SQLException {
		
		PreparedStatement prep = con.prepareStatement("INSERT INTO states VALUES (?, ?, ?);");
		prep.setInt(1, id);
		prep.setString(2, key);
		prep.setString(3, value);
		int rows = prep.executeUpdate();
		
		String output = String.valueOf(rows) + " rows affected.";
		
		return output;
	}
	
	public String updateState(int id, String key, String value) throws SQLException {
		PreparedStatement prep = con.prepareStatement("UPDATE states SET value = ? WHERE deviceID = ? and key = ?;");
		prep.setString(1, value);
		prep.setInt(2, id);
		prep.setString(3, key);
		int rows = prep.executeUpdate();
		
		String output = String.valueOf(rows) + " rows affected.";
		
		return output;
		
	}
	
	public String deleteState(int id, String key) throws SQLException {
		PreparedStatement prep = con.prepareStatement("DELETE FROM states WHERE deviceID = ? and key = ?;");
		prep.setInt(1, id);
		prep.setString(2, key);
		int rows = prep.executeUpdate();
		
		String output = String.valueOf(rows) + " rows affected.";
		
		return output;
	}
	
	public Object[] getState() {//maybe implement to handle as json object but for now just a map
		
		return null;
	}
	
	public String createGroup(int groupID, String name) throws SQLException {
		
		PreparedStatement prep = con.prepareStatement("INSERT INTO groups VALUES (?, ?);");
		prep.setInt(1, groupID);
		prep.setString(2, name);
		int rows = prep.executeUpdate();
		
		String output = String.valueOf(rows) + " rows affected.";
		
		return output;
		
	}
	
	public String setGroup(int groupID, int deviceID) throws SQLException {
		PreparedStatement prep = con.prepareStatement("INSERT INTO deviceGroup VALUES (?, ?);");
		prep.setInt(1, groupID);
		prep.setInt(2, deviceID);
		int rows = prep.executeUpdate();
		
		String output = String.valueOf(rows) + " rows affected.";
		
		return output;	
	}
	
	public String deleteGroup(int groupID) throws SQLException {
		
		PreparedStatement prep = con.prepareStatement("DELETE FROM groups WHERE groupID = ?;");
		prep.setInt(1, groupID);
		int rows = prep.executeUpdate();
		
		String output = String.valueOf(rows) + " rows affected.";
		
		return output;
	}
	
	public String deleteFromGroup(int groupID, int deviceID) throws SQLException {
		
		PreparedStatement prep = con.prepareStatement("DELETE FROM groups WHERE groupID = ? and deviceID = ?;");
		prep.setInt(1, groupID);
		prep.setInt(2, deviceID);
		int rows = prep.executeUpdate();
		
		String output = String.valueOf(rows) + " rows affected.";
		
		return output;
	}
}








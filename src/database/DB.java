package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DB {
	public static Connection con;
	public static Statement stmt;
	static {
		try {
			con = DriverManager.getConnection("jdbc:mysql://192.168.103.34/problem_management?serverTimezone=UTC&allowLoadLocalInfile=true&allowPublicKeyTrieval=true", "client_identification", "1324");
			stmt = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	static public ArrayList<ArrayList<Object>> getRows(String sql, Object... objects) {
		var rows = new ArrayList<ArrayList<Object>>();
		
		try {
			var pStmt = con.prepareStatement(sql);
			for(int i = 0; i < objects.length; i++) {
				pStmt.setObject(i + 1, objects[i]);
			}
			
			sql = pStmt.toString().replaceFirst("(.*):", "");
			
			System.out.println(sql);
			
			var query = pStmt.executeQuery();
			var length = query.getMetaData().getColumnCount();
			
			while(query.next()) {
				var row = new ArrayList<Object>();
				for(int i = 0; i < length; i++) {
					row.add(query.getObject(i + 1));
				}
				rows.add(row);
			}
		} catch (SQLException e) {
			System.out.println(sql);
			e.printStackTrace();
		}
		
		return rows;
	}
	
	static public void execute(String sql, Object...objects) {
		try {
			var pStmt = con.prepareStatement(sql);
			for(int i = 0; i < objects.length; i++) {
				pStmt.setObject(i + 1, objects[i]);
			}
			
			sql = pStmt.toString().replaceFirst("(.*):", "");
			
			pStmt.execute();
		} catch (SQLException e) {
			System.out.println(sql);
			e.printStackTrace();
		}
	}
	
}

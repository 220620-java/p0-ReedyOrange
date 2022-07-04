package com.revature.pepinData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.revature.pepinUtil.ConnectionUtil;

public class GetData {

	private ConnectionUtil connUtil = ConnectionUtil.getConnectionUtil();
	
	Scanner testScan = new Scanner(System.in);

	public void testMethod() {
		try (Connection conn = connUtil.getConnection()) {

			String sql = "select * from robins_quirks";

			Statement stmt = conn.createStatement();

			ResultSet resultSet = stmt.executeQuery(sql);

			while (resultSet.next()) {
				String x = "";
				
				x = x.concat(resultSet.getString(1)).concat(" ").concat(resultSet.getString(2)).concat(" ").concat(resultSet.getString(3));
				
				System.out.println(x);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		

		
	}
	
	public void testInsert() {
		
		try (Connection conn = connUtil.getConnection()) {
			
			String quirkInput;
			System.out.print("Enter new Quirk: ");
			quirkInput = testScan.next();
			
			System.out.println("");
			
			String intensityInput;
			System.out.print("Enter that quirks intensity(positive number): ");
			intensityInput = testScan.next();
			
			String sql = "";
			sql = sql.concat("insert into robins_quirks values (default, '").concat(quirkInput).concat("', ").concat(intensityInput).concat(")");
			
			Statement stmt = conn.createStatement();

			stmt.executeUpdate(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		testScan.close();
	
	}
	
	
}

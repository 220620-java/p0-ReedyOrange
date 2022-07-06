package com.revature.pepinData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.revature.pepinUtil.ConnectionUtil;

import com.revature.pepinMenu.*;

public class GetData {

	private ConnectionUtil connUtil = ConnectionUtil.getConnectionUtil();
	
	Scanner testScan = new Scanner(System.in);

	// ------------------------------------------------------------
	
	public void testMethod() {
		
		// Outputs Everything in User Table (FOR TESTING)
		
		try (Connection conn = connUtil.getConnection()) {

			String sql = "select * from users";

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
	
	// ------------------------------------------------------------
	
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
	
	// ------------------------------------------------------------
	
	public void insertUserIntoDB() {
		try (Connection conn = connUtil.getConnection()){
			
			System.out.println("CREATE A USERNAME AND PASSWORD BELOW");
			
			String inputUser;
			System.out.print("USERNAME: ");
			inputUser = testScan.next();
			
			//----CHECK FOR USERNAME ALREADY TAKEN (and for if its too long)----//
			
			String checkUser = "select exists(select * from users where user_name = '".concat(inputUser).concat("')");
			
			Statement CheckUserStmt = conn.createStatement();
			ResultSet userResult = CheckUserStmt.executeQuery(checkUser);
			
			boolean checker = true;
			
			while (userResult.next()) {
				
				String validCheck = userResult.getString(1);
				
				if(validCheck.equals("t")) {
					System.out.println("USERNAME ALREADY EXISTS");
					checker = false;
				}
				if(inputUser.length() > 20) {
					System.out.println("USERNAME TOO LONG (MAX 20 CHARACTERS)");
					checker = false;
				}
				else {
					if(checker == true) {
						System.out.println("USERNAME ALL GOOD");
					}
				}
				
			}
			
			//--------if invalid user (do this) if valid, skip this-------//
			
			while(checker == false) {
				System.out.print("ENTER NEW USERNAME: ");
				
				inputUser = testScan.next();
				checkUser = "select exists(select * from users where user_name = '".concat(inputUser).concat("')");
				
				CheckUserStmt = conn.createStatement();
				userResult = CheckUserStmt.executeQuery(checkUser);
				
				
				while (userResult.next()) {
					
					String validCheck = userResult.getString(1);
					
					if(validCheck.equals("t")) {
						System.out.println("USERNAME ALREADY EXISTS");
						checker = false;
					}
					if(inputUser.length() > 20) {
						System.out.println("USERNAME TOO LONG (MAX 20 CHARACTERS)");
						checker = false;
					}
					else {
						if(validCheck.equals("f") && inputUser.length() < 21) {
							System.out.println("USERNAME ALL GOOD");
							checker = true;
						}
					}
					
				}
				
			}
			
			
			//---------------PASSWORD CHECK-----------------------//
			
			String inputPass;
			System.out.print("\nPASSWORD: ");
			inputPass = testScan.next();
			
			while(inputPass.length() > 20) {
				System.out.println("PASSWORD TOO LONG (MAX 20 CHARACTERS)");
				System.out.print("ENTER NEW PASSWORD: ");
				
				inputPass = testScan.next();
			}
			
			String sql = "";
			sql = sql.concat("insert into users values (default, '").concat(inputUser).concat("', '").concat(inputPass).concat("')");
			
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("YOU SUCCESSFULLY REGISTERED");
		
		logIn();
		
		testScan.close();
		
	}
	
	// ------------------------------------------------------------
	
	public void logIn() {
		
		try (Connection conn = connUtil.getConnection()){
			
			System.out.println("LOG IN WITH YOUR USERNAME AND PASSWORD");
			
			System.out.print("USERNAME: ");
			String userLG = testScan.next();
			
			System.out.print("\nPASSWORD: ");
			String userPW = testScan.next();
			
			String checkLG = "select exists(select * from users where user_name = '".concat(userLG).concat("' AND user_pass = '").concat(userPW).concat("')");
			
			Statement checkLogIn = conn.createStatement();
			ResultSet logInResult = checkLogIn.executeQuery(checkLG);
			
			// ------------- CHECK FOR VALID LOG IN ------------------//
			
			boolean checker = true;
			
			while (logInResult.next()) {
				
				String validCheck = logInResult.getString(1);
				
				if(validCheck.equals("f")) {
					System.out.println("USERNAME/PASSWORD IS INCORRECT");
					checker = false;
				}
				else {
					System.out.println("USERNAME AND PASSWORD ALL GOOD");
				}
				
			}
			
			// --------- IF LOG IN VALID, SKIP THIS -------------------//
			
			while(checker == false) {
				System.out.println("TRY AGAIN");
				
				System.out.println("LOG IN WITH YOUR USERNAME AND PASSWORD");
				
				System.out.print("USERNAME: ");
				userLG = testScan.next();
				
				System.out.print("\nPASSWORD: ");
				userPW = testScan.next();
				
				checkLG = "select exists(select * from users where user_name = '".concat(userLG).concat("' AND user_pass = '").concat(userPW).concat("')");
				
				checkLogIn = conn.createStatement();
				logInResult = checkLogIn.executeQuery(checkLG);
				
				while (logInResult.next()) {
					
					String validCheck = logInResult.getString(1);
					
					if(validCheck.equals("f")) {
						System.out.println("USERNAME/PASSWORD IS INCORRECT");
						checker = false;
					}
					else {
						System.out.println("USERNAME AND PASSWORD ALL GOOD");
						checker = true;
					}
					
				}
				
			}
			
			System.out.println("YOU SUCCESSFULLY LOGGED IN!!!\n");
			
			//-----Get User ID to Pass into Main Menu-----//
			
			String getUID = "select user_id from users where user_name = '".concat(userLG).concat("'");
			
			checkLogIn = conn.createStatement();
			logInResult = checkLogIn.executeQuery(getUID);
			
			int gotTheUID = 0; // default to initialize
			
			while (logInResult.next()) {
				
				gotTheUID = logInResult.getInt(1);
				
			}
			
			//--------------------------------------------//
			
			MainMenu newMenu = new MainMenu();
			
			newMenu.displayMenu(gotTheUID);
			
			//----------------------------------------------------------//
			
		} catch(SQLException e) {
			
			e.printStackTrace();
			
		}
		
		testScan.close();
		
	}
	
}

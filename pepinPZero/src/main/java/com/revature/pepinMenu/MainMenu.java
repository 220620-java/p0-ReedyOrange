package com.revature.pepinMenu;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.revature.pepinUtil.ConnectionUtil;

public class MainMenu {
	
	private ConnectionUtil connUtil = ConnectionUtil.getConnectionUtil();

	public void displayMenu(int u_id) {

		Scanner menuScan = new Scanner(System.in);

		System.out.println("        MAIN MENU");
		System.out.println("--------------------------\nWHAT WOULD YOU LIKE TO DO?");
		System.out.println("--------------------------\nTYPE 1 TO CREATE BANK ACCOUNT");
		System.out.println("TYPE 2 TO DISPLAY FUNDS");
		System.out.println("TYPE 3 TO DEPOSIT FUNDS");
		System.out.println("TYPE 4 TO WITHDRAW FUNDS");
		System.out.println("TYPE 5 TO LOG OUT");
		System.out.print("-----------------\nTYPE HERE: ");

		String menuInput = menuScan.next();

		switch (menuInput) {

		case "1":
			// create account
			createBankAccount(u_id);
			break;
		case "2":
			// display funds
			displayFunds(u_id);
			break;
		case "3":
			// deposit funds
			depositFunds(u_id);
			break;
		case "4":
			// withdraw funds
			withdrawFunds(u_id);
			break;
		case "5":
			// log out
			System.out.println("YOU HAVE LOGGED OUT\nGOODBYE!");
			break;
		default:
			System.out.println("INVALID INPUT\n\nTRY AGAIN");
			displayMenu(u_id);

		}

		menuScan.close();

	}

	public void createBankAccount(int u_id) {

		try (Connection conn = connUtil.getConnection()) {

			Scanner createScan = new Scanner(System.in);

			System.out.println("\nCREATING NEW BANK ACCOUNT");

			String accountName;
			System.out.print("ACCOUNT NAME: ");
			accountName = createScan.next();

			// CHECK IF NAME ALREADY EXISTS FOR CURRENT USER ID
			
			String checkBank = "select exists(select * from account as a, has_account as ha where ha.user_id = " + u_id + " and ha.account_id = a.account_id and a.account_name = '" + accountName + "')";
			
			Statement CheckUserStmt = conn.createStatement();
			ResultSet userResult = CheckUserStmt.executeQuery(checkBank);
			
			boolean checker = true;
			
			while (userResult.next()) {
				
				String validCheck = userResult.getString(1);
				
				if(validCheck.equals("t")) {
					System.out.println("ACCOUNT ALREADY EXISTS");
					checker = false;
				}
				if(accountName.length() > 20) {
					System.out.println("ACCOUNT NAME TOO LONG (MAX 20 CHARACTERS)");
					checker = false;
				}
				else {
					
					if(checker == false) {
						// do nothing
					}
					else {
						System.out.println("ACCOUNT NAME ALL GOOD");
					}
					
				}
				
			}
			
			//--------if invalid account (do this) if valid, skip this-------//
			
			while(checker == false) {
				System.out.print("ENTER NEW ACCOUNT NAME: ");
				
				accountName = createScan.next();
				
				checkBank = "select exists(select * from account as a, has_account as ha where ha.user_id = " + u_id + " and ha.account_id = a.account_id and a.account_name = '" + accountName + "')"; 
				
				CheckUserStmt = conn.createStatement();
				userResult = CheckUserStmt.executeQuery(checkBank);
				
				while (userResult.next()) {
					
					String validCheck = userResult.getString(1);
					
					if(validCheck.equals("t")) {
						System.out.println("ACCOUNT ALREADY EXISTS");
						checker = false;
					}
					if(accountName.length() > 20) {
						System.out.println("ACCOUNT NAME TOO LONG (MAX 20 CHARACTERS)");
						checker = false;
					}
					else {
						if(validCheck.equals("f") && accountName.length() < 21) {
							checker = true;
							System.out.println("ACCOUNT NAME ALL GOOD");
						}
					}
					
				}
					
			}

			// ----------------------------------------------------------------------

			String insertBank = "insert into account values (default, '" + accountName + "', 0)";

			Statement stmt = conn.createStatement();
			stmt.executeUpdate(insertBank);

			// NOW ALSO INSERT IT INTO HAS_ACCOUNT TABLE
			
			// have to get account id first
			
			String getAccountID = "select max(account_id) from account";
			
			stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery(getAccountID);
			
			int temp = 0;
			while (resultSet.next()) {
				
				temp = resultSet.getInt(1);
				
			}
			
			// -------------------------
			
			String insertConnectTable = "insert into has_account values (" + u_id + ", " + temp + ")";
			
			stmt = conn.createStatement();
			stmt.executeUpdate(insertConnectTable);
			
			System.out.println("YOU HAVE SUCCESSFULLY CREATED NEW BANKACCOUNT");
			
			displayMenu(u_id);
			
			createScan.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	
	public void displayFunds(int u_id) {
		
		Scanner displayScanner = new Scanner(System.in);
		
		try (Connection conn = connUtil.getConnection()) {
			String accountName;
			
			System.out.print("TYPE ACCOUNT NAME: ");
			accountName = displayScanner.next();
			
			String displaySQL = "select a.account_name, a.account_Balance from account as a, has_account as ha where ha.user_id = " + u_id + " and ha.account_id = a.account_id and a.account_Name = '" + accountName + "'";

			Statement stmt = conn.createStatement();

			ResultSet resultSet = stmt.executeQuery(displaySQL);
		
			boolean foundAccount = false;
			
			while(resultSet.next()) {
				
				System.out.println("\nACCOUNT NAME: " + resultSet.getString(1) + "\nACCOUNT BALANCE: $" + resultSet.getString(2) + "\n");
				
				foundAccount = true;
				
			}
			
			if(foundAccount == false) {
				
				System.out.println("\nACCOUNT DOES NOT EXIST\n");
				
			}
			
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		// -------------------------
		
		displayMenu(u_id);
		
		displayScanner.close();
		
	}
	
	
	public void depositFunds(int u_id) {
		
		Scanner depositScanner = new Scanner(System.in);
		
		try (Connection conn = connUtil.getConnection()) {
			
			String depoName;
			System.out.print("\nDEPOSIT FUNDS\nSELECT ACCOUNT NAME: ");
			depoName = depositScanner.next();
			
			// Need to Check if this account exists
			
			String checkDepo = "select exists(select * from account, has_account where has_account.user_id = " + u_id + "and has_account.account_id = account.account_id and account.account_name = '" + depoName + "')";
			
			Statement checkStmt = conn.createStatement();
			ResultSet checkResult = checkStmt.executeQuery(checkDepo);
			
			boolean checker = true;
			
			while (checkResult.next()) {
				
				String validCheck = checkResult.getString(1);
				
				if(validCheck.equals("f")) {
					System.out.println("ACCOUNT DOES NOT EXIST");
					checker = false;
				}
				if(depoName.length() > 20) {
					System.out.println("ACCOUNT TOO LONG(MAX 20 CHARACTERS)");
					checker = false;
				}
				else {
					
					if(checker == true) {
						// do nothing, move on
					}
					
				}
				
			}
			
			// ---------------if found account, SKIP THIS -------------
			
			while(checker == false) {
				
				System.out.print("\nTRY AGAIN: ");
				depoName = depositScanner.next();
				
				checkDepo = "select exists(select * from account, has_account where has_account.user_id = " + u_id + "and has_account.account_id = account.account_id and account.account_name = '" + depoName + "')";
				checkStmt = conn.createStatement();
				checkResult = checkStmt.executeQuery(checkDepo);
				
				while (checkResult.next()) {
					
					String validCheck = checkResult.getString(1);
					
					if(validCheck.equals("f")) {
						System.out.println("ACCOUNT DOES NOT EXIST");
						checker = false;
					}
					if(depoName.length() > 20) {
						System.out.println("ACCOUNT TOO LONG(MAX 20 CHARACTERS)");
						checker = false;
					}
					else {
						
						if(validCheck.equals("t") && depoName.length() < 21) {
							checker = true;
							System.out.println("FOUND ACCOUNT\n");
						}
						
					}
					
				}
				
			}
			
			
			// -----------------------------------------------------------
			
			double depoAmount;
			System.out.print("\nDEPOSIT AMOUNT: ");
			depoAmount = depositScanner.nextDouble();
			
			while(depoAmount <= 0) {
				System.out.println("INVALID: ENTER A POSITIVE NUMBER \nEXAMPLE: 50.75");
				System.out.print("\nTRY AGAIN: ");
				depoAmount = depositScanner.nextDouble();
			}
			
			
			String depoSQL = "update account set account_balance = account_balance + " + depoAmount + " from has_account where has_account.user_id = " + u_id + "and has_account.account_id = account.account_id and account.account_name = '" + depoName + "'";
			
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(depoSQL);
			
			System.out.println("SUCCESS");
			
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		displayMenu(u_id);
		
		depositScanner.close();
		
	}
	
	
	public void withdrawFunds(int u_id) {
		
		Scanner withdrawScanner = new Scanner(System.in);
		
		try (Connection conn = connUtil.getConnection()) {
			
			System.out.println("\nWITHDRAW FUNDS");
			System.out.print("ENTER ACCOUNT: ");
			String withdrawAccount = withdrawScanner.next();
			
			// Check if account exists
			
			String checkWD = "select exists(select * from account, has_account where has_account.user_id = " + u_id + "and has_account.account_id = account.account_id and account.account_name = '" + withdrawAccount + "')";
			
			Statement checkStmt = conn.createStatement();
			ResultSet checkResult = checkStmt.executeQuery(checkWD);
			
			boolean checker = true;
			
			while (checkResult.next()) {
				
				String validCheck = checkResult.getString(1);
				
				if(validCheck.equals("f")) {
					System.out.println("ACCOUNT DOES NOT EXIST");
					checker = false;
				}
				if(withdrawAccount.length() > 20) {
					System.out.println("ACCOUNT TOO LONG(MAX 20 CHARACTERS)");
					checker = false;
				}
				else {
					
					if(checker == true) {
						// do nothing, move on
					}
					
				}
				
			}
			
			// ---------------if found account, SKIP THIS ----------
			
			while(checker == false) {
				
				System.out.print("\nTRY AGAIN: ");
				withdrawAccount = withdrawScanner.next();
				
				checkWD = "select exists(select * from account, has_account where has_account.user_id = " + u_id + "and has_account.account_id = account.account_id and account.account_name = '" + withdrawAccount + "')";
				checkStmt = conn.createStatement();
				checkResult = checkStmt.executeQuery(checkWD);
				
				while (checkResult.next()) {
					
					String validCheck = checkResult.getString(1);
					
					if(validCheck.equals("f")) {
						System.out.println("ACCOUNT DOES NOT EXIST");
						checker = false;
					}
					if(withdrawAccount.length() > 20) {
						System.out.println("ACCOUNT TOO LONG(MAX 20 CHARACTERS)");
						checker = false;
					}
					else {
						
						if(validCheck.equals("t") && withdrawAccount.length() < 21) {
							checker = true;
							System.out.println("FOUND ACCOUNT\n");
						}
						
					}
					
				}
				
			}
			
			
			// -------------------------Account is now Found-----------------------
			
			// get current balance in account
			
			String getBal = "select a.account_Balance from account as a, has_account as ha where ha.user_id = " + u_id + " and ha.account_id = a.account_id and a.account_name = '" + withdrawAccount + "'";
			
			Statement wdStmt = conn.createStatement();
			ResultSet wdResult = wdStmt.executeQuery(getBal);
			
			double temp = 0;
			while(wdResult.next()) {
				
				temp = wdResult.getDouble(1);
				
			}
			
			if(temp == 0) {
				System.out.println("\nYOU HAVE ZERO DOLLARS IN THIS ACCOUNT\nCANNOT WITHDRAW\n");
				displayMenu(u_id);
				return;
			}
			
			System.out.print("\nYOU HAVE: $" + temp + "\nWITHDRAW AMOUNT: ");
			double wdAmnt = withdrawScanner.nextDouble();
			
			boolean newCheck = true;
			
			if(wdAmnt > temp || wdAmnt <= 0) {
				newCheck = false;
			}
			
			while(newCheck == false) {
				System.out.print("\nCAN'T WITHDRAW MORE THAN YOU HAVE\nTRY AGAIN: ");
				wdAmnt = withdrawScanner.nextDouble();
				if(wdAmnt > temp || wdAmnt <= 0) {
					if(wdAmnt <= 0) {
						System.out.println("CAN'T WITHDRAW NEGATIVE\nENTER NUMBER ABOVE 0\n");
					}
					newCheck = false;
				}
				else {
					newCheck = true;
				}
			}
			
			//--------------everything is verified now ------------//
			
			String wdSQL = "update account set account_balance = account_balance - " + wdAmnt + " from has_account where has_account.user_id = " + u_id + "and has_account.account_id = account.account_id and account.account_name = '" + withdrawAccount + "'";
			Statement lastStatement = conn.createStatement();
			lastStatement.executeUpdate(wdSQL);
			
			System.out.println("SUCCESS");
			
			
		} catch(SQLException e) {
			
			e.printStackTrace();
			
		}
		
		
		
		displayMenu(u_id);
		
		withdrawScanner.close();
		
	}
	
}

package com.revature.pepinDriver;

import java.util.Scanner;

import com.revature.pepinData.*;

public class TheDriver {

	public static void main(String[] args) {

		Scanner mainScan = new Scanner(System.in);

		boolean isValid = false;
		
		while (isValid == false) {

			System.out.println("//---WELCOME-TO-REVBANK-PRO---//");
			System.out.println("//-----ENTER-1-TO-LOG-IN------//");
			System.out.println("//-----ENTER-2-TO-REGISTER----//");
			System.out.print("\nENTER HERE: ");

			String temp = mainScan.next();

			switch (temp) {

			case "1":

				GetData newLogInUser = new GetData();

				newLogInUser.logIn();
				
				isValid = true;

				break;

			case "2":

				GetData newRegisterUser = new GetData();

				newRegisterUser.insertUserIntoDB();
				
				isValid = true;

				break;

			default:
				System.out.println("INVALID INPUT\n\nTRY AGAIN\n");
			}

		}

		mainScan.close();

	}

}

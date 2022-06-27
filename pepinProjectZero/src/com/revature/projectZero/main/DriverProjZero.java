package com.revature.projectZero.main;

import java.util.*;

public class DriverProjZero {
	public static void main (String[] args) {
		Scanner mainScan = new Scanner(System.in);
		
		System.out.println("//---WELCOME-TO-REVBANK-PRO---//");
		System.out.println("//-----ENTER-1-TO-LOG-IN------//");
		System.out.println("//-----ENTER-2-TO-REGISTER----//");
		System.out.print("\nENTER HERE: ");
		
		int temp = mainScan.nextInt();
		//mainScan.nextLine(); //to skip \n so nextLine() will work next time
		
		switch(temp) {
		
		case 1:
			System.out.println("LOG IN TBD");
			//create log in
			break;
		
		case 2:
			System.out.println("CREATE A USERNAME AND PASSWORD BELOW");
			
			String inputUser;
			
			System.out.print("USERNAME: ");
			inputUser = mainScan.next();
			//inputUser = mainScan.nextLine(); //if i want the whole line
			
			System.out.println("YOUR USER NAME IS: " + inputUser);
			//check if username is unique, at least 6 characters
			break;
			
		default:
			System.out.println("INVALID INPUT");
		}
		
		mainScan.close();
	}

}

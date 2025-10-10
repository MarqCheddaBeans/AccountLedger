package com.pluralsight;

import java.io.*;
import java.util.Scanner;

public class LedgarApp {

    //Create scanner that is class wide
    public static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedReader buffRead = new BufferedReader( new FileReader("transactions.csv"));

        displayFullLedger();

        userLogin();

        int choice = displayMainMenu();

        //Split transaction info into array named transInfo
      String line;
      while((line = buffRead.readLine()) != null){
          buffRead.readLine();
          String[] transInfo = line.split("\\|");
          buffRead.close();
      }


        if(choice == 1){
            System.out.println("Add Deposit: ");
            System.out.println("Enter Description (ex. Invoice 1001 paid");


        }

    }

    private static int displayMainMenu() {
        System.out.println("What would you like to do today?");
        System.out.println("""
                1) Add deposit
                2) Make a payment(Debit)
                3) Ledger
                4) Exit
                """);
        int choice = scan.nextInt();
        while (choice<=0 || choice>=6){
            System.out.println("Invalid input. Try again");
            System.out.println("""
                1) Add deposit
                2) Make a payment(Debit)
                3) Ledger
                4) Exit
                """);
            choice = scan.nextInt();
        }
        return choice;
    }

    private static void displayFullLedger() {
        try {
            BufferedReader buffRead = new BufferedReader( new FileReader("transactions.csv"));
            buffRead.readLine();
            String line;
            while((line = buffRead.readLine())!= null)
            System.out.println(line);
            buffRead.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found.");
            throw new RuntimeException(e);
        } catch (IOException e){
            throw new RuntimeException(e);
        }

    }

    // Method that prompts user to log in
    private static void userLogin() {
        String validUser = "Marques123";
        String validPass = "notPassword";

        System.out.println("Please log in to your account.");
        System.out.print("Username: ");
        String userName = scan.nextLine();

        while(!userName.equalsIgnoreCase(validUser) ){
            System.out.println("Invalid username. Try again!");
            System.out.print("Username: ");
            userName = scan.nextLine();
        }
        System.out.print("Password: ");
        String password = scan.nextLine();

        while(!password.equals(validPass)){
            System.out.println("Invalid password. Try again!");
            System.out.print("Password: ");
            password = scan.nextLine();
        }
        System.out.println("Welcome Marques!");
    }
}

package com.pluralsight;

import java.io.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class LedgarApp {

    //Create scanner that is class wide
    public static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        //Create BufferReader w/FileReader to read our csv file
        BufferedReader buffRead = new BufferedReader( new FileReader("transactions.csv"));

        //Create BufferWriter w/ FileWriter , file name + bool value to allow append information
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter("transactions.csv",true));

        //Method to prompt user to login
        userLogin();

        //Method to display main menu
        int choice = displayMainMenu();

        //Split transaction info into array named transInfo
      String line;
      while((line = buffRead.readLine()) != null) {
          //buffRead.readLine();
          String[] transInfo = line.split("\\|");

        //Creating variables + storing individual information from split transaction.csv
          try {
              LocalDate date = LocalDate.parse(transInfo[0]);
              LocalTime time = LocalTime.parse(transInfo[1]);
              String description = transInfo[2];
              String vendor = transInfo[3];
              double amount = Double.parseDouble(transInfo[4]);

              Transaction trans = new Transaction(date,time,description,vendor,amount);

              System.out.println(trans.getTime());
          }catch(DateTimeException e){
              System.out.print("");
          }
      }
        //Hungry buffer
        scan.nextLine();

        if(choice == 1){
            userDeposit(buffWrite);
        } else if (choice == 2) {
            userPayment(buffWrite);
        } else if(choice == 3){
            displayFullLedger();
        }

    }

    private static void userPayment(BufferedWriter buffWrite) throws IOException {
        System.out.println("Make a payment:");
        System.out.println("Enter Description (ex. garnishment)");

        String description = scan.nextLine();

        while(description.equalsIgnoreCase("")){
            System.out.println("Bad description...Try Again");
            description = scan.nextLine();
        }

        System.out.println("Enter Vendor (ex. Uncle Sam)");
        String vendor = scan.nextLine();

        while(vendor.equalsIgnoreCase("")){
            System.out.println("Um...No, Try Again");
            vendor = scan.nextLine();
        }

        System.out.println("How much did you spend this time??");
        double amount = scan.nextDouble();

        while(amount >=0){
            System.out.println("Enter a valid number");
            amount = scan.nextDouble();
        }

        LocalDate date = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = date.format(dateFormat);

        LocalTime time = LocalTime.now();
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = time.format(timeFormat);

        buffWrite.write("\n" + formattedDate + "  |");
        buffWrite.write(formattedTime + "   |");
        buffWrite.write(description + "            |");
        buffWrite.write(vendor + "    |");
        buffWrite.write(amount +"");  //Trouble w/o adding ""
        //Close buffWriter or else information will not write to file
        buffWrite.close();
    }

    private static void userDeposit(BufferedWriter buffWrite) throws IOException {
        System.out.println("Add Deposit: ");
        System.out.println("Enter Description (ex. Invoice 1001 paid)");

        String description = scan.nextLine();

        while(description.equalsIgnoreCase("")){
            System.out.println("You did not enter anything...Try Again");
            description = scan.nextLine();
        }
        System.out.println("Enter Vendor (ex. Amazon)");
        String vendor = scan.nextLine();

        while(vendor.equalsIgnoreCase("")){
            System.out.println("You did not enter anything...Try Again");
            vendor = scan.nextLine();
        }

        System.out.println("Enter Deposit Amount(You don't need an example for this)");
        double amount = 0.00;
        amount = scan.nextDouble();

        while(amount < 0){
            System.out.println("Invalid number. Try again");
            amount = scan.nextDouble();
        }

        LocalDate date = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = date.format(dateFormat);

        LocalTime time = LocalTime.now();
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = time.format(timeFormat);

        buffWrite.write("\n" + formattedDate + "  |");
        buffWrite.write(formattedTime + "   |");
        buffWrite.write(description + "            |");
        buffWrite.write(vendor + "    |");
        buffWrite.write(amount +"");  //Trouble w/o adding ""
        //Close buffWriter or else information will not write to file
        buffWrite.close();
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
            //Skip first line in .csv file
           // buffRead.readLine();
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

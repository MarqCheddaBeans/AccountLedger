package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;

import static com.pluralsight.LedgarApp.scan;
import static com.pluralsight.Transaction.*;

public class Features {

    public static void customSearch() {

        //Create List of Transaction named allTransactions
        List<Transaction> allTransactions = readTransactions("transactions.csv");

        //Create variable to keep customSearch running
        boolean found = false;
        //Loop to keep customSearch running until broken by user
        while(!found){
                    //Create list containing Transaction object named customSearch
                    List<Transaction> customSearch = new ArrayList<>();

                    //Prompt user for filters + store input
                    System.out.print("Enter start date (yyyy-MM-dd) or leave empty: ");
                    String userStartDate = scan.nextLine().trim();

                    System.out.print("Enter end date (yyyy-MM-dd) or leave empty: ");
                    String userEndDate = scan.nextLine().trim();

                    System.out.print("Enter description or leave empty: ");
                    String userDescription = scan.nextLine().trim();

                    System.out.print("Enter vendor or leave empty: ");
                    String userVendor = scan.nextLine().trim();

                    System.out.print("Enter amount or leave empty: ");
                    String inputAmount = scan.nextLine().trim();

                    //Create variables set to null as placeholders
                    LocalDate startDate = null;
                    LocalDate endDate = null;
                    Double userAmount = null;

                    //Create date format
                    DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");


                    try {
                        //check if user inputted start date is empty
                        if (!userStartDate.isEmpty()){
                            //if not empty, parse input string into LocalDate using created date format
                            startDate = LocalDate.parse(userStartDate, formatDate);
                        }
                        //check if user inputted end date is empty
                        if(!userEndDate.isEmpty()){
                            //if not empty, parse input string into LocalDate using created date format
                            endDate = LocalDate.parse(userEndDate, formatDate);
                        }
                        //check if user inputted amount is empty
                        if(!inputAmount.isEmpty()) {
                            //if not empty, parse into double and store in userAmount
                            userAmount = Double.parseDouble(inputAmount);
                        }
                    }
                    //handle any exception and print error message
                    catch (Exception e){
                        System.out.println("Invalid date or amount format. Try again");
                        //restart loop
                        continue;
                    }

                    //Cycle trough allTransactions, storing in transaction object with t variable
                    for(Transaction t : allTransactions){
                        LocalDate date = t.getDate();
                        String description = t.getDescription();
                        String vendor = t.getVendor();
                        double amount = t.getAmount();

                        //filter start date
                        if(startDate != null && date.isBefore(startDate)){
                            continue;
                        }

                        //filter end date
                        if(endDate != null && date.isAfter(endDate)){
                            continue;
                        }

                        //filter description
                        if(!userDescription.isEmpty() && !description.toLowerCase().contains(userDescription.toLowerCase())){
                            continue;
                        }

                        //filter vendor
                        if(!userVendor.isEmpty() && !vendor.toLowerCase().contains(userVendor.toLowerCase())){
                            continue;
                        }
                        //filter amount
                        if(userAmount != null && Double.compare(userAmount, amount) != 0){
                            continue;
                        }

                        //add if passed all filters
                        customSearch.add(t);
                    }

                    //Check if any transactions were found
                    if(customSearch.isEmpty()){
                        //if not transactions found, prompt user to enter info again
                        System.out.println("\nNo transactions were found based on your inputs. Try again.");
                    }
                    //if found, exit loop and print transactions
                    else{
                        found = true;
                        printTransactions(customSearch);
                    }
        }
    }

    //method to sort by vendor
    public static void sortByVendor() {

        //create transaction list object named allTransactions and store read transactions from file in allTransactions
        List<Transaction> allTransactions = readTransactions("transactions.csv");

        //Hungry buffer
        scan.nextLine();

        //Create bool variable with false value
        boolean found = false;

        //Place code in while loop to allow user to retry entering vendor if no transactions found with searched vendor
        while (!found) {

            //Prompt user for desired vendor + store in string inputVendor
            System.out.print("\nPlease enter a vendor: ");
            String inputVendor = scan.nextLine();

            //Create arraylist with Transaction object named sortByVendor, holds transactions
            ArrayList<Transaction> sortByVendor = new ArrayList<>();

            //loop through allTransactions assigned by Transaction object t
            for (Transaction t : allTransactions){
                //check if transaction vendor contains data from user inputted vendor
                if(t.getVendor().toLowerCase().contains(inputVendor.toLowerCase())){
                    //if true, add transaction to sortByVendor list
                    sortByVendor.add(t);
                }
            }

            //check if any transactions were found
            if(sortByVendor.isEmpty()){
                //if no vendor found display message prompting user to try again
                System.out.println("\nNo transactions found from: |" + inputVendor + " | Try Again");
            }
            else{
                //if vendor found, exit loop and display transactions
                found = true;
                printTransactions(sortByVendor);
            }
        }
    }

    //method to sort by previous year
    public static void sortPreviousYear(){

        //create Transaction list object named allTransactions that reads transactions file
        List<Transaction> allTransactions = readTransactions("transactions.csv");
        //Create arraylist containing Transaction object, named prevYearList
        ArrayList<Transaction> prevYearList = new ArrayList<>();

        //Create LocalDate object named today, assigns variable to current date according to system calendar (yyyy-MM-dd)
        LocalDate today = LocalDate.now();
        //Create LocalDate object named prevYearDate, assigns variable to 1 year from current date(today) according to system(ex. 2024-10-12)
        LocalDate prevYearDate = today.minusYears(1);
        //Create variable named prevYear, takes year value of prevYearDate and stores in an integer (prevYearDate = 2024-10-12, int prevYear = 2024)
        int prevYear = prevYearDate.getYear();

        //loop through transactions in file
        for(Transaction t: allTransactions){
            //check if transaction year is equal to variable previous year
            if(t.getDate().getYear() == prevYear){
                //if true, add transaction to prevYearList list
                prevYearList.add(t);
            }
        }
        //display transactions
        printTransactions(prevYearList);
    }

    //method to sort by previous month
    public static void sortPreviousMonth(){

        //create list Transaction object named allTransactions, read transactions from transactions file and store in allTransactions
        List<Transaction> allTransactions = readTransactions("transactions.csv");
        //Create ArrayList containing Transaction object, named prevMonthList
        ArrayList<Transaction> prevMonthList = new ArrayList<>();

        //Define LocalDate variable named today, store current date based on system (ex. 2025-10-13)
        LocalDate today = LocalDate.now();
        //Define LocalDate variable named prevMonthDate, store 1 month prior to current date based on system (ex. 2025-09-13)
        LocalDate prevMonthDate = today.minusMonths(1);
        //Create int variable named prevMonth, store value of month from prevMonthDate (ex. 2025-09-13 == 09)
        int prevMonth = prevMonthDate.getMonthValue();
        //Create int variable named year, store value of year from prevMonthDate (ex. 2025-09-13 == 2025)
        int year = prevMonthDate.getYear();

        //Avoid app failure, changes month 0 -> month 12
        if(prevMonth == 0){
            prevMonth = 12;
        }

        //cycle through allTransactions
        for(Transaction t:allTransactions){
            //check if transaction month and year equal prevMonth and prevYear
            if(t.getDate().getMonthValue() == prevMonth && t.getDate().getYear() == year){
                //if true, add transaction to prevMonthList
                prevMonthList.add(t);
            }
        }
        //display transactions
        printTransactions(prevMonthList);
    }

    //method to sort year to date
    public static void sortYearToDate(){

        //create Transaction list object named allTransactions, read transactions file and store in allTransactions
        List<Transaction> allTransactions = readTransactions("transactions.csv");
        //Create Arraylist containing Transaction object named yearToDate
        ArrayList<Transaction> yearToDate = new ArrayList<>();

        //Define variables for todays date according to system and 1 year from todays date
        LocalDate today = LocalDate.now();
        int year = today.getYear();

        //cycle through allTransactions
        for(Transaction t: allTransactions){
            //check if transaction year equals defined year, and year is not after todays date based on system
            if(t.getDate().getYear() == year && !t.getDate().isAfter(today)){
                //if true, add transaction to yearToDate
                yearToDate.add(t);
            }
        }
        //display transactions
        printTransactions(yearToDate);
    }

    //method to sort by month to date
    public static void sortMonthToDate() {

        //create Transaction list object named all Transactions, read transactions file and store in allTransactions
        List<Transaction> allTransactions = readTransactions("transactions.csv");
        //Create an arraylist with Transaction object named monthToDate
        ArrayList<Transaction> monthToDate = new ArrayList<>();

        //Define and assign variables for current date (Month + Year)
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();

        //cycle through allTransactions
        for(Transaction t: allTransactions){
            //check if transactions month is equal to defined month and transactions date is not after todays date, and transactions year is equal to defined year
            if(t.getDate().getMonthValue() == month && !t.getDate().isAfter(today) && t.getDate().getYear()==year){
                //if true, add to monthToDate
                monthToDate.add(t);
            }
        }
        //display transactions
        printTransactions(monthToDate);
    }

    //method to sort by payments
    public static void sortByPayments(){

        //create Transaction list object named allTransactions, read transactions file and store in allTransaction
        List<Transaction> allTransactions = readTransactions("transactions.csv");
        //Create a list named payments with Transaction object class
        List<Transaction> payments = new ArrayList<>();

        //loop through all transactions
        for(Transaction t : allTransactions){
            //check if transactions amount is less than 0
            if(t.getAmount() < 0){
                //if true, add to payments
                payments.add(t);
            }
        }
        //display confirmation message
        System.out.println("----------------------------");
        System.out.println("Displaying Payments");
        System.out.println("----------------------------");

        //display payments
        printTransactions(payments);
    }

    //method to sort by deposits
    public static void sortByDeposits(){

        //create Transaction object list named all transactions, read transactions file and store in allTransactions
       List<Transaction> allTransactions = readTransactions("transactions.csv");
       //create Transaction object list named deposits
       List<Transaction> deposits = new ArrayList<>();

        //cycle through all transactions
       for (Transaction t : allTransactions){
           //check if transaction amount is greater than 0
           if(t.getAmount() > 0){
               //if true, add to deposits
               deposits.add(t);
           }
       }
       //display confirmation message
        System.out.println("----------------------------");
        System.out.println("Displaying Deposits");
        System.out.println("----------------------------");

        //display deposits
       printTransactions(deposits);
    }

    //method to allow user to add payment
    public static void userPayment() {

        //prompt user for payment information + store in variables
        System.out.println("-------------------");
        System.out.println("Make a payment:");
        System.out.println("-------------------");
        System.out.println("Enter Description (ex. garnishment)");

        String description = scan.nextLine();

        //No user error
        while (description.equalsIgnoreCase("")) {
            System.out.println("Bad description...Try Again");
            description = scan.nextLine();
        }

        System.out.println("Enter Vendor (ex. Uncle Sam)");
        String vendor = scan.nextLine();

        //No user error
        while (vendor.equalsIgnoreCase("")) {
            System.out.println("Um...No, Try Again");
            vendor = scan.nextLine();
        }

        //preset amount to -1 to demand valid input from user
        double amount = -1;

        //prompt user for amount
        System.out.println("How much did you spend this time??");
        //loop to harass user for amount until valid input given
        while (true) {
            //try\catch to handle InputMismatchException
            try {

                amount = scan.nextDouble();

                //check if user input is valid
                if (amount < 0) {
                    System.out.print("Invalid number. Try again. ");
                } else {
                    // Valid input, exit the loop
                    break;
                }
            }   //handle InputMismatchException and display error text
                catch (InputMismatchException e) {
                //prompt user to try again
                System.out.print("Invalid number. Try again. ");
                // Clear the invalid input from the scanner
                scan.nextLine();
            }
        }

        //Turn user amount negative to represent payment
        amount = -amount;

        //Create Transaction object named payment
        Transaction payment = new Transaction(LocalDate.now(), LocalTime.now(), description, vendor, amount);

        //Call method to read file
        List<Transaction> allTransactions = readTransactions("transactions.csv");

        //add payment to allTransactions list
        allTransactions.add(payment);

        //Rewrite file to add user payment
        saveTransactions(allTransactions);

        //Display confirmation to user
        System.out.println("----------------------------");
        System.out.println("Payment Added Successfully!");
        System.out.println("----------------------------");
    }

    //Method to allow user to add deposit
    public static void userDeposit(){

        //Prompt user for deposit information + store in variables
        System.out.println("--------------");
        System.out.println("Add Deposit: ");
        System.out.println("--------------");
        System.out.print("Enter Description (ex. Invoice 1001 paid): ");

        String description = scan.nextLine();

        //No user error
        while(description.equalsIgnoreCase("")){
            System.out.print("You did not enter anything...Try Again: ");
            description = scan.nextLine();
        }
        System.out.print("Enter Vendor (ex. Amazon): ");
        String vendor = scan.nextLine();

        //No user error
        while(vendor.equalsIgnoreCase("")){
            System.out.print("You did not enter anything...Try Again: ");
            vendor = scan.nextLine();
        }

        //preset amount to -1 to demand valid input from user
        double amount = -1;

        //prompt user for amount
        System.out.print("Enter Deposit Amount (You don't need an example for this): ");

        //loop to harass user for valid input
        while (true) {
            //try\catch to handle InputMismatchException
            try {
                //receive user input
                amount = scan.nextDouble();

                //check if amount is less than 0
                if (amount < 0) {
                    //if true, prompt user to try again
                    System.out.print("Invalid number. Try again. ");
                }
                else {
                    // Valid input, exit the loop
                    break;
                }
            } //handle InputMismatchException and display error message
            catch (InputMismatchException e) {
                //Scold the user for invalid input
                System.out.print("Invalid number. Try again. ");
                // Clear the invalid input from the scanner
                scan.nextLine();
            }
        }

        //Create Transaction object named deposit
        Transaction deposit = new Transaction(LocalDate.now(),LocalTime.now(),description,vendor,amount);

        //Call method to read file
        List<Transaction> allTransactions = readTransactions("transactions.csv");

        //add deposits to allTransactions list
        allTransactions.add(deposit);

        //Rewrite file to add user deposit
        saveTransactions(allTransactions);

        //Display confirmation to user
        System.out.println("----------------------------");
        System.out.println("Deposit Added Successfully!");
        System.out.println("----------------------------");

    }

    //Create method to display full Ledger
    public static void displayFullLedger() {

        //Create Transaction list object named allTransactions that will store read transactions from file
        List<Transaction> allTransactions = readTransactions("transactions.csv");

        //Call method to print transactions
        printTransactions(allTransactions);

        //Call method to save transactions(rewrite file all with all transactions)
        saveTransactions(allTransactions);
    }

    // Method that prompts user to log in
    public static void userLogin() {
        //Create variables representing login credentials
        String validUser = "Marques123";
        String validPass = "notMyPass";

        //prompt user for credentials

        System.out.println("Please log in to your account.");

        //Loop to prevent user error, breaks if user enters valid input
        for(int attempt = 3; attempt > 0; attempt--){
            System.out.print("Enter Username: ");
            String userName = scan.nextLine();

            //exits loop + allow user to continue
            if(userName.equalsIgnoreCase(validUser)){
                break;
            }
            //closes app if user runs out of attempts
            else if(attempt == 1){
                System.out.println("You shall not pass!");
                System.exit(0);
            }
            else{
                System.out.println("Incorrect Username | Attempts remaining: " + (attempt-1));
            }
        }

        //Loop to prevent user error, breaks if user enters valid input
        for(int attempt = 3; attempt > 0; attempt--){

            System.out.print("Enter password: ");
            String password = scan.nextLine();

            if(password.equals(validPass)){
                break;
            }
            //closes app if user runs out of attempts
            else if(attempt == 1){
                System.out.println("You shall not pass!");
                System.exit(0);
            }
            else{
                System.out.println("Incorrect Password | Attempts remaining: " + (attempt-1));
            }

        }
        //if valid credentials, display welcome text
        System.out.println("Welcome Marques!");
    }
}


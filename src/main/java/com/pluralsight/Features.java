package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
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


                    LocalDate startDate = null;
                    LocalDate endDate = null;
                    Double userAmount = null;

                    DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                    try {
                        if (!userStartDate.isEmpty()){
                            startDate = LocalDate.parse(userStartDate, formatDate);
                        }
                        if(!userEndDate.isEmpty()){
                            endDate = LocalDate.parse(userEndDate, formatDate);
                        }
                        if(!inputAmount.isEmpty())
                            userAmount = Double.parseDouble(inputAmount);
                    }
                    catch (Exception e){

                        System.out.println("Invalid date or amount format. Try again");

                        //restart loop
                        continue;

                    }

                    for(Transaction t : allTransactions){
                        LocalDate date = t.getDate();
                        String description = t.getDescription();
                        String vendor = t.getVendor();
                        double amount = t.getAmount();

                        if(startDate != null && date.isBefore(startDate)){
                            continue;
                        }

                        if(endDate != null && date.isAfter(endDate)){
                            continue;
                        }

                        if(!userDescription.isEmpty() && !description.toLowerCase().contains(userDescription.toLowerCase())){
                            continue;
                        }

                        if(!userVendor.isEmpty() && !vendor.toLowerCase().contains(userVendor.toLowerCase())){
                            continue;
                        }

                        if(userAmount != null && Double.compare(userAmount, amount) != 0){
                            continue;
                        }

                        //add if passed all filters
                        customSearch.add(t);
                    }

                    //Check if any transactions were found
                    if(customSearch.isEmpty()){
                        System.out.println("\nNo transactions were found based on your inputs. Try again.");
                    }
                    else{
                        found = true;
                        printTransactions(customSearch);
                    }
        }
    }

    public static void sortByVendor() {

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

            for (Transaction t : allTransactions){
                if(t.getVendor().toLowerCase().contains(inputVendor.toLowerCase())){
                    sortByVendor.add(t);
                }
            }

            if(sortByVendor.isEmpty()){
                System.out.println("\nNo transactions found from: |" + inputVendor + " | Try Again");
            }
            else{
                found = true;
                printTransactions(sortByVendor);
            }
        }
    }

    public static void sortPreviousYear(){

        List<Transaction> allTransactions = readTransactions("transactions.csv");
        //Create arraylist containing Transaction object, named prevYearList
        ArrayList<Transaction> prevYearList = new ArrayList<>();

        //Create LocalDate object named today, assigns variable to current date according to system calendar (yyyy-MM-dd)
        LocalDate today = LocalDate.now();
        //Create LocalDate object named prevYearDate, assigns variable to 1 year from current date(today) according to system(ex. 2024-10-12)
        LocalDate prevYearDate = today.minusYears(1);
        //Create variable named prevYear, takes year value of prevYearDate and stores in an integer (prevYearDate = 2024-10-12, int prevYear = 2024)
        int prevYear = prevYearDate.getYear();

        for(Transaction t: allTransactions){
            if(t.getDate().getYear() == prevYear){
                prevYearList.add(t);
            }
        }
        printTransactions(prevYearList);
    }

    public static void sortPreviousMonth(){

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

        if(prevMonth == 0){
            prevMonth = 12;
        }

        for(Transaction t:allTransactions){
            if(t.getDate().getMonthValue() == prevMonth && t.getDate().getYear() == year){
                prevMonthList.add(t);
            }
        }
        printTransactions(prevMonthList);
    }

    public static void sortYearToDate(){

        List<Transaction> allTransactions = readTransactions("transactions.csv");
        //Create Arraylist containing Transaction object named yearToDate
        ArrayList<Transaction> yearToDate = new ArrayList<>();

        //Define variables for todays date according to system and 1 year from todays date
        LocalDate today = LocalDate.now();
        int year = today.getYear();

        for(Transaction t: allTransactions){
            if(t.getDate().getYear() == year && !t.getDate().isAfter(today)){
                yearToDate.add(t);
            }
        }
        printTransactions(yearToDate);
    }

    public static void sortMonthToDate() {
        List<Transaction> allTransactions = readTransactions("transactions.csv");
        //Create an arraylist with Transaction object named monthToDate
        ArrayList<Transaction> monthToDate = new ArrayList<>();

        //Define and assign variables for current date (Month + Year)
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();

        for(Transaction t: allTransactions){
            if(t.getDate().getMonthValue() == month && !t.getDate().isAfter(today) && t.getDate().getYear()==year){
                monthToDate.add(t);
            }
        }
        printTransactions(monthToDate);
    }

    public static void sortByPayments(){

        List<Transaction> allTransactions = readTransactions("transactions.csv");
        //Create an arraylist named payments with Transaction object class
        List<Transaction> payments = new ArrayList<>();

        for(Transaction t : allTransactions){
            if(t.getAmount() < 0){
                payments.add(t);
            }
        }
        payments.sort(Comparator.comparing(Transaction::getDate).thenComparing(Transaction::getTime).reversed());

        //Create String named header to add header to be displayed to user
        String header = ( "\nDate\t\t| Time\t\t | Description\t\t\t\t\t\t\t\t\t\t\t\t   | Vendor\t\t\t\t   |   Amount\n");
        System.out.println(header);

        //Loop through all Transaction objects in the payment ArrayList
        for(Transaction t: payments){
            //Print out formatted version of information contained in Transaction defined as t, (%-12s = Left align String with 12 character space, %10.2f = Right aligned float with 10 character space and shows 2 decimal points)
            System.out.printf("%-12s| %-11s| %-60s| %-22s|%10.2f\n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
        }

    }

    public static void sortByDeposits(){

       List<Transaction> allTransactions = readTransactions("transactions.csv");
       List<Transaction> deposits = new ArrayList<>();

       for (Transaction t : allTransactions){
           if(t.getAmount() > 0){
               deposits.add(t);
           }
       }

       printTransactions(deposits);
    }

    //method to allow user to add payment
    public static void userPayment() {

        //prompt user for information
        System.out.println("-------------------");
        System.out.println("Make a payment:");
        System.out.println("-------------------");
        System.out.println("Enter Description (ex. garnishment)");

        String description = scan.nextLine();

        //No user error
        while(description.equalsIgnoreCase("")){
            System.out.println("Bad description...Try Again");
            description = scan.nextLine();
        }

        System.out.println("Enter Vendor (ex. Uncle Sam)");
        String vendor = scan.nextLine();

        //No user error
        while(vendor.equalsIgnoreCase("")){
            System.out.println("Um...No, Try Again");
            vendor = scan.nextLine();
        }

        System.out.println("How much did you spend this time??");
        double amount = scan.nextDouble();

        //No user error
        while(amount <=0){
            System.out.println("Enter a valid number");
            amount = scan.nextDouble();
        }

        //Turn user amount negative to represent payment
        amount = -amount;

        //Create LocalDate variable named date, assigning to date now based on system
        LocalDate date = LocalDate.now();
        //Create a variable to store format rules for date
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //Create string to store formatted date
        String formattedDate = date.format(dateFormat);

        //Create LocalTime variable named time, store current time based on system
        LocalTime time = LocalTime.now();
        //Create variable to store format rules for time
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        //Create string to store formatted time
        String formattedTime = time.format(timeFormat);

        //try\catch with resources to create BufferWriter containing filewriter to write to transactions.csv, append = true to add to file instead of overwriting file
        try (BufferedWriter buffWrite = new BufferedWriter(new FileWriter("transactions.csv",true))){

            //Format data in transaction to look pretty
            String newLine = String.format("%-12s| %-11s| %-50s| %-22s|%10.2f", formattedDate, formattedTime, description, vendor, amount);
            //Write foratted date in transactions.csv
            buffWrite.write("\n"+newLine);

            //Close buffWriter or else information will not write to file
            buffWrite.close();

            //Display confirmation message
            System.out.println("----------------------------");
            System.out.println("Payment Added Successfully!");
            System.out.println("----------------------------");

            //Handle IOException
        } catch (IOException e) {
            System.out.println();;
        }
    }

    //Method to allow user to add deposit
    public static void userDeposit(){

        //Prompt user for deposit information + store in variables
        System.out.println("--------------");
        System.out.println("Add Deposit: ");
        System.out.println("--------------");
        System.out.println("Enter Description (ex. Invoice 1001 paid)");

        String description = scan.nextLine();

        //No user error
        while(description.equalsIgnoreCase("")){
            System.out.println("You did not enter anything...Try Again");
            description = scan.nextLine();
        }
        System.out.println("Enter Vendor (ex. Amazon)");
        String vendor = scan.nextLine();

        //No user error
        while(vendor.equalsIgnoreCase("")){
            System.out.println("You did not enter anything...Try Again");
            vendor = scan.nextLine();
        }

        System.out.println("Enter Deposit Amount(You don't need an example for this)");
        double amount = 0.00;
        amount = scan.nextDouble();

        //No user error
        while(amount < 0){
            System.out.println("Invalid number. Try again");
            amount = scan.nextDouble();
        }

        //Create Transaction object named deposit
        Transaction deposit = new Transaction(LocalDate.now(),LocalTime.now(),description,vendor,amount);

        //Call method to read file
        List<Transaction> allTransactions = readTransactions("transactions.csv");

        //add deposits to allTransactions list
        allTransactions.add(deposit);

        //Call method to display transactions stored in allTransactions list(deposits)
        printTransactions(allTransactions);

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

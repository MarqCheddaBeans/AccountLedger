package com.pluralsight;

import java.io.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class LedgarApp {

    //Create scanner that is class wide
    public static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        //Create bool variable that will keep application running until user chooses to exit
        boolean mainActive = true;

        //Method to prompt user to login
        userLogin();

        // place code in while loop to keep application running until exited by user, (Returns user to main menu instead of ending app)
        while (true) {
            //Method to display main menu
            int choice = displayMainMenu();

            //Hungry buffer
            scan.nextLine();

            //if statements to allow user to navigate through menu
            //User chose to add deposit
            if (choice == 1) {
                //Method to allow user to add a deposit to ledger
                userDeposit();
            }

            //User chose to add a payment
            else if (choice == 2) {
                //Method to allow user to add a payment to ledger
                userPayment();
            }
                //User chose to display Ledger
            if (choice == 3) {
                //Created bool variable to return user to LedgerMenu after completing prompt until exited by user
                boolean ledgerMenuActive = true;

                //Place ledger menu in while loop while bool ledgerMenuActive is true(allows user to return to ledger menu instead of application auto closing)
                while(ledgerMenuActive) {

                    //Display ledger menu for user + prompt user for choice
                    int input = ledgerMenu();

                    //User chose to display full ledger
                    if (input == 1) {
                        //Method to display full ledger, sorting them newest at the top
                        displayFullLedger();
                    }

                    //User chose to display only deposits from ledger
                    else if (input == 2) {
                        //Method to sort and display only deposits from transactions in ledger
                        sortByDeposits();
                    }

                    //User chose to display only payments from ledger
                    else if (input == 3) {
                        //Method to sort and display only payments from transaction in ledger
                        sortByPayments();
                    }
                    //User chose report menu for filtered ledger search
                    else if (input == 4) {
                        //Create bool variable to keep returning user to report menu until user exits or goes to previous menu
                        boolean reportMenuActive = true;

                        //Place report menu in loop to prevent application closing without user prompt
                        while (reportMenuActive) {
                            // A new screen that allows the user to run pre-defined reports
                            int reportInput = displayReportMenu();

                            // User chose to return to ledger menu
                            if (reportInput == 0){
                                //Change bool value to exit report menu loop, returning user to previous menu(Ledger menu)
                                reportMenuActive = false;
                            }
                            //User chose to sort transaction month to date(past 30 days)
                            else if (reportInput == 1) {
                                //Method to sort and display transaction by month to date
                                sortMonthToDate();
                            }
                            //User chose to sort transaction by previous month
                            else if (reportInput == 2) {
                                //Method to sort and display transactions by previous month
                                sortPreviousMonth();
                            }
                            //User chose to sort transactions year to date (past 365 days)
                            else if (reportInput == 3) {
                                //Method to sort and display transaction by year to date
                                sortYearToDate();
                            }
                            //User chose to sort transactions by previous year
                            else if (reportInput == 4) {
                                //Method to sort and display transactions by previous year
                                sortPreviousYear();
                            }
                            //User chose to sort transactions by searched vendor
                            else if (reportInput == 5) {
                                //Method to sort and display transactions from user inputted vendors
                                sortByVendor();
                            }
                            else if (reportInput == 6){
                                scan.nextLine();
                                customSearch();
                            }
                        }
                    }
                    //User chose to return to main menu/ homepage
                    else if (input == 5) {
                        //Change ledgerMenuActive to false, exiting ledger menu loop and sending user to previous menu(Main menu)
                        ledgerMenuActive = false;
                    }
                }
            }
                //User chose to close application
                else if (choice == 4) {
                    //Display text to user stating ledger app is closing
                    System.out.println("Closing Ledger Application");
                    //break = Exit Main menu loop, ending application
                    break;
                }
        }
    }

    private static void customSearch() {

        boolean found = false;

        //keeps user in loop until broken out(found==true)
        while (!found) {

            //Create ArrayList containing Transaction object named sortCustom
            ArrayList<Transaction> sortCustom = new ArrayList<>();

            // Prompt user for optional search filters + store in String variables
            System.out.print("Enter start date (yyyy-MM-dd) or leave blank: ");
            String userStartDate = scan.nextLine().trim();

            System.out.print("Enter end date (yyyy-MM-dd) or leave blank: ");
            String userEndDate = scan.nextLine().trim();

            System.out.print("Enter description or leave blank: ");
            String userDescription = scan.nextLine().trim();

            System.out.print("Enter vendor or leave blank: ");
            String userVendor = scan.nextLine().trim();

            System.out.print("Enter amount or leave blank: ");
            String inputAmount = scan.nextLine().trim();

            //We will use these later
            LocalDate startDate = null;
            LocalDate endDate = null;
            Double userAmount = null;

            //DateTimeFormatter class to print + parse date-time objects, set pattern for date to be formatted
            DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            //try catch method to parse user inputs for filters
            try{
                //checks if user entered start date
                if (!userStartDate.isEmpty()){
                    //if true, parse user start date in LocalDate startDate defined earlier, parameters including (user entered input + DateTimeFormatter)
                    startDate = LocalDate.parse(userStartDate, formatDate);
                }

                //checks if user entered end date
                if(!userEndDate.isEmpty()){
                    //if true, parse user end date in LocalDate endDate defined earlier, parameters including (user entered input + DateTimeFormatter)
                    endDate = LocalDate.parse(userEndDate, formatDate);
                }

                //checks if user entered an amount
                if(!inputAmount.isEmpty()){
                    //if true, parse user amount to double named user amount, parameters including (user entered amount)
                    userAmount = Double.parseDouble(inputAmount);
                }
            }
            //Handle any exceptions thrown, display error message
            catch (Exception e){
                System.out.println("Date or amount format no good. Try again");
                continue;
            }

            //Try\catch with elements, create BufferedReader named buffRead containing FileReader, read "transactions.csv"
            try (BufferedReader buffRead = new BufferedReader(new FileReader("transactions.csv"))) {

                //Create String variable named line
                String line;

                //Loop to read and store each line in "transactions.csv" file until end of file (end of file == null)
                while ((line = buffRead.readLine()) != null) {
                    String[] transInfo = line.split("\\|");

                    //Check for header and skip, we dont need header
                    if (transInfo[0].trim().equalsIgnoreCase("Date")) {
                        continue;
                    }

                    //try\catch block to handle exceptions while parsing variables
                    try {
                        LocalDate date = LocalDate.parse(transInfo[0].trim());
                        LocalTime time = LocalTime.parse(transInfo[1].trim());
                        String description = transInfo[2].trim();
                        String vendor = transInfo[3].trim();
                        double amount = Double.parseDouble(transInfo[4].trim());

                        //Create filter funnel

                        //check if user entered a start date + date stored from transactions file is before user entered start date//Filter by start date
                        if (startDate != null && date.isBefore(startDate)) {
                            //if true, continue to next check
                            continue;
                        }

                        //check if user entered an end date + date stored from transactions file is after user entered end date//Filter by end date
                        if (endDate != null && date.isAfter(endDate)) {
                            //if true, continue to next check
                            continue;
                        }

                        //check if user entered a description + description stored from transactions file does not contains similar value from user entered description .toLowerCase() to avoid case sensitivity// Filter by description
                        if (!userDescription.isEmpty() && !description.toLowerCase().contains(userDescription.toLowerCase())) {
                            //if true, continue to next check
                            continue;
                        }

                        //check if user entered a vendor + vendor stored from transactions file does not contains similar value from user entered vendor .toLowerCase() avoids case sensitivity// Filter by vendor
                        if (!userVendor.isEmpty() && !vendor.toLowerCase().contains(userVendor.toLowerCase())) {
                            //if true, continue to next check
                            continue;
                        }

                        //check if user entered an amount + compare user input amount with amount stored from transactions file, double.compare(a,d) = 0 if equal //filter by amount
                        if (userAmount != null && Double.compare(userAmount, amount) != 0) {
                            continue;
                        }

                        //All filters passed
                        sortCustom.add(new Transaction(date, time, description, vendor, amount));

                        //Handle DateTimeParseException and display error text
                    } catch (DateTimeParseException e) {
                        System.out.println();
                    }
                }

                    //Prompt user to re enter filters if no transactions
                    if (sortCustom.isEmpty()) {
                        //No transactions added == invalid inputs, prompt user to reenter
                        System.out.println("\nNo transactions | Try Again");
                    }
                    else {
                        //Exit loop since transactions were found
                        found = true;

                        //Sort stored transactions in ArrayList using comparator.comparing each date in sortByVendor arraylist, .thencomparing each time in sortByVendor arraylist, .reversed() will reverse output, setting highest date + time(most recent) at the top
                        sortCustom.sort(Comparator.comparing(Transaction::getDate).thenComparing(Transaction::getTime).reversed());

                        //Create String named header to add header to be displayed to user
                        String header = ("\nDate\t\t| Time\t\t | Description\t\t\t\t\t\t\t\t\t\t\t\t   | Vendor\t\t\t\t   |   Amount\n");
                        System.out.println(header);

                        //Loop through all Transaction objects in the sortByVendor ArrayList
                        for (Transaction t : sortCustom) {
                            //Print out formatted version of information contained in Transaction defined as t, (%-12s = Left align String with 12 character space, %10.2f = Right aligned float with 10 character space and shows 2 decimal points)
                            System.out.printf("%-12s| %-11s| %-60s| %-22s|%10.2f\n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                        }

                    }

            }
            //Handle IOException , display warning text
            catch (IOException e) {
                System.out.println();
            }
        }
    }

    private static void sortByVendor() {

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

            // Try/catch with resources creating BufferedReader containing FileReader , reading "transactions.csv" file
            try (BufferedReader buffRead = new BufferedReader(new FileReader("transactions.csv"))) {

                //Create string named line
                String line;
                //while loop, reading file line by line, storing each line in string named line, read as long as line does not equal null(Null = end of lines)
                while ((line = buffRead.readLine()) != null) {
                    //Create String array named transInfo, split line read from transaction.csv by | and store each value as string in transInfo array
                    String[] transInfo = line.split("\\|");

                    //if first element of transInfo == "Date" -> that line is the header, skip
                    if (transInfo[0].trim().equalsIgnoreCase("Date")) {
                        //skip if true
                        continue;
                    }

                    //try/catch block while parsing variables
                    try {
                        //.trim() all to remove excess spacing after values
                        //Parse first element of transInfo from String to LocalDate, store in variable date
                        LocalDate date = LocalDate.parse(transInfo[0].trim());
                        //Pars second element of transInfo from String to LocalTime, store in variable time
                        LocalTime time = LocalTime.parse(transInfo[1].trim());
                        //No need to parse , store in string variable description
                        String description = transInfo[2].trim();
                        //No parse, store in variable vendor
                        String vendor = transInfo[3].trim();
                        //Parse 5th element from String to double, store in double amount
                        double amount = Double.parseDouble(transInfo[4].trim());

                        //compare user input vendor to vendor from each transaction
                        if (vendor.equalsIgnoreCase(inputVendor)) {
                            //if user input vendor == vendor from transaction, add to sortByVendor arraylist
                            sortByVendor.add(new Transaction(date, time, description, vendor, amount));
                        }

                    //Handle specific exception expected when performing code in try brackets
                    } catch (DateTimeParseException e) {
                        //Display text if exception is caught,(Helps with debugging code)
                        System.out.println("");
                    }
                }

                //Check if any transactions were added to sortByVendor arraylist
                if (sortByVendor.isEmpty()) {
                    //No transactions added == invalid vendor, prompt user to reenter
                    System.out.println("\nNo transactions from: |" + inputVendor + "| Try Again");
                }
                    //Else = transactions found
                    else {
                        //Exit loop since transactions were found
                        found = true;

                        //Sort stored transactions in ArrayList using comparator.comparing each date in sortByVendor arraylist, .thencomparing each time in sortByVendor arraylist, .reversed() will reverse output, setting highest date + time(most recent) at the top
                        sortByVendor.sort(Comparator.comparing(Transaction::getDate).thenComparing(Transaction::getTime).reversed());

                    //Create String named header to add header to be displayed to user
                    String header = ( "\nDate\t\t| Time\t\t | Description\t\t\t\t\t\t\t\t\t\t\t\t   | Vendor\t\t\t\t   |   Amount\n");
                    System.out.println(header);

                    //Loop through all Transaction objects in the sortByVendor ArrayList
                    for (Transaction t : sortByVendor) {
                        //Print out formatted version of information contained in Transaction defined as t, (%-12s = Left align String with 12 character space, %10.2f = Right aligned float with 10 character space and shows 2 decimal points)
                        System.out.printf("%-12s| %-11s| %-60s| %-22s|%10.2f\n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                    }
                }
                    //Handle any IOException (Input/Output)
            } catch (IOException e) {
                System.out.println("Uh Oh");
            }
        }
    }

    private static void sortPreviousYear(){

        //Create arraylist containing Transaction object, named prevYearList
        ArrayList<Transaction> prevYearList = new ArrayList<>();

        //Create LocalDate object named today, assigns variable to current date according to system calendar (yyyy-MM-dd)
        LocalDate today = LocalDate.now();
        //Create LocalDate object named prevYearDate, assigns variable to 1 year from current date(today) according to system(ex. 2024-10-12)
        LocalDate prevYearDate = today.minusYears(1);
        //Create variable named prevYear, takes year value of prevYearDate and stores in an integer (prevYearDate = 2024-10-12, int prevYear = 2024)
        int prevYear = prevYearDate.getYear();

        //try/catch block with resources creating BufferReader containing FileReader, reading transactions.csv
        try(BufferedReader buffRead = new BufferedReader(new FileReader("transactions.csv"))){

            //Create string named line
            String line;

            //while loop, read transaction lines, assigning read lines to string line, as long as != null (Null = end of file)
            while((line = buffRead.readLine()) != null) {

                //Create String array named transInfo, splitting each line of transaction by | and storing in string array named transInfo
                String[] transInfo = line.split("\\|");

                //if transInfo index 0 equals "Date" == is the header
                if (transInfo[0].trim().equalsIgnoreCase("Date")) {
                    //if true, skip(We dont need the header)
                    continue;
                }

                //Try\catch block to parse transInfo + handle exception
                try {

                    //Parse transInfo first element from String to LocalDate, store in date
                    LocalDate date = LocalDate.parse(transInfo[0].trim());
                    //Parse transInfo second element from String to LocalTime, store in time
                    LocalTime time = LocalTime.parse(transInfo[1].trim());
                    //No need to parse, store 3rd element of transInfo in string named description
                    String description = transInfo[2].trim();
                    //No parse, store 4th element of transInfo in String named vendor
                    String vendor = transInfo[3].trim();
                    //Parse transInfo 5th element from String to Double, store in amount
                    double amount = Double.parseDouble(transInfo[4].trim());

                    //compare date from transInfo to variable holding previous year
                    if (date.getYear() == prevYear) {
                        //if true, add transaction containing previous year
                        prevYearList.add(new Transaction(date, time, description, vendor, amount));
                    }
                //Handle DateTimeParseException
                } catch (DateTimeParseException e) {
                    //Print text when exception is caught
                    System.out.println();
                }
            }
                //Sort arraylist prevYearList using composite comparator (multiple comparators comparing), compares Transaction object returned dates, then comparing Transaction object returned time, .reversed() entire statement for highest date first(most recent at top)
                prevYearList.sort(Comparator.comparing(Transaction::getDate).thenComparing(Transaction::getTime).reversed());

                //Create String named header to add header to be displayed to user
                String header = ( "\nDate\t\t| Time\t\t | Description\t\t\t\t\t\t\t\t\t\t\t\t   | Vendor\t\t\t\t   |   Amount\n");
                System.out.println(header);

                //Loop through all Transaction objects added in the prevYearList ArrayList
                for(Transaction t: prevYearList){
                    //Print out formatted version of information contained in Transaction defined as t, (%-12s = Left align String with 12 character space, %10.2f = Right aligned float with 10 character space and shows 2 decimal points)
                    System.out.printf("%-12s| %-11s| %-60s| %-22s|%10.2f\n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                }
        }
            //Handle IOException + displaying text
            catch(IOException e){
                System.out.println("Oops");
        }
    }

    private static void sortPreviousMonth(){

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

        //try\catch block with resources, create BufferedReader named buffRead containing FileReader, reading "transactions.csv" file
        try(BufferedReader buffRead = new BufferedReader(new FileReader("transactions.csv"))){

            //Create String named line
            String line;
            //while loop, read line of transaction file and assign to line variable, read until end of file (end of file == null)
            while((line = buffRead.readLine())!= null) {
                //Create string array named transInfo, split line from transaction stored in variable line by | (pipe)
                String[] transInfo = line.split("\\|");

                //if index 0 of transInfo == "Date" -> that line is the header, we dont need the header
                if (transInfo[0].trim().equalsIgnoreCase("Date")) {
                    //if true, skip
                    continue;
                }

                //try\catch block to parse transInfo
                try {
                    //Create LocalDate variable named date, parse index 0 of transInfo from String to LocalDate + store in date variable
                    LocalDate date = LocalDate.parse(transInfo[0].trim());
                    //Create LocalTime variable named time, parse index 1 of transInfo from String to LocalTime + store in time variable
                    LocalTime time = LocalTime.parse(transInfo[1].trim());
                    //Create String named description, no need to parse, just .trim() excess spaces + store in description variable
                    String description = transInfo[2].trim();
                    //Create String named vendor, no parse, .trim() excess spaces + store in vendor variable
                    String vendor = transInfo[3].trim();
                    //Create double variable named amount, parse index 4 of transInfo from String to double, store in amount variable
                    double amount = Double.parseDouble(transInfo[4].trim());

                    //compare transaction month == previous month and transaction year == current year, we want it
                    if (date.getMonthValue() == prevMonth && date.getYear() == year) {
                        //if true, add to array list prevMonthList
                        prevMonthList.add(new Transaction(date, time, description, vendor, amount));
                    }
                    //Handle DateTimeParseException by displaying text
                } catch (DateTimeParseException e) {
                    System.out.println("");
                }
            }
            //Sort arraylist prevMonthList using composite comparator (multiple comparators comparing), compares Transaction object returned dates, then comparing Transaction object returned time, .reversed() entire statement for highest date first(most recent at top)
            prevMonthList.sort(Comparator.comparing(Transaction::getDate).thenComparing(Transaction::getTime).reversed());

                //Create String named header to add header to be displayed to user
                String header = ( "\nDate\t\t| Time\t\t | Description\t\t\t\t\t\t\t\t\t\t\t\t   | Vendor\t\t\t\t   |   Amount\n");
                System.out.println(header);

                //Loop through all Transaction objects in the prevMonthList ArrayList
                for(Transaction t: prevMonthList){
                    //Print out formatted version of information contained in Transaction defined as t, (%-12s = Left align String with 12 character space, %10.2f = Right aligned float with 10 character space and shows 2 decimal points)
                    System.out.printf("%-12s| %-11s| %-60s| %-22s|%10.2f\n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                }
        }
        //Handle IOException + display warning text
            catch (IOException e){
                System.out.println("Oh No");
            }
    }

    private static void sortYearToDate(){

        //Create Arraylist containing Transaction object named yearToDate
        ArrayList<Transaction> yearToDate = new ArrayList<>();

        //Define variables for todays date according to system and 1 year from todays date
        LocalDate today = LocalDate.now();
        LocalDate oneYearAgo = today.minusYears(1);

        //try\catch with resources , create buffered readyer containing file reader , reading "transactions.csv" file
        try(BufferedReader buffRead = new BufferedReader( new FileReader("transactions.csv"))){


            String line;
            while((line = buffRead.readLine()) != null) {

                String[] transInfo = line.split("\\|");

                //Skip heading
                if (transInfo[0].trim().equalsIgnoreCase("Date")) {
                    continue;
                }

                try {

                    LocalDate date = LocalDate.parse(transInfo[0].trim());
                    LocalTime time = LocalTime.parse(transInfo[1].trim());
                    String description = transInfo[2].trim();
                    String vendor = transInfo[3].trim();
                    double amount = Double.parseDouble(transInfo[4].trim());

                    if (!date.isBefore(oneYearAgo) && !date.isAfter(today)) {
                        yearToDate.add(new Transaction(date, time, description, vendor, amount));
                    }
                } catch (Exception e) {
                    System.out.println("");
                }
            }

                yearToDate.sort(Comparator.comparing(Transaction::getDate).thenComparing(Transaction::getTime).reversed());

                //Create String named header to add header to be displayed to user
                String header = ( "\nDate\t\t| Time\t\t | Description\t\t\t\t\t\t\t\t\t\t\t\t   | Vendor\t\t\t\t   |   Amount\n");
                System.out.println(header);

                //Loop through all Transaction objects in the yearToDate ArrayList
                for(Transaction t: yearToDate){
                    //Print out formatted version of information contained in Transaction defined as t, (%-12s = Left align String with 12 character space, %10.2f = Right aligned float with 10 character space and shows 2 decimal points)
                    System.out.printf("%-12s| %-11s| %-60s| %-22s|%10.2f\n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                }
        }
            catch(IOException e){
                System.out.println("Oops");
        }

    }

    private static void sortMonthToDate() {
        //Create an arraylist with Transaction object named monthToDate
        ArrayList<Transaction> monthToDate = new ArrayList<>();

        //Define and assign variables for current date (Month + Year)
        LocalDate today = LocalDate.now();
        LocalDate prevMonth = today.minusDays(30);

        //try\catch with resources , Create Buffered Reader with File Reader to read transactions file
        try(BufferedReader buffRead = new BufferedReader(new FileReader("transactions.csv"))){

            //Read each line in file and split by |
            String line;
            while((line = buffRead.readLine()) != null){
                String[] transInfo = line.split("\\|");

                //Skip over the header
                if(transInfo[0].trim().equalsIgnoreCase("Date")){
                    continue;
                }

                try{
                    //Parse necessary variables based on data types
                    LocalDate date = LocalDate.parse(transInfo[0].trim());
                    LocalTime time = LocalTime.parse(transInfo[1].trim());
                    String description = transInfo[2].trim();
                    String vendor = transInfo[3].trim();
                    double amount = Double.parseDouble(transInfo[4].trim());

                    //Ask if Month + Year in file is the same is current Month + Year , then adding to monthToDate arraylist if true
                    if(!date.isBefore(prevMonth) && !date.isAfter(today)){
                        monthToDate.add(new Transaction(date,time,description,vendor,amount));
                    }

                }//Handles any exception and throws text to console
                catch(Exception e){
                    System.out.println("");
                }
            }
            //Call .sort method on monthToDate arraylist, Sort by date field in each Transaction, .thenComparing adds secondary sorting rule which sorts by time after, .reversed() flips order of comparison
            monthToDate.sort(Comparator.comparing(Transaction::getDate).thenComparing(Transaction::getTime).reversed());

            //Create String named header to add header to be displayed to user
            String header = ( "\nDate\t\t| Time\t\t | Description\t\t\t\t\t\t\t\t\t\t\t\t   | Vendor\t\t\t\t   |   Amount\n");
            System.out.println(header);

            //Loop through all Transaction objects in the monthToDate ArrayList
            for(Transaction t: monthToDate){
                //Print out formatted version of information contained in Transaction defined as t, (%-12s = Left align String with 12 character space, %10.2f = Right aligned float with 10 character space and shows 2 decimal points)
                System.out.printf("%-12s| %-11s| %-60s| %-22s|%10.2f\n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
            }
        }
           catch (IOException e){
               System.out.println("Could not read file.");
           }
    }

    private static int displayReportMenu() {

        System.out.println("\nReport: ");
        System.out.println("----------------------------------------------------");
        System.out.println("Please select how you would like to filter ledger");
        System.out.println("----------------------------------------------------");
        System.out.println("\n1) Month To Date\n2) Previous Month\n3) Year To Date\n4) Previous Year\n5) Search by Vendor\n6) Custom Search\n0) Back to Ledger page");

        int reportInput = scan.nextInt();
        while(reportInput <-1 || reportInput >6){
            System.out.println("Invalid input. Try Again");
            reportInput = scan.nextInt();
        }
        return reportInput;
    }

    private static void sortByPayments(){

        //Create an arraylist named payment with Transaction object class
        ArrayList<Transaction> payment = new ArrayList<>();

        //Try\catch with resources will automatically close reader when finished
        try(BufferedReader buffRead = new BufferedReader(new FileReader("transactions.csv"))){

            //Read through csv file until last line, store read line in String named lines
            String lines;
            while((lines = buffRead.readLine()) !=null){

                //Create array for transaction information (transInfo) sort transaction information splitting
                String[] transInfo = lines.split("\\|");

                //Skips the header
                if(transInfo[0].trim().equalsIgnoreCase("Date")){
                    continue;
                }

                //try/catch block to parse + store information extracted from line in file
                try{
                    //Declare LocalDate named date and set value to index 0 of transInfo after parsing from String to LocalDate
                    LocalDate date = LocalDate.parse(transInfo[0].trim());
                    //Declare LocalTime named time and set value to index 1 of transInfo after parsing from String to LocalTime
                    LocalTime time = LocalTime.parse(transInfo[1].trim());
                    String description = transInfo[2].trim();
                    String vendor = transInfo[3].trim();
                    //Declare double named amount and set value to index 4 of transInfo after parsing from String to double
                    double amount = Double.parseDouble(transInfo[4].trim());

                    // Negative amounts = payments, only add payments to payment ArrayList
                    if (amount < 0) {
                        payment.add(new Transaction(date, time, description, vendor, amount));
                    }
                    //catch any exception store in e + display text
                } catch(Exception e){
                    System.out.println("");
                }
            }
            //Sort by date and time (.reversed() will sort newest at the top)
            payment.sort(Comparator.comparing(Transaction::getDate).thenComparing(Transaction::getTime).reversed());

            //Create String named header to add header to be displayed to user
            String header = ( "\nDate\t\t| Time\t\t | Description\t\t\t\t\t\t\t\t\t\t\t\t   | Vendor\t\t\t\t   |   Amount\n");
            System.out.println(header);

            //Loop through all Transaction objects in the payment ArrayList
            for(Transaction t: payment){
                //Print out formatted version of information contained in Transaction defined as t, (%-12s = Left align String with 12 character space, %10.2f = Right aligned float with 10 character space and shows 2 decimal points)
                System.out.printf("%-12s| %-11s| %-60s| %-22s|%10.2f\n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
            }
            //Handle IOException displaying text to console
        }   catch(IOException e){
            System.out.println("Oh Noo!");
        }

    }

    private static void sortByDeposits(){

        List<Transaction> deposits = new ArrayList<>();

        try(BufferedReader buffRead = new BufferedReader(new FileReader("transactions.csv"))){

            String lines;
                while((lines = buffRead.readLine()) !=null){

                    String[] transInfo = lines.split("\\|");

                    //Skips the header
                    if(transInfo[0].trim().equalsIgnoreCase("Date")){
                        continue;
                    }

                    try{
                        LocalDate date = LocalDate.parse(transInfo[0].trim());
                        LocalTime time = LocalTime.parse(transInfo[1].trim());
                        String description = transInfo[2].trim();
                        String vendor = transInfo[3].trim();
                        double amount = Double.parseDouble(transInfo[4].trim());
                        //Add split up transaction to defined Transaction ArrayList
                        if (amount > 0) {
                            deposits.add(new Transaction(date, time, description, vendor, amount));
                        }
                    } catch(Exception e){
                        System.out.println("");
                    }
                }
                //Sort by date and time (.reversed() will sort newest at the top)
                deposits.sort(Comparator.comparing(Transaction::getDate).thenComparing(Transaction::getTime).reversed());

            String header = ( "\nDate\t\t| Time\t\t | Description\t\t\t\t\t\t\t\t\t\t\t\t   | Vendor\t\t\t\t   |   Amount\n");
            System.out.println(header);

            for(Transaction t: deposits){
                    System.out.printf("%-12s| %-11s| %-60s| %-22s|%10.2f\n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                }

        }   catch(IOException e){
            System.out.println("Oh Noo!");
        }
    }

    private static int ledgerMenu() {
        System.out.println("""
                \n------------------------------------
                How would you like to view ledgers?
                ------------------------------------
                1) All Ledgers
                2) Deposits
                3) Payments
                4) Reports
                5) Back To Home
                """);
        int input = 0;
        input = scan.nextInt();

        while (input <= 0 || input >= 6) {
            System.out.println("Not a valid input..Try Again");
            input = scan.nextInt();
        }
        return input;
    }

    private static void userPayment() {
        System.out.println("-------------------");
        System.out.println("Make a payment:");
        System.out.println("-------------------");
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

        while(amount <=0){
            System.out.println("Enter a valid number");
            amount = scan.nextDouble();
        }
        //Turn user amount negative to represent payment
        amount *= -1;

        LocalDate date = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = date.format(dateFormat);

        LocalTime time = LocalTime.now();
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = time.format(timeFormat);

        try (BufferedWriter buffWrite = new BufferedWriter(new FileWriter("transactions.csv",true))){

            String newLine = String.format("%-12s| %-11s| %-60s| %-22s|%10.2f", formattedDate, formattedTime, description, vendor, amount);
            buffWrite.write("\n"+newLine);
            //Close buffWriter or else information will not write to file
            buffWrite.close();
            System.out.println("----------------------------");
            System.out.println("Payment Added Successfully!");
            System.out.println("----------------------------");
        } catch (IOException e) {
            System.out.println();;
        }
    }

    private static void userDeposit(){
        System.out.println("--------------");
        System.out.println("Add Deposit: ");
        System.out.println("--------------");
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

        try ( BufferedWriter buffWrite = new BufferedWriter(new FileWriter("transactions.csv",true))){
            String newLine = String.format("%-12s| %-11s| %-50s| %-22s|%10.2f", formattedDate, formattedTime, description, vendor, amount);
            buffWrite.write("\n"+newLine);
            //Close buffWriter or else information will not write to file
            buffWrite.close();
            System.out.println("----------------------------");
            System.out.println("Deposit Added Successfully!");
            System.out.println("----------------------------");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static int displayMainMenu() {

        System.out.println("\n-----------------------------------");
        System.out.println("What would you like to do today?");
        System.out.println("-----------------------------------");
        System.out.println("""
                1) Add deposit
                2) Make a payment(Debit)
                3) Ledger
                4) Exit
                """);
        int choice = scan.nextInt();
        while (choice<=0 || choice>=5){
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
        try (BufferedReader buffRead = new BufferedReader( new FileReader("transactions.csv"))){

            ArrayList<Transaction> transactions = new ArrayList<>();

            String lines;
            while((lines = buffRead.readLine()) != null) {
                //split by delimiter
                String[] transInfo = lines.split("\\|");

                //Creating variables + storing individual information from split transaction.csv
                try {
                    LocalDate date = LocalDate.parse(transInfo[0].trim());
                    LocalTime time = LocalTime.parse(transInfo[1].trim());
                    String description = transInfo[2].trim();
                    String vendor = transInfo[3].trim();
                    double amount = Double.parseDouble(transInfo[4].trim());

                    //Add split up transaction to defined Transaction ArrayList
                    transactions.add(new Transaction(date,time,description,vendor,amount));

                } catch (DateTimeException | ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    System.out.print("");
                }

            }
            //Sort transactions by date and time, reverse for newest transactions at the top
            transactions.sort(Comparator.comparing(Transaction::getDate).thenComparing(Transaction::getTime).reversed());

            try (BufferedWriter buffWrite = new BufferedWriter(new FileWriter("transactions.csv"))){

                String header = ( "\nDate\t\t| Time\t\t | Description\t\t\t\t\t\t\t\t\t\t\t\t   | Vendor\t\t\t\t   |   Amount\n");
                buffWrite.write(header);

                System.out.print(header);
                for(Transaction t : transactions) {
                    String formatInfo = String.format("%-12s| %-11s| %-60s| %-22s|%10.2f", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                    System.out.println(formatInfo);
                    buffWrite.write(formatInfo);
                    buffWrite.newLine();
                }
            } catch (IOException e) {
                System.out.println("Woa, theres a problem here.");
            }

        } catch (IOException e) {
            System.out.println("File Not Found.");
        }
    }

    // Method that prompts user to log in
    private static void userLogin() {
        String validUser = "Marques123";
        String validPass = "notMyPass";

        System.out.println("Please log in to your account.");

        for(int attempt = 3; attempt > 0; attempt--){
            System.out.print("Enter Username: ");
            String userName = scan.nextLine();

            if(userName.equalsIgnoreCase(validUser)){
                break;
            }
                else if(attempt == 1){
                System.out.println("You shall not pass!");
                System.exit(0);
            }
                else{
                System.out.println("Incorrect Username | Attempts remaining: " + (attempt-1));
            }
        }

        for(int attempt = 3; attempt > 0; attempt--){

            System.out.print("Enter password: ");
            String password = scan.nextLine();

            if(password.equals(validPass)){
                break;
            }
                else if(attempt == 1){
                System.out.println("You shall not pass!");
                System.exit(0);
            }
                else{
                System.out.println("Incorrect Password | Attempts remaining: " + (attempt-1));
            }

        }
        System.out.println("Welcome Marques!");
    }
}

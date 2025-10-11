package com.pluralsight;

import java.io.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class LedgarApp {

    //Create scanner that is class wide
    public static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        //Method to prompt user to login
         userLogin();

        //Method to display main menu
        int choice = displayMainMenu();

        //Hungry buffer
        scan.nextLine();

        //if statements to allow user to navigate through menu
        //User chose to add deposit
        if(choice == 1) {
            userDeposit();
            }

        //User chose to add a payment
        else if (choice == 2) {
            userPayment();
            }

        //User chose to display Ledger
        else if(choice == 3) {

            //Display ledger menu for user
            int input = ledgerMenu();

                //User chose to display full ledger
                if (input == 1) {
                displayFullLedger();
                    }

                //User chose to display only deposits from ledger
                else if (input == 2) {
                    sortByDeposits();
                    }

                //User chose to display only payments from ledger
                else if (input == 3) {
                    sortByPayments();
                    }

                //User chose to run custom search
                else if(input == 4) {
                // A new screen that allows the user to run pre-defined reports or to run a custom search
                    }
            //User chose to return to main menu/ homepage
            else if(input == 5){
                //Home - go back to home page
            }

        //User chose to close application
        }else if (choice == 4) {
                System.out.println("Closing Ledger Application");
                System.exit(0);
            }
    }

    private static void sortByPayments(){

        //Create an arraylist named payment with Transaction object class
        ArrayList<Transaction> payment = new ArrayList<>();

        //Try with resources will automatically close reader when finished
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
                    System.out.println("WHAT DID YOU DO!?");
                }
            }
            //Sort by date and time (.reversed() will sort newest at the top)
            payment.sort(Comparator.comparing(Transaction::getDate).reversed().thenComparing(Transaction::getTime).reversed());

            //Create String named header to add header to be displayed to user
            String header = ( "Date\t\t| Time\t\t | Description\t\t\t\t\t\t\t\t\t\t | Vendor\t\t\t\t |   Amount");
            System.out.println(header);

            //Loop through all Transaction objects in the payment ArrayList
            for(Transaction t: payment){
                //Print out formatted version of information contained in Transaction defined as t, (%-12s = Left align String with 12 character space, %10.2f = Right aligned float with 10 character space and shows 2 decimal points)
                System.out.printf("%-12s| %-11s| %-50s| %-22s|%10.2f\n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
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
                        System.out.println("WHAT DID YOU DO!?");
                    }
                }
                //Sort by date and time (.reversed() will sort newest at the top)
                deposits.sort(Comparator.comparing(Transaction::getDate).reversed().thenComparing(Transaction::getTime).reversed());

            String header = ( "Date\t\t| Time\t\t | Description\t\t\t\t\t\t\t\t\t\t | Vendor\t\t\t\t |   Amount");
            System.out.println(header);

            for(Transaction t: deposits){
                    System.out.printf("%-12s| %-11s| %-50s| %-22s|%10.2f\n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                }

        }   catch(IOException e){
            System.out.println("Oh Noo!");
        }
    }


    private static int ledgerMenu() {
        System.out.println("""
                How would you like to view ledgers?
                1) All Ledgers
                2) Deposits
                3) Payments
                4) Reports
                """);
        int input = 0;
        input = scan.nextInt();

        while (input <= 0 || input >= 5) {
            System.out.println("Not a valid input..Try Again");
            input = scan.nextInt();
        }
        return input;
    }

    private static void userPayment() {
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

        try {BufferedWriter buffWrite = new BufferedWriter(new FileWriter("transactions.csv",true));

            String newLine = String.format("%-12s| %-11s| %-50s| %-22s|%10.2f", formattedDate, formattedTime, description, vendor, amount);
            buffWrite.write("\n"+newLine);
            //Close buffWriter or else information will not write to file
            buffWrite.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void userDeposit(){
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

        try { BufferedWriter buffWrite = new BufferedWriter(new FileWriter("transactions.csv",true));
            String newLine = String.format("%-12s| %-11s| %-50s| %-22s|%10.2f", formattedDate, formattedTime, description, vendor, amount);
            buffWrite.write("\n"+newLine);
            //Close buffWriter or else information will not write to file
            buffWrite.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
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
        try (BufferedReader buffRead = new BufferedReader( new FileReader("transactions.csv"))){

            ArrayList<Transaction> transactions = new ArrayList<>();

            String lines;
            while((lines = buffRead.readLine()) != null) {
                //buffRead.readLine();
                String[] transInfo = lines.split("\\|");

                //Creating variables + storing individual information from split transaction.csv
                try {
                    LocalDate date = LocalDate.parse(transInfo[0].trim());
                    LocalTime time = LocalTime.parse(transInfo[1].trim());
                    String description = transInfo[2].trim();
                    String vendor = transInfo[3].trim();
                    double amount = Double.parseDouble(transInfo[4].trim());

                    //Transaction trans = new Transaction(date, time, description, vendor, amount);

                    //Add split up transaction to defined Transaction ArrayList
                    transactions.add(new Transaction(date,time,description,vendor,amount));

                } catch (DateTimeException | ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    System.out.print("");
                }

            }
            //Sort transactions by date and time, reverse for newest transactions at the top
            transactions.sort(Comparator.comparing(Transaction::getDate).thenComparing(Transaction::getTime).reversed());

            try (BufferedWriter buffWrite = new BufferedWriter(new FileWriter("transactions.csv"))){

                String header = ( "Date\t\t| Time\t\t | Description\t\t\t\t\t\t\t\t\t\t | Vendor\t\t\t\t |   Amount\n");
                buffWrite.write(header);

                System.out.print(header);
                for(Transaction t : transactions) {
                    String formatInfo = String.format("%-12s| %-11s| %-50s| %-22s|%10.2f", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
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

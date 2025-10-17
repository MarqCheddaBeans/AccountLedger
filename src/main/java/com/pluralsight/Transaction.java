package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Transaction {

    //Create variables for transactions
    private final LocalDate date;
    private final LocalTime time;
    private final String description;
    private final String vendor;
    private final double amount;

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }

    //Constructor for transactions
    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    //Method to read transactions taking in string filepath("transactions.csv")
    public static List<Transaction> readTransactions(String filePath){

        //Create list with Transaction object named transactions
        List<Transaction> transactions = new ArrayList<>();

        //try\catch with resources, create buffered reader containing file reader, reading filepath ("transactions.csv")
        try(BufferedReader buffRead = new BufferedReader(new FileReader(filePath))){

            //Create string named line, will store line read from transactions.csv
            String line;
            //loop through transactions file, storing in line, until the end (end == null)
            while((line = buffRead.readLine()) != null){
                //Create string array named transInfo, split variable line by delimiter ( | ), store split line in transInfo
                String[] transInfo = line.split("\\|");

                //Look for header, we dont need header
                if(transInfo[0].equalsIgnoreCase("Date")){
                    //if true, skip header
                    continue;
                }

                //try\catch block to parse variables
                try{
                    //Create local date variable named date, parse first element of transInfo from string to localdate, store in variable date
                    LocalDate date = LocalDate.parse(transInfo[0].trim());
                    //Create local time variable named time, parse second element of transInfo from string to localtime, store in variable time
                    LocalTime time = LocalTime.parse(transInfo[1].trim());
                    //Create string variable named description, .trim() 3rd element in transInfo and store in variable description
                    String description = transInfo[2].trim();
                    //Create string variable named vendor, .trim() 4th element in transInfo and store in variable vendor
                    String vendor = transInfo[3].trim();
                    //Create double variable named amount, parse 5th element in transInfo from string to double and store in variable amount
                    double amount = Double.parseDouble(transInfo[4].trim());

                    //Add assigned variables to transaction list
                    transactions.add(new Transaction(date,time,description,vendor,amount));
                }
                //handle exception
                catch(Exception e){
                    System.out.println();
                }
            }
            //handle IOException
        } catch (IOException e) {
            //display error text
            System.out.println("You done did it");
        }
        //return list transactions
        return transactions;
    }

    //method to print transactions
    public static void printTransactions(List<Transaction> transactions) {

        //sort transactions using Comparator.comparing(rules for comparing) comparing the dates in Transaction object, then comparing the time in Transaction object .reversed() for newest transactions at the top
        transactions.sort(Comparator.comparing(Transaction::getDate).thenComparing(Transaction::getTime).reversed());

        // DateTimeFormatter named dateFormat, set pattern for date to be formatted ("yyyy-MM-dd")
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // DateTimeFormatter named timeFormat, set pattern for time to be formatted ("HH:mm:ss")
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

        //Create header
        String header = String.format("\n%-12s| %-11s| %-60s| %-22s| %10s\n", "Date", "Time", "Description", "Vendor", "Amount");

        //Display header
        System.out.print(header);

        //assign Transaction object to t, loop through transactions
        for (Transaction t : transactions) {
            // Create string named formattedDate, get date from transaction and format with created format, store in variable formattedDate
            String formattedDate = t.getDate().format(dateFormat);
            // Create string named formatted Time, get time from transaction and format with created format, store in variable formattedTime
            String formattedTime = t.getTime().format(timeFormat);

            //Create a string storing formatted transactions
            String formatTrans = String.format("%-12s| %-11s| %-60s| %-22s|%10.2f\n",
                    formattedDate, formattedTime, t.getDescription(), t.getVendor(), t.getAmount());

            //display formatted transactions
            System.out.print(formatTrans);
        }
    }

    //Method to saveTransactions, taking in Transaction list named allTransactions
    public static void saveTransactions(List<Transaction> allTransactions) {

        //try\catch with resources, create BufferedWriter containing FileWriter, reading "transactions.csv"
        try (BufferedWriter buffWrite = new BufferedWriter(new FileWriter("transactions.csv"))) {
            //Create string for header
            String header = String.format("\n%-12s| %-11s| %-60s| %-22s| %10s\n", "Date", "Time", "Description", "Vendor", "Amount");
            //Write header to file
            buffWrite.write(header);

            // DateTimeFormatter named dateFormat, set pattern for date to be formatted ("yyyy-MM-dd")
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // DateTimeFormatter named timeFormat, set pattern for time to be formatted ("HH:mm:ss")
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

            //assign Transaction object to t, loop through transactions
            for (Transaction t : allTransactions) {
                // Create string named formattedDate, get date from transaction and format with created format, store in variable formattedDate
                String formattedDate = t.getDate().format(dateFormat);
                // Create string named formatted Time, get time from transaction and format with created format, store in variable formattedTime
                String formattedTime = t.getTime().format(timeFormat);

                //Create a string storing formatted transactions
                String formatTrans = String.format("%-12s| %-11s| %-60s| %-22s|%10.2f\n",
                        formattedDate, formattedTime, t.getDescription(), t.getVendor(), t.getAmount());
                //Write formatted transactions (formatTrans) to file
                buffWrite.write(formatTrans);
            }
            //Handle IOException
        } catch (IOException e) {
            //Display error message
            System.out.println("Error saving transactions to file.");
        }
    }
}
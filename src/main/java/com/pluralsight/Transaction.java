package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Transaction {

    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private double amount;


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


    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public static List<Transaction> readTransactions(String filePath){
        List<Transaction> transactions = new ArrayList<>();

        try(BufferedReader buffRead = new BufferedReader(new FileReader(filePath))){

            String line;
            while((line = buffRead.readLine()) != null){
                String[] transInfo = line.split("\\|");

                if(transInfo[0].equalsIgnoreCase("Date")){
                    continue;
                }

                try{
                    LocalDate date = LocalDate.parse(transInfo[0].trim());
                    LocalTime time = LocalTime.parse(transInfo[1].trim());
                    String description = transInfo[2].trim();
                    String vendor = transInfo[3].trim();
                    double amount = Double.parseDouble(transInfo[4].trim());

                    transactions.add(new Transaction(date,time,description,vendor,amount));
                }
                catch(Exception e){
                    System.out.println();
                }
            }
        } catch (IOException e) {
            System.out.println("You done did it");
        }

        return transactions;
    }

    public static void printTransactions(List<Transaction> transactions) {

        transactions.sort(Comparator.comparing(Transaction::getDate).thenComparing(Transaction::getTime).reversed());

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

        String header = ("\nDate\t\t| Time\t\t | Description\t\t\t\t\t\t\t\t\t\t\t\t   | Vendor\t\t\t\t   |   Amount\n");
        System.out.print(header);

        for (Transaction t : transactions) {
            String formattedDate = t.getDate().format(dateFormat);
            String formattedTime = t.getTime().format(timeFormat);

            String formatTrans = String.format("%-12s| %-11s| %-60s| %-22s|%10.2f\n",
                    formattedDate, formattedTime, t.getDescription(), t.getVendor(), t.getAmount());
            System.out.print(formatTrans);
        }

    }

    public static void saveTransactions(List<Transaction> allTransactions) {
        try (BufferedWriter buffWrite = new BufferedWriter(new FileWriter("transactions.csv"))) {
            String header = "Date\t\t| Time\t\t | Description\t\t\t\t\t\t\t\t\t\t\t\t   | Vendor\t\t\t\t   |   Amount\n";
            buffWrite.write(header);

            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

            for (Transaction t : allTransactions) {
                String formattedDate = t.getDate().format(dateFormat);
                String formattedTime = t.getTime().format(timeFormat);

                String formatTrans = String.format("%-12s| %-11s| %-60s| %-22s|%10.2f\n",
                        formattedDate, formattedTime, t.getDescription(), t.getVendor(), t.getAmount());
                buffWrite.write(formatTrans);
            }
        } catch (IOException e) {
            System.out.println("Error saving transactions to file.");
        }
    }


}
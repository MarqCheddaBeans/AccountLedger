package com.pluralsight;

import java.util.InputMismatchException;

import static com.pluralsight.LedgarApp.scan;

public class UserMenus {

    public static int displayMainMenu() {

        System.out.println("\n-----------------------------------");
        System.out.println("What would you like to do today?");
        System.out.println("-----------------------------------");
        System.out.println("""
                1) Add deposit
                2) Make a payment(Debit)
                3) Ledger
                4) Exit
                """);

        int choice = -1;

        while(true) {
            try {
                choice = scan.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("No. Try Again: ");
                scan.nextLine();
            }
        }

        return choice;
    }

    public static int ledgerMenu() {
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

        int input = -1;

        while(true) {
            try {
                input = scan.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("No. Try Again: ");
                scan.nextLine();
            }
        }

        return input;
    }

    public static int displayReportMenu() {

        System.out.println("\nReport: ");
        System.out.println("----------------------------------------------------");
        System.out.println("Please select how you would like to filter ledger");
        System.out.println("----------------------------------------------------");
        System.out.println("\n1) Month To Date\n2) Previous Month\n3) Year To Date\n4) Previous Year\n5) Search by Vendor\n6) Custom Search\n0) Back to Ledger page");

        int reportInput = -1;

        while(true) {
            try {
                reportInput = scan.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("No. Try Again: ");
                scan.nextLine();
            }
        }

        return reportInput;
    }

    public static void printFramedTitle() {
        String title = " MarqBuryComics ";
        int width = title.length() + 4;

        // Top border
        System.out.println("╔" + "═".repeat(width) + "╗");

        // Title line
        System.out.println("║  " + title + "  ║");

        // Bottom border
        System.out.println("╚" + "═".repeat(width) + "╝\n");
    }
}

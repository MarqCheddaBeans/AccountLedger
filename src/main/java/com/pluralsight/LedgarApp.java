package com.pluralsight;

import java.io.*;
import java.util.Scanner;

import static com.pluralsight.Features.*;
import static com.pluralsight.UserMenus.*;

public class LedgarApp {

    //Create scanner that is class wide
    public static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        //Create bool variable that will keep application running until user chooses to exit
        boolean mainActive = true;

        //Method to prompt user to login
        userLogin();

        while (true) {
            int choice = displayMainMenu();
            scan.nextLine(); // Hungry buffer

            switch (choice) {
                case 1:
                    userDeposit();
                    break;
                case 2:
                    userPayment();
                    break;
                case 3:

                    boolean ledgerMenuActive = true;

                    while (ledgerMenuActive) {

                        int input = ledgerMenu();

                        switch (input) {
                            case 1:
                                displayFullLedger();
                                break;
                            case 2:
                                sortByDeposits();
                                break;
                            case 3:
                                sortByPayments();
                                break;
                            case 4:

                                boolean reportMenuActive = true;

                                while (reportMenuActive) {

                                    int reportInput = displayReportMenu();

                                    switch (reportInput) {
                                        case 0:
                                            reportMenuActive = false;
                                            break;
                                        case 1:
                                            sortMonthToDate();
                                            break;
                                        case 2:
                                            sortPreviousMonth();
                                            break;
                                        case 3:
                                            sortYearToDate();
                                            break;
                                        case 4:
                                            sortPreviousYear();
                                            break;
                                        case 5:
                                            sortByVendor();
                                            break;
                                        case 6:
                                            scan.nextLine(); // if needed
                                            customSearch();
                                            break;
                                        default:
                                            System.out.println("Invalid option in report menu.");
                                    }
                                }
                                break;
                            case 5:
                                ledgerMenuActive = false;
                                break;
                            default:
                                System.out.println("Invalid option in ledger menu.");
                        }
                    }
                    break;
                case 4:
                    System.out.println("Closing Ledger Application");
                    break;
                default:
                    System.out.println("Invalid main menu option.");
            }
            // Exit main loop if user chose option 4
            if (choice == 4) {
                break;
            }
        }
        // place code in while loop to keep application running until exited by user, (Returns user to main menu instead of ending app)
//        while (true) {
//            //Method to display main menu
//            int choice = displayMainMenu();
//
//            //Hungry buffer
//            scan.nextLine();
//
//            //if statements to allow user to navigate through menu
//            //User chose to add deposit
//            if (choice == 1) {
//                //Method to allow user to add a deposit to ledger
//                userDeposit();
//            }
//
//            //User chose to add a payment
//            else if (choice == 2) {
//                //Method to allow user to add a payment to ledger
//                userPayment();
//            }
//                //User chose to display Ledger
//            if (choice == 3) {
//                //Created bool variable to return user to LedgerMenu after completing prompt until exited by user
//                boolean ledgerMenuActive = true;
//
//                //Place ledger menu in while loop while bool ledgerMenuActive is true(allows user to return to ledger menu instead of application auto closing)
//                while(ledgerMenuActive) {
//
//                    //Display ledger menu for user + prompt user for choice
//                    int input = ledgerMenu();
//
//                    //User chose to display full ledger
//                    if (input == 1) {
//                        //Method to display full ledger, sorting them newest at the top
//                        displayFullLedger();
//                    }
//
//                    //User chose to display only deposits from ledger
//                    else if (input == 2) {
//                        //Method to sort and display only deposits from transactions in ledger
//                        sortByDeposits();
//                    }
//
//                    //User chose to display only payments from ledger
//                    else if (input == 3) {
//                        //Method to sort and display only payments from transaction in ledger
//                        sortByPayments();
//                    }
//                    //User chose report menu for filtered ledger search
//                    else if (input == 4) {
//                        //Create bool variable to keep returning user to report menu until user exits or goes to previous menu
//                        boolean reportMenuActive = true;
//
//                        //Place report menu in loop to prevent application closing without user prompt
//                        while (reportMenuActive) {
//                            // A new screen that allows the user to run pre-defined reports
//                            int reportInput = displayReportMenu();
//
//                            // User chose to return to ledger menu
//                            if (reportInput == 0){
//                                //Change bool value to exit report menu loop, returning user to previous menu(Ledger menu)
//                                reportMenuActive = false;
//                            }
//                            //User chose to sort transaction month to date(past 30 days)
//                            else if (reportInput == 1) {
//                                //Method to sort and display transaction by month to date
//                                sortMonthToDate();
//                            }
//                            //User chose to sort transaction by previous month
//                            else if (reportInput == 2) {
//                                //Method to sort and display transactions by previous month
//                                sortPreviousMonth();
//                            }
//                            //User chose to sort transactions year to date (past 365 days)
//                            else if (reportInput == 3) {
//                                //Method to sort and display transaction by year to date
//                                sortYearToDate();
//                            }
//                            //User chose to sort transactions by previous year
//                            else if (reportInput == 4) {
//                                //Method to sort and display transactions by previous year
//                                sortPreviousYear();
//                            }
//                            //User chose to sort transactions by searched vendor
//                            else if (reportInput == 5) {
//                                //Method to sort and display transactions from user inputted vendors
//                                sortByVendor();
//                            }
//                            else if (reportInput == 6){
//                                scan.nextLine();
//                                customSearch();
//                            }
//                        }
//                    }
//                    //User chose to return to main menu/ homepage
//                    else if (input == 5) {
//                        //Change ledgerMenuActive to false, exiting ledger menu loop and sending user to previous menu(Main menu)
//                        ledgerMenuActive = false;
//                    }
//                }
//            }
//                //User chose to close application
//                else if (choice == 4) {
//                    //Display text to user stating ledger app is closing
//                    System.out.println("Closing Ledger Application");
//                    //break = Exit Main menu loop, ending application
//                    break;
//                }
//        }
    }

}

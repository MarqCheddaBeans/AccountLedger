package com.pluralsight;

import java.io.*;
import java.util.Scanner;

import static com.pluralsight.Features.*;
import static com.pluralsight.UserMenus.*;

public class LedgarApp {

    //Create scanner that is class wide
    public static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {

        //Nice framed title
        printFramedTitle();

        //Method to prompt user to login
        userLogin();

        //Main loop to run application menu until user exits
        while (true) {

            //Display main menu and get users choice
            int choice = displayMainMenu();

            // Hungry buffer
            scan.nextLine();

            //Use switch case to handle users main menu input
            switch (choice) {
                case 1:
                    //Allow user to deposit if chose 1
                    userDeposit();
                    break;

                case 2:
                    //Allow user to add payment if chose 2
                    userPayment();
                    break;

                case 3:

                    //If user chose 3, create bool to keep on ledger menu
                    boolean ledgerMenuActive = true;

                    //keep ledger menu active until user exits
                    while (ledgerMenuActive) {

                        //display ledger menu and get user input
                        int input = ledgerMenu();

                        //Handle user input from ledger menu with switch case
                        switch (input) {
                            case 1:
                                //Display full ledger
                                displayFullLedger();
                                break;

                            case 2:
                                //Display only deposits
                                sortByDeposits();
                                break;

                            case 3:
                                //Display only payments
                                sortByPayments();
                                break;

                            case 4:
                                //Create bool to keep report menu active
                                boolean reportMenuActive = true;

                                //while loop keeping report menu active
                                while (reportMenuActive) {

                                    //Display report menu and get user input
                                    int reportInput = displayReportMenu();

                                    //Switch case to handle user report menu input
                                    switch (reportInput) {
                                        case 0:
                                            //User chose to return to previous menu, close report menu
                                            reportMenuActive = false;
                                            break;

                                        case 1:
                                            //sort transactions by month to date
                                            sortMonthToDate();
                                            break;

                                        case 2:
                                            //sort transactions by previous month
                                            sortPreviousMonth();
                                            break;

                                        case 3:
                                            //sort transactions by year to date
                                            sortYearToDate();
                                            break;

                                        case 4:
                                            //sort transactions by previous year
                                            sortPreviousYear();
                                            break;

                                        case 5:
                                            //sort transactions by user inputted vendor
                                            sortByVendor();
                                            break;

                                        case 6:
                                            //hungry buffer
                                            scan.nextLine();
                                            //allow user to sort by custom input
                                            customSearch();
                                            break;

                                        default:
                                            //Handles invalid input in reports menu
                                            System.out.println("Invalid option in report menu.");
                                    }
                                }
                                break;

                            case 5:
                                //User chose to return to previous screen, close ledger menu
                                ledgerMenuActive = false;
                                break;

                            default:
                                //Handle invalid input in ledger menu
                                System.out.println("Invalid option in ledger menu.");
                        }
                    }
                    break;

                case 4:
                    //user chose to exit application
                    System.out.println("Closing Ledger Application");
                    break;

                default:
                    //Handles invalid input in main menu
                    System.out.println("Invalid main menu option.");
            }

            // Exit main loop if user chose option 4, ending application
            if (choice == 4) {
                break;
            }
        }
    }
}

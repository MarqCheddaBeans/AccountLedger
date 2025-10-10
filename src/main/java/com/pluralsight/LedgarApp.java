package com.pluralsight;

import java.util.Scanner;

public class LedgarApp {

    //Create scanner that is class wide
    public static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {

        userLogin();


    }

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
        System.out.print("\nPassword: ");
        String password = scan.nextLine();

        while(!password.equals(validPass)){
            System.out.println("Invalid password. Try again!");
            System.out.print("Password: ");
            password = scan.nextLine();
        }
    }
}

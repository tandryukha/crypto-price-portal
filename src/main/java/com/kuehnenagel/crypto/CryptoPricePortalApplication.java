package com.kuehnenagel.crypto;


import java.util.Scanner;

public class CryptoPricePortalApplication {

    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        while(true) {
            System.out.println("Ready for a new command sir.");

            String input = userInput.nextLine();
            System.out.println("input is '" + input + "'");

            if (!input.isBlank()) {
                System.out.println("Processing input ...");
                // Handle input
            }
        }
    }

}

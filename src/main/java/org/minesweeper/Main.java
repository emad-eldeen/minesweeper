package org.minesweeper;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Minesweeper!");
        System.out.println("How many mines do you want on the field?");
        Scanner scanner = new Scanner(System.in);
        boolean minesNumberIsValid = false;
        int minesNumber = 0;
        while (!minesNumberIsValid) {
            try {
                minesNumber = Integer.parseInt(scanner.nextLine());
                minesNumberIsValid = true;
            } catch (NumberFormatException e) {
                System.out.println("Please insert a valid number");
            }
        }
        Game mineSweeperGame = new Game(9, minesNumber);
        mineSweeperGame.checkNeighboring();
        System.out.println("The mines field was generated:");
        mineSweeperGame.printNeighboringWithUserGuess();

        boolean inputIsValid;
        boolean playerWon = false;
        while (!playerWon) {
            System.out.print("Set/delete mines marks (x and y coordinates):");
            String userInput = scanner.nextLine();
            int[] coordinates;
            try {
                coordinates = UserInputParser.parseCoordinates(userInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input!");
                continue;
            }
            inputIsValid = mineSweeperGame.setUserGuess(coordinates[0], coordinates[1]);
            if (!inputIsValid) {
                System.out.println("Invalid input!");
                continue;
            }
            playerWon = mineSweeperGame.doesPlayerWin();
            mineSweeperGame.printNeighboringWithUserGuess();
        }
        System.out.println("Congratulations! You found all the mines!");
    }
}
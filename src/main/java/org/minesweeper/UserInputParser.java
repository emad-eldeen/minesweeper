package org.minesweeper;

public class UserInputParser {
    public static int[] parseCoordinates(String userInput) {
        String[] userInputArr = userInput.split(" ");
        if (userInputArr.length != 2) {
            throw new IllegalArgumentException("Wrong coordinates");
        } else {
            int[] coordinates = new int[2];
            // indexes in the game filed start from zero instead of one
            coordinates[0] = Integer.parseInt(userInputArr[1]) - 1;
            coordinates[1] = Integer.parseInt(userInputArr[0]) - 1;
            return coordinates;
        }
    }
}

package org.minesweeper;
import java.util.Arrays;
        import java.util.Random;
        import java.util.stream.Collectors;
        import java.util.stream.Stream;

public class Game {
    private static final char MINE_CHAR = 'X';
    private static final char EMPTY_CHAR = '.';
    private static final char GUESS_CHAR = '*';
    private final int fieldDimension;
    private final char[][] field;
    // an array that contains in each cell the number of mines around the same cell in the "field" array
    private final char[][] neighboring;
    // a clone of the "neighboring" array where the player guess is going to be added. before any guesses, it is an exact copy of "neighboring" array
    private char[][] neighboringWithUserGuess;
    private final int minesNumber;
    private int guessedCellsNumber = 0;

    Game(int fieldDimension, int minesNumber) {
        this.fieldDimension = fieldDimension;
        this.field = new char[fieldDimension][fieldDimension];
        this.neighboring = new char[fieldDimension][fieldDimension];
        this.minesNumber = minesNumber;
        initiateField();
    }

    private void initiateField() {
        for (char[] row : field) {
            Arrays.fill(row, EMPTY_CHAR);
        }
        placeMines();
    }

    private void placeMines() {
        Random random = new Random();
        int currentMinesNumber = 0;
        while (currentMinesNumber < minesNumber) {
            // get a random row
            int row = random.nextInt(fieldDimension);
            // and a random column
            int column = random.nextInt(fieldDimension);
            // if it does not already contain a mine
            if (field[row][column] != MINE_CHAR) {
                // set mine
                field[row][column] = MINE_CHAR;
                currentMinesNumber++;
            }
        }
    }

    /**
     *
     */
    public void checkNeighboring() {
        for (char[] row : neighboring) {
            Arrays.fill(row, EMPTY_CHAR);
        }
        for (int row = 0; row < fieldDimension; row++) {
            for (int column = 0; column < fieldDimension; column++) {
                if (field[row][column] == MINE_CHAR) {
                    continue;
                }
                neighboring[row][column] = getNumberOfMinesAroundCellAsChar(row, column);
            }
        }
        // create a clone of the neighboring array
        neighboringWithUserGuess = Arrays.stream(neighboring).map(char[]::clone).toArray(char[][]::new);
    }

    private char getNumberOfMinesAroundCellAsChar(int rowIndex, int columnIndex) {
        int numberOfMines = 0;
        int rowStart = Math.max(rowIndex - 1, 0);
        int rowFinish = Math.min(rowIndex + 1, fieldDimension - 1);
        int colStart = Math.max(columnIndex - 1, 0);
        int colFinish = Math.min(columnIndex + 1, fieldDimension - 1);
        for (int curRow = rowStart; curRow <= rowFinish; curRow++) {
            for (int curCol = colStart; curCol <= colFinish; curCol++) {
                // cell of interest
                if (curRow == rowIndex && curCol == columnIndex) {
                    continue;
                }
                if (field[curRow][curCol] == MINE_CHAR) {
                    numberOfMines++;
                }
            }
        }
        if (numberOfMines == 0) {
            return EMPTY_CHAR;
        } else {
            return Character.forDigit(numberOfMines, 10);
        }
    }

    private void printFieldRow(int rowIndex, char[][] field) {
        String row = Stream.iterate(0, x -> x + 1)
                .limit(fieldDimension)
                .map(x -> String.valueOf(field[rowIndex][x]))
                .collect(Collectors.joining(""));
        System.out.printf("%d|%s|%n", rowIndex+1, row);
    }

    private void printField(char[][] field) {
        String firstRow = Stream.iterate(1, n -> n + 1)
                .limit(fieldDimension)
                .map(String::valueOf)
                .collect(Collectors.joining("", " |", "|"));
        System.out.println(firstRow);
        String dashRow = Stream.iterate(1, n -> n + 1)
                .limit(fieldDimension)
                .map(x -> "-")
                .collect(Collectors.joining("", "-|", "|"));
        System.out.println(dashRow);
        for (int i = 0; i < fieldDimension; i++) {
            printFieldRow(i, field);
        }
        System.out.println(dashRow);
    }

    public void printNeighboringWithUserGuess() {
        printField(neighboringWithUserGuess);
    }

    public boolean setUserGuess(int row, int col) {
        if (!userGuessIsValid(row, col)) {
            return false;
        } else {
            markUnmarkMine(row, col);
            return true;
        }
    }

    /**
     * checks the provided cell coordinates and checks it as a guess if it is empty. otherwise, it unchecks the cell
     * @param row index of the row
     * @param col index of the column
     */
    private void markUnmarkMine(int row, int col) {
        char cellValue = neighboringWithUserGuess[row][col];
        char replacingChar;
        if (cellValue == EMPTY_CHAR) {
            replacingChar = GUESS_CHAR;
            guessedCellsNumber++;
        } else {
            replacingChar = EMPTY_CHAR;
            guessedCellsNumber--;
        }
        neighboringWithUserGuess[row][col] = replacingChar;
    }

    // whether the player already won the game by guessing all mines locations
    public boolean doesPlayerWin() {
        if (guessedCellsNumber == minesNumber) {
            for (int row = 0; row < fieldDimension; row++) {
                for (int col = 0; col < fieldDimension; col++) {
                    if (neighboringWithUserGuess[row][col] == GUESS_CHAR) {
                        if (field[row][col] != MINE_CHAR) {
                            return false;
                        }
                    }
                }
            }
            // here, all guesses match mines
            return true;
        }
        return false;
    }

    // whether indexes are valid
    private boolean userGuessIsValid(int row, int col) {
        // row and column indexes are inside the filed
        if (row >= fieldDimension || col >= fieldDimension) {
            return false;
        }
        return neighboringWithUserGuess[row][col] == EMPTY_CHAR ||
                neighboringWithUserGuess[row][col] == GUESS_CHAR;
    }


}

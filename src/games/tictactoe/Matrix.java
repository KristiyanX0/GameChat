package games.tictactoe;

public class Matrix {
    public static final int MATRIX_SIZE = 3;
    private Character[][] matrix;

    public Matrix() {
        matrix = new Character[MATRIX_SIZE][MATRIX_SIZE];
    }
    public void set(Character c, int row, int column) {
        if (row < MATRIX_SIZE && column < MATRIX_SIZE) {
            matrix[row][column] = c;
        }
    }
    public Character get(int row, int column) {
        return matrix[row][column];
    }
}

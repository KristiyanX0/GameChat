package util;

public class Matrix {
    public final int size;
    private Character[][] matrix;
    public Matrix(int size1) {
        this.size = size1;
        matrix = new Character[size1][size1];
        fill(matrix, '-');
    }

    private void fill(Character[][] matrix, Character x) {
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                matrix[i][j] = (char) x;
            }
        }
    }

    public boolean validArguments(int row, int column) {
        return row < size && column < size;
    }
    public void set(Character c, int row, int column) {
        if (validArguments(row,column)) {
            matrix[row][column] = c;
        }
    }
    public Character get(int row, int column) {
        if (validArguments(row,column)) {
            return matrix[row][column];
        }
        return null;
    }

    public int size() {
        return size;
    }
}

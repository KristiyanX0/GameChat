package games.tictactoe;

import games.Game;
import util.Pair;

import java.security.KeyPair;
import java.util.List;

public class TicTacToe extends Game {
    Matrix matrix;
    public final Character X = 'X';
    public final Character O = 'O';
    Pair<Integer,Integer> lastMove;
    public TicTacToe() {
        matrix = new Matrix();
    }

    public boolean put(Character c, int row, int col) {
        if (!matrix.get(row,col).equals(X) && !matrix.get(row,col).equals(O)) {
            matrix.set(c,row,col);
            lastMove = new Pair<>(row, col);
            return true;
        }
        return false;
    }

    public Pair<Character,List<Pair<Integer,Integer>>> existWinningSequence() {
        for (int i = 0; i < Matrix.MATRIX_SIZE; i++) {

        }
        for (int i = 0; i < Matrix.MATRIX_SIZE; i++) {
//            if (matrix) {
//
//            }
        }
        return null;
    }

    public boolean hasEnded() {
        return !existWinningSequence().getSecond().isEmpty();
    }
}

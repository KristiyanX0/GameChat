package games.tictactoe;

import games.Game;
import util.Pair;

import java.util.ArrayList;
import java.util.List;

public class TicTacToe implements Game {
    Matrix matrix;
    public final Character X = 'X';
    public final Character O = 'O';
    Pair<Character,Pair<Integer,Integer>> lastMove;
    public TicTacToe() {
        matrix = new Matrix();
    }

    public boolean put(Character c, int row, int col) {
        if (!matrix.get(row,col).equals(X) && !matrix.get(row,col).equals(O)) {
            matrix.set(c,row,col);
            lastMove = new Pair<>(c,new Pair<>(row, col));
            return true;
        }
        return false;
    }

    public Pair<Character,List<Pair<Integer,Integer>>> existWinningSequence() {
        for (int i = 0; i < Matrix.MATRIX_SIZE; i++) {
            if (matrix.get(0,i) == matrix.get(1,i) && matrix.get(1,i) == matrix.get(2,i)) {
                ArrayList<Pair<Integer, Integer>> list = new ArrayList<>();
                list.add(new Pair<>(0,i));
                list.add(new Pair<>(1,i));
                list.add(new Pair<>(2,i));
                return new Pair<>(matrix.get(0,i), list);
            }
        }
        for (int i = 0; i < Matrix.MATRIX_SIZE; i++) {
            if (matrix.get(i,0) == matrix.get(i,1) && matrix.get(i,1) == matrix.get(i,2)) {
                ArrayList<Pair<Integer, Integer>> list = new ArrayList<>();
                list.add(new Pair<>(i,0));
                list.add(new Pair<>(i,1));
                list.add(new Pair<>(i,2));
                return new Pair<>(matrix.get(i,0), list);
            }
        }
        if (matrix.get(0,0) == matrix.get(1,1) && matrix.get(1,1) == matrix.get(2,2)) {
            ArrayList<Pair<Integer, Integer>> list = new ArrayList<>();
            list.add(new Pair<>(0,0));
            list.add(new Pair<>(1,1));
            list.add(new Pair<>(2,2));
            return new Pair<>(matrix.get(0,0), list);
        }
        if (matrix.get(0,0) == matrix.get(1,1) && matrix.get(1,1) == matrix.get(2,2)) {
            ArrayList<Pair<Integer, Integer>> list = new ArrayList<>();
            list.add(new Pair<>(2,0));
            list.add(new Pair<>(1,1));
            list.add(new Pair<>(0,2));
            return new Pair<>(matrix.get(2,0), list);
        }
        return null;
    }

    public boolean hasEnded() {
        return !(existWinningSequence() == null);
    }
}

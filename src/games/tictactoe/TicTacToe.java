package games.tictactoe;

import util.Matrix;
import util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TicTacToe {
    public static final int MATRIX_SIZE = 3;
    public final Character X = 'X';
    public final Character O = 'O';
    Matrix matrix;
    private int movesCount;
    Pair<Character, String> player1;
    Pair<Character, String> player2;
    Pair<Character, Pair<Integer, Integer>> lastMove;

    TicTacToe(String username1, String username2) {
        this.player1 = new Pair<>(X, username1);
        this.player2 = new Pair<>(O, username2);
        movesCount = 0;
        lastMove = new Pair<>(O, new Pair<>(-1, -1));
        matrix = new Matrix(MATRIX_SIZE);
    }

    /**
     * @param c   X or O
     * @param row
     * @param col
     * @return If on the specific square we have put a 'c'
     */
    public boolean put(Character c, int row, int col) {
        // LEFT FOR DEBUGGING PURPOSES
        System.out.println((!X.equals(matrix.get(row, col)) && !O.equals(matrix.get(row, col))) + " - First ");
        System.out.println((!lastMove.first().equals(c)) + " - Second ");
        if (!matrix.get(row, col).equals(X) && !matrix.get(row, col).equals(O) &&
                !lastMove.first().equals(c)) {
            matrix.set(c, row, col);
            lastMove = new Pair<>(c, new Pair<>(row, col));
            movesCount++;
            return true;
        }
        System.out.println(lastMove);
        System.out.println("modeCount: " + movesCount);
        return false;
    }

    public boolean hit(String player, int row, int col) {
        boolean temp = false;
        if (player.equals(player1.second())) {
            temp = put(player1.first(), row, col);
        } else if (player.equals(player2.second())) {
            temp = put(player2.first(), row, col);
        }
        if (hasEnded()) {
            temp = false;
        }
        return temp;
    }

    private Pair<Character, List<Pair<Integer, Integer>>> winningSequence() {
        Pair<Character, List<Pair<Integer, Integer>>> temp = null;
        for (int i = 0; i < MATRIX_SIZE; i++) {
            if (matrix.get(0, i) == matrix.get(1, i) && matrix.get(1, i) == matrix.get(2, i)
                    && matrix.get(0, i) != '-') {
                ArrayList<Pair<Integer, Integer>> list = new ArrayList<>();
                list.add(new Pair<>(0, i));
                list.add(new Pair<>(1, i));
                list.add(new Pair<>(2, i));
                temp = new Pair<>(matrix.get(0, i), list);
            }
        }
        for (int i = 0; i < MATRIX_SIZE; i++) {
            if (matrix.get(i, 0) == matrix.get(i, 1) && matrix.get(i, 1) == matrix.get(i, 2)
                    && matrix.get(i, 0) != '-') {
                ArrayList<Pair<Integer, Integer>> list = new ArrayList<>();
                list.add(new Pair<>(i, 0));
                list.add(new Pair<>(i, 1));
                list.add(new Pair<>(i, 2));
                temp = new Pair<>(matrix.get(i, 0), list);
            }
        }
        if (matrix.get(0, 0) == matrix.get(1, 1) && matrix.get(1, 1) == matrix.get(2, 2)
                && matrix.get(0, 0) != '-') {
            ArrayList<Pair<Integer, Integer>> list = new ArrayList<>();
            list.add(new Pair<>(0, 0));
            list.add(new Pair<>(1, 1));
            list.add(new Pair<>(2, 2));
            temp = new Pair<>(matrix.get(0, 0), list);
        }
        if (matrix.get(2, 0) == matrix.get(1, 1) && matrix.get(1, 1) == matrix.get(0, 2)
                && matrix.get(2, 0) != '-') {
            ArrayList<Pair<Integer, Integer>> list = new ArrayList<>();
            list.add(new Pair<>(2, 0));
            list.add(new Pair<>(1, 1));
            list.add(new Pair<>(0, 2));
            temp = new Pair<>(matrix.get(2, 0), list);
        }
        return temp;
    }

    private boolean existWinningSequence() {
        return !(winningSequence() == null);
    }

    public Character getPlayerSign(String player) {
        return (player.equals(player1.second())) ? player1.first() : player2.first();
    }

    public String getWinner() {
        if (hasEnded()) {
            List<Pair<Integer, Integer>> list = winningSequence().second();
            if (list == null) {
                return "DRAW";
            }
            else if (matrix.get(list.get(0).first(), list.get(0).second()).equals(player1.first())) {
                return player1.second();
            } else if (matrix.get(list.get(0).first(), list.get(0).second()).equals(player2.first())) {
                return player2.second();
            }
        }
        return null;
    }

    public boolean hasEnded() {
        return (movesCount >= 9) || existWinningSequence();
    }

    public String turn() {
        if (lastMove.first().equals(player2.first())) {
            return player1.second();
        }
        return player2.second();
    }

    public boolean turn(String name) {
        return this.turn().equals(name);
    }

    Matrix getMatrix() {
        return matrix;
    }
}

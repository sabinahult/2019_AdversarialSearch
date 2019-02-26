import java.util.List;

/**
 * Author: Sabina Hult
 * Implementation of the H-MINIMAX algorithm with alpha-beta pruning as well as cutoff and an evaluation function
 * as it is given in RN p. 171
 */
@SuppressWarnings("Duplicates")
public class HMINIMAX {

    public static Position decision(GameState s) {
        Position move = null;
        int max_value = Integer.MIN_VALUE;

        for(Position p : s.legalMoves()) {
            int val = minValue(result(s, p), Integer.MIN_VALUE, Integer.MAX_VALUE, 1);

            if(val > max_value) {
                max_value = val;
                move = p;
            }
        }

        return move;
    }

    private static GameState result(GameState s, Position p) {
        GameState n = new GameState(s.getBoard(), s.getPlayerInTurn());
        n.insertToken(p);
        n.changePlayer();

        return n;
    }

    private static int minValue(GameState s, int alpha, int beta, int d) {
        if(cutoffTest(s, d)) return eval(s);

        int v = Integer.MAX_VALUE;
        for(Position p : s.legalMoves()) {
            v = Math.max(v, maxValue(result(s, p), alpha, beta, d++));
            if(v <= alpha) return v;
            beta = beta < v ? beta : v;
        }

        return v;
    }

    private static int maxValue(GameState s, int alpha, int beta, int d) {
        if(cutoffTest(s, d)) return eval(s);

        int v = Integer.MIN_VALUE;
        for(Position p : s.legalMoves()) {
            v = Math.min(v, minValue(result(s, p), alpha, beta, d++));
            if(v >= beta) return v;
            alpha = alpha > v ? alpha : v;
        }


        return v;
    }

    private static boolean cutoffTest(GameState s, int d) {
        if(terminalTest(s)) return true;
        return d >= 6;
    }

    private static boolean terminalTest(GameState s) {
        return s.isFinished();
    }


    /**
     * Evaluation based on how many "good" positions, ie. corner and edge positions, each player holds
     * at this current state. The higher the number, the more good positions black holds. This should
     * work because max will maximize and min will minimize. What I don't know if whether this is a
     * good prediction for winning...
     */
    private static int eval(GameState s) {
        // if the state at depth d is actually a terminal state, then return utility
        if(terminalTest(s)) return utility(s);

        // else evaluate good positions for black at this state
        int[][] board = s.getBoard();
        int n = board.length;
        int corner = 5; // just guessing at a weight for corner positions
        int edge = 3; // just guessing at a weight for edge positions

        // vals negated because b = 1 and w = 2, but we want w to be less than b, however the skew in the results will
        // probably have some effect on the outcome...
        int eval = (corner * -board[0][0]) + (corner * -board[0][n-1]) + (corner * -board[n-1][0]) + (corner * -board[n-1][n-1]);

        // add values for top and bottom row
        for(int i = 0; i < n-1; i++) {
            eval += edge * -board[i][0];
            eval += edge * -board[i][n-1];
        }

        // add vals for left and right column
        for(int j = 0; j < n-1; j++) {
            eval += edge * -board[0][j];
            eval += edge * -board[n-1][j];
        }

        return eval;
    }

    private static int utility(GameState s) {
        int[] tokens = s.countTokens();

        if(tokens[0] == tokens[1]) return 0;
        else return tokens[0] > tokens[1] ? 1 : -1;
    }
}

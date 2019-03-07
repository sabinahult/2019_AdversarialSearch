/**
 * @author Sabina Hult
 * @version 7.3.2019
 * Implementation of the H-MINIMAX algorithm with alpha-beta pruning as well as
 * cutoff and an evaluation function.
 */
@SuppressWarnings("Duplicates")
public class HMINIMAX {
    private static final int CUTOFF_DEPTH = 6;
    // to make the algorithm independent on whether SmarterAI is player 1 (black) or player 2 (white)
    private static int thisPlayer;

    public static Position decision(GameState s) {
        thisPlayer = s.getPlayerInTurn();
        Position move = null;
        int max_value = Integer.MIN_VALUE;

        for(Position p : s.legalMoves()) {
            int val = minValue(result(s, p), Integer.MIN_VALUE, Integer.MAX_VALUE, 1);

            if(val >= max_value) {
                max_value = val;
                move = p;
            }
        }

        return move;
    }

    private static GameState result(GameState s, Position p) {
        GameState n = new GameState(s.getBoard(), s.getPlayerInTurn());
        n.insertToken(p);
        return n;
    }

    private static int maxValue(GameState s, int alpha, int beta, int d) {
        if(cutoffTest(s, d)) return eval(s);
        d++;

        int v = Integer.MIN_VALUE;
        for(Position p : s.legalMoves()) {
            v = Math.max(v, minValue(result(s, p), alpha, beta, d));
            if(v >= beta) return v;
            alpha = alpha > v ? alpha : v;
        }
        return v;
    }

    private static int minValue(GameState s, int alpha, int beta, int d) {
        if(cutoffTest(s, d)) return eval(s);
        d++;

        int v = Integer.MAX_VALUE;
        for(Position p : s.legalMoves()) {
            v = Math.min(v, maxValue(result(s, p), alpha, beta, d));
            if(v <= alpha) return v;
            beta = beta < v ? beta : v;
        }

        return v;
    }

    private static boolean cutoffTest(GameState s, int d) {
        if(terminalTest(s)) return true;
        return d >= CUTOFF_DEPTH;
    }

    private static boolean terminalTest(GameState s) {
        return s.isFinished();
    }

    /**
     * Evaluation based on how many "good" positions, ie. corner and edge positions,
     * each player holds at this current state.
     * @param s the games tate to evaluate
     * @return 1 if this player has more valuable positions, -1 if the other player
     * holds more valuable positions and 0 if it's equal
     */
    private static int eval(GameState s) {
        // if the state at depth d is actually a terminal state, then return utility
        if(terminalTest(s)) return utility(s);

        int[][] board = s.getBoard();
        int n = board.length;
        int corner = 4; // just guessing at a weight for corner positions
        int edge = 2; // just guessing at a weight for edge positions

        // corner positions, positive if thisPlayer, negative if other, nothing if blank
        int eval = 0;
        if(board[0][0] != 0) eval += board[0][0] == thisPlayer ? corner : -corner;
        if(board[0][n-1] != 0) eval += board[0][n-1] == thisPlayer ? corner : -corner;
        if(board[n-1][0] != 0) eval += board[n-1][0] == thisPlayer ? corner : -corner;
        if(board[n-1][n-1] != 0) eval += board[n-1][n-1] == thisPlayer ? corner : -corner;

        // add values for left and right column, excl. corners
        for(int i = 1; i < n-2; i++) {
            if(board[i][0] != 0) eval += board[i][0] == thisPlayer ? edge : -edge;
            if(board[i][n-1] != 0)eval += board[i][n-1] == thisPlayer ? edge : -edge;
        }

        // add vals for top and bottom row, excl. corners
        for(int j = 1; j < n-2; j++) {
            if(board[0][j] != 0) eval += board[0][j] == thisPlayer ? edge : -edge;
            if(board[n-1][j] != 0) eval += board[n-1][j] == thisPlayer ? edge : -edge;
        }

        if(eval > 0) return 1; // this player has more valuable positions
        else if(eval < 0) return -1; // other player has more valuable positions

        return 0;
    }

    /**
     * Returns a utility value based on the winner at this game state
     * @param s terminal game state
     * @return 1 if this player is the winner, -1 if other player wins and 0 if it's a tie
     */
    private static int utility(GameState s) {
        int other = thisPlayer == 1 ? 2 : 1;
        int[] tokens = s.countTokens();

        if(tokens[thisPlayer-1] == tokens[other-1]) return 0;
        else return tokens[thisPlayer-1] > tokens[other-1] ? 1 : -1;
    }
}

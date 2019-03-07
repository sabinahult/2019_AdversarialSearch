/**
 * @author Sabina Hult
 * @version 7.3.2019
 * Implementation of the MINIMAX algorithm as it is given in the pseudocode in RN p. 166
 */
public class MINIMAX {
    private static int thisPlayer;
    public static Position decision(GameState s) {
        thisPlayer = s.getPlayerInTurn();

        Position move = null;
        int max_value = Integer.MIN_VALUE;

        for(Position p : s.legalMoves()) {
            int val = minValue(result(s, p));

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

    private static int maxValue(GameState s) {
        if(terminalTest(s)) return utility(s);

        int v = Integer.MIN_VALUE;
        for(Position p : s.legalMoves()) {
            v = Math.max(v, minValue(result(s, p)));
        }

        return v;
    }

    private static int minValue(GameState s) {
        if(terminalTest(s)) return utility(s);

        int v = Integer.MAX_VALUE;
        for(Position p : s.legalMoves()) {
            v = Math.min(v, maxValue(result(s, p)));
        }

        return v;
    }

    private static boolean terminalTest(GameState s) {
        return s.isFinished();
    }

    public static int utility(GameState s) {
        int other = thisPlayer == 1 ? 2 : 1;
        int[] tokens = s.countTokens();

        if(tokens[thisPlayer -1] == tokens[other-1]) return 0;
        else return tokens[thisPlayer -1] > tokens[other-1] ? 1 : -1;
    }
}

import java.util.ArrayList;
import java.util.List;

/**
 * An attempt at a smarter AI using the MINIMAX algorithm for move decisions.
 */

public class SmarterAI implements IOthelloAI {
    private List<Double> times;

    public SmarterAI() {
        times = new ArrayList<>();
    }

    /**
     * Returns the best move given by the MINIMAX algorithm
     */
    public Position decideMove(GameState s) {
        double start = System.currentTimeMillis();
        //Position move = MINIMAX.decision(s);
        //Position move = MINIMAXAB.alphaBetaSearch(s);
        Position move = HMINIMAX.decision(s);
        times.add(System.currentTimeMillis()-start);

        return move;
    }

    /**
     * Calculates the average over all decideMove execution times, as well as the max.
     * @return an array of {avg, max}
     */
    public double[] getTimes() {
        double avg = times.stream().mapToDouble(Number::doubleValue).average().getAsDouble();
        double max = times.stream().mapToDouble(Number::doubleValue).max().getAsDouble();

        return new double[]{avg, max};
    }
}

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sabina Hult
 * @version 7.3.2019
 * An attempt at a smarter AI (than DumAI) using either the MINIMAX algorithm, MINIMAX with alpha-beta
 * pruning or H-MINIMAX for move decisions.
 */
public class SmarterAI implements IOthelloAI {
    // for calculating avg and max duration of decision
    private List<Double> times;

    public SmarterAI() {
        times = new ArrayList<>();
    }

    /**
     * Calculates the move to make for the given game state.
     * @param s The current state of the game in which it should be the AI's turn.
     * @return the position for the best move to make at this current state
     */
    public Position decideMove(GameState s) {
        System.out.print("Deciding move...");

        double start = System.currentTimeMillis();
        //Position move = MINIMAX.decision(s);
        //Position move = MINIMAXAB.alphaBetaSearch(s);
        Position move = HMINIMAX.decision(s);
        times.add(System.currentTimeMillis()-start);

        System.out.println("Done");
        return move;
    }

    /**
     * Calculates the average over all decideMove execution times, as well as the max.
     * @return an array of {avg, max} in doubles
     */
    public double[] getTimes() {
        double avg = times.stream().mapToDouble(Number::doubleValue).average().getAsDouble();
        double max = times.stream().mapToDouble(Number::doubleValue).max().getAsDouble();

        return new double[]{avg, max};
    }
}

import java.util.ArrayList;
import java.util.Random;

/**
 * A simple OthelloAI-implementation.
 * @author Sabina Hult
 * @version 7.3.2019
 */
public class RandomAI implements IOthelloAI {

    /**
     * @param s The current state of the game in which it should be the AI's turn.
     * @return a random legal move
     */
    public Position decideMove(GameState s) {
        ArrayList<Position> moves = s.legalMoves();

        if(!moves.isEmpty()) {
            // pick a random move
            Random r = new Random();
            return moves.get(r.nextInt(moves.size()));
        } else
            return new Position(-1,-1);
    }
}

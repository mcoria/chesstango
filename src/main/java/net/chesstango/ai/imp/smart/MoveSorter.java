package net.chesstango.ai.imp.smart;

import net.chesstango.board.moves.Move;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author Mauricio Coria
 */
public class MoveSorter {
    public Queue<Move> sortMoves(Iterable<Move> possibleMoves) {

        Queue<Move> queue = new PriorityQueue<Move>(new MoveComparator());

        possibleMoves.forEach(queue::add);

        return queue;
    }

    private static class MoveComparator implements Comparator<Move> {

        @Override
        public int compare(Move move1, Move move2) {
            int result = 0;


            if (move1.getTo().getValue() == null && move2.getTo().getValue() == null) {
                result = Math.abs(move1.getFrom().getValue().getMoveValue()) - Math.abs(move2.getFrom().getValue().getMoveValue());

            }else if(move1.getTo().getValue() == null && move2.getTo().getValue() != null){
                result =  -1;

            } else if(move1.getTo().getValue() != null && move2.getTo().getValue() == null){
                result =  1;

            } else  if (move1.getTo().getValue() != null && move2.getTo().getValue() != null) {
                result = Math.abs(move1.getTo().getValue().getPieceValue()) - Math.abs(move2.getTo().getValue().getPieceValue());

            }

            if(move1.equals(move2) && result != 0){
                throw new RuntimeException("move1.equals(move2) && result != 0");
            } else if (move2.equals(move1) && result != 0) {
                throw new RuntimeException("move2.equals(move1) && result != 0");
            }

            // Ambos son capturas o ambos no son capturas
            return  -result;
        }
    }

}

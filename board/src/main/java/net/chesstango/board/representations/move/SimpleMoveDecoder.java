package net.chesstango.board.representations.move;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;

/**
 * @author Mauricio Coria
 */
public class SimpleMoveDecoder {

    public Move decode(MoveContainerReader<? extends Move> possibleMoves, String moveStr) {
        for (Move move : possibleMoves) {
            String encodedMoveStr = move.coordinateEncoding();
            if (encodedMoveStr.equals(moveStr.toLowerCase())) {
                return move;
            }
        }
        return null;
    }

}

package net.chesstango.board.representations.move;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;

/**
 * @author Mauricio Coria
 */
public class SimpleMoveDecoder {

    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    public Move decode(MoveContainerReader possibleMoves, String moveStr) {
        for (Move move : possibleMoves) {
            String encodedMoveStr = simpleMoveEncoder.encode(move);
            if (encodedMoveStr.equals(moveStr.toLowerCase())) {
                return move;
            }
        }
        return null;
    }

}

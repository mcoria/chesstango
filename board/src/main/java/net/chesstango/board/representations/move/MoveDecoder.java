package net.chesstango.board.representations.move;

import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public class MoveDecoder {

    public Move decode(String moveStr, Iterable<Move> possibleMoves) {
        SANDecoder sanDecoder = new SANDecoder();
        LANDecoder lanDecoder = new LANDecoder();

        Move move = sanDecoder.decode(moveStr, possibleMoves);

        if (move == null) {
            move = lanDecoder.decode(moveStr, possibleMoves);
        }

        return move;
    }
}

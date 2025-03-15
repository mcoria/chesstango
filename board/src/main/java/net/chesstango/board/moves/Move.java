package net.chesstango.board.moves;

import net.chesstango.board.Game;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.MoveCommand;

/**
 * @author Mauricio Coria
 */
public interface Move extends MoveCommand {
    PiecePositioned getFrom();

    PiecePositioned getTo();

    /**
     * "move" is a bit field with the following meaning (bit 0 is the least significant bit)
     * bits                meaning
     * ===================================
     * 0,1,2               to file
     * 3,4,5               to row
     * 6,7,8               from file
     * 9,10,11             from row
     * 12,13,14            promotion piece
     * <p>
     * "promotion piece" is encoded as follows
     * none       0
     * knight     1
     * bishop     2
     * rook       3
     * queen      4
     *
     * @return
     */
    default short binaryEncoding() {
        Square fromSquare = getFrom().getSquare();

        Square toSquare = getTo().getSquare();

        return (short) (fromSquare.getBinaryEncodedFrom() | toSquare.getBinaryEncodedTo());
    }

    Cardinal getMoveDirection();

    boolean isQuiet();

    long getZobristHash(ChessPosition chessPosition);


    default boolean isLegalMove(LegalMoveFilter filter) {
        return filter.isLegalMove(this);
    }
}
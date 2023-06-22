package net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.movesgenerators.pseudo.strategies.BishopMoveGenerator;
import net.chesstango.board.position.BitBoardReader;
import net.chesstango.board.position.SquareBoardReader;

/**
 * @author Mauricio Coria
 */
public class CapturerByBishop extends CapturerByCardinals {
    public CapturerByBishop(SquareBoardReader squareBoardReader, BitBoardReader bitBoardReader, Color color) {
        super(squareBoardReader, bitBoardReader, color, BishopMoveGenerator.BISHOP_CARDINAL);
    }

    @Override
    protected long getThreatsInCardinalDirection(Square square, Cardinal cardinal) {
        return (cardinal.getPosiciones(square) & bitBoardReader.getPositions(color)) &
                ( bitBoardReader.getBishopPositions() | bitBoardReader.getQueenPositions() );
    }
}

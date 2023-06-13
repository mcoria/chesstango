package net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.movesgenerators.pseudo.strategies.RookMoveGenerator;
import net.chesstango.board.position.BitBoardReader;
import net.chesstango.board.position.SquareBoardReader;

/**
 * @author Mauricio Coria
 */
public class CapturerByRook extends CapturerByCardinals {
    public CapturerByRook(SquareBoardReader squareBoardReader, BitBoardReader bitBoardReader, Color color) {
        super(squareBoardReader, bitBoardReader, color, RookMoveGenerator.ROOK_CARDINAL, Piece.getRook(color));
    }

    @Override
    protected boolean thereIsCapturerInCardinalDirection(Square square, Cardinal cardinal) {
        long result =  (cardinal.getPosiciones(square) & bitBoardReader.getPositions(color)) &
                ( bitBoardReader.getRookPositions() | bitBoardReader.getQueenPositions() );

        return result != 0  ;
    }
}

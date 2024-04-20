package net.chesstango.board.moves.generators.legal.squarecapturers.bypiece;

import net.chesstango.board.Color;
import net.chesstango.board.moves.generators.pseudo.strategies.RookMoveGenerator;
import net.chesstango.board.position.BitBoardReader;
import net.chesstango.board.position.SquareBoardReader;

/**
 * @author Mauricio Coria
 */
public class CapturerByRook extends CapturerByCardinals {
    public CapturerByRook(SquareBoardReader squareBoardReader, BitBoardReader bitBoardReader, Color color) {
        super(squareBoardReader, bitBoardReader, color, RookMoveGenerator.ROOK_CARDINAL);
    }

    @Override
    protected long getCardinalThreats() {
        return  bitBoardReader.getPositions(color) & ( bitBoardReader.getRookPositions() | bitBoardReader.getQueenPositions() );
    }
}

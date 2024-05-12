package net.chesstango.board.moves.generators.legal.squarecapturers.bypiece;

import net.chesstango.board.Color;
import net.chesstango.board.moves.generators.pseudo.strategies.BishopMoveGenerator;
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
    protected long getCardinalThreats() {
        return bitBoardReader.getPositions(color) & ( bitBoardReader.getBishopPositions() | bitBoardReader.getQueenPositions() );
    }
}

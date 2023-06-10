package net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.movesgenerators.pseudo.strategies.BishopMoveGenerator;
import net.chesstango.board.position.SquareBoardReader;

/**
 * @author Mauricio Coria
 */
public class CapturerByBishop extends CapturerByCardinals {
    public CapturerByBishop(SquareBoardReader squareBoardReader, Color color) {
        super(squareBoardReader, color, BishopMoveGenerator.BISHOP_CARDINAL, Piece.getBishop(color));
    }
}

package net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.movesgenerators.pseudo.strategies.RookMoveGenerator;
import net.chesstango.board.position.SquareBoardReader;

/**
 * @author Mauricio Coria
 */
public class CapturerByRook extends CapturerByCardinals {
    public CapturerByRook(SquareBoardReader squareBoardReader, Color color) {
        super(squareBoardReader, color, RookMoveGenerator.ROOK_CARDINAL, Piece.getRook(color));
    }
}

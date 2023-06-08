package net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.movesgenerators.pseudo.strategies.RookMoveGenerator;
import net.chesstango.board.position.BoardReader;

/**
 * @author Mauricio Coria
 */
public class CapturerByRook extends CapturerByCardinals {
    public CapturerByRook(BoardReader boardReader, Color color) {
        super(boardReader, color, RookMoveGenerator.ROOK_CARDINAL, Piece.getRook(color));
    }
}

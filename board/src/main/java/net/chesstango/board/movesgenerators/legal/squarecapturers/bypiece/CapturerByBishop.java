package net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.movesgenerators.pseudo.strategies.BishopMoveGenerator;
import net.chesstango.board.position.BoardReader;

/**
 * @author Mauricio Coria
 */
public class CapturerByBishop extends CapturerByCardinals {
    public CapturerByBishop(BoardReader boardReader, Color color) {
        super(boardReader, color, BishopMoveGenerator.BISHOP_CARDINAL, Piece.getBishop(color));
    }
}

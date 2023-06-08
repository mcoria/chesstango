package net.chesstango.board.position.imp;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mauricio Coria
 *
 */
public class ChessPositionImpTest {

    private ChessPositionImp chessPosition;

    @BeforeEach
    public void setUp() {
        chessPosition = new ChessPositionImp();
    }

    @Test
    public void test_iterator() {
        ArrayBoard tablero = new ArrayBoard();

        tablero.setPiece(Square.a1, Piece.ROOK_WHITE);
        tablero.setPiece(Square.b7, Piece.PAWN_BLACK);
        tablero.setPiece(Square.b8, Piece.KNIGHT_BLACK);
        tablero.setPiece(Square.e1, Piece.KING_WHITE);
        tablero.setPiece(Square.e8, Piece.KING_BLACK);

        ColorBoard colorBoard = new ColorBoard();
        colorBoard.init(tablero);

        chessPosition.setPiecePlacement(tablero);
        chessPosition.setColorBoard(colorBoard);

        List<PiecePositioned> posicionesList = new ArrayList<PiecePositioned>();

        for (Iterator<PiecePositioned> iterator = chessPosition.iteratorAllPieces() ; iterator.hasNext();) {
            posicionesList.add(iterator.next());
        }


        assertTrue(posicionesList.contains(PiecePositioned.getPiecePositioned(Square.a1, Piece.ROOK_WHITE)));
        assertTrue(posicionesList.contains(PiecePositioned.getPiecePositioned(Square.b7, Piece.PAWN_BLACK)));
        assertTrue(posicionesList.contains(PiecePositioned.getPiecePositioned(Square.b8, Piece.KNIGHT_BLACK)));
        assertTrue(posicionesList.contains(PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE)));
        assertTrue(posicionesList.contains(PiecePositioned.getPiecePositioned(Square.e8, Piece.KING_BLACK)));
        assertFalse(posicionesList.contains(PiecePositioned.getPiecePositioned(Square.e3, null)));
        assertEquals(5, posicionesList.size());
    }

}

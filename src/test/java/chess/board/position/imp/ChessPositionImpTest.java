package chess.board.position.imp;

import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.moves.imp.MoveFactoryWhite;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class ChessPositionImpTest {

    private ChessPositionImp chessPosition;

    @Before
    public void setUp() {
        chessPosition = new ChessPositionImp();
    }

    @Test
    public void test_iterator() {
        ArrayPiecePlacement tablero = new ArrayPiecePlacement();

        tablero.setPieza(Square.a1, Piece.ROOK_WHITE);
        tablero.setPieza(Square.b7, Piece.PAWN_BLACK);
        tablero.setPieza(Square.b8, Piece.KNIGHT_BLACK);
        tablero.setPieza(Square.e1, Piece.KING_WHITE);
        tablero.setPieza(Square.e8, Piece.KING_BLACK);

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
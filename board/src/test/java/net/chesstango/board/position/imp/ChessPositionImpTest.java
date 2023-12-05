package net.chesstango.board.position.imp;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.position.BitBoard;
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


    @Test
    public void test_iterator() {
        ChessPositionImp chessPosition = new ChessPositionImp();

        SquareBoardImp squareBoardImp = new SquareBoardImp();

        squareBoardImp.setPiece(Square.a1, Piece.ROOK_WHITE);
        squareBoardImp.setPiece(Square.b7, Piece.PAWN_BLACK);
        squareBoardImp.setPiece(Square.b8, Piece.KNIGHT_BLACK);
        squareBoardImp.setPiece(Square.e1, Piece.KING_WHITE);
        squareBoardImp.setPiece(Square.e8, Piece.KING_BLACK);

        BitBoard bitBoard = new BitBoardImp();
        bitBoard.init(squareBoardImp);

        chessPosition.setPiecePlacement(squareBoardImp);
        chessPosition.setColorBoard(bitBoard);

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

package net.chesstango.board.moves.containers;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.internal.moves.MoveImp;
import net.chesstango.board.internal.moves.factories.MoveFactoryWhite;
import net.chesstango.board.moves.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mauricio Coria
 */
public class MoveContainerTest {

    private MoveContainer<Move> moveContainer;

    private MoveFactoryWhite factory;

    @BeforeEach
    public void setUp() throws Exception {
        moveContainer = new MoveContainer<>();
        factory = new MoveFactoryWhite();
    }

    @Test
    public void test1() {
        PiecePositioned origen = PiecePositioned.of(Square.e5, Piece.ROOK_WHITE);

        PiecePositioned destino = PiecePositioned.of(Square.e7, null);
        MoveImp move = factory.createSimpleKnightMove(origen, destino);
        moveContainer.add(move);

        Move foundMove = null;
        for (Move theMove : moveContainer) {
            if (theMove.equals(move)) {
                foundMove = move;
            }
        }
        assertEquals(move, foundMove);
        assertEquals(1, moveContainer.size());
        assertTrue(moveContainer.hasQuietMoves());
        assertFalse(moveContainer.hasPromotionMoves());
    }

    @Test
    public void test2() {
        PiecePositioned origen = PiecePositioned.of(Square.e5, Piece.ROOK_WHITE);
        PiecePositioned destino = PiecePositioned.of(Square.e7, null);

        MoveImp move1 = factory.createSimpleKnightMove(origen, destino);

        MoveList<Move> moveList = new MoveList<>();
        moveList.add(move1);

        moveContainer.add(moveList);

        Move foundMove1 = null;
        for (Move move : moveContainer) {
            if (move1.equals(move)) {
                foundMove1 = move;
            }
        }
        assertEquals(move1, foundMove1);
        assertEquals(1, moveContainer.size());
        assertTrue(moveContainer.hasQuietMoves());
        assertFalse(moveContainer.hasPromotionMoves());
    }

    @Test
    public void test3() {
        PiecePositioned origen = PiecePositioned.of(Square.e5, Piece.ROOK_WHITE);
        PiecePositioned destino1 = PiecePositioned.of(Square.e7, null);
        MoveImp move1 = factory.createSimpleKnightMove(origen, destino1);
        MoveList<Move> moveList1 = new MoveList<>();
        moveList1.add(move1);
        moveContainer.add(moveList1);

        PiecePositioned destino2 = PiecePositioned.of(Square.e8, null);
        MoveImp move2 = factory.createSimpleKnightMove(origen, destino2);
        MoveList<Move> moveList2 = new MoveList<>();
        moveList2.add(move2);
        moveContainer.add(moveList2);

        Move foundMove1 = null;
        Move foundMove2 = null;
        for (Move move : moveContainer) {
            if (move1.equals(move)) {
                foundMove1 = move;
            }
            if (move2.equals(move)) {
                foundMove2 = move;
            }
        }

        assertEquals(move1, foundMove1);
        assertEquals(move2, foundMove2);
        assertEquals(2, moveContainer.size());
        assertTrue(moveContainer.hasQuietMoves());
        assertFalse(moveContainer.hasPromotionMoves());
    }

    @Test
    public void test4() {
        PiecePositioned origen = PiecePositioned.of(Square.e5, Piece.ROOK_WHITE);

        PiecePositioned destino = PiecePositioned.of(Square.e4, null);
        MoveImp move = factory.createSimpleKnightMove(origen, destino);
        moveContainer.add(move);


        PiecePositioned destino1 = PiecePositioned.of(Square.e7, null);
        MoveImp move1 = factory.createSimpleKnightMove(origen, destino1);
        MoveList<Move> moveList1 = new MoveList<>();
        moveList1.add(move1);
        moveContainer.add(moveList1);

        PiecePositioned destino2 = PiecePositioned.of(Square.e8, null);
        MoveImp move2 = factory.createSimpleKnightMove(origen, destino2);
        MoveList<Move> moveList2 = new MoveList<>();
        moveList2.add(move2);
        moveContainer.add(moveList2);

        Move foundMove = null;
        Move foundMove1 = null;
        Move foundMove2 = null;
        for (Move themove : moveContainer) {
            if (move.equals(themove)) {
                foundMove = themove;
            }
            if (move1.equals(themove)) {
                foundMove1 = themove;
            }
            if (move2.equals(themove)) {
                foundMove2 = themove;
            }
        }

        assertEquals(move, foundMove);
        assertEquals(move1, foundMove1);
        assertEquals(move2, foundMove2);
        assertEquals(3, moveContainer.size());
        assertTrue(moveContainer.hasQuietMoves());
        assertFalse(moveContainer.hasPromotionMoves());
    }

    @Test
    public void test5() {
        PiecePositioned origen = PiecePositioned.of(Square.e5, Piece.ROOK_WHITE);

        PiecePositioned destino = PiecePositioned.of(Square.e7, Piece.KNIGHT_BLACK);
        MoveImp move = factory.createCaptureKnightMove(origen, destino);
        moveContainer.add(move);

        Move foundMove = null;
        for (Move theMove : moveContainer) {
            if (theMove.equals(move)) {
                foundMove = move;
            }
        }
        assertEquals(move, foundMove);
        assertEquals(1, moveContainer.size());
        assertFalse(moveContainer.hasQuietMoves());
        assertFalse(moveContainer.hasPromotionMoves());
    }

    @Test
    public void test6() {
        PiecePositioned origen = PiecePositioned.of(Square.e5, Piece.ROOK_WHITE);
        PiecePositioned destino = PiecePositioned.of(Square.e7, Piece.KNIGHT_BLACK);

        MoveImp move1 = factory.createCaptureKnightMove(origen, destino);

        MoveList<Move> moveList = new MoveList<>();
        moveList.add(move1);

        moveContainer.add(moveList);

        Move foundMove1 = null;
        for (Move move : moveContainer) {
            if (move1.equals(move)) {
                foundMove1 = move;
            }
        }
        assertEquals(move1, foundMove1);
        assertEquals(1, moveContainer.size());
        assertFalse(moveContainer.hasQuietMoves());
        assertFalse(moveContainer.hasPromotionMoves());
    }
}

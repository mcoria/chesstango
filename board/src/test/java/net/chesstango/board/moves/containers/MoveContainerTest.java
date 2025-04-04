package net.chesstango.board.moves.containers;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.internal.moves.factories.MoveFactoryWhite;
import net.chesstango.board.internal.moves.MoveImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class MoveContainerTest {

    private MoveContainer<Move> moveContainerImp;

    private MoveFactoryWhite factory;

    @BeforeEach
    public void setUp() throws Exception {
        moveContainerImp = new MoveContainer<>();
        factory = new MoveFactoryWhite();
    }

    @Test
    public void test1() {
        PiecePositioned origen = PiecePositioned.of(Square.e5, Piece.ROOK_WHITE);

        PiecePositioned destino = PiecePositioned.of(Square.e7, null);
        MoveImp move = factory.createSimpleKnightMove(origen, destino);
        moveContainerImp.add(move);

        Move foundMove = null;
        for (Move theMove : moveContainerImp) {
            if (theMove.equals(move)) {
                foundMove = move;
            }
        }
        assertEquals(move, foundMove);
        assertEquals(1, moveContainerImp.size());
    }

    @Test
    public void test2() {
        PiecePositioned origen = PiecePositioned.of(Square.e5, Piece.ROOK_WHITE);
        PiecePositioned destino = PiecePositioned.of(Square.e7, null);

        MoveImp move1 = factory.createSimpleKnightMove(origen, destino);

        MoveList moveList = new MoveList();
        moveList.add(move1);

        moveContainerImp.add(moveList);

        Move foundMove1 = null;
        for (Move move : moveContainerImp) {
            if (move1.equals(move)) {
                foundMove1 = move;
            }
        }
        assertEquals(move1, foundMove1);
        assertEquals(1, moveContainerImp.size());
    }

    @Test
    public void test3() {
        PiecePositioned origen = PiecePositioned.of(Square.e5, Piece.ROOK_WHITE);
        PiecePositioned destino1 = PiecePositioned.of(Square.e7, null);
        MoveImp move1 = factory.createSimpleKnightMove(origen, destino1);
        MoveList moveList1 = new MoveList();
        moveList1.add(move1);
        moveContainerImp.add(moveList1);

        PiecePositioned destino2 = PiecePositioned.of(Square.e8, null);
        MoveImp move2 = factory.createSimpleKnightMove(origen, destino2);
        MoveList moveList2 = new MoveList();
        moveList2.add(move2);
        moveContainerImp.add(moveList2);

        Move foundMove1 = null;
        Move foundMove2 = null;
        for (Move move : moveContainerImp) {
            if (move1.equals(move)) {
                foundMove1 = move;
            }
            if (move2.equals(move)) {
                foundMove2 = move;
            }
        }

        assertEquals(move1, foundMove1);
        assertEquals(move2, foundMove2);
        assertEquals(2, moveContainerImp.size());
    }

    @Test
    public void test4() {
        PiecePositioned origen = PiecePositioned.of(Square.e5, Piece.ROOK_WHITE);

        PiecePositioned destino = PiecePositioned.of(Square.e4, null);
        MoveImp move = factory.createSimpleKnightMove(origen, destino);
        moveContainerImp.add(move);


        PiecePositioned destino1 = PiecePositioned.of(Square.e7, null);
        MoveImp move1 = factory.createSimpleKnightMove(origen, destino1);
        MoveList moveList1 = new MoveList();
        moveList1.add(move1);
        moveContainerImp.add(moveList1);

        PiecePositioned destino2 = PiecePositioned.of(Square.e8, null);
        MoveImp move2 = factory.createSimpleKnightMove(origen, destino2);
        MoveList moveList2 = new MoveList();
        moveList2.add(move2);
        moveContainerImp.add(moveList2);

        Move foundMove = null;
        Move foundMove1 = null;
        Move foundMove2 = null;
        for (Move themove : moveContainerImp) {
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
        assertEquals(3, moveContainerImp.size());
    }
}

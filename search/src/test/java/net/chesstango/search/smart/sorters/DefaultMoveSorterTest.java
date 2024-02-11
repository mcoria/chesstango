package net.chesstango.search.smart.sorters;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class DefaultMoveSorterTest {

    private NodeMoveSorter nodeMoveSorter;

    @BeforeEach
    public void setUp() {
        nodeMoveSorter = new NodeMoveSorter();
        nodeMoveSorter.setMoveComparator(new DefaultMoveComparator());
    }

    /**
     * Hay dos opciones para capturar la reina negra, desde un peon blanco o desde un caballo blanco
     * Debemos comenzar con la pieza de menor valor, es decir, desde el peon
     */
    @Test
    public void testCapture01() {
        Move move = null;

        Game game = FENDecoder.loadGame("rnb1kbnr/pppp1ppp/4p3/5q2/4P2N/8/PPPP1PPP/RNBQKB1R w KQkq - 4 4");

        initMoveSorter(nodeMoveSorter, game);

        Iterable<Move> movesSorted = nodeMoveSorter.getOrderedMoves();

        Iterator<Move> movesSortedIt = movesSorted.iterator();

        move = movesSortedIt.next();
        assertEquals(Piece.PAWN_WHITE, move.getFrom().getPiece());
        assertEquals(Square.e4, move.getFrom().getSquare());
        assertEquals(Square.f5, move.getTo().getSquare());

        move = movesSortedIt.next();
        assertEquals(Piece.KNIGHT_WHITE, move.getFrom().getPiece());
        assertEquals(Square.h4, move.getFrom().getSquare());
        assertEquals(Square.f5, move.getTo().getSquare());
    }

    @Test
    public void testCapture01Mirror() {
        Move move = null;

        Game game = FENDecoder.loadGame("rnbqkb1r/pppp1ppp/8/4p2n/5Q2/4P3/PPPP1PPP/RNB1KBNR b KQkq - 4 1");

        initMoveSorter(nodeMoveSorter, game);

        Iterable<Move> movesSorted = nodeMoveSorter.getOrderedMoves();

        Iterator<Move> movesSortedIt = movesSorted.iterator();

        move = movesSortedIt.next();
        assertEquals(Piece.PAWN_BLACK, move.getFrom().getPiece());
        assertEquals(Square.e5, move.getFrom().getSquare());
        assertEquals(Square.f4, move.getTo().getSquare());

        move = movesSortedIt.next();
        assertEquals(Piece.KNIGHT_BLACK, move.getFrom().getPiece());
        assertEquals(Square.h5, move.getFrom().getSquare());
        assertEquals(Square.f4, move.getTo().getSquare());
    }


    /**
     * EL peon blanco puede capturar Reina o Torre negra
     * El caballo blanco puede capturar Reina o Alfil negro
     */

    @Test
    public void testCapture02() {
        Move move = null;

        Game game = FENDecoder.loadGame("1nb1k1nr/pppp1ppp/4p1b1/3r1q2/4P2N/8/PPPP1PPP/RNBQKB1R w KQk - 4 1");

        initMoveSorter(nodeMoveSorter, game);

        Iterable<Move> movesSorted = nodeMoveSorter.getOrderedMoves();

        Iterator<Move> movesSortedIt = movesSorted.iterator();

        move = movesSortedIt.next();
        assertEquals(Piece.PAWN_WHITE, move.getFrom().getPiece());
        assertEquals(Square.e4, move.getFrom().getSquare());
        assertEquals(Square.f5, move.getTo().getSquare());

        move = movesSortedIt.next();
        assertEquals(Piece.KNIGHT_WHITE, move.getFrom().getPiece());
        assertEquals(Square.h4, move.getFrom().getSquare());
        assertEquals(Square.f5, move.getTo().getSquare());

        move = movesSortedIt.next();
        assertEquals(Piece.PAWN_WHITE, move.getFrom().getPiece());
        assertEquals(Square.e4, move.getFrom().getSquare());
        assertEquals(Square.d5, move.getTo().getSquare());

        move = movesSortedIt.next();
        assertEquals(Piece.KNIGHT_WHITE, move.getFrom().getPiece());
        assertEquals(Square.h4, move.getFrom().getSquare());
        assertEquals(Square.g6, move.getTo().getSquare());
    }

    @Test
    public void testCapture02Mirror() {
        Move move = null;

        Game game = FENDecoder.loadGame("rnbqkb1r/pppp1ppp/8/4p2n/3R1Q2/4P1B1/PPPP1PPP/1NB1K1NR b Kkq - 4 1");

        initMoveSorter(nodeMoveSorter, game);

        Iterable<Move> movesSorted = nodeMoveSorter.getOrderedMoves();

        Iterator<Move> movesSortedIt = movesSorted.iterator();

        move = movesSortedIt.next();
        assertEquals(Piece.PAWN_BLACK, move.getFrom().getPiece());
        assertEquals(Square.e5, move.getFrom().getSquare());
        assertEquals(Square.f4, move.getTo().getSquare());

        move = movesSortedIt.next();
        assertEquals(Piece.KNIGHT_BLACK, move.getFrom().getPiece());
        assertEquals(Square.h5, move.getFrom().getSquare());
        assertEquals(Square.f4, move.getTo().getSquare());

        move = movesSortedIt.next();
        assertEquals(Piece.PAWN_BLACK, move.getFrom().getPiece());
        assertEquals(Square.e5, move.getFrom().getSquare());
        assertEquals(Square.d4, move.getTo().getSquare());

        move = movesSortedIt.next();
        assertEquals(Piece.KNIGHT_BLACK, move.getFrom().getPiece());
        assertEquals(Square.h5, move.getFrom().getSquare());
        assertEquals(Square.g3, move.getTo().getSquare());
    }

    @Test
    public void testInitial() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        initMoveSorter(nodeMoveSorter, game);

        Move move;
        Iterable<Move> movesSorted = nodeMoveSorter.getOrderedMoves();
        Iterator<Move> movesSortedIt = movesSorted.iterator();

        move = movesSortedIt.next();
        assertEquals(Piece.KNIGHT_WHITE, move.getFrom().getPiece());
        assertEquals(Square.g1, move.getFrom().getSquare());
        assertEquals(Square.h3, move.getTo().getSquare());
    }

    @Test
    public void testGamesMirror() {
        testMirror(FENDecoder.loadGame(FENDecoder.INITIAL_FEN));
        testMirror(FENDecoder.loadGame("r4rk1/1pp2ppp/p2b1n2/3pp3/8/PPNbPN2/3P1PPP/R1B1K2R b KQ - 0 14"));
        testMirror(FENDecoder.loadGame("2r1nrk1/p2q1ppp/bp1p4/n1pPp3/P1P1P3/2PBB1N1/4QPPP/R4RK1 w - - 0 1"));
        testMirror(FENDecoder.loadGame("r1bqk2r/pp2bppp/2p5/3pP3/P2Q1P2/2N1B3/1PP3PP/R4RK1 b kq - 0 1"));
        testMirror(FENDecoder.loadGame("rnb1kbnr/pppp1ppp/4p3/5q2/4P2N/8/PPPP1PPP/RNBQKB1R w KQkq - 4 4"));
        testMirror(FENDecoder.loadGame("1nb1k1nr/pppp1ppp/4p1b1/3r1q2/4P2N/8/PPPP1PPP/RNBQKB1R w KQk - 4 1"));
    }


    private void testMirror(Game game) {
        NodeMoveSorter moveSorter = new NodeMoveSorter();
        moveSorter.setMoveComparator(new DefaultMoveComparator());

        Game gameMirror = game.mirror();

        NodeMoveSorter moveSorterMirror = new NodeMoveSorter();
        moveSorterMirror.setMoveComparator(new DefaultMoveComparator());

        initMoveSorter(moveSorter, game);
        initMoveSorter(moveSorterMirror, gameMirror);

        List<Move> movesSorted = new ArrayList<>();
        moveSorter.getOrderedMoves().forEach(movesSorted::add);

        List<Move> movesSortedMirror = new ArrayList<>();
        moveSorterMirror.getOrderedMoves().forEach(movesSortedMirror::add);

        final int moveCounter = movesSorted.size();

        assertEquals(moveCounter, movesSortedMirror.size());
        for (int i = 0; i < moveCounter; i++) {
            Move move = movesSorted.get(i);
            Move moveMirror = movesSortedMirror.get(i);

            assertEquals(move.getFrom().getPiece(), moveMirror.getFrom().getPiece().getOpposite());
            assertEquals(move.getFrom().getSquare(), moveMirror.getFrom().getSquare().getMirrorSquare());
            assertEquals(move.getTo().getSquare(), moveMirror.getTo().getSquare().getMirrorSquare());

            if (move instanceof MovePromotion) {
                MovePromotion movePromotion = (MovePromotion) move;
                MovePromotion movePromotionMirror = (MovePromotion) moveMirror;
                assertEquals(movePromotion.getPromotion(), movePromotionMirror.getPromotion().getOpposite());
            }

            //System.out.println("OK " + i);
        }
    }

    private void initMoveSorter(NodeMoveSorter nodeMoveSorter, Game game) {
        SearchByCycleContext searchByCycleContext = new SearchByCycleContext(game);

        nodeMoveSorter.beforeSearch(searchByCycleContext);
    }

}

package net.chesstango.search.smart.sorters;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveFactory;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.search.smart.SearchContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Mauricio Coria
 */
public class DefaultMoveSorterTest {

    private DefaultMoveSorter moveSorter;

    private final MoveFactory moveFactoryWhite = SingletonMoveFactories.getDefaultMoveFactoryWhite();

    private final MoveFactory moveFactoryBlack = SingletonMoveFactories.getDefaultMoveFactoryBlack();

    @BeforeEach
    public void setUp() {
        moveSorter = new DefaultMoveSorter();
    }

    @Test
    public void sortMoveToEmptySquareWhite() {
        Move move = null;

        List<Move> moveList = new ArrayList<>();

        moveList.add(moveFactoryWhite.createSimpleOneSquarePawnMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.PAWN_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.QUEEN_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.KING_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.ROOK_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.BISHOP_WHITE),
                PiecePositioned.getPosition(Square.e3)));


        List<Move> movesSorted = moveSorter.getSortedMoves(moveList);
        Iterator<Move> movesSortedIt = movesSorted.iterator();

        move = movesSortedIt.next();
        assertEquals(Piece.QUEEN_WHITE, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.KNIGHT_WHITE, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.BISHOP_WHITE, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.ROOK_WHITE, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.PAWN_WHITE, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.KING_WHITE, move.getFrom().getPiece());

        assertFalse(movesSortedIt.hasNext());
    }


    @Test
    public void sortMoveCaptureWhite() {
        Move move = null;

        List<Move> moveList = new ArrayList<>();

        moveList.add(moveFactoryWhite.createCapturePawnMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.PAWN_WHITE),
                PiecePositioned.getPiecePositioned(Square.f3, Piece.QUEEN_BLACK), Cardinal.NorteEste));

        moveList.add(moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.QUEEN_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.KING_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.ROOK_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(moveFactoryWhite.createCaptureMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.BISHOP_WHITE),
                PiecePositioned.getPiecePositioned(Square.e3, Piece.PAWN_BLACK)));

        List<Move> movesSorted = moveSorter.getSortedMoves(moveList);
        Iterator<Move> movesSortedIt = movesSorted.iterator();

        move = movesSortedIt.next();
        assertEquals(Piece.PAWN_WHITE, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.BISHOP_WHITE, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.QUEEN_WHITE, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.KNIGHT_WHITE, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.ROOK_WHITE, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.KING_WHITE, move.getFrom().getPiece());

        assertFalse(movesSortedIt.hasNext());
    }

    @Test
    public void sortMoveToEmptySquareBlack() {
        Move move = null;

        List<Move> moveList = new ArrayList<>();

        moveList.add(moveFactoryBlack.createSimpleOneSquarePawnMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.PAWN_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.QUEEN_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.KING_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.KNIGHT_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.ROOK_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.BISHOP_BLACK),
                PiecePositioned.getPosition(Square.e6)));


        List<Move> movesSorted = moveSorter.getSortedMoves(moveList);
        Iterator<Move> movesSortedIt = movesSorted.iterator();

        move = movesSortedIt.next();
        assertEquals(Piece.QUEEN_BLACK, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.KNIGHT_BLACK, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.BISHOP_BLACK, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.ROOK_BLACK, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.PAWN_BLACK, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.KING_BLACK, move.getFrom().getPiece());

        assertFalse(movesSortedIt.hasNext());
    }

    @Test
    public void sortMoveCaptureBlack() {
        Move move = null;

        List<Move> moveList = new ArrayList<>();

        moveList.add(moveFactoryBlack.createCapturePawnMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.PAWN_BLACK),
                PiecePositioned.getPiecePositioned(Square.f6, Piece.QUEEN_WHITE), Cardinal.SurEste));

        moveList.add(moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.QUEEN_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.KING_BLACK),
                PiecePositioned.getPosition(Square.e5)));

        moveList.add(moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.KNIGHT_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.ROOK_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(moveFactoryBlack.createCaptureMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.BISHOP_BLACK),
                PiecePositioned.getPiecePositioned(Square.e6, Piece.PAWN_WHITE)));


        List<Move> movesSorted = moveSorter.getSortedMoves(moveList);
        Iterator<Move> movesSortedIt = movesSorted.iterator();

        move = movesSortedIt.next();
        assertEquals(Piece.PAWN_BLACK, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.BISHOP_BLACK, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.QUEEN_BLACK, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.KNIGHT_BLACK, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.ROOK_BLACK, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.KING_BLACK, move.getFrom().getPiece());

        assertFalse(movesSortedIt.hasNext());
    }

    /**
     * Hay dos opciones para capturar la reina negra, desde un peon blanco o desde un caballo blanco
     * Debemos comenzar con la pieza de menor valor, es decir, desde el peon
     */
    @Test
    public void testCapture01() {
        Move move = null;

        Game game = FENDecoder.loadGame("rnb1kbnr/pppp1ppp/4p3/5q2/4P2N/8/PPPP1PPP/RNBQKB1R w KQkq - 4 4");

        initMoveSorter(moveSorter, game);

        List<Move> movesSorted = moveSorter.getSortedMoves();

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

        initMoveSorter(moveSorter, game);

        List<Move> movesSorted = moveSorter.getSortedMoves();

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

        initMoveSorter(moveSorter, game);

        List<Move> movesSorted = moveSorter.getSortedMoves();

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

        initMoveSorter(moveSorter, game);

        List<Move> movesSorted = moveSorter.getSortedMoves();

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

        initMoveSorter(moveSorter, game);

        Move move;
        List<Move> movesSorted = moveSorter.getSortedMoves();
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
        DefaultMoveSorter moveSorter = new DefaultMoveSorter();

        Game gameMirror = game.mirror();

        DefaultMoveSorter moveSorterMirror = new DefaultMoveSorter();

        initMoveSorter(moveSorter, game);
        initMoveSorter(moveSorterMirror, gameMirror);

        List<Move> movesSorted = moveSorter.getSortedMoves();
        List<Move> movesSortedMirror = moveSorterMirror.getSortedMoves();

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

    private void initMoveSorter(MoveSorter moveSorter, Game game) {
        moveSorter.beforeSearch(game);

        SearchContext context = new SearchContext(1);

        //moveSorter.beforeSearchByDepth(context);
    }

}

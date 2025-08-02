package net.chesstango.board.internal;

import net.chesstango.board.*;
import net.chesstango.board.builders.GameBuilder;
import net.chesstango.board.builders.GameBuilderDebug;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.internal.moves.factories.MoveFactoryBlack;
import net.chesstango.board.internal.moves.factories.MoveFactoryWhite;
import net.chesstango.board.position.PositionReader;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENBuilder;
import net.chesstango.gardel.fen.FENParser;
import net.chesstango.gardel.fen.FENExporter;
import net.chesstango.gardel.polyglot.PolyglotKeyBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Mauricio Coria
 */
public class GameTest {

    @Test
    public void testInitialPosition() {
        Game game = getGame(FENParser.INITIAL_FEN);

        Assertions.assertEquals(Color.WHITE, game.getPosition().getCurrentTurn());
        assertTrue(game.getPosition().isCastlingWhiteQueenAllowed());
        assertTrue(game.getPosition().isCastlingWhiteKingAllowed());
        assertTrue(game.getPosition().isCastlingBlackQueenAllowed());
        assertTrue(game.getPosition().isCastlingBlackKingAllowed());
        assertEquals(0, game.getPosition().getHalfMoveClock());
        assertEquals(1, game.getPosition().getFullMoveClock());

        Assertions.assertEquals(Status.NO_CHECK, game.getStatus());
        assertEquals(20, game.getPossibleMoves().size());
    }

    @Test
    public void test_mate() {
        Game game = getGame(FENParser.INITIAL_FEN);
        assertEquals(20, game.getPossibleMoves().size());
        assertEquals(Color.WHITE, game.getPosition().getCurrentTurn());

        game.executeMove(Square.e2, Square.e4)
                .executeMove(Square.e7, Square.e5)
                .executeMove(Square.f1, Square.c4)
                .executeMove(Square.b8, Square.c6)
                .executeMove(Square.d1, Square.f3)
                .executeMove(Square.f8, Square.c5)
                .executeMove(Square.f3, Square.f7);

        assertEquals(Color.BLACK, game.getPosition().getCurrentTurn());
        assertEquals(Status.MATE, game.getStatus());
        assertTrue(game.getPossibleMoves().isEmpty());
        assertEquals(0, game.getPosition().getHalfMoveClock());
        assertEquals(4, game.getPosition().getFullMoveClock());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());
    }

    @Test
    public void testMateAndUndo() {
        Game game = getGame(FENParser.INITIAL_FEN);
        assertEquals(20, game.getPossibleMoves().size());
        assertEquals(Color.WHITE, game.getPosition().getCurrentTurn());

        game.executeMove(Square.e2, Square.e4)
                .executeMove(Square.e7, Square.e5)
                .executeMove(Square.f1, Square.c4)
                .executeMove(Square.b8, Square.c6)
                .executeMove(Square.d1, Square.f3)
                .executeMove(Square.f8, Square.c5)
                .executeMove(Square.f3, Square.f7);

        assertEquals(Color.BLACK, game.getPosition().getCurrentTurn());
        assertEquals(Status.MATE, game.getStatus());
        assertTrue(game.getPossibleMoves().isEmpty());
        assertEquals(0, game.getPosition().getHalfMoveClock());
        assertEquals(4, game.getPosition().getFullMoveClock());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.undoMove()
                .undoMove()
                .undoMove()
                .undoMove()
                .undoMove()
                .undoMove()
                .undoMove();
        assertEquals(20, game.getPossibleMoves().size());
        assertEquals(Color.WHITE, game.getPosition().getCurrentTurn());
        assertEquals(0, game.getPosition().getHalfMoveClock());
        assertEquals(1, game.getPosition().getFullMoveClock());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());
    }

    @Test
    public void testJuegoJaque() {
        Game game = getGame(FENParser.INITIAL_FEN);

        assertEquals(20, game.getPossibleMoves().size());
        assertEquals(Color.WHITE, game.getPosition().getCurrentTurn());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.e2, Square.e4);
        assertEquals(20, game.getPossibleMoves().size());
        assertEquals(Color.BLACK, game.getPosition().getCurrentTurn());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.e7, Square.e5);
        assertEquals(29, game.getPossibleMoves().size());
        assertEquals(Color.WHITE, game.getPosition().getCurrentTurn());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.f1, Square.c4);
        assertEquals(29, game.getPossibleMoves().size());
        assertEquals(Color.BLACK, game.getPosition().getCurrentTurn());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.b8, Square.c6);
        assertEquals(33, game.getPossibleMoves().size());
        assertEquals(Color.WHITE, game.getPosition().getCurrentTurn());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.d1, Square.f3);
        assertEquals(31, game.getPossibleMoves().size());
        assertEquals(Color.BLACK, game.getPosition().getCurrentTurn());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.g8, Square.h6);
        assertEquals(42, game.getPossibleMoves().size());
        assertEquals(Color.WHITE, game.getPosition().getCurrentTurn());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());


        game.executeMove(Square.f3, Square.f7);
        assertEquals(Color.BLACK, game.getPosition().getCurrentTurn());
        assertEquals(Status.CHECK, game.getStatus());
        assertEquals(1, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());
    }

    @Test
    public void testJuegoDraw() {
        Game game = getGame("k7/7Q/K7/8/8/8/8/8 w - - 0 1");

        assertEquals(Color.WHITE, game.getPosition().getCurrentTurn());
        assertEquals(0, game.getPosition().getHalfMoveClock());
        assertEquals(1, game.getPosition().getFullMoveClock());

        game.executeMove(Square.h7, Square.c7);

        assertEquals(Color.BLACK, game.getPosition().getCurrentTurn());
        assertEquals(Status.STALEMATE, game.getStatus());
        assertEquals(0, game.getPossibleMoves().size());
        assertEquals(1, game.getPosition().getHalfMoveClock());
        assertEquals(1, game.getPosition().getFullMoveClock());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());
    }

    @Test
    public void testDrawByFiftyMoveRule() {
        Game game = getGame("rn1qkbnr/pp2ppp1/2p4p/3pPb2/3P2PP/8/PPP2P2/RNBQKBNR b KQkq - 99 125");

        assertEquals(Color.BLACK, game.getPosition().getCurrentTurn());
        assertEquals(99, game.getPosition().getHalfMoveClock());
        assertEquals(125, game.getPosition().getFullMoveClock());

        game.executeMove(Square.f5, Square.e4);

        assertEquals(Color.WHITE, game.getPosition().getCurrentTurn());
        assertEquals(Status.DRAW_BY_FIFTY_RULE, game.getStatus());
        assertEquals(0, game.getPossibleMoves().size());
        assertEquals(100, game.getPosition().getHalfMoveClock());
        assertEquals(126, game.getPosition().getFullMoveClock());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());
    }


    @Test
    public void testDrawByThreeFoldRepetition() {
        Game game = getGame(FENParser.INITIAL_FEN);

        assertEquals(Color.WHITE, game.getPosition().getCurrentTurn());
        assertEquals(Status.NO_CHECK, game.getStatus());
        assertTrue(game.getPosition().isCastlingWhiteQueenAllowed());
        assertTrue(game.getPosition().isCastlingWhiteKingAllowed());
        assertTrue(game.getPosition().isCastlingBlackQueenAllowed());
        assertTrue(game.getPosition().isCastlingBlackKingAllowed());
        assertEquals(0, game.getPosition().getHalfMoveClock());
        assertEquals(1, game.getPosition().getFullMoveClock());
        assertEquals(20, game.getPossibleMoves().size());

        game
                .executeMove(Square.g1, Square.f3)
                .executeMove(Square.b8, Square.c6)
                .executeMove(Square.f3, Square.g1)
                .executeMove(Square.c6, Square.b8)

                .executeMove(Square.g1, Square.f3)
                .executeMove(Square.b8, Square.c6)
                .executeMove(Square.f3, Square.g1)
                .executeMove(Square.c6, Square.b8);

        assertEquals(Color.WHITE, game.getPosition().getCurrentTurn());
        assertEquals(Status.DRAW_BY_FOLD_REPETITION, game.getStatus());
        assertTrue(game.getPosition().isCastlingWhiteQueenAllowed());
        assertTrue(game.getPosition().isCastlingWhiteKingAllowed());
        assertTrue(game.getPosition().isCastlingBlackQueenAllowed());
        assertTrue(game.getPosition().isCastlingBlackKingAllowed());
        assertEquals(8, game.getPosition().getHalfMoveClock());
        assertEquals(5, game.getPosition().getFullMoveClock());
        assertEquals(0, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());
    }


    @Test
    public void testDrawByThreeFoldRepetition01() {
        Game game = getGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 8 4");

        assertEquals(Color.WHITE, game.getPosition().getCurrentTurn());
        assertEquals(Status.NO_CHECK, game.getStatus());
        assertTrue(game.getPosition().isCastlingWhiteQueenAllowed());
        assertTrue(game.getPosition().isCastlingWhiteKingAllowed());
        assertTrue(game.getPosition().isCastlingBlackQueenAllowed());
        assertTrue(game.getPosition().isCastlingBlackKingAllowed());
        assertEquals(8, game.getPosition().getHalfMoveClock());
        assertEquals(4, game.getPosition().getFullMoveClock());
        assertEquals(20, game.getPossibleMoves().size());

        game
                .executeMove(Square.g1, Square.f3)
                .executeMove(Square.b8, Square.c6)
                .executeMove(Square.f3, Square.g1)
                .executeMove(Square.c6, Square.b8)

                .executeMove(Square.g1, Square.f3)
                .executeMove(Square.b8, Square.c6)
                .executeMove(Square.f3, Square.g1)
                .executeMove(Square.c6, Square.b8);

        assertEquals(Color.WHITE, game.getPosition().getCurrentTurn());
        assertEquals(Status.DRAW_BY_FOLD_REPETITION, game.getStatus());
        assertTrue(game.getPosition().isCastlingWhiteQueenAllowed());
        assertTrue(game.getPosition().isCastlingWhiteKingAllowed());
        assertTrue(game.getPosition().isCastlingBlackQueenAllowed());
        assertTrue(game.getPosition().isCastlingBlackKingAllowed());
        assertEquals(16, game.getPosition().getHalfMoveClock());
        assertEquals(8, game.getPosition().getFullMoveClock());
        assertEquals(0, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());
    }


    @Test
    public void testGetRepetitionCounter() {
        Game game = getGame(FENParser.INITIAL_FEN);

        assertEquals(Color.WHITE, game.getPosition().getCurrentTurn());
        assertEquals(Status.NO_CHECK, game.getStatus());
        assertTrue(game.getPosition().isCastlingWhiteQueenAllowed());
        assertTrue(game.getPosition().isCastlingWhiteKingAllowed());
        assertTrue(game.getPosition().isCastlingBlackQueenAllowed());
        assertTrue(game.getPosition().isCastlingBlackKingAllowed());
        assertEquals(0, game.getPosition().getHalfMoveClock());
        assertEquals(1, game.getPosition().getFullMoveClock());
        assertEquals(20, game.getPossibleMoves().size());

        game
                .executeMove(Square.g1, Square.f3)
                .executeMove(Square.b8, Square.c6)
                .executeMove(Square.f3, Square.g1)
                .executeMove(Square.c6, Square.b8);

        assertEquals(Color.WHITE, game.getPosition().getCurrentTurn());
        assertEquals(Status.NO_CHECK, game.getStatus());
        assertEquals(2, game.getState().getRepetitionCounter());
        assertTrue(game.getPosition().isCastlingWhiteQueenAllowed());
        assertTrue(game.getPosition().isCastlingWhiteKingAllowed());
        assertTrue(game.getPosition().isCastlingBlackQueenAllowed());
        assertTrue(game.getPosition().isCastlingBlackKingAllowed());
        assertEquals(4, game.getPosition().getHalfMoveClock());
        assertEquals(3, game.getPosition().getFullMoveClock());
        assertEquals(20, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());
    }

    @Test
    public void testGetRepetitionCounter01() {
        Game game = getGame("3b1rk1/1bq3pp/5pn1/1p2rN2/2p1p3/2P1B2Q/1PB2PPP/R2R2K1 w - - 0 1");

        assertEquals(Color.WHITE, game.getPosition().getCurrentTurn());
        assertEquals(Status.NO_CHECK, game.getStatus());

        game
                .executeMove(Square.a1, Square.a7)
                .executeMove(Square.c7, Square.c6)
                .executeMove(Square.d1, Square.d6)
                .executeMove(Square.c6, Square.c7)
                .executeMove(Square.d6, Square.d1);

        assertEquals(Color.BLACK, game.getPosition().getCurrentTurn());
        assertEquals(Status.NO_CHECK, game.getStatus());
        assertEquals(2, game.getState().getRepetitionCounter());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());
    }

    @Test
    public void test_undo() {
        Game game = getGame(FENParser.INITIAL_FEN);

        assertEquals(20, game.getPossibleMoves().size());
        assertEquals(Color.WHITE, game.getPosition().getCurrentTurn());
        assertEquals(0, game.getPosition().getHalfMoveClock());
        assertEquals(1, game.getPosition().getFullMoveClock());

        game.executeMove(Square.e2, Square.e4);
        assertEquals(20, game.getPossibleMoves().size());
        assertEquals(Color.BLACK, game.getPosition().getCurrentTurn());
        assertEquals(0, game.getPosition().getHalfMoveClock());
        assertEquals(1, game.getPosition().getFullMoveClock());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.undoMove();
        assertEquals(20, game.getPossibleMoves().size());
        assertEquals(Color.WHITE, game.getPosition().getCurrentTurn());
        assertEquals(0, game.getPosition().getHalfMoveClock());
        assertEquals(1, game.getPosition().getFullMoveClock());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());
    }

    @Test
    public void testJuegoNoEnPassant() {
        Game game = getGame("rnbqkbnr/p1pppppp/1p6/P7/8/8/1PPPPPPP/RNBQKBNR b KQkq - 0 2");

        game.executeMove(Square.b6, Square.b5);

        assertEquals(22, game.getPossibleMoves().size());
        assertEquals(0, game.getPosition().getHalfMoveClock());
        assertEquals(3, game.getPosition().getFullMoveClock()); // Movio Negras
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());
    }

    @Test
    public void testJuegoEnPassant() {
        Game game = getGame("rnbqkbnr/1ppppppp/8/pP6/8/8/P1PPPPPP/RNBQKBNR b KQkq - 0 2");

        game.executeMove(Square.c7, Square.c5);

        assertEquals(Square.c6, game.getPosition().getEnPassantSquare());
        assertNotNull(game.getMove(Square.b5, Square.c6));
        assertEquals(22, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());
    }


    //TODO: No solo cuando se mueve la torre sino tambien cuando la capturan
    @Test
    public void testMueveRookPierdeEnroque() {
        Game game = getGame("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");

        game.executeMove(Square.a1, Square.b1);

        assertFalse(game.getPosition().isCastlingWhiteQueenAllowed());

        assertEquals(43, game.getPossibleMoves().size());

        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());
    }

    @Test
    public void testJuegoKiwipeteTestUndo() {
        Game game = getGame("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");

        assertEquals(48, game.getPossibleMoves().size());

        game.executeMove(Square.e1, Square.g1);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.e1, Square.c1);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.e1, Square.f1);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.e1, Square.d1);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.d5, Square.d6);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.d5, Square.e6);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.a2, Square.a3);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.a2, Square.a4);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.b2, Square.b3);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.g2, Square.g3);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.g2, Square.g4);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.g2, Square.h3);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.e5, Square.d3);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.e5, Square.f7);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.e5, Square.c4);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.e5, Square.g6);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.e5, Square.g4);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.e5, Square.c6);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.e5, Square.d7);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.c3, Square.b1);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.c3, Square.a4);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.c3, Square.d1);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.c3, Square.b5);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.f3, Square.g3);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.f3, Square.h3);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.f3, Square.e3);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.f3, Square.d3);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.f3, Square.g4);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.f3, Square.h5);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.f3, Square.f4);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.f3, Square.f5);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.f3, Square.f6);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.d2, Square.c1);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.d2, Square.e3);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.d2, Square.f4);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.d2, Square.g5);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.d2, Square.h6);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.e2, Square.d1);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.e2, Square.f1);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.e2, Square.d3);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.e2, Square.c4);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.e2, Square.b5);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.e2, Square.a6);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.a1, Square.b1);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.a1, Square.c1);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.a1, Square.d1);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.h1, Square.g1);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        game.executeMove(Square.h1, Square.f1);
        game.undoMove();
        assertEquals(48, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

    }

    @Test
    public void testCaptureRook() {
        Game game = getGame("4k2r/8/8/8/3B4/8/8/4K3 w k - 0 1");

        MoveFactoryBlack moveFactory = new MoveFactoryBlack((GameImp) game);

        //Estado inicial
        assertEquals(18, game.getPossibleMoves().size());
        assertTrue(game.getPosition().isCastlingBlackKingAllowed());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        //Capturamos la torre negra
        game.executeMove(Square.d4, Square.h8);
        assertEquals(5, game.getPossibleMoves().size());
        //Ya no tenemos castling
        assertNull(game.getMove(Square.e8, Square.h8));
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        //Undo captura de torre negra ---- Volvemos al estado inicial
        game.undoMove();  // Aca esta el problema, el UNDO no borra los movimientos de king del cache
        assertEquals(18, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        //Movimiento 1 - lo repetimos
        game.executeMove(Square.d4, Square.c3);
        assertEquals(15, game.getPossibleMoves().size());
        //CastlingBlackKingMove es uno de los movimientos posibles
        assertEquals(moveFactory.createCastlingKingMove(), game.getMove(Square.e8, Square.g8));
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());
    }

    @Test
    public void testUndoCaptureRook() {
        Game game = getGame("4k2r/8/8/8/3B4/8/8/4K3 w k - 0 1");

        MoveFactoryBlack moveFactory = new MoveFactoryBlack((GameImp) game);

        //Estado inicial
        assertEquals(18, game.getPossibleMoves().size());

        //Movimiento 1 - cualquier movimiento
        game.executeMove(Square.d4, Square.c3);
        assertEquals(15, game.getPossibleMoves().size());
        //CastlingBlackKingMove es uno de los movimientos posibles
        assertEquals(moveFactory.createCastlingKingMove(), game.getMove(Square.e8, Square.g8));
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        //Undo movimiento 1 y volvemos al estado inicial
        game.undoMove();

        //Estado inicial
        assertEquals(18, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        //Capturamos la torre negra
        game.executeMove(Square.d4, Square.h8);
        assertEquals(5, game.getPossibleMoves().size());
        //Ya no tenemos castling
        assertNull(game.getMove(Square.e8, Square.h8));
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        //Undo captura de torre negra ---- Volvemos al estado inicial
        game.undoMove();  // Aca esta el problema, el UNDO no borra los movimientos de king del cache
        assertEquals(18, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        //Movimiento 1 - lo repetimos
        game.executeMove(Square.d4, Square.c3);
        assertEquals(15, game.getPossibleMoves().size());
        //CastlingBlackKingMove es uno de los movimientos posibles
        assertEquals(moveFactory.createCastlingKingMove(), game.getMove(Square.e8, Square.g8));
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());
    }


    @Test
    public void testUndoRookMove() {
        Game game = getGame("4k3/8/8/8/8/8/8/R3K2R w KQ - 0 1");

        //Estado inicial
        assertEquals(26, game.getPossibleMoves().size());

        //Mueve la torre blanca de king
        game.executeMove(Square.h1, Square.h2);

        //Mueve king negro
        game.executeMove(Square.e8, Square.e7);

        game.undoMove();

        game.undoMove();

        //Estado inicial
        assertEquals(26, game.getPossibleMoves().size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());
    }

    @Test
    public void testKingWhitePierdeCastling() {
        Game game = getGame("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");

        game.executeMove(Square.a1, Square.b1);

        game.executeMove(Square.h3, Square.g2);

        game.executeMove(Square.b1, Square.c1);

        game.executeMove(Square.g2, Square.h1, Piece.QUEEN_BLACK);

        PositionReader reader = game.getPosition();

        assertFalse(reader.isCastlingWhiteKingAllowed());

        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());
    }


    @Test
    public void testCacheEnEstadoInvalido01() {
        Game game = getGame("4k3/8/8/8/4b3/8/8/R3K2R w KQ - 0 1");

        MoveFactoryWhite moveFactory = new MoveFactoryWhite((GameImp) game);

        //Antes de mover blanca podemos ver que tenemos enroque
        assertTrue(game.getPossibleMoves().contains(moveFactory.createCastlingKingMove()), "castlingKingMove not present");

        // Mueve torre blanca
        game.executeMove(Square.a1, Square.d1);
        // .... y limpia los movimientos de cache de REY

        // Mueve Negra y capture torre blanca de Rey
        game.executeMove(Square.e4, Square.h1);

        // Blanca pierde el enroque de rey
        // Rey establece los movimientos en cache (sin enroque de Torre Rey)
        // Los movimientos que establece en cache no dependen de lo que hay en h1 (puesto que no hay torre blanca)

        assertFalse(game.getPossibleMoves().contains(moveFactory.createCastlingKingMove()), "castlingKingMove not present");

        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        // Aca comienza el bolonqui
        game.undoMove();
        // El undo deja los movimientos de REY recien almacenados en cache

        // Negra NO come torre de Rey
        game.executeMove(Square.e4, Square.d5);
        // Y cuando se pregunta por movimientos de rey en cache

        //Blanca deberia tener enroque de rey nuevamente
        assertTrue(game.getPossibleMoves().contains(moveFactory.createCastlingKingMove()), "castlingKingMove not present");

        assertFalse(game.getPosition().isCastlingWhiteQueenAllowed());

        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

    }


    @Test
    public void testJuegoEnPassantUndo() {
        MoveContainerReader<? extends Move> legalMoves = null;

        Game game = getGame("rnbqkbnr/pppppppp/8/1P6/8/8/P1PPPPPP/RNBQKBNR b KQkq - 0 2");

        legalMoves = game.getPossibleMoves();

        // Estado inicial
        assertEquals(19, legalMoves.size());
        assertFalse(contieneMove(legalMoves, Square.b5, Square.c6));

        // Mueve el pawn pasante
        game.executeMove(Square.c7, Square.c5);


        // Podemos capturarlo
        legalMoves = game.getPossibleMoves();
        assertTrue(contieneMove(legalMoves, Square.b5, Square.c6));
        assertEquals(22, legalMoves.size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        // Volvemos atras
        game.undoMove();

        // No podemos capturarlo
        legalMoves = game.getPossibleMoves();
        assertEquals(19, legalMoves.size());
        assertFalse(contieneMove(legalMoves, Square.b5, Square.c6));
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());
    }

    @Test
    public void testJuegoEnPassant01() {
        MoveContainerReader<? extends Move> legalMoves = null;

        Game game = getGame("rnbqkbnr/pppppppp/8/1P6/8/8/P1PPPPPP/RNBQKBNR b KQkq - 0 2");

        // Estado inicial
        legalMoves = game.getPossibleMoves();

        assertEquals(19, legalMoves.size());
        assertFalse(contieneMove(legalMoves, Square.b5, Square.c6));
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        // Mueve el pawn pasante
        game.executeMove(Square.c7, Square.c5);

        // Podemos capturarlo
        legalMoves = game.getPossibleMoves();
        assertTrue(contieneMove(legalMoves, Square.b5, Square.c6));
        assertEquals(22, legalMoves.size());
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        // Pero NO lo capturamos
        game.executeMove(Square.h2, Square.h3);


        game.executeMove(Square.h7, Square.h6);

        // Ahora no podemos capturar el pawn pasante !!!
        legalMoves = game.getPossibleMoves();
        assertFalse(contieneMove(legalMoves, Square.b5, Square.c6));
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());
    }

    @Test
    public void testJuegoEnPassant02() {
        MoveContainerReader<? extends Move> legalMoves = null;

        Game game = getGame("rnbqkbnr/pppppppp/8/1P6/3P4/8/P1P1PPPP/RNBQKBNR b KQkq - 0 2");

        // Estado inicial
        legalMoves = game.getPossibleMoves();

        assertFalse(contieneMove(legalMoves, Square.b5, Square.c6));

        // Mueve el pawn pasante
        game.executeMove(Square.c7, Square.c5);

        // Podemos capturarlo
        legalMoves = game.getPossibleMoves();
        assertTrue(contieneMove(legalMoves, Square.b5, Square.c6));
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());

        // Capturamos peon pasante
        game.executeMove(Square.b5, Square.c6);
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());


        // Undo
        game.undoMove();
        assertTrue(contieneMove(legalMoves, Square.b5, Square.c6));
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());
    }

    @Test
    public void testPawnPinned() {
        Game game = getGame("rnbqkbnr/pp1ppppp/8/8/1Pp5/3P4/P1PKPPPP/RNBQ1BNR b kq b3 0 3");
        game.executeMove(Square.d8, Square.a5);

        MoveContainerReader<? extends Move> legalMoves = game.getPossibleMoves();
        assertTrue(contieneMove(legalMoves, Square.b4, Square.a5));
        assertEquals(getPolyglotKey(game), game.getPosition().getZobristHash());
    }

    @Test
    public void testLegalMove() {
        Game game = getGame("r1bq1rk1/ppp2ppp/3p1n2/3Np3/1bPnP3/5NP1/PP1P1PBP/R1BQ1RK1 b - - 2 8");

        MoveContainerReader<? extends Move> legalMoves = game.getPossibleMoves();
        assertTrue(contieneMove(legalMoves, Square.d4, Square.f3));
    }

    @Test
    public void testCapturePanwPassante01() {
        Game game = getGame("rnbqkbnr/2pppppp/p7/Pp6/8/8/1PPPPPPP/RNBQKBNR w KQkq b6 0 3");

        Move move = game.getMove(Square.a5, Square.b6);

        assertNotNull(move);
        assertEquals(Cardinal.NorteEste, move.getMoveDirection());
    }

    @Test
    public void testCapturePanwPassante02() {
        Game game = getGame("rnbqkbnr/1ppppppp/8/8/pP6/N7/P1PPPPPP/1RBQKBNR b Kkq b3 0 3");

        Move move = game.getMove(Square.a4, Square.b3);

        assertNotNull(move);
        assertEquals(Cardinal.SurEste, move.getMoveDirection());
    }

    @Test
    public void test_encode_with_clocks1() {
        FENBuilder coder = new FENBuilder();

        Game game = Game.from(FEN.of(FENParser.INITIAL_FEN));

        game.getPosition().constructChessPositionRepresentation(coder);

        FEN fen = coder.getPositionRepresentation();

        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", fen.toString());
    }

    @Test
    public void test_encode_with_clocks2() {
        FENBuilder coder = new FENBuilder();

        Game game = Game.from(FEN.of(FENParser.INITIAL_FEN));

        game.executeMove(Square.g1, Square.f3);

        game.getPosition().constructChessPositionRepresentation(coder);

        FEN fen = coder.getPositionRepresentation();

        assertEquals("rnbqkbnr/pppppppp/8/8/8/5N2/PPPPPPPP/RNBQKB1R b KQkq - 1 1", fen.toString());
    }


    @Test
    public void test_encode_with_clocks3() {
        FENBuilder coder = new FENBuilder();

        Game game = Game.from(FEN.of(FENParser.INITIAL_FEN));

        game.executeMove(Square.g1, Square.f3)
                .executeMove(Square.g8, Square.f6);

        game.getPosition().constructChessPositionRepresentation(coder);

        FEN fen = coder.getPositionRepresentation();

        assertEquals("rnbqkb1r/pppppppp/5n2/8/8/5N2/PPPPPPPP/RNBQKB1R w KQkq - 2 2", fen.toString());
    }

    protected boolean contieneMove(MoveContainerReader<? extends Move> movimientos, Square from, Square to) {
        for (Move move : movimientos) {
            if (from.equals(move.getFrom().getSquare()) && to.equals(move.getTo().getSquare())) {
                return true;
            }
        }
        return false;
    }


    private Game getGame(String string) {
        GameBuilder builder = new GameBuilderDebug();

        FENExporter exporter = new FENExporter(builder);

        exporter.export(FEN.of(string));

        return builder.getPositionRepresentation();
    }


    private long getPolyglotKey(Game game){
        PolyglotKeyBuilder polyglotKeyBuilder = new PolyglotKeyBuilder();
        game.getPosition().constructChessPositionRepresentation(polyglotKeyBuilder);
        return polyglotKeyBuilder.getPositionRepresentation();
    }

}

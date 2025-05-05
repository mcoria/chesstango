package net.chesstango.board.internal.position;

import net.chesstango.board.*;
import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.builders.GameBuilder;
import net.chesstango.board.builders.GameBuilderDebug;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENParser;
import net.chesstango.gardel.fen.FENExporter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Mauricio Coria
 */
public class PositionTest {

    private Game game;

    @Test
    public void testDefaultPosition() {
        setupWithDefaultBoard();

        MoveContainerReader<Move> moves = game.getPossibleMoves();

        assertNotNull(game.getMove(Square.a2, Square.a3));
        assertNotNull(game.getMove(Square.a2, Square.a4));

        assertNotNull(game.getMove(Square.b2, Square.b3));
        assertNotNull(game.getMove(Square.b2, Square.b4));

        assertNotNull(game.getMove(Square.c2, Square.c3));
        assertNotNull(game.getMove(Square.c2, Square.c4));

        assertNotNull(game.getMove(Square.d2, Square.d3));
        assertNotNull(game.getMove(Square.d2, Square.d4));

        assertNotNull(game.getMove(Square.e2, Square.e3));
        assertNotNull(game.getMove(Square.e2, Square.e4));

        assertNotNull(game.getMove(Square.f2, Square.f3));
        assertNotNull(game.getMove(Square.f2, Square.f4));

        assertNotNull(game.getMove(Square.g2, Square.g3));
        assertNotNull(game.getMove(Square.g2, Square.g4));

        assertNotNull(game.getMove(Square.h2, Square.h3));
        assertNotNull(game.getMove(Square.h2, Square.h4));

        //Knights
        assertNotNull(game.getMove(Square.b1, Square.a3));
        assertNotNull(game.getMove(Square.b1, Square.c3));

        //Knights
        assertNotNull(game.getMove(Square.g1, Square.f3));
        assertNotNull(game.getMove(Square.g1, Square.h3));

        //Debe haber 20 movimientos
        assertEquals(20, moves.size());

        //State
        assertEquals(Color.WHITE, game.getPosition().getCurrentTurn());
        assertNull(game.getPosition().getEnPassantSquare());
    }

    @Test
    public void testKingInCheck01() {
        setupWithBoard("r1bqkb1r/pppp1Qpp/2n4n/4p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 1");

        MoveContainerReader<Move> moves = game.getPossibleMoves();

        assertEquals(Color.BLACK, game.getPosition().getCurrentTurn());
        assertEquals(Status.CHECK, game.getStatus());

        assertNotNull(game.getMove(Square.h6, Square.f7));
        assertNull(game.getMove(Square.e8, Square.e7));
        assertNull(game.getMove(Square.e8, Square.f7));

        assertEquals(1, moves.size());
    }

    @Test
    public void testKingInCheck02() {
        setupWithBoard("rnb1kbnr/pp1ppppp/8/q1p5/8/3P4/PPPKPPPP/RNBQ1BNR w KQkq - 0 1");

        AnalyzerResult result = game.getState().getAnalyzerResult();

        assertEquals(Color.WHITE, game.getPosition().getCurrentTurn());
        assertTrue(result.isKingInCheck());

        MoveContainerReader<Move> moves = game.getPossibleMoves();

        assertNotNull(game.getMove(Square.b1, Square.c3));
        assertNotNull(game.getMove(Square.b2, Square.b4));
        assertNotNull(game.getMove(Square.c2, Square.c3));
        assertNotNull(game.getMove(Square.d2, Square.e3));

        assertNull(game.getMove(Square.d2, Square.e1));

        assertEquals(4, moves.size());
    }

    @Test
    public void testJuegoCastlingWhiteJaque() {
        setupWithBoard("r3k3/8/8/8/4r3/8/8/R3K2R w KQq - 0 1");

        AnalyzerResult result = game.getState().getAnalyzerResult();

        assertEquals(Color.WHITE, game.getPosition().getCurrentTurn());
        assertTrue(result.isKingInCheck());

        MoveContainerReader<Move> moves = game.getPossibleMoves();

        assertNotNull(game.getMove(Square.e1, Square.d1));
        assertNotNull(game.getMove(Square.e1, Square.d2));
        assertNull(game.getMove(Square.e1, Square.e2));
        assertNotNull(game.getMove(Square.e1, Square.f2));
        assertNotNull(game.getMove(Square.e1, Square.f1));

        //assertFalse(game.getMove(moveFactoryWhite.createCastlingKingMove()));
        //assertFalse(game.getMove(moveFactoryWhite.createCastlingQueenMove()));

        assertEquals(4, moves.size());
    }

    @Test
    public void testJuegoPawnPromocion() {
        setupWithBoard("r3k2r/p1ppqpb1/bn1Ppnp1/4N3/1p2P3/2N2Q2/PPPBBPpP/R4RK1 b kq - 0 2");

        MoveContainerReader<Move> moves = game.getPossibleMoves();

        assertNotNull(game.getMove(Square.g2, Square.f1,
                Piece.ROOK_BLACK));
        assertNotNull(game.getMove(Square.g2, Square.f1,
                Piece.KNIGHT_BLACK));
        assertNotNull(game.getMove(Square.g2, Square.f1,
                Piece.BISHOP_BLACK));
        assertNotNull(game.getMove(Square.g2, Square.f1,
                Piece.QUEEN_BLACK));

        assertEquals(46, moves.size());
    }


    @Test
    public void testJauqeMate() {
        setupWithBoard("r1bqk1nr/pppp1Qpp/2n5/2b1p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 1");

        AnalyzerResult result = game.getState().getAnalyzerResult();

        assertTrue(result.isKingInCheck());
        assertTrue(game.getPossibleMoves().isEmpty());
        assertEquals(Status.MATE, game.getStatus());

    }

    @Test
    public void testKingNoPuedeMoverAJaque() {
        setupWithBoard("8/8/8/8/8/8/6k1/4K2R w K - 0 1");

        MoveContainerReader<Move> moves = game.getPossibleMoves();

        assertNull(game.getMove(Square.e1, Square.f2));
        assertNull(game.getMove(Square.e1, Square.f1));
        assertNull(game.getMove(Square.e1, Square.g1));

        assertEquals(12, moves.size());

    }

    @Test
    public void testMovimientoEnPassantNoAllowed() {
        setupWithBoard("8/2p5/3p4/KP5r/1R3pPk/8/4P3/8 b - g3 0 1");

        MoveContainerReader<Move> moves = game.getPossibleMoves();

        assertNull(game.getMove(Square.f4, Square.g3));

        assertEquals(17, moves.size());

    }


    @Test
    public void testGetZobristHashMove() {
        setupWithDefaultBoard();

        MoveContainerReader<Move> moves = game.getPossibleMoves();

        long initialZobristHash = game.getPosition().getZobristHash();

        for (Move theMove : moves) {

            long zobristHash = theMove.getZobristHash();

            theMove.executeMove();

            assertEquals(zobristHash, game.getPosition().getZobristHash());

            theMove.undoMove();
        }

        assertEquals(initialZobristHash, game.getPosition().getZobristHash());
    }


    private void setupWithDefaultBoard() {
        setupWithBoard(FENParser.INITIAL_FEN);
    }


    private void setupWithBoard(String string) {
        GameBuilder builder = new GameBuilderDebug();

        FENExporter exporter = new FENExporter(builder);

        exporter.export(FEN.of(string));

        game = builder.getPositionRepresentation();
    }


}

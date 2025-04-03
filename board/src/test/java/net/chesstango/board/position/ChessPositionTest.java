package net.chesstango.board.position;

import net.chesstango.board.*;
import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.analyzer.PositionAnalyzer;
import net.chesstango.board.builders.GameBuilder;
import net.chesstango.board.builders.GameBuilderDebug;
import net.chesstango.board.debug.builder.ChessFactoryDebug;
import net.chesstango.board.factory.ChessInjector;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.moves.imp.MoveImp;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Mauricio Coria
 */
public class ChessPositionTest {

    private MoveFactory moveFactoryWhite;

    private MoveFactory moveFactoryBlack;

    private PositionAnalyzer analyzer;

    private ChessPosition chessPosition;

    private GameState gameState;

    @Test
    public void testDefaultPosition() {
        setupWithDefaultBoard();

        MoveContainerReader<Move> moves = gameState.getLegalMoves();

        assertTrue(moves.contains(createSimplePawnMove(Square.a2, Piece.PAWN_WHITE, Square.a3)));
        assertTrue(moves.contains(createSaltoDobleMove(Square.a2, Piece.PAWN_WHITE, Square.a4, Square.a3)));

        assertTrue(moves.contains(createSimplePawnMove(Square.b2, Piece.PAWN_WHITE, Square.b3)));
        assertTrue(moves.contains(createSaltoDobleMove(Square.b2, Piece.PAWN_WHITE, Square.b4, Square.b3)));

        assertTrue(moves.contains(createSimplePawnMove(Square.c2, Piece.PAWN_WHITE, Square.c3)));
        assertTrue(moves.contains(createSaltoDobleMove(Square.c2, Piece.PAWN_WHITE, Square.c4, Square.c3)));

        assertTrue(moves.contains(createSimplePawnMove(Square.d2, Piece.PAWN_WHITE, Square.d3)));
        assertTrue(moves.contains(createSaltoDobleMove(Square.d2, Piece.PAWN_WHITE, Square.d4, Square.d3)));

        assertTrue(moves.contains(createSimplePawnMove(Square.e2, Piece.PAWN_WHITE, Square.e3)));
        assertTrue(moves.contains(createSaltoDobleMove(Square.e2, Piece.PAWN_WHITE, Square.e4, Square.e3)));

        assertTrue(moves.contains(createSimplePawnMove(Square.f2, Piece.PAWN_WHITE, Square.f3)));
        assertTrue(moves.contains(createSaltoDobleMove(Square.f2, Piece.PAWN_WHITE, Square.f4, Square.f3)));

        assertTrue(moves.contains(createSimplePawnMove(Square.g2, Piece.PAWN_WHITE, Square.g3)));
        assertTrue(moves.contains(createSaltoDobleMove(Square.g2, Piece.PAWN_WHITE, Square.g4, Square.g3)));

        assertTrue(moves.contains(createSimplePawnMove(Square.h2, Piece.PAWN_WHITE, Square.h3)));
        assertTrue(moves.contains(createSaltoDobleMove(Square.h2, Piece.PAWN_WHITE, Square.h4, Square.h3)));

        //CapturerByKnight Kingna
        assertTrue(moves.contains(createSimpleMove(Square.b1, Piece.KNIGHT_WHITE, Square.a3)));
        assertTrue(moves.contains(createSimpleMove(Square.b1, Piece.KNIGHT_WHITE, Square.c3)));

        //CapturerByKnight King
        assertTrue(moves.contains(createSimpleMove(Square.g1, Piece.KNIGHT_WHITE, Square.f3)));
        assertTrue(moves.contains(createSimpleMove(Square.g1, Piece.KNIGHT_WHITE, Square.h3)));

        //Debe haber 20 movimientos
        assertEquals(20, moves.size());

        //State
        assertEquals(Color.WHITE, chessPosition.getCurrentTurn());
        assertNull(chessPosition.getEnPassantSquare());
    }

    @Test
    public void testKingInCheck01() {
        setupWithBoard("r1bqkb1r/pppp1Qpp/2n4n/4p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 1");

        MoveContainerReader<Move> moves = gameState.getLegalMoves();

        assertEquals(Color.BLACK, chessPosition.getCurrentTurn());
        assertEquals(GameStatus.CHECK, gameState.getStatus());

        assertTrue(moves.contains(createCaptureMove(Square.h6, Piece.KNIGHT_BLACK, Square.f7, Piece.QUEEN_WHITE)));
        assertFalse(moves.contains(createSimpleMove(Square.e8, Piece.KING_BLACK, Square.e7)));
        assertFalse(moves.contains(createCaptureMove(Square.e8, Piece.KING_BLACK, Square.f7, Piece.QUEEN_WHITE)));

        assertEquals(1, moves.size());
    }

    @Test
    public void testKingInCheck02() {
        setupWithBoard("rnb1kbnr/pp1ppppp/8/q1p5/8/3P4/PPPKPPPP/RNBQ1BNR w KQkq - 0 1");

        AnalyzerResult result = analyzer.analyze();

        assertEquals(Color.WHITE, chessPosition.getCurrentTurn());
        assertTrue(result.isKingInCheck());

        MoveContainerReader<Move> moves = gameState.getLegalMoves();

        assertTrue(moves.contains(createSimpleMove(Square.b1, Piece.KNIGHT_WHITE, Square.c3)));
        assertTrue(moves.contains(createSaltoDobleMove(Square.b2, Piece.PAWN_WHITE, Square.b4, Square.b3)));
        assertTrue(moves.contains(createSimplePawnMove(Square.c2, Piece.PAWN_WHITE, Square.c3)));
        assertTrue(moves.contains(createSimpleMove(Square.d2, Piece.KING_WHITE, Square.e3)));

        assertFalse(moves.contains(createSimpleMove(Square.d2, Piece.KING_WHITE, Square.e1)));

        assertEquals(4, moves.size());
    }

    @Test
    public void testJuegoCastlingWhiteJaque() {
        setupWithBoard("r3k3/8/8/8/4r3/8/8/R3K2R w KQq - 0 1");

        AnalyzerResult result = analyzer.analyze();

        assertEquals(Color.WHITE, chessPosition.getCurrentTurn());
        assertTrue(result.isKingInCheck());

        MoveContainerReader<Move> moves = gameState.getLegalMoves();

        assertTrue(moves.contains(createSimpleKingMoveWhite(Square.e1, Square.d1)));
        assertTrue(moves.contains(createSimpleKingMoveWhite(Square.e1, Square.d2)));
        assertFalse(moves.contains(createSimpleKingMoveWhite(Square.e1, Square.e2)));
        assertTrue(moves.contains(createSimpleKingMoveWhite(Square.e1, Square.f2)));
        assertTrue(moves.contains(createSimpleKingMoveWhite(Square.e1, Square.f1)));

        assertFalse(moves.contains(moveFactoryWhite.createCastlingKingMove()));
        assertFalse(moves.contains(moveFactoryWhite.createCastlingQueenMove()));

        assertEquals(4, moves.size());
    }

    @Test
    public void testJuegoPawnPromocion() {
        setupWithBoard("r3k2r/p1ppqpb1/bn1Ppnp1/4N3/1p2P3/2N2Q2/PPPBBPpP/R4RK1 b kq - 0 2");

        MoveContainerReader<Move> moves = gameState.getLegalMoves();

        assertTrue(moves.contains(createCapturePawnPromocionBlack(Square.g2, Piece.PAWN_BLACK, Square.f1, Piece.ROOK_WHITE,
                Piece.ROOK_BLACK, Cardinal.SurOeste)));
        assertTrue(moves.contains(createCapturePawnPromocionBlack(Square.g2, Piece.PAWN_BLACK, Square.f1, Piece.ROOK_WHITE,
                Piece.KNIGHT_BLACK, Cardinal.SurOeste)));
        assertTrue(moves.contains(createCapturePawnPromocionBlack(Square.g2, Piece.PAWN_BLACK, Square.f1, Piece.ROOK_WHITE,
                Piece.BISHOP_BLACK, Cardinal.SurOeste)));
        assertTrue(moves.contains(createCapturePawnPromocionBlack(Square.g2, Piece.PAWN_BLACK, Square.f1, Piece.ROOK_WHITE,
                Piece.QUEEN_BLACK, Cardinal.SurOeste)));

        assertEquals(46, moves.size());
    }


    @Test
    public void testJauqeMate() {
        setupWithBoard("r1bqk1nr/pppp1Qpp/2n5/2b1p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 1");

        AnalyzerResult result = analyzer.analyze();

        assertTrue(result.isKingInCheck());
        assertTrue(gameState.getLegalMoves().isEmpty());
        assertEquals(GameStatus.MATE, gameState.getStatus());

    }

    @Test
    public void testKingNoPuedeMoverAJaque() {
        setupWithBoard("8/8/8/8/8/8/6k1/4K2R w K - 0 1");

        MoveContainerReader<Move> moves = gameState.getLegalMoves();

        assertFalse(moves.contains(createSimpleMove(Square.e1, Piece.KING_WHITE, Square.f2)));
        assertFalse(moves.contains(createSimpleMove(Square.e1, Piece.KING_WHITE, Square.f1)));
        assertFalse(moves.contains(moveFactoryWhite.createCastlingKingMove()));

        assertEquals(12, moves.size());

    }

    @Test
    public void testMovimientoEnPassantNoAllowed() {
        setupWithBoard("8/2p5/3p4/KP5r/1R3pPk/8/4P3/8 b - g3 0 1");

        MoveContainerReader<Move> moves = gameState.getLegalMoves();

        assertFalse(moves.contains(createCaptureEnPassantMoveBlack(Square.f4, Square.g3)));

        assertEquals(17, moves.size());

    }


    @Test
    public void testGetZobristHashMove() {
        setupWithDefaultBoard();

        MoveContainerReader<Move> moves = gameState.getLegalMoves();

        long initialZobristHash = chessPosition.getZobristHash();

        for (Move theMove : moves) {

            MoveImp move = (MoveImp) theMove;

            long zobristHash = move.getZobristHash();

            move.doMove(chessPosition);

            assertEquals(zobristHash, chessPosition.getZobristHash());

            move.undoMove(chessPosition);
        }

        assertEquals(initialZobristHash, chessPosition.getZobristHash());
    }


    private Move createSimpleMove(Square origenSquare, Piece origenPieza, Square destinoSquare) {
        return moveFactoryWhite.createSimpleKnightMove(PiecePositioned.of(origenSquare, origenPieza), PiecePositioned.of(destinoSquare, null));
    }

    private Move createCaptureMove(Square origenSquare, Piece origenPieza, Square destinoSquare, Piece destinoPieza) {
        return moveFactoryWhite.createCaptureKnightMove(PiecePositioned.of(origenSquare, origenPieza), PiecePositioned.of(destinoSquare, destinoPieza));
    }


    private Move createSimplePawnMove(Square origenSquare, Piece origenPieza, Square destinoSquare) {
        return moveFactoryWhite.createSimpleOneSquarePawnMove(PiecePositioned.of(origenSquare, origenPieza), PiecePositioned.of(destinoSquare, null));
    }

    private Move createSaltoDobleMove(Square origen, Piece piece, Square destinoSquare, Square squarePasante) {
        return moveFactoryWhite.createSimpleTwoSquaresPawnMove(PiecePositioned.of(origen, piece), PiecePositioned.of(destinoSquare, null), squarePasante);
    }

    private Move createCapturePawnPromocionBlack(Square origenSquare, Piece origenPieza, Square destinoSquare,
                                                 Piece destinoPieza, Piece promocion, Cardinal cardinal) {
        return moveFactoryBlack.createCapturePromotionPawnMove(PiecePositioned.of(origenSquare, origenPieza),
                PiecePositioned.of(destinoSquare, destinoPieza), promocion, cardinal);
    }

    private Move createCaptureEnPassantMoveBlack(Square origen, Square destinoSquare) {
        return moveFactoryWhite.createCaptureEnPassantPawnMove(PiecePositioned.of(origen, Piece.PAWN_BLACK),
                PiecePositioned.of(destinoSquare, null), PiecePositioned.of(
                        Square.getSquare(destinoSquare.getFile(), destinoSquare.getRank() + 1), Piece.PAWN_WHITE), Cardinal.calculateSquaresDirection(origen, destinoSquare));
    }

    private Move createSimpleKingMoveWhite(Square origen, Square destino) {
        return moveFactoryWhite.createSimpleKingMove(PiecePositioned.of(origen, Piece.KING_WHITE), PiecePositioned.of(destino, null));
    }

    private void setupWithDefaultBoard() {
        setupWithBoard(FENDecoder.INITIAL_FEN);
    }
    

    private void setupWithBoard(String string) {
        ChessInjector injector = new ChessInjector(new ChessFactoryDebug());

        GameBuilder builder = new GameBuilderDebug(injector);

        FENDecoder parser = new FENDecoder(builder);
        parser.parseFEN(string);

        Game game = builder.getChessRepresentation();

        moveFactoryWhite = injector.getMoveFactoryWhite();

        moveFactoryBlack = injector.getMoveFactoryBlack();

        chessPosition = injector.getChessPosition();

        gameState = injector.getGameState();

        analyzer = injector.getAnalyzer();
    }


}

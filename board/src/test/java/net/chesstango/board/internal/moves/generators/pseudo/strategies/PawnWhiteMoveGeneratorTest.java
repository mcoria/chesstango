package net.chesstango.board.internal.moves.generators.pseudo.strategies;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.SquareBoardBuilder;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.internal.moves.factories.MoveFactoryWhite;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPieceResult;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENExporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



/**
 * @author Mauricio Coria
 */
public class PawnWhiteMoveGeneratorTest {
    private PawnWhiteMoveGenerator moveGenerator;

    private Collection<PseudoMove> moves;

    private MoveFactory moveFactoryImp;

    @BeforeEach
    public void setUp() throws Exception {
        moveFactoryImp = new MoveFactoryWhite();
        moves = new ArrayList<>();

        moveGenerator = new PawnWhiteMoveGenerator();
        moveGenerator.setMoveFactory(moveFactoryImp);
    }

    @Test
    public void testSaltoSimple() {
        SquareBoard tablero = getTablero("8/8/8/8/8/P7/8/8 w KQkq - 0 1");

        moveGenerator.setSquareBoard(tablero);

        Square from = Square.a3;
        assertEquals(Piece.PAWN_WHITE, tablero.getPiece(from));

        PiecePositioned origen = PiecePositioned.of(from, Piece.PAWN_WHITE);

        MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();

        assertEquals(1, moves.size());

        assertTrue(moves.contains(createSimplePawnMove(origen, Square.a4)));
    }

    @Test
    public void testSaltoDoble() {
        SquareBoard tablero = getTablero("8/8/8/8/8/8/P7/8 w KQkq - 0 1");

        moveGenerator.setSquareBoard(tablero);

        Square from = Square.a2;
        assertEquals(Piece.PAWN_WHITE, tablero.getPiece(from));

        PiecePositioned origen = PiecePositioned.of(from, Piece.PAWN_WHITE);

        MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();

        assertEquals(2, moves.size());

        assertTrue(moves.contains(createSimplePawnMove(origen, Square.a3)));
        assertTrue(moves.contains(createSaltoDobleMove(origen, Square.a4, Square.a3)));
    }

    @Test
    public void testSaltoDoble01() {
        SquareBoard tablero = getTablero("8/8/8/8/8/N7/P7/8 w KQkq - 0 1");

        moveGenerator.setSquareBoard(tablero);

        Square from = Square.a2;
        assertEquals(Piece.PAWN_WHITE, tablero.getPiece(from));

        PiecePositioned origen = PiecePositioned.of(from, Piece.PAWN_WHITE);

        MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();

        assertEquals(0, moves.size());


    }

    @Test
    public void testAtaqueIzquierda() {
        SquareBoard tablero = getTablero("8/8/8/8/8/3p4/4P3/8 w KQkq - 0 1");

        moveGenerator.setSquareBoard(tablero);

        Square from = Square.e2;
        assertEquals(Piece.PAWN_WHITE, tablero.getPiece(from));
        assertEquals(Piece.PAWN_BLACK, tablero.getPiece(Square.d3));

        PiecePositioned origen = PiecePositioned.of(from, Piece.PAWN_WHITE);

        MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();

        assertEquals(3, moves.size());

        assertTrue(moves.contains(createSimplePawnMove(origen, Square.e3)));
        assertTrue(moves.contains(createSaltoDobleMove(origen, Square.e4, Square.e3)));
        assertTrue(moves.contains(createCapturePawnMove(origen, Square.d3, Piece.PAWN_BLACK, Cardinal.NorteOeste)));
    }

    @Test
    public void testAtaqueIzquierda01() {
        SquareBoard tablero = getTablero("rnb1kbnr/pp1ppppp/8/q7/1Pp5/3P4/P1PKPPPP/RNBQ1BNR w KQkq - 0 1");

        moveGenerator.setSquareBoard(tablero);

        Square from = Square.b4;
        assertEquals(Piece.PAWN_WHITE, tablero.getPiece(from));
        assertEquals(Piece.QUEEN_BLACK, tablero.getPiece(Square.a5));

        PiecePositioned origen = PiecePositioned.of(from, Piece.QUEEN_BLACK);

        MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();

        assertEquals(2, moves.size());

        assertTrue(moves.contains(createSimplePawnMove(origen, Square.b5)));
        assertTrue(moves.contains(createCapturePawnMove(origen, Square.a5, Piece.QUEEN_BLACK, Cardinal.NorteOeste)));
    }


    @Test
    public void testAtaqueDerecha() {
        SquareBoard tablero = getTablero("8/8/8/8/8/5p2/4P3/8 w KQkq - 0 1");

        moveGenerator.setSquareBoard(tablero);

        Square from = Square.e2;
        assertEquals(Piece.PAWN_WHITE, tablero.getPiece(from));
        assertEquals(Piece.PAWN_BLACK, tablero.getPiece(Square.f3));

        PiecePositioned origen = PiecePositioned.of(from, Piece.PAWN_WHITE);

        MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();

        assertEquals(3, moves.size());

        assertTrue(moves.contains(createSimplePawnMove(origen, Square.e3)));
        assertTrue(moves.contains(createSaltoDobleMove(origen, Square.e4, Square.e3)));
        assertTrue(moves.contains(createCapturePawnMove(origen, Square.f3, Piece.PAWN_BLACK, Cardinal.NorteEste)));
    }

    @Test
    public void testPawnSimplePawnPromocion() {
        SquareBoard tablero = getTablero("8/3P4/8/8/8/8/8/8 w KQkq - 0 1");

        moveGenerator.setSquareBoard(tablero);

        Square from = Square.d7;

        assertEquals(Piece.PAWN_WHITE, tablero.getPiece(from));

        PiecePositioned origen = PiecePositioned.of(from, Piece.PAWN_WHITE);

        MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();

        assertTrue(moves.contains(createSimplePawnPromocion(origen, Square.d8, Piece.ROOK_WHITE)));
        assertTrue(moves.contains(createSimplePawnPromocion(origen, Square.d8, Piece.KNIGHT_WHITE)));
        assertTrue(moves.contains(createSimplePawnPromocion(origen, Square.d8, Piece.BISHOP_WHITE)));
        assertTrue(moves.contains(createSimplePawnPromocion(origen, Square.d8, Piece.QUEEN_WHITE)));

        assertEquals(4, moves.size());
    }

    @Test
    public void testPawnCapturaPawnPromocion() {
        SquareBoard tablero = getTablero("2rr4/3P4/8/8/8/8/8/8 w KQkq - 0 1");
        moveGenerator.setSquareBoard(tablero);

        Square from = Square.d7;

        assertEquals(Piece.PAWN_WHITE, tablero.getPiece(from));
        assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.c8));
        assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.d8));

        PiecePositioned origen = PiecePositioned.of(from, Piece.PAWN_WHITE);

        MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();

        assertTrue(moves.contains(createCapturePawnPromocion(origen, Square.c8, Piece.ROOK_BLACK, Piece.ROOK_WHITE, Cardinal.NorteOeste)));
        assertTrue(moves.contains(createCapturePawnPromocion(origen, Square.c8, Piece.ROOK_BLACK, Piece.KNIGHT_WHITE, Cardinal.NorteOeste)));
        assertTrue(moves.contains(createCapturePawnPromocion(origen, Square.c8, Piece.ROOK_BLACK, Piece.BISHOP_WHITE, Cardinal.NorteOeste)));
        assertTrue(moves.contains(createCapturePawnPromocion(origen, Square.c8, Piece.ROOK_BLACK, Piece.QUEEN_WHITE, Cardinal.NorteOeste)));

        assertEquals(4, moves.size());
    }

    private PseudoMove createSimplePawnMove(PiecePositioned origen, Square destinoSquare) {
        return moveFactoryImp.createSimpleOneSquarePawnMove(origen, PiecePositioned.of(destinoSquare, null));
    }

    private PseudoMove createSaltoDobleMove(PiecePositioned origen, Square destinoSquare, Square squarePasante) {
        return moveFactoryImp.createSimpleTwoSquaresPawnMove(origen, PiecePositioned.of(destinoSquare, null), squarePasante);
    }

    private PseudoMove createCapturePawnMove(PiecePositioned origen, Square destinoSquare, Piece destinoPieza, Cardinal cardinal) {
        return moveFactoryImp.createCapturePawnMove(origen, PiecePositioned.of(destinoSquare, destinoPieza), cardinal);
    }

    private PseudoMove createSimplePawnPromocion(PiecePositioned origen, Square destinoSquare, Piece promocion) {
        return moveFactoryImp.createSimplePromotionPawnMove(origen, PiecePositioned.of(destinoSquare, null), promocion);
    }

    private PseudoMove createCapturePawnPromocion(PiecePositioned origen, Square destinoSquare, Piece destinoPieza, Piece promocion, Cardinal direction) {
        return moveFactoryImp.createCapturePromotionPawnMove(origen, PiecePositioned.of(destinoSquare, destinoPieza), promocion, direction);
    }

    private SquareBoard getTablero(String string) {
        SquareBoardBuilder builder = new SquareBoardBuilder();

        FENExporter exporter = new FENExporter(builder);

        exporter.export(FEN.of(string));

        return builder.getPositionRepresentation();
    }
}

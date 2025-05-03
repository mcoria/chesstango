package net.chesstango.board.internal.moves.generators.pseudo.strategies;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.SquareBoardBuilder;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.internal.moves.factories.MoveFactoryBlack;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPieceResult;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENExporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 */
public class PawnBlackMoveGeneratorTest {

    private PawnBlackMoveGenerator moveGenerator;

    private Collection<PseudoMove> moves;

    private MoveFactory moveFactoryImp;

    @BeforeEach
    public void setUp() throws Exception {
        moveFactoryImp = new MoveFactoryBlack();
        moves = new ArrayList<>();

        moveGenerator = new PawnBlackMoveGenerator();
        moveGenerator.setMoveFactory(moveFactoryImp);
    }

    @Test
    public void testSaltoSimple() {
        SquareBoard tablero = getTablero("8/8/p7/8/8/8/8/8 w KQkq - 0 1");

        moveGenerator.setSquareBoard(tablero);

        Square from = Square.a6;
        assertEquals(Piece.PAWN_BLACK, tablero.getPiece(from));

        PiecePositioned origen = PiecePositioned.of(from, Piece.PAWN_BLACK);

        MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();

        assertEquals(1, moves.size());

        assertTrue(moves.contains(createSimplePawnMove(origen, Square.a5)));
    }

    @Test
    public void testSaltoDoble() {
        SquareBoard tablero = getTablero("8/p7/8/8/8/8/8/8 w KQkq - 0 1");

        moveGenerator.setSquareBoard(tablero);

        Square from = Square.a7;
        assertEquals(Piece.PAWN_BLACK, tablero.getPiece(from));

        PiecePositioned origen = PiecePositioned.of(from, Piece.PAWN_BLACK);

        MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();

        assertEquals(2, moves.size());

        assertTrue(moves.contains(createSimplePawnMove(origen, Square.a6)));
        assertTrue(moves.contains(createSimpleTwoSquaresPawnMove(origen, Square.a5, Square.a6)));
    }

    @Test
    public void testAtaqueIzquierda() {
        SquareBoard tablero = getTablero("8/4p3/3P4/8/8/8/8/8 w KQkq - 0 1");

        moveGenerator.setSquareBoard(tablero);

        Square from = Square.e7;
        assertEquals(Piece.PAWN_BLACK, tablero.getPiece(from));
        assertEquals(Piece.PAWN_WHITE, tablero.getPiece(Square.d6));

        PiecePositioned origen = PiecePositioned.of(from, Piece.PAWN_BLACK);

        MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();

        assertEquals(3, moves.size());

        assertTrue(moves.contains(createSimplePawnMove(origen, Square.e6)));
        assertTrue(moves.contains(createSimpleTwoSquaresPawnMove(origen, Square.e5, Square.e6)));
        assertTrue(moves.contains(createCapturePawnMove(origen, Square.d6, Piece.PAWN_WHITE, Cardinal.SurOeste)));
    }

    @Test
    public void testAtaqueDerecha() {
        SquareBoard tablero = getTablero("8/4p3/5P2/8/8/8/8/8 w KQkq - 0 1");

        moveGenerator.setSquareBoard(tablero);

        Square from = Square.e7;
        assertEquals(Piece.PAWN_BLACK, tablero.getPiece(from));
        assertEquals(Piece.PAWN_WHITE, tablero.getPiece(Square.f6));

        PiecePositioned origen = PiecePositioned.of(from, Piece.PAWN_BLACK);

        MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();

        assertTrue(moves.contains(createSimplePawnMove(origen, Square.e6)));
        assertTrue(moves.contains(createSimpleTwoSquaresPawnMove(origen, Square.e5, Square.e6)));
        assertTrue(moves.contains(createCapturePawnMove(origen, Square.f6, Piece.PAWN_WHITE, Cardinal.SurEste)));

        assertEquals(3, moves.size());
    }

    private PseudoMove createSimplePawnMove(PiecePositioned origen, Square destinoSquare) {
        return moveFactoryImp.createSimpleOneSquarePawnMove(origen, PiecePositioned.of(destinoSquare, null));
    }

    private PseudoMove createSimpleTwoSquaresPawnMove(PiecePositioned origen, Square destinoSquare, Square squarePasante) {
        return moveFactoryImp.createSimpleTwoSquaresPawnMove(origen, PiecePositioned.of(destinoSquare, null), squarePasante);
    }

    private PseudoMove createCapturePawnMove(PiecePositioned origen, Square destinoSquare, Piece destinoPieza, Cardinal cardinal) {
        return moveFactoryImp.createCapturePawnMove(origen, PiecePositioned.of(destinoSquare, destinoPieza), cardinal);
    }

    private PseudoMove createSimplePawnPromocion(PiecePositioned origen, Square destinoSquare, Piece promocion) {
        return moveFactoryImp.createSimplePromotionPawnMove(origen, PiecePositioned.of(destinoSquare, null), promocion);
    }

    private PseudoMove createCapturePawnPromocion(PiecePositioned origen, Square destinoSquare, Piece destinoPieza, Piece promocion) {
        return moveFactoryImp.createCapturePromotionPawnMove(origen, PiecePositioned.of(destinoSquare, destinoPieza), promocion, Cardinal.NorteEste);
    }

    private SquareBoard getTablero(String string) {
        SquareBoardBuilder builder = new SquareBoardBuilder();

        FENExporter exporter = new FENExporter(builder);

        exporter.export(FEN.of(string));

        return builder.getPositionRepresentation();
    }
}

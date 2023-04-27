package net.chesstango.board.movesgenerators.pseudo.strategies;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.PiecePlacementBuilder;
import net.chesstango.board.debug.builder.ChessFactoryDebug;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveFactory;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;
import net.chesstango.board.position.Board;
import net.chesstango.board.representations.fen.FENDecoder;
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

    private Collection<Move> moves;

    private MoveFactory moveFactoryImp;

    @BeforeEach
    public void setUp() throws Exception {
        moveFactoryImp = SingletonMoveFactories.getDefaultMoveFactoryBlack();
        moves = new ArrayList<Move>();

        moveGenerator = new PawnBlackMoveGenerator();
        moveGenerator.setMoveFactory(moveFactoryImp);
    }

    @Test
    public void testSaltoSimple() {
        Board tablero = getTablero("8/8/p7/8/8/8/8/8");

        moveGenerator.setPiecePlacement(tablero);

        Square from = Square.a6;
        assertEquals(Piece.PAWN_BLACK, tablero.getPiece(from));

        PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_BLACK);

        MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();

        assertEquals(1, moves.size());

        assertTrue(moves.contains(createSimplePawnMove(origen, Square.a5)));
    }

    @Test
    public void testSaltoDoble() {
        Board tablero = getTablero("8/p7/8/8/8/8/8/8");

        moveGenerator.setPiecePlacement(tablero);

        Square from = Square.a7;
        assertEquals(Piece.PAWN_BLACK, tablero.getPiece(from));

        PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_BLACK);

        MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();

        assertEquals(2, moves.size());

        assertTrue(moves.contains(createSimplePawnMove(origen, Square.a6)));
        assertTrue(moves.contains(createSimpleTwoSquaresPawnMove(origen, Square.a5, Square.a6)));
    }

    @Test
    public void testAtaqueIzquierda() {
        Board tablero = getTablero("8/4p3/3P4/8/8/8/8/8");

        moveGenerator.setPiecePlacement(tablero);

        Square from = Square.e7;
        assertEquals(Piece.PAWN_BLACK, tablero.getPiece(from));
        assertEquals(Piece.PAWN_WHITE, tablero.getPiece(Square.d6));

        PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_BLACK);

        MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();

        assertEquals(3, moves.size());

        assertTrue(moves.contains(createSimplePawnMove(origen, Square.e6)));
        assertTrue(moves.contains(createSimpleTwoSquaresPawnMove(origen, Square.e5, Square.e6)));
        assertTrue(moves.contains(createCapturePawnMove(origen, Square.d6, Piece.PAWN_WHITE, Cardinal.SurOeste)));
    }

    @Test
    public void testAtaqueDerecha() {
        Board tablero = getTablero("8/4p3/5P2/8/8/8/8/8");

        moveGenerator.setPiecePlacement(tablero);

        Square from = Square.e7;
        assertEquals(Piece.PAWN_BLACK, tablero.getPiece(from));
        assertEquals(Piece.PAWN_WHITE, tablero.getPiece(Square.f6));

        PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_BLACK);

        MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();

        assertTrue(moves.contains(createSimplePawnMove(origen, Square.e6)));
        assertTrue(moves.contains(createSimpleTwoSquaresPawnMove(origen, Square.e5, Square.e6)));
        assertTrue(moves.contains(createCapturePawnMove(origen, Square.f6, Piece.PAWN_WHITE, Cardinal.SurEste)));

        assertEquals(3, moves.size());
    }

    private Move createSimplePawnMove(PiecePositioned origen, Square destinoSquare) {
        return moveFactoryImp.createSimpleOneSquarePawnMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, null));
    }

    private Move createSimpleTwoSquaresPawnMove(PiecePositioned origen, Square destinoSquare, Square squarePasante) {
        return moveFactoryImp.createSimpleTwoSquaresPawnMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, null), squarePasante);
    }

    private Move createCapturePawnMove(PiecePositioned origen, Square destinoSquare, Piece destinoPieza, Cardinal cardinal) {
        return moveFactoryImp.createCapturePawnMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, destinoPieza), cardinal);
    }

    private Move createSimplePawnPromocion(PiecePositioned origen, Square destinoSquare, Piece promocion) {
        return moveFactoryImp.createSimplePromotionPawnMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, null), promocion);
    }

    private Move createCapturePawnPromocion(PiecePositioned origen, Square destinoSquare, Piece destinoPieza, Piece promocion) {
        return moveFactoryImp.createCapturePromotionPawnMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, destinoPieza), promocion, Cardinal.NorteEste);
    }

    private Board getTablero(String string) {
        PiecePlacementBuilder builder = new PiecePlacementBuilder(new ChessFactoryDebug());

        FENDecoder parser = new FENDecoder(builder);

        parser.parsePiecePlacement(string);

        return builder.getChessRepresentation();
    }
}

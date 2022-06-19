package chess.board.movesgenerators.pseudo.strategies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import chess.board.iterators.Cardinal;
import chess.board.moves.imp.MoveFactoryBlack;
import org.junit.Before;
import org.junit.Test;

import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.builder.imp.PiecePlacementBuilder;
import chess.board.debug.builder.ChessFactoryDebug;
import chess.board.representations.fen.FENDecoder;
import chess.board.moves.Move;
import chess.board.position.PiecePlacement;
import chess.board.movesgenerators.pseudo.MoveGeneratorResult;

/**
 * @author Mauricio Coria
 */
public class PawnBlackMoveGeneratorTest {

    private PawnBlackMoveGenerator moveGenerator;

    private Collection<Move> moves;

    private MoveFactoryBlack moveFactoryImp;

    @Before
    public void setUp() throws Exception {
        moveFactoryImp = new MoveFactoryBlack();
        moves = new ArrayList<Move>();

        moveGenerator = new PawnBlackMoveGenerator();
        moveGenerator.setMoveFactory(moveFactoryImp);
    }

    @Test
    public void testSaltoSimple() {
        PiecePlacement tablero = getTablero("8/8/p7/8/8/8/8/8");

        moveGenerator.setPiecePlacement(tablero);

        Square from = Square.a6;
        assertEquals(Piece.PAWN_BLACK, tablero.getPiece(from));

        PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_BLACK);

        MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();

        assertEquals(1, moves.size());

        assertTrue(moves.contains(createSimpleMove(origen, Square.a5)));
    }

    @Test
    public void testSaltoDoble() {
        PiecePlacement tablero = getTablero("8/p7/8/8/8/8/8/8");

        moveGenerator.setPiecePlacement(tablero);

        Square from = Square.a7;
        assertEquals(Piece.PAWN_BLACK, tablero.getPiece(from));

        PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_BLACK);

        MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();

        assertEquals(2, moves.size());

        assertTrue(moves.contains(createSimpleMove(origen, Square.a6)));
        assertTrue(moves.contains(createSaltoDobleMove(origen, Square.a5, Square.a6)));
    }

    @Test
    public void testAtaqueIzquierda() {
        PiecePlacement tablero = getTablero("8/4p3/3P4/8/8/8/8/8");

        moveGenerator.setPiecePlacement(tablero);

        Square from = Square.e7;
        assertEquals(Piece.PAWN_BLACK, tablero.getPiece(from));
        assertEquals(Piece.PAWN_WHITE, tablero.getPiece(Square.d6));

        PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_BLACK);

        MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();

        assertEquals(3, moves.size());

        assertTrue(moves.contains(createSimpleMove(origen, Square.e6)));
        assertTrue(moves.contains(createSaltoDobleMove(origen, Square.e5, Square.a6)));
        assertTrue(moves.contains(createCapturePawnMove(origen, Square.d6, Piece.PAWN_WHITE, Cardinal.SurOeste)));
    }

    @Test
    public void testAtaqueDerecha() {
        PiecePlacement tablero = getTablero("8/4p3/5P2/8/8/8/8/8");

        moveGenerator.setPiecePlacement(tablero);

        Square from = Square.e7;
        assertEquals(Piece.PAWN_BLACK, tablero.getPiece(from));
        assertEquals(Piece.PAWN_WHITE, tablero.getPiece(Square.f6));

        PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_BLACK);

        MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();

        assertTrue(moves.contains(createSimpleMove(origen, Square.e6)));
        assertTrue(moves.contains(createSaltoDobleMove(origen, Square.e5, Square.e6)));
        assertTrue(moves.contains( createCapturePawnMove(origen, Square.f6, Piece.PAWN_WHITE, Cardinal.SurEste) ));

        assertEquals(3, moves.size());
    }

    private Move createSimpleMove(PiecePositioned origen, Square destinoSquare) {
        return moveFactoryImp.createSimplePawnMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, null), Cardinal.Sur);
    }

    private Move createSaltoDobleMove(PiecePositioned origen, Square destinoSquare, Square squarePasante) {
        return moveFactoryImp.createSaltoDoblePawnMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, null), squarePasante, Cardinal.Sur);
    }

    private Move createCapturePawnMove(PiecePositioned origen, Square destinoSquare, Piece destinoPieza, Cardinal cardinal) {
        return moveFactoryImp.createCapturePawnMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, destinoPieza), cardinal);
    }

    private Move createSimplePawnPromocion(PiecePositioned origen, Square destinoSquare, Piece promocion) {
        return moveFactoryImp.createSimplePawnPromotion(origen, PiecePositioned.getPiecePositioned(destinoSquare, null), promocion);
    }

    private Move createCapturePawnPromocion(PiecePositioned origen, Square destinoSquare, Piece destinoPieza, Piece promocion) {
        return moveFactoryImp.createCapturePawnPromotion(origen, PiecePositioned.getPiecePositioned(destinoSquare, destinoPieza), promocion);
    }

    private PiecePlacement getTablero(String string) {
        PiecePlacementBuilder builder = new PiecePlacementBuilder(new ChessFactoryDebug());

        FENDecoder parser = new FENDecoder(builder);

        parser.parsePiecePlacement(string);

        return builder.getResult();
    }
}

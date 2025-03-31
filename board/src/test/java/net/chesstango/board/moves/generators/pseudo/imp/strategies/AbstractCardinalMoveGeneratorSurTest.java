package net.chesstango.board.moves.generators.pseudo.imp.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.SquareBoardBuilder;
import net.chesstango.board.debug.builder.ChessFactoryDebug;
import net.chesstango.board.debug.chess.BitBoardDebug;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.moves.factories.imp.MoveFactoryWhite;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPieceResult;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.position.BitBoard;
import net.chesstango.board.position.SquareBoard;
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
public class AbstractCardinalMoveGeneratorSurTest {

    private AbstractCardinalMoveGenerator moveGenerator;

    private Collection<PseudoMove> moves;

    private MoveFactory moveFactoryImp;

    @BeforeEach
    public void setUp() throws Exception {
        moveFactoryImp = new MoveFactoryWhite();
        moveGenerator = new AbstractCardinalMoveGenerator(Color.WHITE, new Cardinal[]{Cardinal.Sur}) {

            @Override
            protected PseudoMove createSimpleMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
                return moveFactoryImp.createSimpleKnightMove(from, to);
            }

            @Override
            protected PseudoMove createCaptureMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
                return moveFactoryImp.createCaptureKnightMove(from, to);
            }

        };

        moves = new ArrayList<>();
    }

    @Test
    public void testSur() {
        SquareBoard tablero = getTablero("8/8/8/4R3/8/8/8/8");
        moveGenerator.setSquareBoard(tablero);

        BitBoard bitBoard = new BitBoardDebug();
        bitBoard.init(tablero);
        moveGenerator.setBitBoard(bitBoard);

        Square from = Square.e5;
        assertEquals(Piece.ROOK_WHITE, tablero.getPiece(from));

        PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.ROOK_WHITE);

        MoveGeneratorByPieceResult generatorResult = moveGenerator.generatePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();

        assertEquals(4, moves.size());

        assertTrue(moves.contains(createSimpleMove(origen, Square.e4)));
        assertTrue(moves.contains(createSimpleMove(origen, Square.e3)));
        assertTrue(moves.contains(createSimpleMove(origen, Square.e2)));
        assertTrue(moves.contains(createSimpleMove(origen, Square.e1)));
    }

    @Test
    public void testSur01() {
        SquareBoard tablero = getTablero("8/8/8/4R3/8/8/8/4B3");
        moveGenerator.setSquareBoard(tablero);

        BitBoard bitBoard = new BitBoardDebug();
        bitBoard.init(tablero);
        moveGenerator.setBitBoard(bitBoard);

        Square from = Square.e5;
        assertEquals(Piece.ROOK_WHITE, tablero.getPiece(from));
        assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(Square.e1));

        PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.ROOK_WHITE);

        MoveGeneratorByPieceResult generatorResult = moveGenerator.generatePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();

        assertEquals(3, moves.size());

        assertTrue(moves.contains(createSimpleMove(origen, Square.e4)));
        assertTrue(moves.contains(createSimpleMove(origen, Square.e3)));
        assertTrue(moves.contains(createSimpleMove(origen, Square.e2)));
    }

    @Test
    public void testSur02() {
        SquareBoard tablero = getTablero("8/8/8/4R3/8/8/8/4b3");
        moveGenerator.setSquareBoard(tablero);

        BitBoard bitBoard = new BitBoardDebug();
        bitBoard.init(tablero);
        moveGenerator.setBitBoard(bitBoard);

        Square from = Square.e5;
        assertEquals(Piece.ROOK_WHITE, tablero.getPiece(from));
        assertEquals(Piece.BISHOP_BLACK, tablero.getPiece(Square.e1));

        PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.ROOK_WHITE);

        MoveGeneratorByPieceResult generatorResult = moveGenerator.generatePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();

        assertEquals(4, moves.size());

        assertTrue(moves.contains(createSimpleMove(origen, Square.e4)));
        assertTrue(moves.contains(createSimpleMove(origen, Square.e3)));
        assertTrue(moves.contains(createSimpleMove(origen, Square.e2)));
        assertTrue(moves.contains(createCaptureMove(origen, Square.e1, Piece.BISHOP_BLACK)));
    }

    private Move createSimpleMove(PiecePositioned origen, Square destinoSquare) {
        return moveFactoryImp.createSimpleKnightMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, null));
    }

    private Move createCaptureMove(PiecePositioned origen, Square destinoSquare, Piece destinoPieza) {
        return moveFactoryImp.createCaptureKnightMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, destinoPieza));
    }

    private SquareBoard getTablero(String string) {
        SquareBoardBuilder builder = new SquareBoardBuilder(new ChessFactoryDebug());

        FENDecoder parser = new FENDecoder(builder);

        parser.parsePiecePlacement(string);

        return builder.getChessRepresentation();
    }

}

package net.chesstango.board.internal.moves.generators.pseudo.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.SquareBoardBuilder;
import net.chesstango.board.internal.position.BitBoardDebug;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.internal.moves.factories.MoveFactoryWhite;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPieceResult;
import net.chesstango.board.position.BitBoard;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.internal.position.BitBoardImp;
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
public class AbstractCardinalMoveGeneratorEsteTest {

    private AbstractCardinalMoveGenerator moveGenerator;

    private Collection<PseudoMove> moves;

    private MoveFactory moveFactoryImp;

    @BeforeEach
    public void setUp() throws Exception {
        moveFactoryImp = new MoveFactoryWhite();
        moveGenerator = new AbstractCardinalMoveGenerator(Color.WHITE, new Cardinal[]{Cardinal.Este}) {

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
    public void testEste() {
        SquareBoard squareBoard = getSquareBoard("8/8/8/4R3/8/8/8/8 w KQkq - 0 1");
        moveGenerator.setSquareBoard(squareBoard);

        BitBoard bitBoard = new BitBoardDebug();
        bitBoard.init(squareBoard);
        moveGenerator.setBitBoard(bitBoard);

        Square from = Square.e5;
        assertEquals(Piece.ROOK_WHITE, squareBoard.getPiece(from));

        PiecePositioned origen = PiecePositioned.of(from, Piece.ROOK_WHITE);

        MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();


        assertEquals(3, moves.size());

        assertTrue(moves.contains(createSimpleMove(origen, Square.f5)));
        assertTrue(moves.contains(createSimpleMove(origen, Square.g5)));
        assertTrue(moves.contains(createSimpleMove(origen, Square.h5)));
    }

    @Test
    public void testEste01() {
        SquareBoard tablero = getSquareBoard("8/8/8/4R2B/8/8/8/8 w KQkq - 0 1");
        moveGenerator.setSquareBoard(tablero);

        BitBoard bitBoard = new BitBoardDebug();
        bitBoard.init(tablero);
        moveGenerator.setBitBoard(bitBoard);

        Square from = Square.e5;
        assertEquals(Piece.ROOK_WHITE, tablero.getPiece(from));
        assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(Square.h5));

        PiecePositioned origen = PiecePositioned.of(from, Piece.ROOK_WHITE);

        MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();

        assertEquals(2, moves.size());

        assertTrue(moves.contains(createSimpleMove(origen, Square.f5)));
        assertTrue(moves.contains(createSimpleMove(origen, Square.g5)));
    }

    @Test
    public void testEste02() {
        SquareBoard squareBoard = getSquareBoard("8/8/8/4R2b/8/8/8/8 w KQkq - 0 1");
        moveGenerator.setSquareBoard(squareBoard);

        BitBoardImp bitBoard = new BitBoardDebug();
        bitBoard.init(squareBoard);
        moveGenerator.setBitBoard(bitBoard);

        Square from = Square.e5;
        assertEquals(Piece.ROOK_WHITE, squareBoard.getPiece(from));
        assertEquals(Piece.BISHOP_BLACK, squareBoard.getPiece(Square.h5));

        PiecePositioned origen = PiecePositioned.of(from, Piece.ROOK_WHITE);

        MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);

        moves = generatorResult.getPseudoMoves();

        assertEquals(3, moves.size());

        assertTrue(moves.contains(createSimpleMove(origen, Square.f5)));
        assertTrue(moves.contains(createSimpleMove(origen, Square.g5)));
        assertTrue(moves.contains(createCaptureMove(origen, Square.h5, Piece.BISHOP_BLACK)));
    }

    private Move createSimpleMove(PiecePositioned origen, Square destinoSquare) {
        return moveFactoryImp.createSimpleKnightMove(origen, PiecePositioned.of(destinoSquare, null));
    }

    private Move createCaptureMove(PiecePositioned origen, Square destinoSquare, Piece destinoPieza) {
        return moveFactoryImp.createCaptureKnightMove(origen, PiecePositioned.of(destinoSquare, destinoPieza));
    }

    private SquareBoard getSquareBoard(String string) {
        SquareBoardBuilder builder = new SquareBoardBuilder();

        FENExporter exporter = new FENExporter(builder);

        exporter.export(FEN.of(string));

        return builder.getPositionRepresentation();
    }

}

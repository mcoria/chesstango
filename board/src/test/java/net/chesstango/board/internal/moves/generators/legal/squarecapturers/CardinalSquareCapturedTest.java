package net.chesstango.board.internal.moves.generators.legal.squarecapturers;

import net.chesstango.board.Color;
import net.chesstango.board.Square;
import net.chesstango.board.builders.SquareBoardBuilder;
import net.chesstango.board.position.BitBoard;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.internal.position.BitBoardImp;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENExporter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 */
public class CardinalSquareCapturedTest {

    @Test
    public void test01(){
        SquareBoard dummySquareBoard = getSquareBoard("8/8/3q4/8/8/8/8/3K4 w KQkq - 0 1");
        BitBoard bitBoard = new BitBoardImp();
        bitBoard.init(dummySquareBoard);

        CardinalSquareCaptured cardinalSquareCaptured = new CardinalSquareCaptured(dummySquareBoard, bitBoard);

        assertTrue(cardinalSquareCaptured.isCaptured(Color.BLACK, Square.d1));
    }


    @Test
    public void test02(){
        /**
         * La reina esta arriba del rey
         */
        SquareBoard dummySquareBoard = getSquareBoard("8/8/8/8/8/8/3q4/3K4 w KQkq - 0 1");
        BitBoard bitBoard = new BitBoardImp();
        bitBoard.init(dummySquareBoard);

        CardinalSquareCaptured cardinalSquareCaptured = new CardinalSquareCaptured(dummySquareBoard, bitBoard);

        assertTrue(cardinalSquareCaptured.isCaptured(Color.BLACK, Square.d1));
    }


    @Test
    public void test03(){
        SquareBoard dummySquareBoard = getSquareBoard("8/3q4/8/8/3P4/8/8/3K4 w KQkq - 0 1");
        BitBoard bitBoard = new BitBoardImp();
        bitBoard.init(dummySquareBoard);

        CardinalSquareCaptured cardinalSquareCaptured = new CardinalSquareCaptured(dummySquareBoard, bitBoard);

        assertFalse(cardinalSquareCaptured.isCaptured(Color.BLACK, Square.d1));
    }


    private SquareBoard getSquareBoard(String string) {
        SquareBoardBuilder builder = new SquareBoardBuilder();

        FENExporter exporter = new FENExporter(builder);

        exporter.export(FEN.of(string));

        return builder.getPositionRepresentation();
    }
}

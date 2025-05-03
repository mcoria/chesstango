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
public class FullScanSquareCapturedTest {

    @Test
    public void testPositionCapturedByPawnWhite() {
        SquareBoard dummySquareBoard = getSquareBoard("8/8/8/1P6/8/8/8/8  w KQkq - 0 1");
        BitBoard bitBoard = new BitBoardImp();
        bitBoard.init(dummySquareBoard);

        FullScanSquareCaptured fullScanSquareCapturer = new FullScanSquareCaptured(dummySquareBoard, bitBoard);

        assertTrue(fullScanSquareCapturer.isCaptured(Color.WHITE, Square.a6));
        assertFalse(fullScanSquareCapturer.isCaptured(Color.WHITE, Square.b6));
        assertTrue(fullScanSquareCapturer.isCaptured(Color.WHITE, Square.c6));
    }

    @Test
    public void testPositionCapturedByPawnBlack() {
        SquareBoard dummySquareBoard = getSquareBoard("8/8/8/1p6/8/8/8/8  w KQkq - 0 1");
        BitBoard bitBoard = new BitBoardImp();
        bitBoard.init(dummySquareBoard);

        FullScanSquareCaptured fullScanSquareCapturer = new FullScanSquareCaptured(dummySquareBoard, bitBoard);

        assertTrue(fullScanSquareCapturer.isCaptured(Color.BLACK, Square.a4));
        assertFalse(fullScanSquareCapturer.isCaptured(Color.BLACK, Square.b4));
        assertTrue(fullScanSquareCapturer.isCaptured(Color.BLACK, Square.c4));
    }


    @Test
    public void testPositionCapturedByKnight() {
        SquareBoard dummySquareBoard = getSquareBoard("8/8/8/3N4/8/8/8/8  w KQkq - 0 1");
        BitBoard bitBoard = new BitBoardImp();
        bitBoard.init(dummySquareBoard);

        FullScanSquareCaptured fullScanSquareCapturer = new FullScanSquareCaptured(dummySquareBoard, bitBoard);

        assertTrue(fullScanSquareCapturer.isCaptured(Color.WHITE, Square.c7));
        assertTrue(fullScanSquareCapturer.isCaptured(Color.WHITE, Square.e7));

        assertTrue(fullScanSquareCapturer.isCaptured(Color.WHITE, Square.b6));
        assertTrue(fullScanSquareCapturer.isCaptured(Color.WHITE, Square.f6));

        assertTrue(fullScanSquareCapturer.isCaptured(Color.WHITE, Square.b4));
        assertTrue(fullScanSquareCapturer.isCaptured(Color.WHITE, Square.f4));

        assertTrue(fullScanSquareCapturer.isCaptured(Color.WHITE, Square.c3));
        assertTrue(fullScanSquareCapturer.isCaptured(Color.WHITE, Square.e3));
    }

    private SquareBoard getSquareBoard(String string) {
        SquareBoardBuilder builder = new SquareBoardBuilder();

        FENExporter exporter = new FENExporter(builder);

        exporter.export(FEN.of(string));

        return builder.getPositionRepresentation();
    }
}

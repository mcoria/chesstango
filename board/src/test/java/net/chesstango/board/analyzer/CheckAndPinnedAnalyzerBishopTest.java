package net.chesstango.board.analyzer;

import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 *
 */
public class CheckAndPinnedAnalyzerBishopTest {

    @Test
    public void testCheck(){
        ChessPosition gamePosition = FENDecoder.loadChessPosition("r1bqkb1r/pppp1Qpp/2n4n/4p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 1");

        CheckAndPinnedAnalyzerBishop checkAndPinnedAnalyzer = new CheckAndPinnedAnalyzerBishop(gamePosition, Color.WHITE);

        Square squareKing = gamePosition.getKingSquareBlack();

        List<AbstractMap.SimpleImmutableEntry<PiecePositioned, Cardinal>> pinnedPositionCardinals = new LinkedList<>();

        boolean positionCaptured = checkAndPinnedAnalyzer.positionCaptured(squareKing, pinnedPositionCardinals);

        Assertions.assertTrue(positionCaptured);

    }
}

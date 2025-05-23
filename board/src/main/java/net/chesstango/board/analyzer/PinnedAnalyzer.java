package net.chesstango.board.analyzer;

import net.chesstango.board.Color;
import net.chesstango.board.Square;
import net.chesstango.board.position.PositionReader;

import java.util.ArrayList;

/**
 * @author Mauricio Coria
 */
public class PinnedAnalyzer implements Analyzer {

    private final PositionReader positionReader;
    private final PinnedAnalyzerByColor analyzerWhite;
    private final PinnedAnalyzerByColor analyzerBlack;

    public PinnedAnalyzer(PositionReader chessPosition) {
        positionReader = chessPosition;
        analyzerWhite = new PinnedAnalyzerByColor(chessPosition, Color.WHITE);
        analyzerBlack = new PinnedAnalyzerByColor(chessPosition, Color.BLACK);
    }

    @Override
    public void analyze(AnalyzerResult result) {
        Color currentTurn = positionReader.getCurrentTurn();

        if (Color.WHITE.equals(currentTurn)) {
            analyzerWhite.analyze(result);
        } else {
            analyzerBlack.analyze(result);
        }
    }

    private class PinnedAnalyzerByColor implements Analyzer{
        private final Color color;
        private final PinnedAnalyzerRook analyzerRook;
        private final PinnedAnalyzerBishop analyzerBishop;

        public PinnedAnalyzerByColor(PositionReader chessPosition, Color color) {
            this.color = color;
            this.analyzerRook = new PinnedAnalyzerRook(chessPosition, color.oppositeColor());
            this.analyzerBishop = new PinnedAnalyzerBishop(chessPosition, color.oppositeColor());
        }

        @Override
        public void analyze(AnalyzerResult result) {
            // King square under attack
            Square squareKing = positionReader.getKingSquare(color);
            result.setPinnedSquares(0);
            result.setPinnedPositionCardinals(new ArrayList<>(8));

            analyzerRook.pinnedSquares(squareKing, result);
            analyzerBishop.pinnedSquares(squareKing, result);
        }
    }
}


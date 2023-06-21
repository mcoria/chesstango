package net.chesstango.board.analyzer;

import net.chesstango.board.Color;
import net.chesstango.board.Square;
import net.chesstango.board.position.ChessPositionReader;

import java.util.ArrayList;

/**
 * @author Mauricio Coria
 */
public class PinnedAnalyzer implements Analyzer {

    private final ChessPositionReader positionReader;
    private final PinnedAnalyzerByColor analyzerWhite;
    private final PinnedAnalyzerByColor analyzerBlack;

    public PinnedAnalyzer(ChessPositionReader chessPosition) {
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

    private class PinnedAnalyzerByColor {
        private final Color color;
        private final PinnedAnalyzerRook analyzerRook;
        private final PinnedAnalyzerBishop analyzerBishop;

        public PinnedAnalyzerByColor(ChessPositionReader chessPosition, Color color) {
            this.color = color;
            this.analyzerRook = new PinnedAnalyzerRook(chessPosition, color.oppositeColor());
            this.analyzerBishop = new PinnedAnalyzerBishop(chessPosition, color.oppositeColor());
        }

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


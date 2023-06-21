package net.chesstango.board.analyzer;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.movesgenerators.pseudo.strategies.RookMoveGenerator;
import net.chesstango.board.position.ChessPositionReader;

/**
 * @author Mauricio Coria
 *
 */
class CheckAndPinnedAnalyzerRook extends CheckAndPinnedAnalyzerCardinal {

    CheckAndPinnedAnalyzerRook(ChessPositionReader positionReader, Color color) {
        super(positionReader, color, RookMoveGenerator.ROOK_CARDINAL, Piece.getRook(color));
    }

    @Override
    protected long getPossibleCapturerInCardinalDirection(Square square, Cardinal cardinal) {
        return (cardinal.getPosiciones(square) & positionReader.getPositions(color)) &
                ( positionReader.getRookPositions() | positionReader.getQueenPositions() );
    }
}

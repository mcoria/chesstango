package net.chesstango.board.analyzer;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.internal.moves.generators.pseudo.strategies.BishopMoveGenerator;
import net.chesstango.board.position.PositionReader;

/**
 * @author Mauricio Coria
 *
 */
class PinnedAnalyzerBishop extends PinnedAnalyzerCardinal {

    PinnedAnalyzerBishop(PositionReader positionReader, Color color) {
        super(positionReader, color, BishopMoveGenerator.BISHOP_CARDINAL, Piece.getBishop(color));
    }

    @Override
    protected long getPossibleCapturerInCardinalDirection(Square square, Cardinal cardinal) {
        return (cardinal.getSquaresInDirection(square) & positionReader.getPositions(color)) &
                ( positionReader.getBishopPositions() | positionReader.getQueenPositions() );
    }
}

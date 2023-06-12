package net.chesstango.board.analyzer;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.movesgenerators.pseudo.strategies.BishopMoveGenerator;
import net.chesstango.board.movesgenerators.pseudo.strategies.RookMoveGenerator;
import net.chesstango.board.position.ChessPositionReader;

/**
 * @author Mauricio Coria
 *
 */
class CheckAndPinnedAnalyzerBishop extends CheckAndPinnedAnalyzerCardinal {

    CheckAndPinnedAnalyzerBishop(ChessPositionReader positionReader, Color color) {
        super(positionReader, color, BishopMoveGenerator.BISHOP_CARDINAL, Piece.getBishop(color));
    }

    @Override
    protected boolean thereIsCapturerInCardinalDirection(Square square, Cardinal cardinal) {
        long result =  (cardinal.getPosiciones(square) & positionReader.getPositions(color)) &
                ( positionReader.getBishopPositions() | positionReader.getQueenPositions() );

        return result != 0  ;
    }
}

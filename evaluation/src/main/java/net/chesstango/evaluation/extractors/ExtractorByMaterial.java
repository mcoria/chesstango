package net.chesstango.evaluation.extractors;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.evaluation.GameFeatures;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class ExtractorByMaterial implements GameFeatures {

    @Override
    public void extractFeatures(final Game game, Map<String, Integer> featuresMap) {
        ChessPositionReader positionReader = game.getChessPosition();

        long whitePositions = positionReader.getPositions(Color.WHITE);

        long blackPositions = positionReader.getPositions(Color.BLACK);

        featuresMap.put(Piece.ROOK_WHITE.toString(), Long.bitCount (whitePositions & positionReader.getRookPositions()));
        featuresMap.put(Piece.KNIGHT_WHITE.toString(), Long.bitCount (whitePositions & positionReader.getKnightPositions()));
        featuresMap.put(Piece.BISHOP_WHITE.toString(), Long.bitCount (whitePositions & positionReader.getBishopPositions()));
        featuresMap.put(Piece.QUEEN_WHITE.toString(), Long.bitCount (whitePositions & positionReader.getQueenPositions()));
        featuresMap.put(Piece.PAWN_WHITE.toString(), Long.bitCount (whitePositions & positionReader.getPawnPositions()));

        featuresMap.put(Piece.ROOK_BLACK.toString(), Long.bitCount (blackPositions & positionReader.getRookPositions()));
        featuresMap.put(Piece.KNIGHT_BLACK.toString(), Long.bitCount (blackPositions & positionReader.getKnightPositions()));
        featuresMap.put(Piece.BISHOP_BLACK.toString(), Long.bitCount (blackPositions & positionReader.getBishopPositions()));
        featuresMap.put(Piece.QUEEN_BLACK.toString(), Long.bitCount (blackPositions & positionReader.getQueenPositions()));
        featuresMap.put(Piece.PAWN_BLACK.toString(), Long.bitCount (blackPositions & positionReader.getPawnPositions()));
    }
}

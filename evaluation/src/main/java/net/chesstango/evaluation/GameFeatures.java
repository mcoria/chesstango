package net.chesstango.evaluation;

import net.chesstango.board.Game;

import java.util.Map;

/**
 * @author Mauricio Coria
 */
public interface GameFeatures {
    void extractFeatures(final Game game, Map<String, Integer> featuresMap);
}

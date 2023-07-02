package net.chesstango.evaluation;

import net.chesstango.board.Game;

import java.util.Map;

/**
 * @author Mauricio Coria
 */
public interface GameFeatures {
    Map<String, Integer> extractFeatures(final Game game);
}

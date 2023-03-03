package net.chesstango.uci.arena;

import net.chesstango.uci.gui.EngineController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class Tournament {
    private final EngineController primaryEngine;
    private final List<EngineController> engineControllerList;

    public Tournament(EngineController primaryEngine, List<EngineController> engineControllerList) {
        this.primaryEngine = primaryEngine;
        this.engineControllerList = engineControllerList;
    }

    public List<GameResult> play(List<String> fenList) {
        List<GameResult> matchResults = new ArrayList<>();
        for (EngineController engineController : engineControllerList) {
            Match match = new Match(primaryEngine, engineController, 1);
            matchResults.addAll(match.play(fenList));
        }
        return matchResults;
    }

}

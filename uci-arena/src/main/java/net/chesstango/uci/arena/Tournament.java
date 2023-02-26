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
    private final List<String> fenList;

    public Tournament(EngineController primaryEngine, List<EngineController> engineControllerList, List<String> fenList) {
        this.primaryEngine = primaryEngine;
        this.engineControllerList = engineControllerList;
        this.fenList = fenList;
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

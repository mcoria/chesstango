package net.chesstango.uci.arbiter;

import net.chesstango.uci.arbiter.EngineController;
import net.chesstango.uci.arbiter.Match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mauricio Coria
 *
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

    public Map<EngineController, TournamentResult> play(List<String> fenList) {
        List<Match.MathResult> matchResults = new ArrayList<>();
        for ( EngineController engineController : engineControllerList) {
            Match match = new Match(primaryEngine, engineController, 1);
            matchResults.addAll(match.play(fenList));
        }

        Map<EngineController, TournamentResult> tournamentResultMap = new HashMap<>();
        matchResults.forEach( match -> {
            EngineController theEngine = match.getEngineWhite();
            if(primaryEngine.equals(theEngine)){
                TournamentResult tournamentResult = tournamentResultMap.computeIfAbsent(match.getEngineBlack(), engine -> new TournamentResult(engine));
                //tournamentResult.increaseBlack(match.getBlackPoints());
            } else {
                TournamentResult tournamentResult = tournamentResultMap.computeIfAbsent(match.getEngineWhite(), engine -> new TournamentResult(engine));
                //tournamentResult.increaseWhite(match.getWhitePoints());
            }
        });

        return tournamentResultMap;
    }

    public static class TournamentResult {
        private int whitePoints;
        private int blackPoints;

        private final EngineController engine;

        public TournamentResult(EngineController engine) {
            this.engine = engine;
        }

        public EngineController getEngine() {
            return engine;
        }

        public int getWhitePoints() {
            return whitePoints;
        }

        public int getBlackPoints() {
            return blackPoints;
        }

        public int getTotalPoints() {
            return whitePoints + blackPoints;
        }

        public void increaseBlack(int points) {
            this.blackPoints += points;
        }

        public void increaseWhite(int points) {
            this.whitePoints += points;
        }
    }
}

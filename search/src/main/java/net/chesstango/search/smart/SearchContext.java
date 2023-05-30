package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * @author Mauricio Coria
 */
public class SearchContext {
    private final Game game;
    private final int maxPly;

    private int[] visitedNodesCounters;
    private int[] expectedNodesCounters;
    private int[] visitedNodesQuiescenceCounter;

    private Set<Move>[] distinctMovesPerLevel;

    private Map<Long, TableEntry> maxMap;

    private Map<Long, TableEntry> minMap;

    public SearchContext(Game game, int maxPly) {
        this.game = game;
        this.maxPly = maxPly;
    }

    public Game getGame() {
        return game;
    }

    public int getMaxPly() {
        return maxPly;
    }

    public int[] getVisitedNodesCounters() {
        return visitedNodesCounters;
    }

    public int[] getExpectedNodesCounters() {
        return expectedNodesCounters;
    }

    public int[] getVisitedNodesQuiescenceCounter() {
        return visitedNodesQuiescenceCounter;
    }

    public Set<Move>[] getDistinctMovesPerLevel() {
        return distinctMovesPerLevel;
    }

    public Map<Long, TableEntry> getMaxMap() {
        return maxMap;
    }

    public Map<Long, TableEntry> getMinMap() {
        return minMap;
    }

    public void setVisitedNodesCounters(int[] visitedNodesCounters) {
        this.visitedNodesCounters = visitedNodesCounters;
    }

    public void setExpectedNodesCounters(int[] expectedNodesCounters) {
        this.expectedNodesCounters = expectedNodesCounters;
    }

    public void setVisitedNodesQuiescenceCounter(int[] visitedNodesQuiescenceCounter) {
        this.visitedNodesQuiescenceCounter = visitedNodesQuiescenceCounter;
    }

    public void setDistinctMovesPerLevel(Set<Move>[] distinctMovesPerLevel) {
        this.distinctMovesPerLevel = distinctMovesPerLevel;
    }

    public void setMaxMap(Map<Long, TableEntry> maxMap) {
        this.maxMap = maxMap;
    }

    public void setMinMap(Map<Long, TableEntry> minMap) {
        this.minMap = minMap;
    }

    public enum EntryType{EXACT((byte)0b00000001), LOWER_BOUND((byte)0b00000010), UPPER_BOUND((byte)0b00000011);
        private final byte byteValue;

        EntryType(byte byteValue) {
            this.byteValue = byteValue;
        }


        public static EntryType valueOf(byte byteValue){
            for (EntryType type:
                    EntryType.values()) {
                if(type.byteValue == byteValue){
                    return type;
                }
            }
            if(byteValue == 0){
                return null;
            }
            throw new  RuntimeException("Unable to convert from byte to EntryType");
        }

        public static byte encode(EntryType entryType){
            if(entryType == null){
                return 0;
            }
            return entryType.byteValue;
        }

    };

    public static class TableEntry implements Serializable {
        public int searchDepth;

        public long bestMoveAndValue;

        public int value;
        public EntryType type;


        public long qBestMoveAndValue;
        public int qValue;
        public EntryType qType;
    }

}

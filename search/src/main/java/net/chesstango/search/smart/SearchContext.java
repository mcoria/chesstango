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
    private final int[] visitedNodesCounters;
    private final int[] expectedNodesCounters;
    private final int[] visitedNodesQuiescenceCounter;

    private final Set<Move>[] distinctMovesPerLevel;

    private final Map<Long, TableEntry> maxMap;

    private final Map<Long, TableEntry> minMap;

    public SearchContext(Game game, int maxPly) {
        this.game = game;
        this.maxPly = maxPly;
        this.visitedNodesCounters = new int[30];
        this.visitedNodesQuiescenceCounter = new int[30];
        this.expectedNodesCounters = new int[30];
        this.distinctMovesPerLevel = new Set[30];
        IntStream.range(0, 30).forEach(i -> this.distinctMovesPerLevel[i] = new HashSet<>());
        this.maxMap = new HashMap<>();
        this.minMap = new HashMap<>();
    }

    public SearchContext(Game game,
                         int maxPly,
                         int[] visitedNodesCounters,
                         int[] expectedNodesCounters,
                         int[] visitedNodesQuiescenceCounter,
                         Set<Move>[] distinctMovesPerLevel,
                         Map<Long, TableEntry> maxMap,
                         Map<Long, TableEntry> minMap) {
        this.game = game;
        this.maxPly = maxPly;
        this.visitedNodesCounters = visitedNodesCounters;
        this.visitedNodesQuiescenceCounter = visitedNodesQuiescenceCounter;
        this.expectedNodesCounters = expectedNodesCounters;
        this.distinctMovesPerLevel = distinctMovesPerLevel;
        this.maxMap = maxMap;
        this.minMap = minMap;
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


    public enum EntryType{EXACT((byte)0b00000001), LOWER_BOUND((byte)0b00000010), UPPER_BOUND((byte)0b00000011);
        private final byte byteValue;

        EntryType(byte byteValue) {
            this.byteValue = byteValue;
        }

        public byte toByte() {
            return byteValue;
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

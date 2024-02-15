package net.chesstango.search.smart.alphabeta.debug;

import net.chesstango.search.smart.transposition.TranspositionBound;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class SearchTracker {

    private DebugNode debugNode;

    private boolean sorting;

    public DebugNode newNode(DebugNode.NodeTopology topology, int currentPly) {
        DebugNode newNode = new DebugNode();

        newNode.topology = topology;
        newNode.ply = currentPly;

        if (debugNode != null) {
            debugNode.childNodes.add(newNode);
            newNode.parent = debugNode;
        }

        debugNode = newNode;

        return debugNode;
    }

    public void sortingON() {
        sorting = true;
    }

    public void sortingOFF() {
        sorting = false;
    }


    public void trackReadTranspositionEntry(DebugNodeTT.TableType tableType, long hashRequested, TranspositionEntry entry) {
        if (entry != null && debugNode != null) {
            if (sorting) {
                debugNode.sorterReads.computeIfAbsent(hashRequested, key ->
                        new DebugNodeTT()
                                .setHashRequested(hashRequested)
                                .setTableType(tableType)
                                .setHash(entry.hash)
                                .setDepth(entry.searchDepth)
                                .setMovesAndValue(entry.movesAndValue)
                                .setBound(entry.transpositionBound));
            } else {
                if (debugNode.entryRead != null) {
                    throw new RuntimeException("Overriding debugNode.entryRead");
                }
                debugNode.entryRead = new DebugNodeTT()
                        .setHashRequested(hashRequested)
                        .setTableType(tableType)
                        .setHash(entry.hash)
                        .setDepth(entry.searchDepth)
                        .setMovesAndValue(entry.movesAndValue)
                        .setBound(entry.transpositionBound);
            }
        }
    }

    public void trackWriteTranspositionEntry(DebugNodeTT.TableType tableType, long hash, int searchDepth, long movesAndValue, TranspositionBound transpositionBound) {
        if (sorting) {
            throw new RuntimeException("Writing TT while sorting");
        } else {
            if (debugNode.entryWrite != null) {
                // Probablemente a un nodo hijo le falta agregar DebugFilter y el nodo hijo esta sobreescribiendo la entrada
                throw new RuntimeException("Overriding debugNode.entryWrite");
            }
            debugNode.entryWrite = new DebugNodeTT()
                    .setHashRequested(hash)
                    .setTableType(tableType)
                    .setHash(hash)
                    .setDepth(searchDepth)
                    .setMovesAndValue(movesAndValue)
                    .setBound(transpositionBound);
        }
    }

    public void trackSortedMoves(List<String> sortedMovesStr) {
        if (debugNode != null) {
            debugNode.sortedMovesStr = sortedMovesStr;
        }
    }

    public void save() {
        if (debugNode.parent != null) {
            debugNode = debugNode.parent;
        }
    }

    public void reset() {
        if (Objects.nonNull(debugNode) && debugNode.parent != null) {
            throw new RuntimeException("Still searching?");
        }
        debugNode = null;
    }

    public DebugNode getRootNode() {
        if (Objects.nonNull(debugNode) && debugNode.parent != null) {
            throw new RuntimeException("Still searching?");
        }
        return debugNode;
    }

    public Map<Long, DebugNodeTT> getSorterReads() {
        return debugNode.sorterReads;
    }
}

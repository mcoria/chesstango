package net.chesstango.search.smart.alphabeta.debug;

import net.chesstango.search.smart.transposition.TranspositionBound;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.List;
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


    public void trackReadTranspositionEntry(DebugOperationTT.TableType tableType, long hashRequested, TranspositionEntry entry) {
        if (entry != null && debugNode != null) {
            assert hashRequested == entry.hash;
            if (sorting) {
                TranspositionEntry entryCloned = entry.clone();
                debugNode.sorterReads.add(new DebugOperationTT()
                        .setHashRequested(hashRequested)
                        .setTableType(tableType)
                        .setEntry(entryCloned));
            } else {
                if (debugNode.entryRead != null) {
                    throw new RuntimeException("Overriding debugNode.entryRead");
                }
                TranspositionEntry entryCloned = entry.clone();
                debugNode.entryRead = new DebugOperationTT()
                        .setHashRequested(hashRequested)
                        .setTableType(tableType)
                        .setEntry(entryCloned);
            }
        }
    }

    public void trackWriteTranspositionEntry(DebugOperationTT.TableType tableType, long hash, int searchDepth, long movesAndValue, TranspositionBound transpositionBound) {
        if (sorting) {
            throw new RuntimeException("Writing TT while sorting");
        } else {
            if (debugNode.entryWrite != null) {
                // Probablemente a un nodo hijo le falta agregar DebugFilter y el nodo hijo esta sobreescribiendo la entrada
                throw new RuntimeException("Overriding debugNode.entryWrite");
            }

            TranspositionEntry entryWrite = new TranspositionEntry()
                    .setHash(hash)
                    .setSearchDepth(searchDepth)
                    .setMovesAndValue(movesAndValue)
                    .setTranspositionBound(transpositionBound);

            debugNode.entryWrite = new DebugOperationTT()
                    .setHashRequested(hash)
                    .setTableType(tableType)
                    .setEntry(entryWrite);
        }
    }

    public void trackSortedMoves(List<String> sortedMovesStr) {
        if (debugNode != null) {
            debugNode.sortedMoves = sortedMovesStr;
        }
    }

    public void trackReadFromCache(long hash, Integer evaluation) {
        debugNode.evalCacheReads.add(new DebugOperationEval()
                .setHashRequested(hash)
                .setEvaluation(evaluation)
        );
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

    public List<DebugOperationTT> getSorterReads() {
        return debugNode.sorterReads;
    }

    public List<DebugOperationEval> getEvalCacheReads() {
        return debugNode.evalCacheReads;
    }

}

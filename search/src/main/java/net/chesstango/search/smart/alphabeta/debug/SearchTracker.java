package net.chesstango.search.smart.alphabeta.debug;

import net.chesstango.search.smart.transposition.TranspositionBound;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.List;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class SearchTracker {

    private DebugNode rootNode;
    private DebugNode currentNode;
    private boolean sorting;


    public DebugNode newNode(DebugNode.NodeTopology topology, int currentPly) {

        DebugNode newNode;
        if (DebugNode.NodeTopology.ROOT.equals(topology)) {
            newNode = createRootNode();
            rootNode = newNode;
        } else {
            newNode = createRegularNode(topology, currentPly);
            currentNode.childNodes.add(newNode);
        }

        currentNode = newNode;

        return currentNode;
    }


    protected DebugNode createRootNode() {
        assert currentNode == null;
        DebugNode newNode = new DebugNode();
        newNode.topology = DebugNode.NodeTopology.ROOT;
        newNode.ply = 0;
        newNode.parent = null;
        return newNode;
    }

    protected DebugNode createRegularNode(DebugNode.NodeTopology topology, int currentPly) {
        DebugNode newNode = new DebugNode();
        newNode.topology = topology;
        newNode.ply = currentPly;
        newNode.parent = currentNode;
        return newNode;
    }


    public void sortingON() {
        sorting = true;
    }

    public void sortingOFF() {
        sorting = false;
    }


    public void trackReadTranspositionEntry(DebugOperationTT.TableType tableType, long hashRequested, TranspositionEntry entry) {
        if (currentNode != null) {
            if (entry != null) {
                assert hashRequested == entry.hash;
                if (sorting) {
                    TranspositionEntry entryCloned = entry.clone();
                    currentNode.sorterReads.add(new DebugOperationTT()
                            .setHashRequested(hashRequested)
                            .setTableType(tableType)
                            .setEntry(entryCloned));
                } else {
                    if (currentNode.entryRead != null) {
                        throw new RuntimeException("Overriding debugNode.entryRead");
                    }
                    TranspositionEntry entryCloned = entry.clone();
                    currentNode.entryRead = new DebugOperationTT()
                            .setHashRequested(hashRequested)
                            .setTableType(tableType)
                            .setEntry(entryCloned);
                }
            }
        }
    }

    public void trackWriteTranspositionEntry(DebugOperationTT.TableType tableType, long hash, int searchDepth, long movesAndValue, TranspositionBound transpositionBound) {
        if (currentNode != null) {
            if (sorting) {
                throw new RuntimeException("Writing TT while sorting");
            } else {
                if (currentNode.entryWrite != null) {
                    // Probablemente a un nodo hijo le falta agregar DebugFilter y el nodo hijo esta sobreescribiendo la entrada
                    throw new RuntimeException("Overriding debugNode.entryWrite");
                }

                TranspositionEntry entryWrite = new TranspositionEntry()
                        .setHash(hash)
                        .setSearchDepth(searchDepth)
                        .setMovesAndValue(movesAndValue)
                        .setTranspositionBound(transpositionBound);

                currentNode.entryWrite = new DebugOperationTT()
                        .setHashRequested(hash)
                        .setTableType(tableType)
                        .setEntry(entryWrite);
            }
        }
    }

    public void trackSortedMoves(List<String> sortedMovesStr) {
        if (currentNode != null) {
            currentNode.sortedMoves = sortedMovesStr;
        }
    }

    public void trackReadFromCache(long hash, Integer evaluation) {
        if (currentNode != null) {
            currentNode.evalCacheReads.add(new DebugOperationEval()
                    .setHashRequested(hash)
                    .setEvaluation(evaluation)
            );
        }
    }


    public void save() {
        currentNode = currentNode.parent;
    }

    public void reset() {
        if (currentNode != null) {
            throw new RuntimeException("Still searching?");
        }
        rootNode = null;
    }

    public DebugNode getRootNode() {
        if (currentNode != null) {
            throw new RuntimeException("Still searching?");
        }
        return rootNode;
    }

    public List<DebugOperationTT> getSorterReads() {
        return currentNode.sorterReads;
    }

    public List<DebugOperationEval> getEvalCacheReads() {
        return currentNode.evalCacheReads;
    }

}

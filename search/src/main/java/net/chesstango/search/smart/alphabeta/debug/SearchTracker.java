package net.chesstango.search.smart.alphabeta.debug;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.debug.model.DebugOperationEval;
import net.chesstango.search.smart.alphabeta.debug.model.DebugOperationTT;
import net.chesstango.search.smart.transposition.TranspositionBound;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.List;
import java.util.Optional;

/**
 * @author Mauricio Coria
 */
public class SearchTracker {

    private DebugNode rootNode;
    private DebugNode currentNode;
    private boolean sorting;

    @Setter
    private Game game;


    public DebugNode newNode(DebugNode.NodeTopology topology, int currentPly) {

        DebugNode newNode;
        if (DebugNode.NodeTopology.ROOT.equals(topology)) {
            newNode = createRootNode();
            rootNode = newNode;
        } else {
            newNode = createRegularNode(topology, currentPly);
            currentNode.getChildNodes().add(newNode);
        }

        newNode.setZobristHash(game.getChessPosition().getZobristHash());
        if (game.getState().getPreviousState() != null) {
            newNode.setSelectedMove(game.getState().getPreviousState().getSelectedMove());
        }

        currentNode = newNode;

        return currentNode;
    }


    protected DebugNode createRootNode() {
        assert currentNode == null;
        DebugNode newNode = new DebugNode();
        newNode.setTopology(DebugNode.NodeTopology.ROOT);
        newNode.setPly(0);
        newNode.setFen(game.getChessPosition().toString());
        return newNode;
    }

    protected DebugNode createRegularNode(DebugNode.NodeTopology topology, int currentPly) {
        DebugNode newNode = new DebugNode();
        newNode.setTopology(topology);
        newNode.setPly(currentPly);
        newNode.setParent(currentNode);
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
                TranspositionEntry entryCloned = entry.clone();

                List<DebugOperationTT> readList = sorting ? currentNode.getSorterReads() : currentNode.getEntryRead();

                Optional<DebugOperationTT> previousReadOpt = readList.stream()
                        .filter(debugOperation -> debugOperation.getHashRequested() == hashRequested && debugOperation.getTableType().equals(tableType))
                        .findFirst();

                if (previousReadOpt.isEmpty()) {
                    readList.add(new DebugOperationTT()
                            .setHashRequested(hashRequested)
                            .setTableType(tableType)
                            .setEntry(entryCloned));
                }
            }
        }
    }

    public void trackWriteTranspositionEntry(DebugOperationTT.TableType tableType, long hash, int searchDepth, long movesAndValue, TranspositionBound transpositionBound) {
        if (currentNode != null) {
            if (sorting) {
                throw new RuntimeException("Writing TT while sorting");
            } else {
                TranspositionEntry entryWrite = new TranspositionEntry()
                        .setHash(hash)
                        .setSearchDepth(searchDepth)
                        .setMovesAndValue(movesAndValue)
                        .setTranspositionBound(transpositionBound);

                currentNode.getEntryWrite().add(new DebugOperationTT()
                        .setHashRequested(hash)
                        .setTableType(tableType)
                        .setEntry(entryWrite));
            }
        }
    }

    public void trackSortedMoves(List<String> sortedMovesStr) {
        if (currentNode != null) {
            currentNode.setSortedMoves(sortedMovesStr);
        }
    }

    public void trackReadFromCache(long hash, Integer evaluation) {
        if (currentNode != null) {
            Optional<DebugOperationEval> previousReadOpt = currentNode.getEvalCacheReads().stream()
                    .filter(debugOperationEval -> debugOperationEval.getHashRequested() == hash)
                    .findFirst();

            if (previousReadOpt.isPresent()) {
                DebugOperationEval previousReadOpEval = previousReadOpt.get();
                if (previousReadOpEval.getEvaluation() != evaluation) {
                    throw new RuntimeException("Lectura repetida pero distinto valor retornado");
                }
            } else {
                currentNode.getEvalCacheReads().add(new DebugOperationEval()
                        .setHashRequested(hash)
                        .setEvaluation(evaluation)
                );
            }
        }
    }

    public void trackEvaluation(int evaluation) {
        if (currentNode != null) {
            currentNode.setStandingPat(evaluation);
        }
    }

    public void save() {
        currentNode.validate();
        currentNode = currentNode.getParent();
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
        return currentNode.getSorterReads();
    }

    public List<DebugOperationEval> getEvalCacheReads() {
        return currentNode.getEvalCacheReads();
    }
}

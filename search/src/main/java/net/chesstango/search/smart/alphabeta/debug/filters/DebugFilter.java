package net.chesstango.search.smart.alphabeta.debug.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.GameHistoryRecord;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Bound;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.debug.DebugNodeTrap;
import net.chesstango.search.smart.alphabeta.debug.DebugNodeTracker;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.debug.model.DebugOperationTT;
import net.chesstango.search.smart.alphabeta.debug.model.NodeTopology;
import net.chesstango.search.smart.alphabeta.pv.model.TriangularPVTable;

import java.util.List;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
@Setter
public class DebugFilter implements AlphaBetaFilter, Acceptor {
    private final SimpleMoveEncoder simpleMoveEncoder = SimpleMoveEncoder.INSTANCE;

    private final NodeTopology topology;

    @Getter
    private AlphaBetaFilter next;

    private DebugNodeTrap debugNodeTrap;

    private DebugNodeTracker debugNodeTracker;

    private Game game;

    private TriangularPVTable trianglePV;

    public DebugFilter(NodeTopology topology) {
        this.topology = topology;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int alphaBeta(int currentPly, int alpha, int beta) {
        DebugNode debugNode = debugNodeTracker.newNode(topology);

        debugNode.setPly(currentPly);

        debugNode.setFen(game.getPosition().toString());

        debugNode.setDebugSearch(game.getPosition().getCurrentTurn().toString(), alpha, beta);

        debugNode.setZobristHash(game.getPosition().getZobristHash());
        if (game.getHistory().peekLastRecord() != null) {
            GameHistoryRecord gameHistoryRecord = game.getHistory().peekLastRecord();
            debugNode.setSelectedMove(gameHistoryRecord.playedMove());
        }

        int currentValue = next.alphaBeta(currentPly, alpha, beta);

        debugNode.setValue(currentValue);
        debugNode.setPv(trianglePV.getPV(currentPly));

        if (currentValue <= alpha) {
            debugNode.setBound(Bound.UPPER_BOUND);
            debugNode.setType(DebugNode.NodeType.ALL);
        } else if (beta <= currentValue) {
            debugNode.setBound(Bound.LOWER_BOUND);
            debugNode.setType(DebugNode.NodeType.CUT);
        } else {
            debugNode.setBound(Bound.EXACT);
            debugNode.setType(DebugNode.NodeType.PV);
        }

        trackTranspositionsAccess(debugNode);

        debugNodeTracker.save();

        if (debugNodeTrap != null && debugNodeTrap.test(debugNode)) {
            debugNodeTrap.debugAction(debugNode);
        }


        return currentValue;
    }

    void trackTranspositionsAccess(DebugNode debugNode) {
        List<DebugOperationTT> entryReads = debugNode.getEntryRead();
        List<DebugOperationTT> entryWrites = debugNode.getEntryWrite();

        for (Move move : game.getPossibleMoves()) {
            final String moveStr = simpleMoveEncoder.encode(move);
            final short moveEncoded = move.binaryEncoding();

            entryReads.stream()
                    .filter(debugNodeTT -> moveEncoded == debugNodeTT.getEntry().getMove())
                    .forEach(debugNodeTT -> debugNodeTT.setMove(moveStr));

            entryWrites.stream()
                    .filter(debugNodeTT -> moveEncoded == debugNodeTT.getEntry().getMove())
                    .forEach(debugNodeTT -> debugNodeTT.setMove(moveStr));

        }

        /**
         * Deberian ser escrituras de nodos HORIZON donde QS search arroja el Standing Pat como mejor evaluacion
         */

        entryReads
                .stream()
                .filter(debugNodeTT -> Objects.isNull(debugNodeTT.getMove()))
                .forEach(debugNodeTT -> debugNodeTT.setMove(debugNodeTT.getEntry().getMove() == 0 ? "NO_MOVE" : "UNKNOWN"));

        entryWrites
                .stream()
                .filter(debugNodeTT -> Objects.isNull(debugNodeTT.getMove()))
                .forEach(debugNodeTT -> debugNodeTT.setMove(debugNodeTT.getEntry().getMove() == 0 ? "NO_MOVE" : "UNKNOWN"));
    }
}

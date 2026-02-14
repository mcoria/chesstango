package net.chesstango.search.smart.alphabeta.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.search.StopSearchingException;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.StopSearchingListener;
import net.chesstango.search.smart.features.egtb.EndGameTableBase;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaFlowControl implements AlphaBetaFilter, SearchByCycleListener, StopSearchingListener {
    private volatile boolean keepProcessing;

    @Setter
    @Getter
    private AlphaBetaFilter terminalNode;

    @Setter
    @Getter
    private AlphaBetaFilter interiorNode;

    @Setter
    @Getter
    private AlphaBetaFilter horizonNode;

    @Setter
    @Getter
    private AlphaBetaFilter loopNode;

    @Setter
    @Getter
    private AlphaBetaFilter leafNode;

    @Setter
    @Getter
    private AlphaBetaFilter egtbNode;

    @Setter
    private Game game;

    @Setter
    private int depth;

    @Setter
    private EndGameTableBase endGameTableBase;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        this.keepProcessing = true;
    }

    @Override
    public void stopSearching() {
        this.keepProcessing = false;
    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        if (!keepProcessing) {
            throw new StopSearchingException();
        }

        if (game.getStatus().isFinalStatus()) {
            return terminalNode.maximize(currentPly, alpha, beta);
        }

        if(endGameTableBase.isProbeAvailable()){
            return egtbNode.maximize(currentPly, alpha, beta);
        }

        if (game.getState().getRepetitionCounter() > 1) {
            return loopNode.maximize(currentPly, alpha, beta);
        }

        if (currentPly == depth) {
            if (isCurrentPositionQuiet()) {
                return leafNode.maximize(currentPly, alpha, beta);
            } else {
                return horizonNode.maximize(currentPly, alpha, beta);
            }
        }

        return interiorNode.maximize(currentPly, alpha, beta);
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        if (!keepProcessing) {
            throw new StopSearchingException();
        }

        if (game.getStatus().isFinalStatus()) {
            return terminalNode.minimize(currentPly, alpha, beta);
        }

        if(endGameTableBase.isProbeAvailable()){
            return egtbNode.minimize(currentPly, alpha, beta);
        }

        if (game.getState().getRepetitionCounter() > 1) {
            return loopNode.minimize(currentPly, alpha, beta);
        }

        if (currentPly == depth) {
            if (isCurrentPositionQuiet()) {
                return leafNode.minimize(currentPly, alpha, beta);
            } else {
                return horizonNode.minimize(currentPly, alpha, beta);
            }
        }

        return interiorNode.minimize(currentPly, alpha, beta);
    }

    private boolean isCurrentPositionQuiet() {
        MoveContainerReader<Move> possibleMoves = game.getPossibleMoves();
        return possibleMoves.hasQuietMoves();
    }
}

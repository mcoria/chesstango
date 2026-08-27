package net.chesstango.search.smart.core.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.search.Acceptor;
import net.chesstango.search.StopSearchingException;
import net.chesstango.search.Visitor;
import net.chesstango.search.SearchListener;
import net.chesstango.search.StopSearchingListener;
import net.chesstango.search.smart.AlphaBetaFilter;
import net.chesstango.search.smart.egtb.EndGameTableBase;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaFlowControl implements AlphaBetaFilter, Acceptor, SearchListener, StopSearchingListener {
    private volatile boolean keepProcessing;

    @Setter
    @Getter
    private AlphaBetaFilter terminalNode;

    @Setter
    @Getter
    private AlphaBetaFilter interiorNode;

    @Setter
    @Getter
    private AlphaBetaFilter quiescenceNode;

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
    public int alphaBeta(int currentPly, int alpha, int beta) {
        if (!keepProcessing) {
            throw new StopSearchingException();
        }

        if (game.getStatus().isFinalStatus()) {
            return -terminalNode.alphaBeta(currentPly + 1, -beta, -alpha);
        }

        if (endGameTableBase.isProbeAvailable()) {
            return -egtbNode.alphaBeta(currentPly + 1, -beta, -alpha);
        }

        if (game.getState().getRepetitionCounter() > 1) {
            return -loopNode.alphaBeta(currentPly + 1, -beta, -alpha);
        }

        if (currentPly + 1 < depth) {
            return -interiorNode.alphaBeta(currentPly + 1, -beta, -alpha);
        } else {
            if (quiescenceNode == null || isCurrentPositionQuiet()) {
                return -leafNode.alphaBeta(currentPly + 1, -beta, -alpha);
            } else {
                return -quiescenceNode.alphaBeta(currentPly + 1, -beta, -alpha);
            }
        }
    }

    private boolean isCurrentPositionQuiet() {
        MoveContainerReader<Move> possibleMoves = game.getPossibleMoves();
        return possibleMoves.hasQuietMoves();
    }
}

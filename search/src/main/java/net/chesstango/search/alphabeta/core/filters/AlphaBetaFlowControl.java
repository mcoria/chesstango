package net.chesstango.search.alphabeta.core.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.search.Acceptor;
import net.chesstango.search.SearchListener;
import net.chesstango.search.StopSearchingListener;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.AlphaBetaFilter;
import net.chesstango.search.alphabeta.egtb.EndGameTableBase;

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
    @Getter
    private AlphaBetaFilter checkEvasionNode;

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
        throw new RuntimeException("Testing");
        /*
        if (!keepProcessing) {
            throw new StopSearchingException();
        }

        int nextPly = currentPly + 1;

        if (game.getStatus().isFinalStatus()) {
            return -terminalNode.alphaBeta(nextPly, -beta, -alpha);
        }

        if (endGameTableBase.isProbeAvailable()) {
            return -egtbNode.alphaBeta(nextPly, -beta, -alpha);
        }

        if (game.getState().getRepetitionCounter() > 1) {
            return -loopNode.alphaBeta(nextPly, -beta, -alpha);
        }

        if (nextPly < depth) {
            return -interiorNode.alphaBeta(nextPly, -beta, -alpha);
        } else {
            if (checkEvasionNode != null && game.getStatus().isCheck()) {
                return -checkEvasionNode.alphaBeta(nextPly, -beta, -alpha);
            } else if (quiescenceNode == null || isCurrentPositionQuiet()) {
                return -leafNode.alphaBeta(nextPly, -beta, -alpha);
            } else {
                return -quiescenceNode.alphaBeta(nextPly, -beta, -alpha);
            }
        }
         */
    }

    private boolean isCurrentPositionQuiet() {
        MoveContainerReader<Move> possibleMoves = game.getPossibleMoves();
        return possibleMoves.hasQuietMoves();
    }
}

package net.chesstango.search.smart.alphabeta.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.search.StopSearchingException;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.StopSearchingListener;

/**
 * @author Mauricio Coria
 */
public class ExtensionFlowControl implements AlphaBetaFilter, SearchByCycleListener, StopSearchingListener {
    private volatile boolean keepProcessing;

    @Setter
    @Getter
    private AlphaBetaFilter terminalNode;

    @Setter
    @Getter
    private AlphaBetaFilter leafNode;

    @Setter
    @Getter
    private AlphaBetaFilter quiescenceNode;

    @Setter
    @Getter
    private AlphaBetaFilter checkResolverNode;

    @Setter
    @Getter
    private AlphaBetaFilter loopNode;

    @Setter
    private Game game;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
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

        if (checkResolverNode != null) {
            if (game.getState().getRepetitionCounter() > 1) {
                /**
                 * Con checkresolver habilitado, puede que algun movimiento para resolver el Jaque repita una posicion
                 * que se encuentre antes de horizonte
                 */
                return loopNode.maximize(currentPly, alpha, beta);
            }
            if (game.getStatus().isCheck()) {
                return checkResolverNode.maximize(currentPly, alpha, beta);
            }
        } else if (game.getState().getRepetitionCounter() > 1) {
            throw new RuntimeException("No se deberia llegar a esta situacion");
        }

        if (isCurrentPositionQuiet()) {
            return leafNode.maximize(currentPly, alpha, beta);
        }

        return quiescenceNode.maximize(currentPly, alpha, beta);
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        if (!keepProcessing) {
            throw new StopSearchingException();
        }

        if (game.getStatus().isFinalStatus()) {
            return terminalNode.minimize(currentPly, alpha, beta);
        }

        if (checkResolverNode != null) {
            if (game.getState().getRepetitionCounter() > 1) {
                /**
                 * Con checkresolver habilitado, puede que algun movimiento para resolver el Jaque repita una posicion
                 * que se encuentre antes de horizonte
                 */
                return loopNode.minimize(currentPly, alpha, beta);
            }

            if (game.getStatus().isCheck()) {
                return checkResolverNode.minimize(currentPly, alpha, beta);
            }
        } else if (game.getState().getRepetitionCounter() > 1) {
            throw new RuntimeException("No se deberia llegar a esta situacion");
        }

        if (isCurrentPositionQuiet()) {
            return leafNode.minimize(currentPly, alpha, beta);
        }

        return quiescenceNode.minimize(currentPly, alpha, beta);
    }

    private boolean isCurrentPositionQuiet() {
        MoveContainerReader<Move> possibleMoves = game.getPossibleMoves();
        return possibleMoves.hasQuietMoves();
    }
}

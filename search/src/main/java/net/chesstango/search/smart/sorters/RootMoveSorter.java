package net.chesstango.search.smart.sorters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.RootChildEvaluation;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthListener;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
public class RootMoveSorter implements MoveSorter, SearchByCycleListener {
    @Getter
    @Setter
    private NodeMoveSorter nodeMoveSorter;

    @Setter
    private Game game;

    @Setter
    private List<RootChildEvaluation> lastRootChildEvaluations;

    @Setter
    private RootChildEvaluation lastRootChildEvaluation;


    private boolean maximize;

    private int numberOfMove;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        this.maximize = Color.WHITE.equals(game.getPosition().getCurrentTurn());
        this.numberOfMove = game.getPossibleMoves().size();
        this.lastRootChildEvaluations = null;
        this.lastRootChildEvaluation = null;
    }

    @Override
    public Iterable<Move> getOrderedMoves(int currentPly) {
        if (lastRootChildEvaluation == null) {
            return nodeMoveSorter.getOrderedMoves(currentPly);
        } else {
            return getSortedMovesByLastMoveEvaluations();
        }
    }


    private List<Move> getSortedMovesByLastMoveEvaluations() {
        List<Move> moveList = new LinkedList<>();

        Move lastBestMove = lastRootChildEvaluation.move();

        moveList.add(lastBestMove);

        Stream<RootChildEvaluation> moveStream = lastRootChildEvaluations.stream()
                .filter(moveEvaluation -> !lastBestMove.equals(moveEvaluation.move()));

        moveStream = maximize ? moveStream.sorted(Comparator.reverseOrder()) : moveStream.sorted();

        moveStream.map(RootChildEvaluation::move).forEach(moveList::add);

        if (moveList.size() != numberOfMove) {
            throw new RuntimeException("Not all move were explorer during last iteration");
        }

        return moveList;
    }
}

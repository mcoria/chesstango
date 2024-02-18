package net.chesstango.search.smart.sorters;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparator;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
public class RootMoveSorter implements MoveSorter, SearchByCycleListener, SearchByDepthListener {
    private final NodeMoveSorter nodeMoveSorter;
    private Game game;
    private Move lastBestMove;
    private List<MoveEvaluation> lastMoveEvaluations;


    public RootMoveSorter() {
        this.nodeMoveSorter = new NodeMoveSorter();
        this.nodeMoveSorter.setMoveComparator(new DefaultMoveComparator());
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
        this.nodeMoveSorter.beforeSearch(context);
    }

    @Override
    public void afterSearch(SearchMoveResult searchMoveResult) {
        this.nodeMoveSorter.afterSearch(searchMoveResult);
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        lastBestMove = Objects.nonNull(context.getLastBestMoveEvaluation()) ? context.getLastBestMoveEvaluation().move() : null;

        lastMoveEvaluations = Objects.nonNull(context.getLastMoveEvaluations()) ? context.getLastMoveEvaluations() : null;
    }


    @Override
    public Iterable<Move> getOrderedMoves(int currentPly) {
        if (lastBestMove == null) {
            return nodeMoveSorter.getOrderedMoves(currentPly);
        } else {
            return getSortedMovesByLastMoveEvaluations();
        }
    }


    private List<Move> getSortedMovesByLastMoveEvaluations() {
        List<Move> moveList = new LinkedList<>();

        moveList.add(lastBestMove);

        Stream<MoveEvaluation> moveStream = lastMoveEvaluations.stream().filter(moveEvaluation -> !lastBestMove.equals(moveEvaluation.move()));

        boolean naturalOrder = Color.BLACK.equals(game.getChessPosition().getCurrentTurn());

        moveStream = naturalOrder ? moveStream.sorted(Comparator.comparing(MoveEvaluation::evaluation)) : moveStream.sorted(Comparator.comparing(MoveEvaluation::evaluation).reversed());

        moveStream.map(MoveEvaluation::move).forEach(moveList::add);

        return moveList;
    }
}

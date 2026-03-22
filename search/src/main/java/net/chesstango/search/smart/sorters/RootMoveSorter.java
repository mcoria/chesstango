package net.chesstango.search.smart.sorters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;

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
    private MoveSorter next;

    @Setter
    private Game game;

    @Setter
    private List<RootMoveEvaluation> lastRootMoveEvaluations;

    @Setter
    private RootMoveEvaluation lastRootMoveEvaluation;


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
        this.lastRootMoveEvaluations = null;
        this.lastRootMoveEvaluation = null;
    }

    @Override
    public Iterable<Move> getOrderedMoves(int currentPly) {
        if (lastRootMoveEvaluation == null) {
            return next.getOrderedMoves(currentPly);
        } else {
            return getSortedMovesByLastMoveEvaluations();
        }
    }


    private List<Move> getSortedMovesByLastMoveEvaluations() {
        List<Move> moveList = new LinkedList<>();

        Move lastBestMove = lastRootMoveEvaluation.move();

        moveList.add(lastBestMove);

        Stream<RootMoveEvaluation> moveStream = lastRootMoveEvaluations.stream()
                .filter(moveEvaluation -> !lastBestMove.equals(moveEvaluation.move()));

        moveStream = maximize ? moveStream.sorted(Comparator.reverseOrder()) : moveStream.sorted();

        moveStream.map(RootMoveEvaluation::move).forEach(moveList::add);

        if (moveList.size() != numberOfMove) {
            throw new RuntimeException("Not all move were explorer during last iteration");
        }

        return moveList;
    }
}

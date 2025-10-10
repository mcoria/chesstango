package net.chesstango.search.smart.sorters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.MoveEvaluation;
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
    private NodeMoveSorter nodeMoveSorter;

    @Setter
    private Game game;

    private boolean maximize;
    private int numberOfMove;

    @Setter
    private Move lastBestMove;

    @Setter
    private List<MoveEvaluation> lastMoveEvaluations;


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        this.maximize = Color.WHITE.equals(game.getPosition().getCurrentTurn());
        this.numberOfMove = game.getPossibleMoves().size();
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

        Stream<MoveEvaluation> moveStream = lastMoveEvaluations.stream()
                .filter(moveEvaluation -> !lastBestMove.equals(moveEvaluation.move()));

        moveStream = maximize ? moveStream.sorted(Comparator.reverseOrder()) : moveStream.sorted();

        moveStream.map(MoveEvaluation::move).forEach(moveList::add);

        if (moveList.size() != numberOfMove) {
            throw new RuntimeException("Not all move were explorer during last iteration");
        }

        return moveList;
    }
}

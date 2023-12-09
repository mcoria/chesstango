package net.chesstango.search.smart.alphabeta.filters.once;

import net.chesstango.board.Color;
import net.chesstango.board.moves.Move;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaAbstract;
import net.chesstango.search.smart.sorters.MoveComparator;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaRoot extends AlphaBetaAbstract implements SearchByDepthListener {
    private static final MoveComparator moveComparator = new MoveComparator();
    private Move lastBestMove;
    private List<MoveEvaluation> lastMoveEvaluations;

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        lastBestMove = context.getLastBestMove();
        lastMoveEvaluations = context.getLastMoveEvaluations();
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
    }


    @Override
    protected List<Move> getSortedMoves() {
        if (lastBestMove == null) {
            return getSortedMovesByMoveComparator();
        } else {
            return getSortedMovesByLastMoveEvaluations();
        }
    }

    private List<Move> getSortedMovesByMoveComparator() {
        List<Move> moves = new LinkedList<>();
        for (Move move : game.getPossibleMoves()) {
            moves.add(move);
        }
        moves.sort(moveComparator.reversed());
        return moves;
    }

    private List<Move> getSortedMovesByLastMoveEvaluations() {
        List<Move> moveList = new LinkedList<>();

        moveList.add(lastBestMove);

        Stream<MoveEvaluation> moveStream = lastMoveEvaluations
                .stream()
                .filter(moveEvaluation -> !lastBestMove.equals(moveEvaluation.move()));

        boolean naturalOrder = Color.BLACK.equals(game.getChessPosition().getCurrentTurn());

        moveStream = naturalOrder ?
                moveStream.sorted(Comparator.comparing(MoveEvaluation::evaluation)) :
                moveStream.sorted(Comparator.comparing(MoveEvaluation::evaluation).reversed());

        moveStream.map(MoveEvaluation::move).forEach(moveList::add);

        return moveList;
    }

}

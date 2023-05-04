package net.chesstango.evaluation.tuning.fitnessfunctions;

import io.jenetics.Genotype;
import io.jenetics.IntegerGene;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.EDPReader;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.DefaultSearchMove;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;

import java.util.*;
import java.util.function.Function;

/**
 * @author Mauricio Coria
 */
public class FitnessBySearch implements FitnessFunction {

    private static final int MATCH_DEPTH = 3;

    private final Function<Genotype<IntegerGene>, GameEvaluator> gameEvaluatorSupplierFn;

    private List<EDPReader.EDPEntry> edpEntries;

    public FitnessBySearch(Function<Genotype<IntegerGene>, GameEvaluator> gameEvaluatorSupplierFn) {
        this.gameEvaluatorSupplierFn = gameEvaluatorSupplierFn;
    }

    @Override
    public long fitness(Genotype<IntegerGene> genotype) {
        return run(gameEvaluatorSupplierFn.apply(genotype));
    }

    @Override
    public void start() {
        //String filename = "C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\40H-EPD-databases-2022-10-04\\failed-2023-04-30.epd";
        String filename = "C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\wac\\wac-2018.epd";

        EDPReader reader = new EDPReader();

        edpEntries = reader.readEdpFile(filename);
    }

    @Override
    public void stop() {
    }

    protected long run(GameEvaluator gameEvaluator) {
        long points = 0;

        for (EDPReader.EDPEntry edpEntry : edpEntries) {
            points += run(edpEntry, gameEvaluator);
        }

        return points;
    }

    protected long run(EDPReader.EDPEntry edpEntry, GameEvaluator gameEvaluator) {
        SearchMove moveFinder = new DefaultSearchMove(gameEvaluator);

        Game game = FENDecoder.loadGame(edpEntry.fen);

        SearchMoveResult searchResult = moveFinder.searchBestMove(game, MATCH_DEPTH);

        return getPoints(edpEntry.bestMoves.get(0), searchResult.getMoveEvaluationList());
    }

    protected long getPoints(Move bestMove, Collection<SearchMoveResult.MoveEvaluation> evaluationCollection) {
        Color turn = bestMove.getFrom().getPiece().getColor();

        List<SearchMoveResult.MoveEvaluation> sortedEvaluationList = new LinkedList<>();
        sortedEvaluationList.addAll(evaluationCollection);

        if (Color.WHITE.equals(turn)) {
            Collections.sort(sortedEvaluationList, Comparator.reverseOrder());
        } else {
            Collections.sort(sortedEvaluationList);
        }

        int i = 0;
        for (SearchMoveResult.MoveEvaluation moveEvaluation : sortedEvaluationList) {
            if (moveEvaluation.move.equals(bestMove)) {
                break;
            }
            i--;
        }
        return i;
    }
}

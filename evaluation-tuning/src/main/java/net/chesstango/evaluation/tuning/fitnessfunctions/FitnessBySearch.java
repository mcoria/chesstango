package net.chesstango.evaluation.tuning.fitnessfunctions;

import io.jenetics.Genotype;
import io.jenetics.IntegerGene;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.EPDEntry;
import net.chesstango.board.representations.EPDReader;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.builders.AlphaBetaBuilder;

import java.util.*;
import java.util.function.Function;

/**
 * @author Mauricio Coria
 */
public class FitnessBySearch implements FitnessFunction {

    private static final int MATCH_DEPTH = 4;

    private final Function<Genotype<IntegerGene>, GameEvaluator> gameEvaluatorSupplierFn;

    private List<EPDEntry> edpEntries;

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

        EPDReader reader = new EPDReader();

        edpEntries = reader.readEdpFile(filename);
    }

    @Override
    public void stop() {
    }

    protected long run(GameEvaluator gameEvaluator) {
        long points = 0;

        for (EPDEntry EPDEntry : edpEntries) {
            points += run(EPDEntry, gameEvaluator);
        }

        return points;
    }

    protected long run(EPDEntry EPDEntry, GameEvaluator gameEvaluator) {
        SearchMove moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(gameEvaluator)

                .withQuiescence()

                .withTranspositionTable()
                .withQTranspositionTable()

                .withTranspositionMoveSorter()
                .withQTranspositionMoveSorter()


                .withIterativeDeepening()

                .withStatistics()

                .build();


        Game game = FENDecoder.loadGame(EPDEntry.fen);

        SearchMoveResult searchResult = moveFinder.search(game, MATCH_DEPTH);

        return getPoints(game.getPossibleMoves().size(), EPDEntry.bestMoves.get(0), searchResult.getMoveEvaluations());
    }

    protected long getPoints(int possibleMoves, Move bestMove, Collection<MoveEvaluation> evaluationCollection) {
        Color turn = bestMove.getFrom().getPiece().getColor();

        List<MoveEvaluation> sortedEvaluationList = new LinkedList<>();
        sortedEvaluationList.addAll(evaluationCollection);

        if (Color.WHITE.equals(turn)) {
            Collections.sort(sortedEvaluationList, Comparator.reverseOrder());
        } else {
            Collections.sort(sortedEvaluationList);
        }

        int i = 0;
        for (MoveEvaluation moveEvaluation : sortedEvaluationList) {
            if (Objects.equals(moveEvaluation.move(), bestMove)) {
                break;
            }
            i--;
        }

        // Premiamos cuando encontramos el mejor movimiento y castigamos cuando no.
        return i == 0 ? possibleMoves : i;
    }
}

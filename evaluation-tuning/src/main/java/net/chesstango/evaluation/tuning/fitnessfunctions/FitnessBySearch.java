package net.chesstango.evaluation.tuning.fitnessfunctions;

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
import net.chesstango.search.SearchParameter;
import net.chesstango.search.builders.AlphaBetaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.OptionalInt;

/**
 * @author Mauricio Coria
 */
public class FitnessBySearch implements FitnessFunction {
    private static final Logger logger = LoggerFactory.getLogger(FitnessBySearch.class);
    private static final int MAX_DEPTH = 1;
    private List<EPDEntry> edpEntries;
    private static final List<String> files = List.of("C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\Bratko-Kopec.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\wac-2018.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS1.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS2.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS3.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS4.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS5.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS6.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS7.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS8.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS9.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS10.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS11.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS12.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS13.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS14.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS15.epd");

    @Override
    public long fitness(GameEvaluator gameEvaluator) {
        return run(gameEvaluator);
    }

    @Override
    public void start() {
        EPDReader reader = new EPDReader();

        edpEntries = new LinkedList<>();

        files.stream().map(reader::readEdpFile).forEach(edpEntries::addAll);
    }

    @Override
    public void stop() {
    }

    protected long run(GameEvaluator gameEvaluator) {
        long points = 0;

        final int printProgress = edpEntries.size() / 10;
        int processedEntries = 0;
        for (EPDEntry EPDEntry : edpEntries) {
            points += run(EPDEntry, gameEvaluator);
            processedEntries++;
            if (processedEntries % printProgress == 0) {
                logger.info("Processed {} / {}", processedEntries, edpEntries.size());
            }
        }

        return points;
    }

    protected long run(EPDEntry epdEntry, GameEvaluator gameEvaluator) {
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


        Game game = FENDecoder.loadGame(epdEntry.fen);

        moveFinder.setParameter(SearchParameter.MAX_DEPTH, MAX_DEPTH);
        SearchMoveResult searchResult = moveFinder.search(game);

        return getPoints(epdEntry, searchResult);
    }

    protected long getPoints(EPDEntry epdEntry, SearchMoveResult searchResult) {
        Color turn = epdEntry.game.getChessPosition().getCurrentTurn();
        List<MoveEvaluation> sortedEvaluationList = new LinkedList<>(searchResult.getMoveEvaluations());

        if (Color.WHITE.equals(turn)) {
            sortedEvaluationList.sort(Comparator.reverseOrder());
        } else {
            sortedEvaluationList.sort(Comparator.naturalOrder());
        }

        long points = 0;
        int movesCounter = 0;
        for (Move bestMove : epdEntry.bestMoves) {
            points += getMovePoints(turn, bestMove, sortedEvaluationList);
            movesCounter++;
        }

        points = points / movesCounter;

        return points / movesCounter;
    }

    /**
     * Los puntos representar la cantidad de movimientos inferiores a bestMove acorde a las evaluaciones
     * Observar que se buscan inferiores
     * <p>
     * Si m1=5; m2=5 y m3=1 y el turno es de blancas (maximixar) entonces los puntos que retorna es m3=1
     * No consideramos que son IGUALES o menores dado que podemos tener el siguiente escenario
     * m1=5; m2=5 y m3=5 y el turno es de blancas (maximixar)
     *
     * @param turn
     * @param bestMove
     * @param moveEvaluations
     * @return
     */
    private long getMovePoints(Color turn, Move bestMove, List<MoveEvaluation> moveEvaluations) {
        OptionalInt bestMoveEvaluationOptional = moveEvaluations.stream()
                .filter(moveEvaluation -> bestMove.equals(moveEvaluation.move()))
                .mapToInt(MoveEvaluation::evaluation)
                .findAny();

        if (bestMoveEvaluationOptional.isEmpty()) {
            return 0;
        }

        final int bestMoveEvaluation = bestMoveEvaluationOptional.getAsInt();

        return moveEvaluations.stream()
                .mapToInt(MoveEvaluation::evaluation)
                .filter(value -> Color.WHITE.equals(turn) ? value < bestMoveEvaluation : value > bestMoveEvaluation)
                .count();
    }
}

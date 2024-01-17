package net.chesstango.evaluation.tuning.fitnessfunctions;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.EPDEntry;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.evaluators.EvaluatorSEandImp02;
import net.chesstango.evaluation.evaluators.EvaluatorSEandImp03;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.MoveEvaluationType;
import net.chesstango.search.SearchMoveResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class FitnessBySearchTest {
    private static final int DEPTH = 1;
    private FitnessBySearch fitnessFn;

    @BeforeEach
    public void setup() {
        fitnessFn = new FitnessBySearch();
    }

    /**
     * Actual best move = Best move found
     */
    @Test
    public void test_white01() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        final Move bestMoveFound = game.getMove(Square.c2, Square.c3);
        final int bestMoveEvaluationFound = 100;

        List<MoveEvaluation> moveEvaluations = new LinkedList<>();
        int i = 1;
        for (Move move : game.getPossibleMoves()) {
            if (move.equals(bestMoveFound)) {
                moveEvaluations.add(new MoveEvaluation(move, bestMoveEvaluationFound, MoveEvaluationType.EXACT));
            } else {
                moveEvaluations.add(new MoveEvaluation(move, i, MoveEvaluationType.EXACT));
            }
            i++;
        }

        EPDEntry epdEntry = createEpdEntry(game, List.of(bestMoveFound));

        SearchMoveResult searchResult = createSearchMoveResult(bestMoveFound, bestMoveEvaluationFound, moveEvaluations);

        long points = fitnessFn.getPoints(epdEntry, searchResult);

        assertEquals(19, points);
    }

    /**
     * Actual best move is the second-best move found
     */
    @Test
    public void test_white02() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        final Move bestMoveFound = game.getMove(Square.c2, Square.c3);
        final int bestMoveEvaluationFound = 100;

        final Move actualBestMove = game.getMove(Square.f2, Square.f4);
        final int actualBestMoveEvaluation = 90;

        List<MoveEvaluation> moveEvaluations = new LinkedList<>();
        int i = 1;
        for (Move move : game.getPossibleMoves()) {
            if (move.equals(bestMoveFound)) {
                moveEvaluations.add(new MoveEvaluation(move, bestMoveEvaluationFound, MoveEvaluationType.EXACT));
            } else if (move.equals(actualBestMove)) {
                moveEvaluations.add(new MoveEvaluation(move, actualBestMoveEvaluation, MoveEvaluationType.EXACT));
            } else {
                moveEvaluations.add(new MoveEvaluation(move, i, MoveEvaluationType.EXACT));
            }
            i++;
        }

        EPDEntry epdEntry = createEpdEntry(game, List.of(actualBestMove));

        SearchMoveResult searchResult = createSearchMoveResult(bestMoveFound, bestMoveEvaluationFound, moveEvaluations);

        long points = fitnessFn.getPoints(epdEntry, searchResult);

        assertEquals(18, points);
    }

    /**
     * Actual best move is last
     */
    @Test
    public void test_white03() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        final Move bestMoveFoundBySearch = game.getMove(Square.c2, Square.c3);
        final int bestEvaluationFoundBySearch = 100;

        final Move actualBestMove = game.getMove(Square.f2, Square.f4);
        final int actualBestMoveEvaluation = -10;

        List<MoveEvaluation> moveEvaluations = new LinkedList<>();
        int i = 1;
        for (Move move : game.getPossibleMoves()) {
            if (move.equals(bestMoveFoundBySearch)) {
                moveEvaluations.add(new MoveEvaluation(move, bestEvaluationFoundBySearch, MoveEvaluationType.EXACT));
            } else if (move.equals(actualBestMove)) {
                moveEvaluations.add(new MoveEvaluation(move, actualBestMoveEvaluation, MoveEvaluationType.EXACT));
            } else {
                moveEvaluations.add(new MoveEvaluation(move, i, MoveEvaluationType.EXACT));
            }
            i++;
        }

        EPDEntry epdEntry = createEpdEntry(game, List.of(actualBestMove));

        SearchMoveResult searchResult = createSearchMoveResult(bestMoveFoundBySearch, bestEvaluationFoundBySearch, moveEvaluations);

        long points = fitnessFn.getPoints(epdEntry, searchResult);

        assertEquals(0, points);
    }


    /**
     * Actual best move was not searched
     */
    @Test
    public void test_white04() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        final Move bestMoveFoundBySearch = game.getMove(Square.c2, Square.c3);
        final int bestEvaluationFoundBySearch = 100;

        final Move actualBestMove = game.getMove(Square.f2, Square.f4);
        final int actualBestMoveEvaluation = 90;

        List<MoveEvaluation> moveEvaluations = new LinkedList<>();
        int i = 1;
        for (Move move : game.getPossibleMoves()) {
            if (move.equals(bestMoveFoundBySearch)) {
                moveEvaluations.add(new MoveEvaluation(move, bestEvaluationFoundBySearch, MoveEvaluationType.EXACT));
            } else if (move.equals(actualBestMove)) {
                //moveEvaluations.add(new MoveEvaluation(move, 90));
            } else {
                moveEvaluations.add(new MoveEvaluation(move, i, MoveEvaluationType.EXACT));
            }
            i++;
        }

        EPDEntry epdEntry = createEpdEntry(game, List.of(actualBestMove));

        SearchMoveResult searchResult = createSearchMoveResult(bestMoveFoundBySearch, bestEvaluationFoundBySearch, moveEvaluations);

        long points = fitnessFn.getPoints(epdEntry, searchResult);

        assertEquals(0, points);
    }

    /**
     * Actual best move = Best move found
     */
    @Test
    public void test_black01() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN).mirror();

        final Move bestMoveFoundBySearch = game.getMove(Square.c7, Square.c6);
        final int bestEvaluationFoundBySearch = -100;

        List<MoveEvaluation> moveEvaluations = new LinkedList<>();
        int i = 1;
        for (Move move : game.getPossibleMoves()) {
            if (move.equals(bestMoveFoundBySearch)) {
                moveEvaluations.add(new MoveEvaluation(move, bestEvaluationFoundBySearch, MoveEvaluationType.EXACT));
            } else {
                moveEvaluations.add(new MoveEvaluation(move, i, MoveEvaluationType.EXACT));
            }
            i++;
        }

        EPDEntry epdEntry = createEpdEntry(game, List.of(bestMoveFoundBySearch));

        SearchMoveResult searchResult = createSearchMoveResult(bestMoveFoundBySearch, bestEvaluationFoundBySearch, moveEvaluations);

        long points = fitnessFn.getPoints(epdEntry, searchResult);

        assertEquals(19, points);
    }

    /**
     * Actual best move is the second-best move found
     */
    @Test
    public void test_black02() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN).mirror();

        final Move bestMoveFoundBySearch = game.getMove(Square.c7, Square.c6);
        final int bestEvaluationFoundBySearch = -100;

        final Move actualBestMove = game.getMove(Square.f7, Square.f5);
        final int actualBestMoveEvaluation = -90;

        List<MoveEvaluation> moveEvaluations = new LinkedList<>();
        int i = 1;
        for (Move move : game.getPossibleMoves()) {
            if (move.equals(bestMoveFoundBySearch)) {
                moveEvaluations.add(new MoveEvaluation(move, bestEvaluationFoundBySearch, MoveEvaluationType.EXACT));
            } else if (move.equals(actualBestMove)) {
                moveEvaluations.add(new MoveEvaluation(move, actualBestMoveEvaluation, MoveEvaluationType.EXACT));
            } else {
                moveEvaluations.add(new MoveEvaluation(move, -i, MoveEvaluationType.EXACT));
            }
            i++;
        }

        EPDEntry epdEntry = createEpdEntry(game, List.of(actualBestMove));

        SearchMoveResult searchResult = createSearchMoveResult(bestMoveFoundBySearch, bestEvaluationFoundBySearch, moveEvaluations);

        long points = fitnessFn.getPoints(epdEntry, searchResult);

        assertEquals(18, points);
    }

    /**
     * Actual best move is last
     */
    @Test
    public void test_black03() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN).mirror();

        final Move bestMoveFoundBySearch = game.getMove(Square.c7, Square.c6);
        final int bestEvaluationFoundBySearch = -100;

        final Move actualBestMove = game.getMove(Square.f7, Square.f5);
        final int actualBestMoveEvaluation = 10;

        List<MoveEvaluation> moveEvaluations = new LinkedList<>();
        int i = 1;
        for (Move move : game.getPossibleMoves()) {
            if (move.equals(bestMoveFoundBySearch)) {
                moveEvaluations.add(new MoveEvaluation(move, bestEvaluationFoundBySearch, MoveEvaluationType.EXACT));
            } else if (move.equals(actualBestMove)) {
                moveEvaluations.add(new MoveEvaluation(move, actualBestMoveEvaluation, MoveEvaluationType.EXACT));
            } else {
                moveEvaluations.add(new MoveEvaluation(move, -i, MoveEvaluationType.EXACT));
            }
            i++;
        }

        EPDEntry epdEntry = createEpdEntry(game, List.of(actualBestMove));

        SearchMoveResult searchResult = createSearchMoveResult(bestMoveFoundBySearch, bestEvaluationFoundBySearch, moveEvaluations);

        long points = fitnessFn.getPoints(epdEntry, searchResult);

        assertEquals(0, points);
    }

    /**
     * Actual best move was not searched
     */
    @Test
    public void test_black04() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN).mirror();

        final Move bestMoveFoundBySearch = game.getMove(Square.c7, Square.c6);
        final int bestEvaluationFoundBySearch = -100;

        final Move actualBestMove = game.getMove(Square.f7, Square.f5);
        final int actualBestMoveEvaluation = -90;

        List<MoveEvaluation> moveEvaluations = new LinkedList<>();
        int i = 1;
        for (Move move : game.getPossibleMoves()) {
            if (move.equals(bestMoveFoundBySearch)) {
                moveEvaluations.add(new MoveEvaluation(move, bestEvaluationFoundBySearch, MoveEvaluationType.EXACT));
            } else if (move.equals(actualBestMove)) {
                //moveEvaluations.add(new MoveEvaluation(move, 90));
            } else {
                moveEvaluations.add(new MoveEvaluation(move, -i, MoveEvaluationType.EXACT));
            }
            i++;
        }

        EPDEntry epdEntry = createEpdEntry(game, List.of(actualBestMove));

        SearchMoveResult searchResult = createSearchMoveResult(bestMoveFoundBySearch, bestEvaluationFoundBySearch, moveEvaluations);

        long points = fitnessFn.getPoints(epdEntry, searchResult);

        assertEquals(0, points);
    }

    private EPDEntry createEpdEntry(Game game, List<Move> actualBestMove) {
        EPDEntry epdEntry = new EPDEntry();
        epdEntry.fen = game.getInitialFen();
        epdEntry.game = game;
        epdEntry.bestMoves = actualBestMove;
        return epdEntry;
    }

    private SearchMoveResult createSearchMoveResult(Move bestMoveFoundBySearch, int bestEvaluationFoundBySearch, List<MoveEvaluation> moveEvaluations) {
        SearchMoveResult searchResult = new SearchMoveResult(DEPTH, bestEvaluationFoundBySearch, bestMoveFoundBySearch, null);
        searchResult.setMoveEvaluations(moveEvaluations);

        return searchResult;
    }


    @Test
    @Disabled
    public void test01() {
        List<String> files = List.of(
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\Bratko-Kopec.epd",
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
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS15.epd"
        );

        FitnessBySearch fitnessBySearch = new FitnessBySearch(files, 1);

        fitnessBySearch.start();
        long points = fitnessBySearch.run(new EvaluatorSEandImp03(205, 753, 17, 25));
        fitnessBySearch.stop();

        assertEquals(49017L, points);
    }


    @Test
    @Disabled
    public void test02() {
        List<String> files = List.of(
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\Bratko-Kopec.epd",
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
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS15.epd"
        );

        FitnessBySearch fitnessBySearch = new FitnessBySearch(files, 3);

        fitnessBySearch.start();
        long points = fitnessBySearch.run(new EvaluatorSEandImp03(725, 223, 31, 21));
        fitnessBySearch.stop();

        assertEquals(50780L, points);
    }


    @Test
    @Disabled
    public void test03() {
        List<String> files = List.of(
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\Bratko-Kopec.epd",
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
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS15.epd"
        );

        FitnessBySearch fitnessBySearch = new FitnessBySearch(files, 3);

        fitnessBySearch.start();
        long points = fitnessBySearch.run(new EvaluatorSEandImp03(545, 423, 17, 15));
        fitnessBySearch.stop();

        assertEquals(51635L, points);
    }

    @Test
    @Disabled
    public void test04() {
        List<String> files = List.of(
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\Bratko-Kopec.epd",
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
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS15.epd"
        );

        FitnessBySearch fitnessBySearch = new FitnessBySearch(files, 5);

        fitnessBySearch.start();
        long points = fitnessBySearch.run(new EvaluatorSEandImp02());
        fitnessBySearch.stop();

        assertEquals(51635L, points);
    }

}

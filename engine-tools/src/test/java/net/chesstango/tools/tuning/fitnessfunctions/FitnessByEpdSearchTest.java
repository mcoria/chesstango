package net.chesstango.tools.tuning.fitnessfunctions;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.epd.EPD;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterialAndPST;
import net.chesstango.evaluation.evaluators.EvaluatorImp04;
import net.chesstango.evaluation.evaluators.EvaluatorImp05;
import net.chesstango.evaluation.evaluators.EvaluatorImp06;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.MoveEvaluationType;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
@Disabled
public class FitnessByEpdSearchTest {

    /**
     * Actual best move = Best move found
     */
    @Test
    public void test_white01() {
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

        FitnessByEpdBestMove fitnessFn = new FitnessByEpdBestMove(files, 2);

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

        EPD epd = createEpdEntry(game, List.of(bestMoveFound));

        SearchResult searchResult = createSearchMoveResult(bestMoveFound, bestMoveEvaluationFound, moveEvaluations);

        long points = fitnessFn.getPoints(epd, searchResult);

        assertEquals(19, points);
    }

    /**
     * Actual best move is the second-best move found
     */
    @Test
    public void test_white02() {
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

        FitnessByEpdBestMove fitnessFn = new FitnessByEpdBestMove(files, 2);

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

        EPD epd = createEpdEntry(game, List.of(actualBestMove));

        SearchResult searchResult = createSearchMoveResult(bestMoveFound, bestMoveEvaluationFound, moveEvaluations);

        long points = fitnessFn.getPoints(epd, searchResult);

        assertEquals(18, points);
    }

    /**
     * Actual best move is last
     */
    @Test
    public void test_white03() {
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

        FitnessByEpdBestMove fitnessFn = new FitnessByEpdBestMove(files, 2);

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

        EPD epd = createEpdEntry(game, List.of(actualBestMove));

        SearchResult searchResult = createSearchMoveResult(bestMoveFoundBySearch, bestEvaluationFoundBySearch, moveEvaluations);

        long points = fitnessFn.getPoints(epd, searchResult);

        assertEquals(0, points);
    }


    /**
     * Actual best move was not searched
     */
    @Test
    public void test_white04() {
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

        FitnessByEpdBestMove fitnessFn = new FitnessByEpdBestMove(files, 2);

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

        EPD epd = createEpdEntry(game, List.of(actualBestMove));

        SearchResult searchResult = createSearchMoveResult(bestMoveFoundBySearch, bestEvaluationFoundBySearch, moveEvaluations);

        long points = fitnessFn.getPoints(epd, searchResult);

        assertEquals(0, points);
    }

    /**
     * Actual best move = Best move found
     */
    @Test
    public void test_black01() {
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

        FitnessByEpdBestMove fitnessFn = new FitnessByEpdBestMove(files, 2);

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

        EPD epd = createEpdEntry(game, List.of(bestMoveFoundBySearch));

        SearchResult searchResult = createSearchMoveResult(bestMoveFoundBySearch, bestEvaluationFoundBySearch, moveEvaluations);

        long points = fitnessFn.getPoints(epd, searchResult);

        assertEquals(19, points);
    }

    /**
     * Actual best move is the second-best move found
     */
    @Test
    public void test_black02() {
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

        FitnessByEpdBestMove fitnessFn = new FitnessByEpdBestMove(files, 2);

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

        EPD epd = createEpdEntry(game, List.of(actualBestMove));

        SearchResult searchResult = createSearchMoveResult(bestMoveFoundBySearch, bestEvaluationFoundBySearch, moveEvaluations);

        long points = fitnessFn.getPoints(epd, searchResult);

        assertEquals(18, points);
    }

    /**
     * Actual best move is last
     */
    @Test
    public void test_black03() {
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

        FitnessByEpdBestMove fitnessFn = new FitnessByEpdBestMove(files, 2);

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

        EPD epd = createEpdEntry(game, List.of(actualBestMove));

        SearchResult searchResult = createSearchMoveResult(bestMoveFoundBySearch, bestEvaluationFoundBySearch, moveEvaluations);

        long points = fitnessFn.getPoints(epd, searchResult);

        assertEquals(0, points);
    }

    /**
     * Actual best move was not searched
     */
    @Test
    public void test_black04() {
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

        FitnessByEpdBestMove fitnessFn = new FitnessByEpdBestMove(files, 2);

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

        EPD epd = createEpdEntry(game, List.of(actualBestMove));

        SearchResult searchResult = createSearchMoveResult(bestMoveFoundBySearch, bestEvaluationFoundBySearch, moveEvaluations);

        long points = fitnessFn.getPoints(epd, searchResult);

        assertEquals(0, points);
    }

    private EPD createEpdEntry(Game game, List<Move> actualBestMove) {
        EPD epd = new EPD();
        epd.setFenWithoutClocks(game.getInitialFEN());
        epd.setBestMoves(actualBestMove);
        return epd;
    }

    private SearchResult createSearchMoveResult(Move bestMoveFoundBySearch, int bestEvaluationFoundBySearch, List<MoveEvaluation> moveEvaluations) {
        return new SearchResult()
                .addSearchResultByDepth(
                        new SearchResultByDepth(1)
                                .setBestMoveEvaluation(
                                        new MoveEvaluation(bestMoveFoundBySearch, bestEvaluationFoundBySearch, MoveEvaluationType.EXACT)
                                )
                );
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

        FitnessByEpdBestMove fitnessByEpdBestMove = new FitnessByEpdBestMove(files, 2);

        fitnessByEpdBestMove.start();
        long points = fitnessByEpdBestMove.fitness(() -> new EvaluatorByMaterialAndPST(205, 753));
        fitnessByEpdBestMove.stop();

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

        FitnessByEpdBestMove fitnessByEpdBestMove = new FitnessByEpdBestMove(files, 3);

        fitnessByEpdBestMove.start();
        long points = fitnessByEpdBestMove.fitness(() -> new EvaluatorByMaterialAndPST(725, 223));
        fitnessByEpdBestMove.stop();

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

        FitnessByEpdBestMove fitnessByEpdBestMove = new FitnessByEpdBestMove(files, 3);

        fitnessByEpdBestMove.start();
        long points = fitnessByEpdBestMove.fitness(() -> new EvaluatorByMaterialAndPST(545, 423));
        fitnessByEpdBestMove.stop();

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

        FitnessByEpdBestMove fitnessByEpdBestMove = new FitnessByEpdBestMove(files, 5);

        fitnessByEpdBestMove.start();
        long points = fitnessByEpdBestMove.fitness(EvaluatorImp04::new);
        fitnessByEpdBestMove.stop();

        assertEquals(51635L, points);
    }

    @Test
    @Disabled
    public void test05() {
        FitnessByEpdBestMove fitnessByEpdBestMove = new FitnessByEpdBestMove();

        fitnessByEpdBestMove.start();
        long points = fitnessByEpdBestMove.fitness(() -> new EvaluatorImp04(700, 964, 209, 46));
        fitnessByEpdBestMove.stop();

        assertEquals(51635L, points);
    }

    @Test
    @Disabled
    public void test06() {
        List<String> files = List.of(
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\Bratko-Kopec.epd",
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\wac-2018.epd",
                "C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\sbd.epd",
                "C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\Nolot.epd",
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

        FitnessByEpdBestMove fitnessByEpdBestMove = new FitnessByEpdBestMove(files, 2);

        fitnessByEpdBestMove.start();
        long points = fitnessByEpdBestMove.fitness(() -> new EvaluatorImp06("{\"id\":\"ee0cb738\",\"weighs\":[540,436,25],\"mgPawnTbl\":[-705,779,226,-315,-683,-649,-433,526,-405,-587,-112,502,-953,-173,789,399,454,479,-813,-251,56,-161,-834,658,163,872,-954,124,190,-685,17,268,872,109,695,11,552,-547,263,-260,-741,-246,232,-86,-968,760,-682,862,606,230,360,808,-726,774,-7,-251,114,-804,-222,194,-770,-911,-799,-256],\"mgKnightTbl\":[821,-921,-396,-411,-410,-90,-458,-462,784,-4,-555,-298,-726,346,409,-319,-438,-474,-654,128,-68,-714,-59,788,907,611,624,-949,-503,882,-311,-602,718,998,-539,-534,-891,-2,-859,342,314,-608,-17,691,799,624,585,24,298,878,-256,982,525,-330,632,750,52,34,798,235,38,798,155,-578],\"mgBishopTbl\":[-803,455,202,-429,-875,25,-556,-926,-457,-398,-399,178,-161,-821,-605,102,53,-729,331,360,752,207,-312,-328,-48,151,-349,558,59,-64,-881,164,-834,-913,-76,693,487,-193,934,462,638,40,624,202,244,-33,-953,41,690,-45,471,-822,415,-267,225,470,-771,-715,263,-834,-897,317,-508,449],\"mgRookTbl\":[862,900,435,-794,-377,833,-350,-49,-847,-428,-241,334,-374,-685,71,-373,-885,-835,-205,-555,515,-981,-755,967,-924,287,483,448,-620,735,-800,33,-167,845,299,-618,-135,-620,662,-948,-388,-158,671,406,670,818,833,199,247,405,598,108,832,-225,483,274,-647,-286,-762,147,869,-301,-874,404],\"mgQueenTbl\":[252,817,128,-637,242,-870,-969,606,-339,-162,257,-18,-617,-848,944,-464,-345,785,-8,-386,760,-718,297,-434,-918,631,-792,102,766,770,-831,400,-415,253,250,468,-329,282,-172,76,-681,-996,394,232,682,-601,847,181,603,418,-837,-820,-510,-880,-221,-390,-124,-152,966,312,806,141,-299,994],\"mgKingTbl\":[-973,878,739,458,-698,140,155,-976,-831,875,-375,-401,274,692,80,869,-103,-256,260,-414,959,839,-713,-277,-605,398,461,-203,719,918,769,-164,344,788,-765,-876,124,693,876,34,-584,-99,-985,-239,274,475,-213,-668,-500,-660,70,-93,-184,488,-335,-887,-560,839,530,-439,666,-606,-742,-306],\"egPawnTbl\":[606,-140,-381,-156,-938,-973,-393,-630,747,243,-413,-405,820,-625,-794,-310,-193,-357,854,-816,437,-258,184,-463,129,-495,909,678,273,-689,90,685,509,181,-315,-317,-502,-839,425,407,-366,-97,-223,938,-369,-315,-139,88,-129,-101,831,127,-209,21,-863,-714,-721,-427,630,280,984,721,-137,940],\"egKnightTbl\":[-266,-427,-32,-92,-824,544,-409,-757,126,144,702,887,586,604,708,-584,-47,129,353,-760,688,-948,-843,-881,739,-411,-154,30,-990,-269,720,-853,258,-237,-664,159,44,382,-780,694,516,-74,-759,-233,-123,500,385,-265,103,-146,-187,-275,720,340,336,-89,-343,31,-19,-439,999,531,173,-711],\"egBishopTbl\":[-891,-694,-7,-767,522,191,250,180,-865,546,-263,-717,-552,146,-349,125,-136,-484,-532,-199,-401,-498,442,-971,569,-134,-849,-387,-298,495,732,389,-498,166,699,738,-383,-808,619,-665,555,-782,97,-200,541,261,55,355,-716,-210,-618,331,836,-83,382,-673,-847,430,-371,-513,448,968,492,58],\"egRookTbl\":[990,-309,-770,146,246,-732,300,868,201,994,-606,238,432,891,-219,290,-519,-955,982,-822,-703,-822,282,-846,621,-869,70,18,705,-589,923,-552,777,-618,-344,-325,855,-886,176,851,243,-677,968,619,-315,-640,-907,986,-579,-741,-581,-312,162,808,251,983,27,566,186,58,859,-623,12,260],\"egQueenTbl\":[673,270,775,-894,655,276,-87,-798,-233,819,-142,-653,528,-185,616,-826,188,219,-367,-505,-690,-237,-284,-676,-524,880,125,54,899,703,-730,-804,-838,-339,380,-939,-211,-146,-459,-359,-196,-960,7,433,550,430,-337,19,216,112,-53,510,37,-569,469,662,555,165,-845,388,762,904,-838,901],\"egKingTbl\":[-121,-974,508,906,548,-785,560,184,-489,79,293,-511,407,630,-558,266,617,349,708,-795,876,-333,435,-672,584,214,-471,482,-318,-341,-8,408,811,-41,-858,-62,551,149,783,-977,-365,-125,-1,34,-344,72,-737,45,49,770,-437,-581,-498,-840,461,485,631,-32,-985,-912,-318,-423,-729,661]}"));
        fitnessByEpdBestMove.stop();

        assertEquals(29429L, points);
    }

    @Test
    @Disabled
    public void testEvaluatorImp05_default() {
        List<String> files = List.of(
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\Bratko-Kopec.epd",
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\wac-2018.epd",
                "C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\sbd.epd",
                "C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\Nolot.epd",
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

        FitnessByEpdBestMove fitnessByEpdBestMove = new FitnessByEpdBestMove(files, 2);

        fitnessByEpdBestMove.start();
        long points = fitnessByEpdBestMove.fitness(EvaluatorImp05::new);
        fitnessByEpdBestMove.stop();

        assertEquals(101160, points);
    }

    @Test
    @Disabled
    public void testEvaluatorImp06_default() {
        List<String> files = List.of(
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\Bratko-Kopec.epd",
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\wac-2018.epd",
                "C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\sbd.epd",
                "C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\Nolot.epd",
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

        FitnessByEpdBestMove fitnessByEpdBestMove = new FitnessByEpdBestMove(files, 2);

        fitnessByEpdBestMove.start();
        long points = fitnessByEpdBestMove.fitness(EvaluatorImp06::new);
        fitnessByEpdBestMove.stop();

        assertEquals(100133L, points);
    }

}

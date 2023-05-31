package net.chesstango.evaluation.tuning.fitnessfunctions;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.BinaryUtils;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.Transposition;
import net.chesstango.search.smart.alphabeta.listeners.SetMoveEvaluations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class FitnessBySearchTest {
    private static final int DEPTH = 1;
    private FitnessBySearch fitnessFn;
    private Map<Long, Transposition> maxMap;
    private Map<Long, Transposition> minMap;
    private SetMoveEvaluations setMoveEvaluations;

    @BeforeEach
    public void setup() {
        fitnessFn = new FitnessBySearch(null);
        setMoveEvaluations = new SetMoveEvaluations();
        maxMap = new HashMap<>();
        minMap = new HashMap<>();
    }

    /**
     * Actual best move = Best move found
     */
    @Test
    public void test_white01() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        final Move bestMoveFoundBySearch = game.getMove(Square.c2, Square.c3);
        final int bestEvaluationFoundBySearch = 100;
        final Move actualBestMove = bestMoveFoundBySearch;

        int i = 1;
        for (Move move : game.getPossibleMoves()) {
            game.executeMove(move);

            if (move.equals(bestMoveFoundBySearch)) {
                saveEntry(game, move, bestEvaluationFoundBySearch);
            } else {
                saveEntry(game, move, i);
            }

            game.undoMove();
            i++;
        }

        Collection<SearchMoveResult.MoveEvaluation> moveEvaluationList = getMoveEvaluationList(game, bestEvaluationFoundBySearch, bestMoveFoundBySearch);

        long points = fitnessFn.getPoints(game.getPossibleMoves().size(), actualBestMove, moveEvaluationList);

        assertEquals(game.getPossibleMoves().size(), points);
    }

    /**
     * Actual best move is the second-best move found
     */
    @Test
    public void test_white02() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        final Move bestMoveFoundBySearch = game.getMove(Square.c2, Square.c3);
        final int bestEvaluationFoundBySearch = 100;
        final Move actualBestMove = game.getMove(Square.f2, Square.f4);

        int i = 1;
        for (Move move : game.getPossibleMoves()) {
            game.executeMove(move);

            if (move.equals(bestMoveFoundBySearch)) {
                saveEntry(game, move, bestEvaluationFoundBySearch);
            } else if (move.equals(actualBestMove)) {
                saveEntry(game, move, 90);
            } else {
                saveEntry(game, move, i);
            }

            game.undoMove();
            i++;
        }

        Collection<SearchMoveResult.MoveEvaluation> moveEvaluationList = getMoveEvaluationList(game, bestEvaluationFoundBySearch, bestMoveFoundBySearch);

        long points = fitnessFn.getPoints(game.getPossibleMoves().size(), actualBestMove, moveEvaluationList);

        assertEquals(-1L, points);
    }

    /**
     * Actual best move was not searched
     */
    @Test
    public void test_white03() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        final Move bestMoveFoundBySearch = game.getMove(Square.c2, Square.c3);
        final int bestEvaluationFoundBySearch = 100;
        final Move actualBestMove = game.getMove(Square.f2, Square.f4);

        int i = 1;
        for (Move move : game.getPossibleMoves()) {
            game.executeMove(move);

            if (move.equals(bestMoveFoundBySearch)) {
                saveEntry(game, move, bestEvaluationFoundBySearch);
            } else if (move.equals(actualBestMove)) {
                //saveEntry(game, move, 90);
            } else {
                saveEntry(game, move, i);
            }

            game.undoMove();
            i++;
        }

        Collection<SearchMoveResult.MoveEvaluation> moveEvaluationList = getMoveEvaluationList(game, bestEvaluationFoundBySearch, bestMoveFoundBySearch);

        long points = fitnessFn.getPoints(game.getPossibleMoves().size(), actualBestMove, moveEvaluationList);

        assertEquals(-19L, points);
    }

    /**
     * Actual best move = Best move found
     */
    @Test
    public void test_black01() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN).mirror();

        final Move bestMoveFoundBySearch = game.getMove(Square.c7, Square.c6);
        final int bestEvaluationFoundBySearch = -100;
        final Move actualBestMove = bestMoveFoundBySearch;

        int i = 1;
        for (Move move : game.getPossibleMoves()) {
            game.executeMove(move);

            if (move.equals(bestMoveFoundBySearch)) {
                saveEntry(game, move, bestEvaluationFoundBySearch);
            } else {
                saveEntry(game, move, i);
            }

            game.undoMove();
            i++;
        }

        Collection<SearchMoveResult.MoveEvaluation> moveEvaluationList = getMoveEvaluationList(game, bestEvaluationFoundBySearch, bestMoveFoundBySearch);

        long points = fitnessFn.getPoints(game.getPossibleMoves().size(), actualBestMove, moveEvaluationList);

        assertEquals(game.getPossibleMoves().size(), points);
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

        int i = 1;
        for (Move move : game.getPossibleMoves()) {
            game.executeMove(move);

            if (move.equals(bestMoveFoundBySearch)) {
                saveEntry(game, move, bestEvaluationFoundBySearch);
            } else if (move.equals(actualBestMove)) {
                saveEntry(game, move, -90);
            } else {
                saveEntry(game, move, i);
            }

            game.undoMove();
            i++;
        }

        Collection<SearchMoveResult.MoveEvaluation> moveEvaluationList = getMoveEvaluationList(game, bestEvaluationFoundBySearch, bestMoveFoundBySearch);

        long points = fitnessFn.getPoints(game.getPossibleMoves().size(), actualBestMove, moveEvaluationList);

        assertEquals(-1L, points);
    }

    /**
     * Actual best move was not searched
     */
    @Test
    public void test_black03() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN).mirror();

        final Move bestMoveFoundBySearch = game.getMove(Square.c7, Square.c6);
        final int bestEvaluationFoundBySearch = -100;
        final Move actualBestMove = game.getMove(Square.f7, Square.f5);

        int i = 1;
        for (Move move : game.getPossibleMoves()) {
            game.executeMove(move);

            if (move.equals(bestMoveFoundBySearch)) {
                saveEntry(game, move, bestEvaluationFoundBySearch);
            } else if (move.equals(actualBestMove)) {
                //saveEntry(game, move, 90);
            } else {
                saveEntry(game, move, i);
            }

            game.undoMove();
            i++;
        }

        Collection<SearchMoveResult.MoveEvaluation> moveEvaluationList = getMoveEvaluationList(game, bestEvaluationFoundBySearch, bestMoveFoundBySearch);

        long points = fitnessFn.getPoints(game.getPossibleMoves().size(), actualBestMove, moveEvaluationList);

        assertEquals(-19L, points);
    }

    private Collection<SearchMoveResult.MoveEvaluation> getMoveEvaluationList(Game game, int bestEvaluationFoundBySearch, Move bestMoveFoundBySearch) {
        SearchContext searchContext = new SearchContext(DEPTH);
        searchContext.setMaxMap(maxMap);
        searchContext.setMinMap(minMap);
        setMoveEvaluations.initSearch(game, DEPTH);
        setMoveEvaluations.init(searchContext);
        SearchMoveResult searchResult = new SearchMoveResult(DEPTH, bestEvaluationFoundBySearch, bestMoveFoundBySearch, null);
        setMoveEvaluations.close(searchResult);
        setMoveEvaluations.closeSearch(searchResult);
        return searchResult.getMoveEvaluations();
    }

    private Transposition saveEntry(Game game, Move move, int value) {
        Transposition entry = new Transposition();

        entry.bestMoveAndValue = BinaryUtils.encodedMoveAndValue(move.binaryEncoding(), value);
        entry.value = BinaryUtils.decodeValue(entry.bestMoveAndValue);
        entry.searchDepth = 0;

        long hash = game.getChessPosition().getPositionHash();

        if (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) {
            maxMap.put(hash, entry);
        } else {
            minMap.put(hash, entry);
        }

        return entry;
    }

}

package net.chesstango.evaluation.tuning.fitnessfunctions;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class FitnessBySearchTest {
    private FitnessBySearch fitnessFn;
    private Map<Long, SearchContext.TableEntry> maxMap;
    private Map<Long, SearchContext.TableEntry> minMap;

    @BeforeEach
    public void setup() {
        fitnessFn = new FitnessBySearch(null);
        maxMap = new HashMap<>();
        minMap = new HashMap<>();
    }

    /**
     * Actual best move = Best move found
     */
    @Test
    public void test_white01() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        Move bestMoveFound = game.getMove(Square.c2, Square.c3);
        Move actualBestMove = bestMoveFound;

        int i = 1;
        for (Move move : game.getPossibleMoves()) {
            game.executeMove(move);

            if (move.equals(bestMoveFound)) {
                saveEntry(game, move, 100);
            } else {
                saveEntry(game, move, i);
            }

            game.undoMove();
            i++;
        }

        SearchMoveResult searchResult = new SearchMoveResult(1, 100, bestMoveFound, null);
        searchResult.storeMoveEvaluations(game, maxMap, minMap);

        long points = fitnessFn.getPoints(actualBestMove, searchResult.getMoveEvaluationList());

        assertEquals(0, points);
    }

    /**
     * Actual best move is the second-best move found
     */
    @Test
    public void test_white02() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        Move bestMoveFound = game.getMove(Square.c2, Square.c3);
        Move actualBestMove = game.getMove(Square.f2, Square.f4);

        int i = 1;
        for (Move move : game.getPossibleMoves()) {
            game.executeMove(move);

            if (move.equals(bestMoveFound)) {
                saveEntry(game, move, 100);
            } else if (move.equals(actualBestMove)) {
                saveEntry(game, move, 90);
            } else {
                saveEntry(game, move, i);
            }

            game.undoMove();
            i++;
        }

        SearchMoveResult searchResult = new SearchMoveResult(1, 100, bestMoveFound, null);
        searchResult.storeMoveEvaluations(game, maxMap, minMap);

        long points = fitnessFn.getPoints(actualBestMove, searchResult.getMoveEvaluationList());

        assertEquals(-1L, points);
    }

    /**
     * Actual best move was not searched
     */
    @Test
    public void test_white03() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        Move bestMoveFound = game.getMove(Square.c2, Square.c3);
        Move actualBestMove = game.getMove(Square.f2, Square.f4);

        int i = 1;
        for (Move move : game.getPossibleMoves()) {
            game.executeMove(move);

            if (move.equals(bestMoveFound)) {
                saveEntry(game, move, 100);
            } else if (move.equals(actualBestMove)) {
                //saveEntry(game, move, 90);
            } else {
                saveEntry(game, move, i);
            }

            game.undoMove();
            i++;
        }

        SearchMoveResult searchResult = new SearchMoveResult(1, 100, bestMoveFound, null);
        searchResult.storeMoveEvaluations(game, maxMap, minMap);

        long points = fitnessFn.getPoints(actualBestMove, searchResult.getMoveEvaluationList());

        assertEquals(-19L, points);
    }


    private SearchContext.TableEntry saveEntry(Game game, Move move, int value) {
        SearchContext.TableEntry entry = new SearchContext.TableEntry();

        entry.bestMoveAndValue = encodedMoveAndValue(move.binaryEncoding(), value);
        entry.value = (int) (0b00000000_00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L & entry.bestMoveAndValue);

        long hash = game.getChessPosition().getPositionHash();

        if (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) {
            maxMap.put(hash, entry);
        } else {
            minMap.put(hash, entry);
        }

        return entry;
    }

    private static long encodedMoveAndValue(short move, int value) {
        long encodedMoveLng = ((long) move) << 32;

        long encodedValueLng = 0b00000000_00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L & value;

        return encodedValueLng | encodedMoveLng;
    }
}

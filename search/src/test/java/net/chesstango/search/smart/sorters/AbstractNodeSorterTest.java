package net.chesstango.search.smart.sorters;

import net.chesstango.board.Game;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.builders.alphabeta.MoveSorterBuilder;
import net.chesstango.search.smart.alphabeta.AlphaBetaHelper;
import net.chesstango.search.smart.features.killermoves.KillerMovesTable;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.features.transposition.TTableMap;
import net.chesstango.search.smart.features.transposition.TTable;
import net.chesstango.search.smart.features.transposition.TranspositionBound;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public abstract class AbstractNodeSorterTest {
    protected final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    protected SearchListenerMediator searchListenerMediator;
    protected MoveSorterBuilder moveSorterBuilder;
    protected TTable maxMap;
    protected TTable minMap;
    protected TTable qMaxMap;
    protected TTable qMinMap;
    protected KillerMovesTable killerMovesTable;
    protected Game game;

    @BeforeEach
    public void setup() {
        game = createGame();

        searchListenerMediator = new SearchListenerMediator();

        maxMap = new TTableMap();
        minMap = new TTableMap();
        qMaxMap = new TTableMap();
        qMinMap = new TTableMap();

        killerMovesTable = new KillerMovesTable();

        moveSorterBuilder = new MoveSorterBuilder()
                .withSmartListenerMediator(searchListenerMediator);
    }

    protected abstract Game createGame();


    protected List<String> convertMoveListToStringList(Iterable<Move> moves) {
        List<String> sortedMovesStr = new ArrayList<>();
        for (Move move : moves) {
            sortedMovesStr.add(simpleMoveEncoder.encode(move));
        }
        return sortedMovesStr;
    }

    protected void saveEntry(TTable table, long hash, TranspositionBound bound, int draft, long moveAndValue) {
        short move = AlphaBetaHelper.decodeMove(moveAndValue);
        int value = AlphaBetaHelper.decodeValue(moveAndValue);

        TranspositionEntry entry = new TranspositionEntry()
                .setHash(hash)
                .setBound(bound)
                .setDraft(draft)
                .setMove(move)
                .setValue(value);

        table.save(entry);
    }


    protected Move createMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal, boolean quiet) {
        return new Move() {
            @Override
            public PiecePositioned getFrom() {
                return from;
            }

            @Override
            public PiecePositioned getTo() {
                return to;
            }

            @Override
            public void executeMove() {
                throw new RuntimeException("Not meant for execution");
            }

            @Override
            public void undoMove() {
                throw new RuntimeException("Not meant for execution");
            }

            @Override
            public Cardinal getMoveDirection() {
                return cardinal;
            }

            @Override
            public boolean isQuiet() {
                return quiet;
            }

            @Override
            public long getZobristHash() {
                throw new RuntimeException("Not meant for execution");
            }
        };
    }
}

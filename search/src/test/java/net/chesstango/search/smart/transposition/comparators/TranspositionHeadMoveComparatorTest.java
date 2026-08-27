package net.chesstango.search.smart.transposition.comparators;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.smart.debug.model.DebugCacheRead;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TTableMap;
import net.chesstango.search.smart.transposition.TranspositionEntry;
import net.chesstango.search.sorters.MoveComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static net.chesstango.search.Bound.EXACT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class TranspositionHeadMoveComparatorTest {

    private TranspositionHeadMoveComparator headMoveComparator;

    private TTable tTable;

    @Mock
    private MoveComparator next;

    @BeforeEach
    public void setup() {
        tTable = new TTableMap();
        headMoveComparator = new TranspositionHeadMoveComparator();
        headMoveComparator.setTTable(tTable);
        headMoveComparator.setNext(next);
    }


    @Test
    public void testBeforeSortWithTranspositionEntry() {
        /**
         * Settup
         */
        Game game = Game.from(FEN.START_POSITION);

        long hash = game.getPosition().getZobristHash();
        Move move = game.getMove(Square.c2, Square.c3);

        TranspositionEntry entry = new TranspositionEntry()
                .setHash(hash)
                .setBound(EXACT)
                .setDraft((byte) 1)
                .setMove(move.binaryEncoding())
                .setValue(1);

        tTable.save(entry);
        headMoveComparator.setGame(game);


        // Method invocation
        headMoveComparator.beforeSort(0);

        // Assert
        assertEquals(move.binaryEncoding(), headMoveComparator.getBestMoveEncoded());
    }

    @Test
    public void testBeforeSortWithoutTranspositionEntry() {
        /**
         * Settup
         */
        Game game = Game.from(FEN.START_POSITION);

        headMoveComparator.setGame(game);

        // Method invocation
        headMoveComparator.beforeSort(0);

        // Assert
        assertEquals(0, headMoveComparator.getBestMoveEncoded());
    }


    @Test
    public void testCompare01() {
        /**
         * Settup
         */
        Game game = Game.from(FEN.START_POSITION);

        Move move = game.getMove(Square.c2, Square.c3);

        headMoveComparator.setBestMoveEncoded(move.binaryEncoding());

        when(next.compare(any(Move.class), any(Move.class))).thenReturn(0);

        /**
         * Assertions
         */
        game.getPossibleMoves().forEach(otherMove -> {
            if (move.equals(otherMove)) {
                assertEquals(0, headMoveComparator.compare(move, otherMove));
                assertEquals(0, headMoveComparator.compare(otherMove, move));
            } else {
                assertTrue(headMoveComparator.compare(move, otherMove) > 0);
                assertTrue(headMoveComparator.compare(otherMove, move) < 0);
            }
        });
    }
}

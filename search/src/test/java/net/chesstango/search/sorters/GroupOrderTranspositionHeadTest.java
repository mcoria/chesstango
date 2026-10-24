package net.chesstango.search.sorters;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.alphabeta.transposition.TTable;
import net.chesstango.search.alphabeta.transposition.TranspositionEntry;
import net.chesstango.search.alphabeta.transposition.groupsorters.TranspositionHeadMoveGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class GroupOrderTranspositionHeadTest {

    private TranspositionHeadMoveGroup transpositionHeadMoveGroup;

    private GroupOrder groupOrder;

    @Mock
    private TTable tTable;

    @Mock
    private Move move1;

    @Mock
    private Move move2;

    @Mock
    private Move move3;

    @Mock
    private Move move4;

    @Mock
    private Move move5;

    @BeforeEach
    public void setUp() {
        transpositionHeadMoveGroup = new TranspositionHeadMoveGroup();
        transpositionHeadMoveGroup.setTTable(tTable);

        groupOrder = GroupOrder.createTranspositionHeadMoveGroup(transpositionHeadMoveGroup);
    }


    @Test
    public void testSortWhenBestMoveIsZero() {
        transpositionHeadMoveGroup.setBestMoveEncoded((short) 0);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3));
        int result = groupOrder.sort(moves, 0);

        assertEquals(0, result);
        assertEquals(List.of(move1, move2, move3), moves);
    }

    @Test
    public void testSortWhenBestMoveIsAlreadyAtFrom() {
        when(move1.binaryEncoding()).thenReturn((short) 0x01);
        transpositionHeadMoveGroup.setBestMoveEncoded((short) 0x01);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3));
        int result = groupOrder.sort(moves, 0);

        assertEquals(1, result);
        assertEquals(List.of(move1, move2, move3), moves);
    }

    @Test
    public void testSortWhenBestMoveInMiddle() {
        when(move1.binaryEncoding()).thenReturn((short) 0x01);
        when(move2.binaryEncoding()).thenReturn((short) 0x02);
        transpositionHeadMoveGroup.setBestMoveEncoded((short) 0x02);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3));
        int result = groupOrder.sort(moves, 0);

        assertEquals(1, result);
        assertEquals(List.of(move2, move1, move3), moves);
    }

    @Test
    public void testSortWhenBestMoveAtEnd() {
        when(move1.binaryEncoding()).thenReturn((short) 0x01);
        when(move2.binaryEncoding()).thenReturn((short) 0x02);
        when(move3.binaryEncoding()).thenReturn((short) 0x03);
        transpositionHeadMoveGroup.setBestMoveEncoded((short) 0x03);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3));
        int result = groupOrder.sort(moves, 0);

        assertEquals(1, result);
        assertEquals(List.of(move3, move2, move1), moves);
    }

    @Test
    public void testSortWhenBestMoveNotFoundInMoves() {
        when(move1.binaryEncoding()).thenReturn((short) 0x01);
        when(move2.binaryEncoding()).thenReturn((short) 0x02);
        when(move3.binaryEncoding()).thenReturn((short) 0x03);
        transpositionHeadMoveGroup.setBestMoveEncoded((short) 0x99);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3));
        int result = groupOrder.sort(moves, 0);

        assertEquals(0, result);
        assertEquals(List.of(move1, move2, move3), moves);
    }

    @Test
    public void testSortSubRangeWhenBestMoveInSubRange() {
        when(move2.binaryEncoding()).thenReturn((short) 0x02);
        when(move3.binaryEncoding()).thenReturn((short) 0x03);
        when(move4.binaryEncoding()).thenReturn((short) 0x04);
        transpositionHeadMoveGroup.setBestMoveEncoded((short) 0x04);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3, move4, move5));
        int result = groupOrder.sort(moves, 1);

        assertEquals(2, result);
        assertEquals(List.of(move1, move4, move3, move2, move5), moves);
    }

    @Test
    public void testSortSubRangeWhenBestMoveOutsideBeforeFrom() {
        when(move2.binaryEncoding()).thenReturn((short) 0x02);
        when(move3.binaryEncoding()).thenReturn((short) 0x03);
        transpositionHeadMoveGroup.setBestMoveEncoded((short) 0x01);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3, move4));
        int result = groupOrder.sort(moves, 1);

        assertEquals(1, result);
        assertEquals(List.of(move1, move2, move3, move4), moves);
    }

    @Test
    public void testSortSubRangeWhenBestMoveOutsideAfterTo() {
        when(move2.binaryEncoding()).thenReturn((short) 0x02);
        when(move3.binaryEncoding()).thenReturn((short) 0x03);
        transpositionHeadMoveGroup.setBestMoveEncoded((short) 0x04);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3, move4));
        int result = groupOrder.sort(moves, 1);

        assertEquals(1, result);
        assertEquals(List.of(move1, move2, move3, move4), moves);
    }

    @Test
    public void testIntegrationWithGameAndPossibleMoves() {
        Game game = Game.from(FEN.START_POSITION);
        long hash = game.getPosition().getZobristHash();
        Move expectedHeadMove = game.getMove(Square.e2, Square.e4);

        transpositionHeadMoveGroup.setGame(game);

        doAnswer(invocation -> {
            TranspositionEntry entry = invocation.getArgument(1);
            entry.setHash(hash);
            entry.setMove(expectedHeadMove.binaryEncoding());
            return true;
        }).when(tTable).load(eq(hash), any(TranspositionEntry.class));

        transpositionHeadMoveGroup.beforeSort(0);

        List<Move> moves = new ArrayList<>();
        game.getPossibleMoves().forEach(moves::add);

        int result = groupOrder.sort(moves, 0);

        assertEquals(1, result);
        assertEquals(expectedHeadMove, moves.getFirst());
    }
}

package net.chesstango.search.smart.transposition;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.smart.debug.DebugNodeTracker;
import net.chesstango.search.smart.debug.model.DebugNode;
import net.chesstango.search.smart.debug.model.DebugReadTT;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TTableComparatorHeadDebugTest {

    @InjectMocks
    private TTableComparatorHeadDebug tTableComparatorHeadDebug;

    @Mock
    private TTable tTable;

    @Mock
    private DebugNodeTracker debugNodeTracker;

    @Mock
    private DebugNode debugNode;

    /**
     * Tests the trackComparatorsTranspositionReads() method of MoveSorterDebug class.
     * Ensures that the transposition reads are populated correctly.
     */
    @Test
    void testTrackComparatorsTranspositionReads_PV() {
        Game game = Game.from(FEN.START_POSITION);
        Move pvMove = game.getMove(Square.e2, Square.e4);

        // Arrange
        tTableComparatorHeadDebug.setGame(game);

        TranspositionEntry pvTranspositionEntry = new TranspositionEntry()
                .setHash(game.getPosition().getZobristHash())
                .setMove(pvMove.binaryEncoding());

        List<DebugReadTT> sorterHeadReads = new ArrayList<>();
        when(debugNode.getSorterHeadReads()).thenReturn(sorterHeadReads);
        when(debugNodeTracker.getCurrentNode()).thenReturn(debugNode);

        // Act
        tTableComparatorHeadDebug.trackReadTranspositionEntry(game.getPosition().getZobristHash(), pvTranspositionEntry);

        // Assert
        assertEquals(1, sorterHeadReads.size());
        assertEquals("e2e4", sorterHeadReads.getFirst().getMove());
        assertEquals(pvTranspositionEntry, sorterHeadReads.getFirst().getEntry());
    }
}

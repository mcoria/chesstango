package net.chesstango.search.smart.sorters;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.debug.SearchTracker;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.debug.model.DebugOperationEval;
import net.chesstango.search.smart.alphabeta.debug.model.DebugOperationTT;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoveSorterDebugTest {

    @InjectMocks
    private MoveSorterDebug moveSorterDebug;

    @Mock
    private MoveSorter nextMock;

    @Mock
    private SearchTracker mockSearchTracker;

    @Mock
    private DebugNode mockDebugNode;

    @Mock
    private Game mockGame;

    @Mock
    private Move mockMove1;

    @Mock
    private Move mockMove2;

    /**
     * Tests the accept() method of the MoveSorterDebug class.
     * Ensures that the accept method properly interacts with a Visitor instance.
     */
    @Test
    void testAccept() {
        // Arrange
        Visitor mockVisitor = mock(Visitor.class);

        // Act
        moveSorterDebug.accept(mockVisitor);

        // Assert
        verify(mockVisitor).visit(moveSorterDebug);
    }

    /**
     * Tests the getOrderedMoves() method of the MoveSorterDebug class.
     * Ensures that this method delegates sorting to the next MoveSorter and tracks debugging operations.
     */
    @Test
    @Disabled
    void testGetOrderedMoves() {
        // Arrange
        Iterable<Move> mockSortedMoves = Arrays.asList(mockMove1, mockMove2);
        when(nextMock.getOrderedMoves(1)).thenReturn(mockSortedMoves);
        when(mockSearchTracker.getCurrentNode()).thenReturn(mockDebugNode);

        // Act
        Iterable<Move> resultMoves = moveSorterDebug.getOrderedMoves(1);

        // Assert
        assertEquals(mockSortedMoves, resultMoves);

        verify(mockDebugNode).sortingON();
        verify(nextMock).getOrderedMoves(1);
        verify(mockDebugNode).setSortedPly(1);
        verify(mockDebugNode).setSortedMoves(List.of("")); // Placeholder for move string via SimpleMoveEncoder
        verify(mockDebugNode).sortingOFF();
    }

    /**
     * Tests the trackComparatorsEvalCacheReads() method of MoveSorterDebug class.
     * Ensures that the evaluation cache reads are populated properly.
     */
    @Test
    @Disabled
    void testTrackComparatorsEvalCacheReads() {
        // Arrange
        when(mockSearchTracker.getCurrentNode()).thenReturn(mockDebugNode);
        //when(mockGame.getPossibleMoves()).thenReturn(List.of(mockMove1));
        when(mockMove1.getZobristHash()).thenReturn(123L);

        DebugOperationEval mockDebugOperation = mock(DebugOperationEval.class);
        when(mockDebugOperation.getHashRequested()).thenReturn(123L);

        List<DebugOperationEval> evalCacheReads = Collections.singletonList(mockDebugOperation);
        when(mockDebugNode.getEvalCacheReads()).thenReturn(evalCacheReads);

        // Act
        moveSorterDebug.trackComparatorsEvalCacheReads();

        // Assert
        verify(mockDebugOperation).setMove("");
    }

    /**
     * Tests the trackComparatorsTranspositionReads() method of MoveSorterDebug class.
     * Ensures that the transposition reads are populated correctly.
     */
    @Test
    @Disabled
    void testTrackComparatorsTranspositionReads() {
        // Arrange
        when(mockSearchTracker.getCurrentNode()).thenReturn(mockDebugNode);
        //when(mockGame.getPossibleMoves()).thenReturn(List.of(mockMove1));
        when(mockGame.getPosition().getZobristHash()).thenReturn(456L);
        when(mockMove1.getZobristHash()).thenReturn(789L);

        DebugOperationTT mockDebugOperationTT = mock(DebugOperationTT.class);
        when(mockDebugOperationTT.getEntry()).thenReturn(null);

        List<DebugOperationTT> sorterReads = Collections.singletonList(mockDebugOperationTT);
        when(mockDebugNode.getSorterReads()).thenReturn(sorterReads);

        // Act
        moveSorterDebug.trackComparatorsTranspositionReads();

        // Assert
        verify(mockDebugOperationTT).setMove("UNKNOWN");
    }
}
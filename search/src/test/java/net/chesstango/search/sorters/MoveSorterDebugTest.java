package net.chesstango.search.sorters;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.debug.DebugNodeTracker;
import net.chesstango.search.smart.debug.model.DebugNode;
import net.chesstango.search.smart.transposition.TranspositionEntry;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MoveSorterDebugTest {

    @InjectMocks
    private MoveSorterDebug moveSorterDebug;

    @Mock
    private MoveSorter nextMock;

    @Mock
    private DebugNodeTracker debugNodeTracker;

    @Mock
    private DebugNode debugNode;


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
    void testGetOrderedMoves() {
        Game game = Game.from(FEN.START_POSITION);
        List<Move> theMoves = new LinkedList<>();
        for (Move move : game.getPossibleMoves()) {
            theMoves.add(move);
        }
        theMoves.sort(Comparator.comparing(Move::coordinateEncoding));

        // Arrange
        moveSorterDebug.setGame(game);
        when(nextMock.getOrderedMoves(1)).thenReturn(theMoves);
        when(debugNodeTracker.getCurrentNode()).thenReturn(debugNode);

        // Act
        Iterable<Move> resultMoves = moveSorterDebug.getOrderedMoves(1);

        // Assert
        assertEquals(theMoves, resultMoves);

        verify(nextMock).getOrderedMoves(1);

        verify(debugNode).setSortedMoves(List.of("a2a3", "a2a4", "b1a3", "b1c3", "b2b3", "b2b4", "c2c3", "c2c4", "d2d3", "d2d4", "e2e3", "e2e4", "f2f3", "f2f4", "g1f3", "g1h3", "g2g3", "g2g4", "h2h3", "h2h4")); // Placeholder for move string via SimpleMoveEncoder
    }
}
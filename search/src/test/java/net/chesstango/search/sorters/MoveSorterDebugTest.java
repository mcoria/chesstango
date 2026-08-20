package net.chesstango.search.sorters;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.debug.DebugNodeTracker;
import net.chesstango.search.smart.debug.model.DebugNode;
import net.chesstango.search.smart.debug.model.DebugOperationEval;
import net.chesstango.search.smart.debug.model.DebugOperationTT;
import net.chesstango.search.smart.transposition.TranspositionEntry;
import net.chesstango.search.sorters.MoveSorter;
import net.chesstango.search.sorters.MoveSorterDebug;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
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
    private DebugNodeTracker mockDebugNodeTracker;

    @Mock
    private DebugNode mockDebugNode;


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
        when(mockDebugNodeTracker.getCurrentNode()).thenReturn(mockDebugNode);

        // Act
        Iterable<Move> resultMoves = moveSorterDebug.getOrderedMoves(1);

        // Assert
        assertEquals(theMoves, resultMoves);

        verify(nextMock).getOrderedMoves(1);

        verify(mockDebugNode).setSortedMoves(List.of("a2a3", "a2a4", "b1a3", "b1c3", "b2b3", "b2b4", "c2c3", "c2c4", "d2d3", "d2d4", "e2e3", "e2e4", "f2f3", "f2f4", "g1f3", "g1h3", "g2g3", "g2g4", "h2h3", "h2h4")); // Placeholder for move string via SimpleMoveEncoder
    }

    /**
     * Tests the trackComparatorsEvalCacheReads() method of MoveSorterDebug class.
     * Ensures that the evaluation cache reads are populated properly.
     */
    @Test
    void testTrackComparatorsEvalCacheReads() {
        Game game = Game.from(FEN.START_POSITION);
        Move move = game.getMove(Square.e2, Square.e4);

        // Arrange
        moveSorterDebug.setGame(game);

        DebugOperationEval mockDebugOperation = new DebugOperationEval()
                .setHashRequested(move.getZobristHash());

        List<DebugOperationEval> evalCacheReads = Collections.singletonList(mockDebugOperation);
        when(mockDebugNode.getEvalCacheReads()).thenReturn(evalCacheReads);

        // Act
        moveSorterDebug.trackComparatorsEvalCacheReads(mockDebugNode);

        // Assert
        assertEquals("e2e4", mockDebugOperation.getMove());
    }

    /**
     * Tests the trackComparatorsTranspositionReads() method of MoveSorterDebug class.
     * Ensures that the transposition reads are populated correctly.
     */
    @Test
    void testTrackComparatorsTranspositionReads_PV() {
        Game game = Game.from(FEN.START_POSITION);
        Move pvMove = game.getMove(Square.e2, Square.e4);

        // Arrange
        moveSorterDebug.setGame(game);

        TranspositionEntry pvTranspositionEntry = new TranspositionEntry()
                .setHash(game.getPosition().getZobristHash())
                .setMove(pvMove.binaryEncoding());

        DebugOperationTT mockDebugOperationTT = new DebugOperationTT()
                .setEntry(pvTranspositionEntry);

        List<DebugOperationTT> sorterReads = Collections.singletonList(mockDebugOperationTT);
        when(mockDebugNode.getSorterReads()).thenReturn(sorterReads);

        // Act
        moveSorterDebug.trackComparatorsTranspositionReads(mockDebugNode);

        // Assert
        assertEquals("e2e4", mockDebugOperationTT.getSortingMove());
    }

    @Test
    void testTrackComparatorsTranspositionReads_Tail() {
        Game game = Game.from(FEN.START_POSITION);
        Move move = game.getMove(Square.e2, Square.e4);

        // Arrange
        moveSorterDebug.setGame(game);

        TranspositionEntry transpositionEntry =  new TranspositionEntry()
                .setHash(move.getZobristHash());

        DebugOperationTT mockDebugOperationTT = new DebugOperationTT()
                .setEntry(transpositionEntry);

        List<DebugOperationTT> sorterReads = Collections.singletonList(mockDebugOperationTT);
        when(mockDebugNode.getSorterReads()).thenReturn(sorterReads);

        // Act
        moveSorterDebug.trackComparatorsTranspositionReads(mockDebugNode);

        // Assert
        assertEquals("e2e4", mockDebugOperationTT.getSortingMove());
    }
}
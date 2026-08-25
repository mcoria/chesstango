package net.chesstango.search.smart.pv.model;

import net.chesstango.board.moves.Move;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the TriangularPVTable class, specifically the propagatePrincipalVariation method.
 * This method propagates the principle variation line from one ply to the previous ply
 * in the triangular table structure.
 * @author Mauricio Coria
 */
public class PVTableTest {
    @Test
    public void testPropagateLine_WithValidNextPlyValues() {
        // Arrange
        PVTable pvTable = new PVTable();
        Move mockMove1 = mock(Move.class);
        Move mockMove2 = mock(Move.class);

        // Simulating the next ply's PV line
        pvTable.extendLine(0, null);
        pvTable.extendLine(1, mockMove1);
        pvTable.extendLine(2, mockMove2);

        // Act
        pvTable.propagatePrincipalVariation(1);

        // Assert
        Move[] resultPV = pvTable.getPV(1);

        assertEquals(2, resultPV.length);
        assertSame(mockMove1, resultPV[0]);
        assertSame(mockMove2, resultPV[1]);
    }

    @Test
    public void testPropagateLine_MultipleMovesPropagation() {
        // Arrange
        PVTable pvTable = new PVTable();

        Move mockMove1 = mock(Move.class);
        Move mockMove2 = mock(Move.class);
        Move mockMove3 = mock(Move.class);
        Move mockMove4 = mock(Move.class);

        // Simulating the next ply's PV line with multiple moves
        pvTable.extendLine(1, mockMove1);
        pvTable.extendLine(2, mockMove2);
        pvTable.extendLine(3, mockMove3);
        pvTable.extendLine(4, mockMove4);

        // Act
        pvTable.propagatePrincipalVariation(3);
        pvTable.propagatePrincipalVariation(2);
        pvTable.propagatePrincipalVariation(1);
        pvTable.propagatePrincipalVariation(0);

        // Assert
        Move[] resultPV = pvTable.getRootPV();

        assertEquals(5, resultPV.length);
        assertNull(resultPV[0]);
        assertSame(mockMove1, resultPV[1]);
        assertSame(mockMove2, resultPV[2]);
        assertSame(mockMove3, resultPV[3]);
        assertSame(mockMove4, resultPV[4]);
    }
}

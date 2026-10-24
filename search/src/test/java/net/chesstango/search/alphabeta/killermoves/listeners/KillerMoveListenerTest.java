package net.chesstango.search.alphabeta.killermoves.listeners;

import net.chesstango.search.alphabeta.killermoves.KillerMovesTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class KillerMoveListenerTest {

    private KillerMoveListener killerMoveListener;

    @Mock
    private KillerMovesTable killerMovesTable;

    @BeforeEach
    public void setUp() {
        killerMoveListener = new KillerMoveListener();
        killerMoveListener.setKillerMoves(killerMovesTable);
    }

    @Test
    public void testBeforeSearch() {
        killerMoveListener.beforeSearch();

        verify(killerMovesTable).reset();
        verifyNoMoreInteractions(killerMovesTable);
    }
}

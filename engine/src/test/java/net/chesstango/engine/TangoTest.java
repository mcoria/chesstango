package net.chesstango.engine;

import net.chesstango.gardel.fen.FEN;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
class TangoTest {
    Tango tango;

    @Mock
    TangoFactorySmart tangoFactorySmart;

    @Mock
    SearchManager searchManager;

    @Mock
    Session session;

    @BeforeEach
    void setup() {
        tango = new Tango(tangoFactorySmart, searchManager);
    }

    @Test
    void testNewSession() {
        // Arrange
        when(searchManager.newSession(any())).thenReturn(session);

        // Act
        Session newSession = tango.newSession(FEN.START_POSITION);

        assertNotNull(session);
        assertEquals(session, newSession);

        // Assert
        verify(searchManager).newSession(FEN.START_POSITION);
    }

    @Test
    void testSetPolyglotFile() {
        // Arrange
        Path polyglotFilePath = Path.of("/path/to/polyglot/book.bin");

        // Act
        tango.setPolyglotFile(polyglotFilePath);

        // Assert
        verify(searchManager).setPolyglotFile(polyglotFilePath);
    }

    @Test
    void testSetSyzygyPath() {
        // Arrange
        Path syzygyPath = Path.of("/path/to/syzygy/tablebase");

        // Act
        tango.setSyzygyPath(syzygyPath);

        // Assert
        verify(searchManager).setSyzygyPath(syzygyPath);
    }
}

package net.chesstango.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        when(searchManager.newSession()).thenReturn(session);

        // Act
        Session newSession = tango.newSession();

        // Assert
        verify(searchManager).newSession();
        assertNotNull(session);
        assertEquals(session, newSession);
    }

    @Test
    void testSetPolyglotFile() {
        // Arrange
        String polyglotFilePath = "/path/to/polyglot/book.bin";

        // Act
        tango.setPolyglotFile(polyglotFilePath);

        // Assert
        verify(searchManager).setPolyglotFile(polyglotFilePath);
    }

    @Test
    void testSetSyzygyPath() {
        // Arrange
        String syzygyPath = "/path/to/syzygy/tablebase";

        // Act
        tango.setSyzygyPath(syzygyPath);

        // Assert
        verify(searchManager).setSyzygyPath(syzygyPath);
    }
}

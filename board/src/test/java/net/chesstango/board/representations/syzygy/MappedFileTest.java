package net.chesstango.board.representations.syzygy;

import org.junit.jupiter.api.Test;

import static net.chesstango.board.representations.syzygy.MappedFile.test_tb;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Mauricio Coria
 */
public class MappedFileTest {

    @Test
    public void test_test_tb() {
        assertFalse(test_tb("C:\\java\\projects\\chess\\chess-utils\\books\\syzygy\\3-4-5", "KQvK", ".rtbm"));
    }
}

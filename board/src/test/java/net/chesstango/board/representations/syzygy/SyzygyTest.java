package net.chesstango.board.representations.syzygy;

import net.chesstango.board.position.Position;
import net.chesstango.board.representations.fen.FEN;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mauricio Coria
 */
public class SyzygyTest {

    public static final String PATH = "C:\\java\\projects\\chess\\chess-utils\\books\\syzygy\\3-4-5";

    private Syzygy syzygy;

    @BeforeEach
    public void setUp() throws Exception {
        syzygy = new Syzygy();
    }

    @Test
    public void test_tb_init() {
        syzygy.tb_init(PATH);

        assertEquals(650, syzygy.pieceEntry.length);
        assertEquals(861, syzygy.pawnEntry.length);
        assertEquals(4096, syzygy.tbHash.length);

        assertEquals(3, syzygy.TB_LARGEST);
        assertEquals(3, syzygy.TB_MaxCardinality);
        assertEquals(0, syzygy.TB_MaxCardinalityDTM);
        assertEquals(4, syzygy.tbNumPiece);
        assertEquals(1, syzygy.tbNumPawn);
        assertEquals(5, syzygy.numWdl);
        assertEquals(0, syzygy.numDtm);
        assertEquals(5, syzygy.numDtz);
    }

    @Test
    public void test_tb_probe_root_KQvK_white() {
        syzygy.setPath(PATH);
        syzygy.init_tb("KQvK");

        FEN fen = FEN.of("7k/8/7K/7Q/8/8/8/8 w - - 0 1");
        Position chessPosition = fen.toChessPosition();
        BitPosition bitPosition = BitPosition.from(chessPosition);

        int[] results = new int[TB_MAX_MOVES];

        int res = syzygy.tb_probe_root(bitPosition, results);

        assertNotEquals(TB_RESULT_FAILED, res);

        assertEquals(TB_WIN, TB_GET_WDL(res));
        assertEquals(1, TB_GET_DTZ(res));

        assertEquals(15, count(results, TB_WIN));
        assertEquals(0, count(results, TB_CURSED_WIN));
        assertEquals(5, count(results, TB_DRAW));
        assertEquals(0, count(results, TB_BLESSED_LOSS));
        assertEquals(0, count(results, TB_LOSS));
    }

    @Test
    public void test_tb_probe_root_KQvK_black() {
        syzygy.setPath(PATH);
        syzygy.init_tb("KQvK");

        FEN fen = FEN.of("7k/8/7K/7Q/8/8/8/8 b - - 0 1");
        Position chessPosition = fen.toChessPosition();
        BitPosition bitPosition = BitPosition.from(chessPosition);

        int[] results = new int[TB_MAX_MOVES];

        int res = syzygy.tb_probe_root(bitPosition, results);

        assertNotEquals(TB_RESULT_FAILED, res);

        assertEquals(TB_LOSS, TB_GET_WDL(res));
        assertEquals(2, TB_GET_DTZ(res));

        assertEquals(0, count(results, TB_WIN));
        assertEquals(0, count(results, TB_CURSED_WIN));
        assertEquals(0, count(results, TB_DRAW));
        assertEquals(0, count(results, TB_BLESSED_LOSS));
        assertEquals(1, count(results, TB_LOSS));
    }

    @Test
    public void test_tb_probe_root_KQvKR_white() {
        syzygy.setPath(PATH);
        syzygy.init_tb("KQvKR");
        syzygy.init_tb("KQvK");
        syzygy.init_tb("KRvK");

        FEN fen = FEN.of("7k/r7/7K/7Q/8/8/8/8 w - - 0 1");
        Position chessPosition = fen.toChessPosition();
        BitPosition bitPosition = BitPosition.from(chessPosition);

        int[] results = new int[TB_MAX_MOVES];

        int res = syzygy.tb_probe_root(bitPosition, results);

        assertNotEquals(TB_RESULT_FAILED, res);

        assertEquals(TB_WIN, TB_GET_WDL(res));
        assertEquals(1, TB_GET_DTZ(res));

        assertEquals(12, count(results, TB_WIN));
        assertEquals(0, count(results, TB_CURSED_WIN));
        assertEquals(3, count(results, TB_DRAW));
        assertEquals(0, count(results, TB_BLESSED_LOSS));
        assertEquals(5, count(results, TB_LOSS));
    }

    @Test
    public void test_tb_probe_root_KQvKR_black() {
        syzygy.setPath(PATH);
        syzygy.init_tb("KQvKR");
        syzygy.init_tb("KQvK");
        syzygy.init_tb("KRvK");

        FEN fen = FEN.of("7k/r7/7K/7Q/8/8/8/8 b - - 0 1");
        Position chessPosition = fen.toChessPosition();
        BitPosition bitPosition = BitPosition.from(chessPosition);

        int[] results = new int[TB_MAX_MOVES];

        int res = syzygy.tb_probe_root(bitPosition, results);

        assertNotEquals(TB_RESULT_FAILED, res);

        assertEquals(TB_DRAW, TB_GET_WDL(res));
        assertEquals(0, TB_GET_DTZ(res));

        assertEquals(0, count(results, TB_WIN));
        assertEquals(0, count(results, TB_CURSED_WIN));
        assertEquals(1, count(results, TB_DRAW));
        assertEquals(0, count(results, TB_BLESSED_LOSS));
        assertEquals(14, count(results, TB_LOSS));
    }

    @Test
    public void test_tb_probe_root_KQvKQ_white() {
        syzygy.setPath(PATH);
        syzygy.init_tb("KQvKQ");
        syzygy.init_tb("KQvK");

        FEN fen = FEN.of("7k/q7/7K/7Q/8/8/8/8 w - - 0 1");
        Position chessPosition = fen.toChessPosition();
        BitPosition bitPosition = BitPosition.from(chessPosition);

        int[] results = new int[TB_MAX_MOVES];

        int res = syzygy.tb_probe_root(bitPosition, results);

        assertNotEquals(TB_RESULT_FAILED, res);

        assertEquals(TB_WIN, TB_GET_WDL(res));
        assertEquals(1, TB_GET_DTZ(res));

        assertEquals(3, count(results, TB_WIN));
        assertEquals(0, count(results, TB_CURSED_WIN));
        assertEquals(10, count(results, TB_DRAW));
        assertEquals(0, count(results, TB_BLESSED_LOSS));
        assertEquals(7, count(results, TB_LOSS));
    }

    @Test
    public void test_tb_probe_root_KQvKQ_black() {
        syzygy.setPath(PATH);
        syzygy.init_tb("KQvKQ");
        syzygy.init_tb("KQvK");

        FEN fen = FEN.of("7k/q7/7K/7Q/8/8/8/8 b - - 0 1");
        Position chessPosition = fen.toChessPosition();
        BitPosition bitPosition = BitPosition.from(chessPosition);

        int[] results = new int[TB_MAX_MOVES];

        int res = syzygy.tb_probe_root(bitPosition, results);

        assertNotEquals(TB_RESULT_FAILED, res);

        assertEquals(TB_WIN, TB_GET_WDL(res));
        assertEquals(1, TB_GET_DTZ(res));

        assertEquals(1, count(results, TB_WIN));
        assertEquals(0, count(results, TB_CURSED_WIN));
        assertEquals(6, count(results, TB_DRAW));
        assertEquals(0, count(results, TB_BLESSED_LOSS));
        assertEquals(15, count(results, TB_LOSS));
    }

    @Test
    public void test_tb_probe_root_KQNvKQ_white() {
        syzygy.setPath(PATH);
        syzygy.init_tb("KQNvKQ");
        syzygy.init_tb("KQvKQ");
        syzygy.init_tb("KQvKN");
        syzygy.init_tb("KQvK");
        syzygy.init_tb("KNvK");

        FEN fen = FEN.of("7k/q7/7K/7Q/4N3/8/8/8 w - - 0 1");
        Position chessPosition = fen.toChessPosition();
        BitPosition bitPosition = BitPosition.from(chessPosition);

        int[] results = new int[TB_MAX_MOVES];

        int res = syzygy.tb_probe_root(bitPosition, results);

        assertNotEquals(TB_RESULT_FAILED, res);

        assertEquals(TB_WIN, TB_GET_WDL(res));
        assertEquals(1, TB_GET_DTZ(res));

        assertEquals(3, count(results, TB_WIN));
        assertEquals(0, count(results, TB_CURSED_WIN));
        assertEquals(11, count(results, TB_DRAW));
        assertEquals(0, count(results, TB_BLESSED_LOSS));
        assertEquals(14, count(results, TB_LOSS));
    }

    @Test
    public void test_tb_probe_root_KQNvKQ_black() {
        syzygy.setPath(PATH);

        //5 pieces
        syzygy.init_tb("KQNvKQ");

        //4 pieces
        syzygy.init_tb("KQNvK");
        syzygy.init_tb("KQvKQ");
        syzygy.init_tb("KQvKN");

        //3 pieces
        syzygy.init_tb("KQvK");
        syzygy.init_tb("KNvK");

        FEN fen = FEN.of("7k/q7/7K/7Q/4N3/8/8/8 b - - 0 1");
        Position chessPosition = fen.toChessPosition();
        BitPosition bitPosition = BitPosition.from(chessPosition);

        int[] results = new int[TB_MAX_MOVES];

        int res = syzygy.tb_probe_root(bitPosition, results);

        assertNotEquals(TB_RESULT_FAILED, res);

        assertEquals(TB_WIN, TB_GET_WDL(res));
        assertEquals(1, TB_GET_DTZ(res));

        assertEquals(1, count(results, TB_WIN));
        assertEquals(0, count(results, TB_CURSED_WIN));
        assertEquals(4, count(results, TB_DRAW));
        assertEquals(0, count(results, TB_BLESSED_LOSS));
        assertEquals(17, count(results, TB_LOSS));
    }


    static int count(int[] results, int wdl) {
        int count = 0;
        for (int i = 0; i < results.length && results[i] != TB_RESULT_FAILED; i++) {
            if (TB_GET_WDL(results[i]) == wdl) {
                count++;
            }
        }
        return count;
    }
}


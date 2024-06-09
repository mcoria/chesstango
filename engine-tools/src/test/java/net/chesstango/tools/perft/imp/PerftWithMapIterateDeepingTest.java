package net.chesstango.tools.perft.imp;

import net.chesstango.board.Square;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.tools.perft.PerftResult;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Mauricio Coria
 *
 */
public class PerftWithMapIterateDeepingTest {

    @Test
    public void initialPosition(){
        PerftWithMapIterateDeeping<Long> perft = new PerftWithMapIterateDeeping<Long>(PerftWithMapIterateDeeping::getZobristGameId);
        perft.depth = 6;
        long result;

        Long initialGameId = PerftWithMapIterateDeeping.getZobristGameId(FENDecoder.loadGame(FENDecoder.INITIAL_FEN));
        Long initialGameIdCounts[] = perft.transpositionTable.computeIfAbsent(initialGameId, k -> new Long[perft.depth]);

        Long b1a3 = PerftWithMapIterateDeeping.getZobristGameId(FENDecoder.loadGame(FENDecoder.INITIAL_FEN).executeMove(Square.b1, Square.a3));

        perft.maxLevel = 1;
        result = perft.visitChild(FENDecoder.loadGame(FENDecoder.INITIAL_FEN), 1, initialGameIdCounts);
        assertEquals(20, result);
        assertEquals(20, initialGameIdCounts[0].longValue());

        perft.maxLevel = 2;
        result = perft.visitChild(FENDecoder.loadGame(FENDecoder.INITIAL_FEN), 1, initialGameIdCounts);
        assertEquals(400, result);

        // main branch
        assertEquals(20, initialGameIdCounts[0].longValue());
        assertEquals(400, initialGameIdCounts[1].longValue());

        // b1a3 branch
        assertEquals(20, perft.transpositionTable.get(b1a3)[0].longValue());


        perft.maxLevel = 3;
        result = perft.visitChild(FENDecoder.loadGame(FENDecoder.INITIAL_FEN), 1, initialGameIdCounts);
        assertEquals(8902, result);

        // main branch
        assertEquals(20, initialGameIdCounts[0].longValue());
        assertEquals(400, initialGameIdCounts[1].longValue());
        assertEquals(8902, initialGameIdCounts[2].longValue());

        // b1a3 branch
        assertEquals(20, perft.transpositionTable.get(b1a3)[0].longValue());
        assertEquals(400, perft.transpositionTable.get(b1a3)[1].longValue());
        assertNull(perft.transpositionTable.get(b1a3)[2]);

        perft.maxLevel = 4;
        result = perft.visitChild(FENDecoder.loadGame(FENDecoder.INITIAL_FEN), 1, initialGameIdCounts);
        assertEquals(197281, result);

        // main branch
        assertEquals(20, initialGameIdCounts[0].longValue());
        assertEquals(400, initialGameIdCounts[1].longValue());
        assertEquals(8902, initialGameIdCounts[2].longValue());
        assertEquals(197281, initialGameIdCounts[3].longValue());

        // b1a3 branch
        assertEquals(20, perft.transpositionTable.get(b1a3)[0].longValue());
        assertEquals(400, perft.transpositionTable.get(b1a3)[1].longValue());
        assertEquals(8885, perft.transpositionTable.get(b1a3)[2].longValue());
        assertNull(perft.transpositionTable.get(b1a3)[3]);

        perft.maxLevel = 5;
        result = perft.visitChild(FENDecoder.loadGame(FENDecoder.INITIAL_FEN), 1, initialGameIdCounts);
        assertEquals(4865609, result);

        // main branch
        assertEquals(20, initialGameIdCounts[0].longValue());
        assertEquals(400, initialGameIdCounts[1].longValue());
        assertEquals(8902, initialGameIdCounts[2].longValue());
        assertEquals(197281, initialGameIdCounts[3].longValue());
        assertEquals(4865609, initialGameIdCounts[4].longValue());

        // b1a3 branch
        assertEquals(20, perft.transpositionTable.get(b1a3)[0].longValue());
        assertEquals(400, perft.transpositionTable.get(b1a3)[1].longValue());
        assertEquals(8885, perft.transpositionTable.get(b1a3)[2].longValue());
        assertEquals(198572, perft.transpositionTable.get(b1a3)[3].longValue());
        assertNull(perft.transpositionTable.get(b1a3)[4]);

        perft.maxLevel = 6;
        result = perft.visitChild(FENDecoder.loadGame(FENDecoder.INITIAL_FEN), 1, initialGameIdCounts);
        assertEquals(119060324, result);

        // main branch
        assertEquals(20, initialGameIdCounts[0].longValue());
        assertEquals(400, initialGameIdCounts[1].longValue());
        assertEquals(8902, initialGameIdCounts[2].longValue());
        assertEquals(197281, initialGameIdCounts[3].longValue());
        assertEquals(4865609, initialGameIdCounts[4].longValue());
        assertEquals(119060324, initialGameIdCounts[5].longValue());

        // b1a3 branch
        assertEquals(20, perft.transpositionTable.get(b1a3)[0].longValue());
        assertEquals(400, perft.transpositionTable.get(b1a3)[1].longValue());
        assertEquals(8885, perft.transpositionTable.get(b1a3)[2].longValue());
        assertEquals(198572, perft.transpositionTable.get(b1a3)[3].longValue());
        assertEquals(4856835, perft.transpositionTable.get(b1a3)[4].longValue());
        assertNull(perft.transpositionTable.get(b1a3)[5]);
    }

    @Test // 39segs !!!!!
    @Disabled
    public void initialPosition_level7() {
        PerftWithMapIterateDeeping<Long> perft = new PerftWithMapIterateDeeping<Long>(PerftWithMapIterateDeeping::getZobristGameId);
        perft.depth = 7;
        perft.maxLevel = 7;

        Long initialGameId = PerftWithMapIterateDeeping.getZobristGameId(FENDecoder.loadGame(FENDecoder.INITIAL_FEN));
        Long initialGameIdCounts[] = perft.transpositionTable.computeIfAbsent(initialGameId, k -> new Long[perft.depth]);

        long result = perft.visitChild(FENDecoder.loadGame(FENDecoder.INITIAL_FEN), 1, initialGameIdCounts);
        assertEquals(3195901860L, result);
    }


    @Test // 43segs !!!!!
    @Disabled
    public void initialPosition_level7_iterative() {
        PerftWithMapIterateDeeping<Long> peft = new PerftWithMapIterateDeeping<Long>(PerftWithMapIterateDeeping::getZobristGameId);

        PerftResult result = peft.start(FENDecoder.loadGame(FENDecoder.INITIAL_FEN), 7);
        assertEquals(20, result.getMovesCount());
        assertEquals(3195901860L, result.getTotalNodes());
    }


    @Test
    public void test_level7_iterative() {
        PerftWithMapIterateDeeping<Long> peft = new PerftWithMapIterateDeeping<Long>(PerftWithMapIterateDeeping::getZobristGameId);

        PerftResult result = peft.start(FENDecoder.loadGame("4k3/7p/8/8/8/8/P7/4K3 w - - 1 1"), 7);
        assertEquals(7, result.getMovesCount());
        assertEquals(1804144, result.getTotalNodes());
    }
}


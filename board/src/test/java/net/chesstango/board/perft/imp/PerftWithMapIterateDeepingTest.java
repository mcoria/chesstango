package net.chesstango.board.perft.imp;

import net.chesstango.board.Square;
import net.chesstango.board.perft.PerftResult;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PerftWithMapIterateDeepingTest {

    @Test
    public void initialPosition(){
        PerftWithMapIterateDeeping<Long> peft = new PerftWithMapIterateDeeping<Long>(PerftWithMapIterateDeeping::getZobristGameId);
        peft.depth = 6;
        long result;

        Long initialGameId = PerftWithMapIterateDeeping.getZobristGameId(FENDecoder.loadGame(FENDecoder.INITIAL_FEN));
        Long b1a3 = PerftWithMapIterateDeeping.getZobristGameId(FENDecoder.loadGame(FENDecoder.INITIAL_FEN).executeMove(Square.b1, Square.a3));


        peft.maxLevel = 1;
        result = peft.visitChild(FENDecoder.loadGame(FENDecoder.INITIAL_FEN), 1);
        assertEquals(20, result);
        assertEquals(20, peft.transpositionTable.get(initialGameId)[0].longValue());


        peft.maxLevel = 2;
        result = peft.visitChild(FENDecoder.loadGame(FENDecoder.INITIAL_FEN), 1);
        assertEquals(400, result);

        // main branch
        assertEquals(20, peft.transpositionTable.get(initialGameId)[0].longValue());
        assertEquals(400, peft.transpositionTable.get(initialGameId)[1].longValue());

        // b1a3 branch
        assertEquals(20, peft.transpositionTable.get(b1a3)[0].longValue());


        peft.maxLevel = 3;
        result = peft.visitChild(FENDecoder.loadGame(FENDecoder.INITIAL_FEN), 1);
        assertEquals(8902, result);

        // main branch
        assertEquals(20, peft.transpositionTable.get(initialGameId)[0].longValue());
        assertEquals(400, peft.transpositionTable.get(initialGameId)[1].longValue());
        assertEquals(8902, peft.transpositionTable.get(initialGameId)[2].longValue());

        // b1a3 branch
        assertEquals(20, peft.transpositionTable.get(b1a3)[0].longValue());
        assertEquals(400, peft.transpositionTable.get(b1a3)[1].longValue());
        assertNull(peft.transpositionTable.get(b1a3)[2]);

        peft.maxLevel = 4;
        result = peft.visitChild(FENDecoder.loadGame(FENDecoder.INITIAL_FEN), 1);
        assertEquals(197281, result);

        // main branch
        assertEquals(20, peft.transpositionTable.get(initialGameId)[0].longValue());
        assertEquals(400, peft.transpositionTable.get(initialGameId)[1].longValue());
        assertEquals(8902, peft.transpositionTable.get(initialGameId)[2].longValue());
        assertEquals(197281, peft.transpositionTable.get(initialGameId)[3].longValue());

        // b1a3 branch
        assertEquals(20, peft.transpositionTable.get(b1a3)[0].longValue());
        assertEquals(400, peft.transpositionTable.get(b1a3)[1].longValue());
        assertEquals(8885, peft.transpositionTable.get(b1a3)[2].longValue());
        assertNull(peft.transpositionTable.get(b1a3)[3]);

        peft.maxLevel = 5;
        result = peft.visitChild(FENDecoder.loadGame(FENDecoder.INITIAL_FEN), 1);
        assertEquals(4865609, result);

        // main branch
        assertEquals(20, peft.transpositionTable.get(initialGameId)[0].longValue());
        assertEquals(400, peft.transpositionTable.get(initialGameId)[1].longValue());
        assertEquals(8902, peft.transpositionTable.get(initialGameId)[2].longValue());
        assertEquals(197281, peft.transpositionTable.get(initialGameId)[3].longValue());
        assertEquals(4865609, peft.transpositionTable.get(initialGameId)[4].longValue());

        // b1a3 branch
        assertEquals(20, peft.transpositionTable.get(b1a3)[0].longValue());
        assertEquals(400, peft.transpositionTable.get(b1a3)[1].longValue());
        assertEquals(8885, peft.transpositionTable.get(b1a3)[2].longValue());
        assertEquals(198572, peft.transpositionTable.get(b1a3)[3].longValue());
        assertNull(peft.transpositionTable.get(b1a3)[4]);

        peft.maxLevel = 6;
        result = peft.visitChild(FENDecoder.loadGame(FENDecoder.INITIAL_FEN), 1);
        assertEquals(119060324, result);

        // main branch
        assertEquals(20, peft.transpositionTable.get(initialGameId)[0].longValue());
        assertEquals(400, peft.transpositionTable.get(initialGameId)[1].longValue());
        assertEquals(8902, peft.transpositionTable.get(initialGameId)[2].longValue());
        assertEquals(197281, peft.transpositionTable.get(initialGameId)[3].longValue());
        assertEquals(4865609, peft.transpositionTable.get(initialGameId)[4].longValue());
        assertEquals(119060324, peft.transpositionTable.get(initialGameId)[5].longValue());

        // b1a3 branch
        assertEquals(20, peft.transpositionTable.get(b1a3)[0].longValue());
        assertEquals(400, peft.transpositionTable.get(b1a3)[1].longValue());
        assertEquals(8885, peft.transpositionTable.get(b1a3)[2].longValue());
        assertEquals(198572, peft.transpositionTable.get(b1a3)[3].longValue());
        assertEquals(4856835, peft.transpositionTable.get(b1a3)[4].longValue());
        assertNull(peft.transpositionTable.get(b1a3)[5]);
    }

    @Test // 53segs
    public void initialPosition_level7() {
        PerftWithMapIterateDeeping<Long> peft = new PerftWithMapIterateDeeping<Long>(PerftWithMapIterateDeeping::getZobristGameId);
        peft.depth = 7;
        peft.maxLevel = 7;
        long result = peft.visitChild(FENDecoder.loadGame(FENDecoder.INITIAL_FEN), 1);
        assertEquals(3195901860L, result);
    }


    @Test // 53segs
    public void initialPosition_level7_iterative() {
        PerftWithMapIterateDeeping<Long> peft = new PerftWithMapIterateDeeping<Long>(PerftWithMapIterateDeeping::getZobristGameId);

        PerftResult result = peft.start(FENDecoder.loadGame(FENDecoder.INITIAL_FEN), 5);
        assertEquals(20, result.getMovesCount());
        assertEquals(119060324, result.getTotalNodes());
    }
}


package net.chesstango.engine.timemgmt;

import org.junit.jupiter.api.Test;

/**
 * @author Mauricio Coria
 */
public class ProportionalMovesTest {

    @Test
    public void fifeMinTest() {
        ProportionalMoves proportionalMoves = new ProportionalMoves();

        int time = 1 * 60 * 1000;
        int pieces = 32;
        for (int i = 0; pieces > 2; i++) {
            int moveTime = proportionalMoves.calculateTime(time, 0, pieces);
            time -= moveTime;
            if (i % 2 == 0) {
                pieces--;
            }
            System.out.println(String.format("Move %d, timeToMove = %d; time left = %d", i + 1,  moveTime, time));
        }
    }
}

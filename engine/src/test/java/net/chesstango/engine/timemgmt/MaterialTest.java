package net.chesstango.engine.timemgmt;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author Mauricio Coria
 */
public class MaterialTest {

    @Test
    @Disabled
    public void fifeMinTest() {
        Material material = new Material();

        int time = 1 * 60 * 1000;
        int pieces = 32;
        for (int i = 0; pieces > 2; i++) {
            int moveTime = material.calculateTime(time, 0, pieces);
            time -= moveTime;
            if (i % 2 == 0) {
                pieces--;
            }
            System.out.println(String.format("Move %d, timeToMove = %d; time left = %d", i + 1,  moveTime, time));
        }
    }
}

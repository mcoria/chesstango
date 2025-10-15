package net.chesstango.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author Mauricio Coria
 */
public class FivePercentageTest {

    private final static int TIME_OUT = 1000; // Cuando resta 1 segundo lo considero como time-out

    private FivePercentage fivePercentage;

    @BeforeEach
    public void setup() {
        fivePercentage = new FivePercentage();
    }

    @Test
    @Disabled
    public void threeMinTest() {
        final int MINUTES = 3;
        int time = 1000 * 60 * MINUTES;
        int moveNumber = 1;
        while (time > TIME_OUT) {
            int moveTime = fivePercentage.calculateTimeOut(time, 0);
            time -= moveTime;
            System.out.println(String.format("Move %d, timeToMove = %d; time left = %d", moveNumber, moveTime, time));
            moveNumber++;
        }
    }

    @Test
    @Disabled
    public void fifeMinTest() {
        final int MINUTES = 5;
        int time = 1000 * 60 * MINUTES;
        int moveNumber = 1;
        while (time > TIME_OUT) {
            int moveTime = fivePercentage.calculateTimeOut(time, 0);
            time -= moveTime;
            System.out.println(String.format("Move %d, timeToMove = %d; time left = %d", moveNumber, moveTime, time));
            moveNumber++;
        }
    }


    @Test
    @Disabled
    public void tenMinTest() {
        final int MINUTES = 10;
        int time = 1000 * 60 * MINUTES;
        int moveNumber = 1;
        while (time > TIME_OUT) {
            int moveTime = fivePercentage.calculateTimeOut(time, 0);
            time -= moveTime;
            System.out.println(String.format("Move %d, timeToMove = %d; time left = %d", moveNumber, moveTime, time));
            moveNumber++;
        }
    }
}

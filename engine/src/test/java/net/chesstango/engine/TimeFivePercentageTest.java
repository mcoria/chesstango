package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.gardel.fen.FEN;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Mauricio Coria
 */
public class TimeFivePercentageTest {

    private final static int TIME_OUT = 1000; // Cuando resta 1 segundo lo considero como time-out

    private TimeFivePercentage timeFivePercentage;

    @BeforeEach
    public void setup() {
        timeFivePercentage = new TimeFivePercentage();
    }

    @Test
    @Disabled
    public void threeMinTest() {
        final int MINUTES = 3;
        int time = 1000 * 60 * MINUTES;
        int moveNumber = 1;
        while (time > TIME_OUT) {
            int moveTime = timeFivePercentage.calculateTimeOut(time, 0);
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
            int moveTime = timeFivePercentage.calculateTimeOut(time, 0);
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
            int moveTime = timeFivePercentage.calculateTimeOut(time, 0);
            time -= moveTime;
            System.out.println(String.format("Move %d, timeToMove = %d; time left = %d", moveNumber, moveTime, time));
            moveNumber++;
        }
    }

    @Test
    public void keepSearchingSingleMoveTest() {
        Game game = Game.from(FEN.from("K1k5/8/8/8/8/8/8/8 w - - 1 1"));

        assertFalse(timeFivePercentage.keepSearching(game, 100000, null));
    }
}

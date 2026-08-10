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
public class TimeMaterialTest {

    private TimeMaterial timeMaterial;

    @BeforeEach
    public void setup() {
        timeMaterial = new TimeMaterial();
    }

    @Test
    @Disabled
    public void fifeMinTest() {
        int time = 1 * 60 * 1000;
        int pieces = 32;
        for (int i = 0; pieces > 2; i++) {
            int moveTime = timeMaterial.calculateTime(time, 0, pieces);
            time -= moveTime;
            if (i % 2 == 0) {
                pieces--;
            }
            System.out.println(String.format("Move %d, timeToMove = %d; time left = %d", i + 1,  moveTime, time));
        }
    }


    @Test
    public void keepSearchingSingleMoveTest() {
        Game game = Game.from(FEN.from("K1k5/8/8/8/8/8/8/8 w - - 1 1"));

        assertFalse(timeMaterial.keepSearching(game, 100000, null));
    }
}

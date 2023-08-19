package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Mauricio Coria
 */
public class PeSTOTest {

    private PeSTO evaluator;

    @BeforeEach
    public void setUp() {
        evaluator = new PeSTO();
    }

    @Test
    public void test01(){
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);
        int result = evaluator.evaluate(game);
        //System.out.println(result);
    }

    @Test
    public void test02(){
        Game game = FENDecoder.loadGame("r1bqk2r/ppp2ppp/2n2n2/3pp3/1b2P3/2NP1N1P/PPP2PP1/R1BQKB1R w KQkq - 2 6");
        int result = evaluator.evaluate(game);
        //System.out.println(result);
    }
}

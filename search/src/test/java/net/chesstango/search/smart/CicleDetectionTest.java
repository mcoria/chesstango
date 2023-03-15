package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.evaluation.imp.GameEvaluatorByFEN;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.minmax.MinMax;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class CicleDetectionTest {


    private MinMax minMax;

    @Before
    public void setup() {
        minMax = new MinMax();
        minMax.setGameEvaluator(new GameEvaluatorByMaterial());
    }


    /**
     *
     *  -------------------------------
     * 8| k |   |   |   | b |   |   |   |
     *   -------------------------------
     * 7|   |   |   | p | P | p |   |   |
     *   -------------------------------
     * 6|   |   | p | P |   | P |   | p |
     *   -------------------------------
     * 5|   | p | P |   |   |   | p | P |
     *   -------------------------------
     * 4| p | P |   |   |   | p | P |   |
     *   -------------------------------
     * 3| P |   | p |   | p | P |   |   |
     *   -------------------------------
     * 2|   |   | P | p | P |   |   |   |
     *   -------------------------------
     * 1|   |   |   | B |   |   |   | K |
     *   -------------------------------
     *    a   b   c   d   e   f   g   h
     *
     *    Ambos reyes se encuentran atrapados por una muralla de peones trabados. Cada rey puede alcanzar solamente los
     *    siguientes squares
     *    Rey blanco: g1; f1; g2; h2; h3
     *    Rey negro: b8; c8; b7; a7; a6
     *
     *    Por mas profundidad que alcance el algoritmo de busqueda, este deberia detectar bucles.
     *
     */

    //TODO: quizas necesitariamos un mapa de posicion->evaluacion
    @Test
    @Ignore
    public void detectCicle(){
        Game game = FENDecoder.loadGame("k3b3/3pPp2/2pP1P1p/1pP3pP/pP3pP1/P1p1pP2/2PpP3/3B3K w - - 0 1");

        GameEvaluatorByFEN evaluatorMock =  new GameEvaluatorByFEN();
        evaluatorMock.setDefaultValue(0);
        //evaluatorMock.addEvaluation(); // Configurar las posiciones objetivo

        SearchMoveResult searchResult = minMax.searchBestMove(game, new SearchContext(20));

        Assert.assertNotNull(searchResult);
    }

}

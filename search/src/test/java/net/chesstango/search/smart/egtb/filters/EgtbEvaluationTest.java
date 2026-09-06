package net.chesstango.search.smart.egtb.filters;

import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.smart.egtb.EndGameTableBase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the EgtbEvaluation class.
 * <p>
 * EgtbEvaluation provides an alphaBeta method to evaluate chess positions
 * using an EndGameTableBase. Depending on the current player's turn, evaluations
 * are either positive or negative to optimize the decision-making process.
 */
@ExtendWith(MockitoExtension.class)
public class EgtbEvaluationTest {

    @InjectMocks
    private EgtbEvaluation egtbEvaluation;

    @Mock
    private EndGameTableBase endGameTableBase;


    @Test
    public void testAlphaBeta_whiteTurn_WhiteWins() {
        // Arrange
        Game game = Game.from(FEN.from("4k3/8/8/8/8/8/2Q5/4K3 w - - 0 1"));
        egtbEvaluation.setGame(game);

        when(endGameTableBase.evaluate()).thenReturn(Evaluator.WHITE_WON);

        // Act
        int result = egtbEvaluation.alphaBeta(0, -200, 200);

        // Assert
        assertEquals(Evaluator.WON, result);
        verify(endGameTableBase, times(1)).evaluate();
    }

    @Test
    public void testAlphaBeta_BlackTurn_BlackWins() {
        // Arrange
        Game game = Game.from(FEN.from("4k3/2q5/8/8/8/8/8/4K3 b - - 0 1"));
        egtbEvaluation.setGame(game);

        when(endGameTableBase.evaluate()).thenReturn(Evaluator.BLACK_WON);

        // Act
        int result = egtbEvaluation.alphaBeta(0, -200, 200);

        // Assert
        assertEquals(Evaluator.WON, result);
        verify(endGameTableBase, times(1)).evaluate();
    }

    @Test
    public void testAlphaBeta_BlackTurn_WhiteWins() {
        // Arrange
        Game game = Game.from(FEN.from("4k3/8/8/8/8/8/2Q5/4K3 b - - 0 1"));
        egtbEvaluation.setGame(game);

        when(endGameTableBase.evaluate()).thenReturn(Evaluator.WHITE_WON);

        // Act
        int result = egtbEvaluation.alphaBeta(0, -200, 200);

        // Assert
        assertEquals(Evaluator.LOST, result);
        verify(endGameTableBase, times(1)).evaluate();
    }

    @Test
    public void testAlphaBeta_WhiteTurn_BlackWins() {
        // Arrange
        Game game = Game.from(FEN.from("4k3/2q5/8/8/8/8/8/4K3 w - - 0 1"));
        egtbEvaluation.setGame(game);

        when(endGameTableBase.evaluate()).thenReturn(Evaluator.BLACK_WON);

        // Act
        int result = egtbEvaluation.alphaBeta(0, -200, 200);

        // Assert
        assertEquals(Evaluator.LOST, result);
        verify(endGameTableBase, times(1)).evaluate();
    }
}



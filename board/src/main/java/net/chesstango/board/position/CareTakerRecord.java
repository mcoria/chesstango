package net.chesstango.board.position;

import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 */
public record CareTakerRecord(GameStateReader gameState, Move playedMove) {
}

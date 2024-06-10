package net.chesstango.board.moves.generators.pseudo;

import net.chesstango.board.moves.containers.MovePair;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveGeneratorEnPassant {

	/*
	 * Este tipo de movimientos no debe entrar en cache
	 * Es necesario validar un movimiento de EnPassant dado que hay posibilidad de jaque
	 * por mas que el peon que cubra al rey no sea pinned position 
	 */
	MovePair generateEnPassantPseudoMoves();

}
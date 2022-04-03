package chess.board.legalmovesgenerators;

import java.util.Collection;

import chess.board.moves.Move;

// Doble  Jaque 										-> Mover el King. 								El castling no está permitido.
// Simple Jaque (Rook; Bishop; Queen; a mas de un paso) -> Comer jaqueador, tapar jaqueador, mover king. El castling no está permitido.
// Simple Jaque (Knight; Pawn; a UN SOLO paso) 			-> Comer jaqueador, mover king. 					El castling no está permitido.
// Sin Jaque    										-> El castling está permidito.
// Movemos la validacion de castlings aqui?

/**
 * @author Mauricio Coria
 *
 */
public interface LegalMoveGenerator {
	//Collection<Move> getLegalMoves(AnalyzerResult); TODO: deberia haber un facade que seleccione el algoritmo adecuado
	Collection<Move> getLegalMoves();
}

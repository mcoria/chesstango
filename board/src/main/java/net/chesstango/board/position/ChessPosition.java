package net.chesstango.board.position;

import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public interface ChessPosition extends ChessPositionReader, ChessPositionWriter {
	void init();

	void acceptForExecute(Move move);

	void acceptForUndo(Move move);
}

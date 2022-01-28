/**
 * 
 */
package chess;

import chess.builder.ChessPositionBuilder;

/**
 * @author Mauricio Coria
 *
 */
public interface ChessPositionReader {

	Color getTurnoActual();

	public boolean isCastlingWhiteQueenAllowed();
	public boolean isCastlingWhiteKingAllowed();

	public boolean isCastlingBlackQueenAllowed();
	public boolean isCastlingBlackKingAllowed();

	Square getPawnPasanteSquare();
	
	void constructBoardRepresentation(ChessPositionBuilder<?> builder);	
}

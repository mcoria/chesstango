/**
 * 
 */
package chess.moves;

import chess.Piece;
import chess.PiecePositioned;
import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveFactory {

	Move createSimpleKingMoveWhite(PiecePositioned origen, PiecePositioned destino);

	Move createCaptureKingMoveWhite(PiecePositioned origen, PiecePositioned destino);

	Move createSimpleKingMoveBlack(PiecePositioned origen, PiecePositioned destino);

	Move createCaptureKingMoveBlack(PiecePositioned origen, PiecePositioned destino);

	Move createSimpleRookMove(PiecePositioned origen, PiecePositioned destino);

	Move createCaptureRookMove(PiecePositioned origen, PiecePositioned destino);

	Move createSimpleMove(PiecePositioned origen, PiecePositioned destino);

	Move createCaptureMove(PiecePositioned origen, PiecePositioned destino);

	Move createSaltoDoblePawnMove(PiecePositioned origen, PiecePositioned destino, Square saltoSimpleCasillero);

	Move createCapturePawnPasante(PiecePositioned origen, PiecePositioned destino, PiecePositioned capturaPawnPasante);

	Move createSimplePawnPromocion(PiecePositioned origen, PiecePositioned destino, Piece piece);

	Move createCapturePawnPromocion(PiecePositioned origen, PiecePositioned destino, Piece piece);

}
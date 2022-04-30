/**
 * 
 */
package chess.board.analyzer;

import chess.board.Color;
import chess.board.Piece;
import chess.board.Square;
import chess.board.iterators.Cardinal;
import chess.board.iterators.square.CardinalSquareIterator;
import chess.board.position.ChessPositionReader;

/**
 * @author Mauricio Coria
 *
 */
// TODO: esta clase deberia fusionarse con Capturer
public class Pinned {	
	private final static Cardinal[] cardinalesBishop = new Cardinal[] {Cardinal.NorteEste, Cardinal.SurEste, Cardinal.SurOeste, Cardinal.NorteOeste};
	private final static Cardinal[] cardinalesRook = new Cardinal[] {Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur};

	private final  PinnedImp white = new PinnedImp(Color.WHITE);
	private final  PinnedImp black = new PinnedImp(Color.BLACK);
	
	private final ChessPositionReader positionReader;
	

	public Pinned(ChessPositionReader positionReader) {
		this.positionReader = positionReader;
	}

	public long getPinnedSquare(Color color) {
		return Color.WHITE.equals(color) ? white.getPinnedSquare(positionReader.getKingSquare(color)) : black.getPinnedSquare(positionReader.getKingSquare(color));
	}


	private class PinnedImp {
		
		private final Color color;
		
		private final Piece reina;
		private final Piece rook;
		private final Piece alfil;	
		
		public PinnedImp(Color color) {
			this.color = color;
			this.reina = Piece.getQueen(color.oppositeColor());
			this.rook = Piece.getRook(color.oppositeColor());
			this.alfil = Piece.getBishop(color.oppositeColor());
		}

		public long getPinnedSquare(Square kingSquare) {
			long pinnedCollection = 0;
			
			pinnedCollection |= getPinnedCardinales(kingSquare, alfil, reina, cardinalesBishop);
			pinnedCollection |= getPinnedCardinales(kingSquare, rook, reina, cardinalesRook);

			return pinnedCollection;
		}
		
		protected long getPinnedCardinales(Square kingSquare, Piece rookOBishop, Piece reina,
				Cardinal[] direcciones) {
			long pinnedCollection = 0;
			for (Cardinal cardinal : direcciones) {
				Square pinned = getPinned(kingSquare, rookOBishop, reina, cardinal);
				if (pinned != null) {
					pinnedCollection |= pinned.getPosicion();
				}
			}
			return pinnedCollection;
		}
		
		
		protected Square getPinned(Square kingSquare, Piece rookOBishop, Piece reina, Cardinal cardinal) {
			Square pinned = null;
			CardinalSquareIterator iterator = new CardinalSquareIterator(kingSquare, cardinal);
			while (iterator.hasNext()) {
				Square destino = iterator.next();
				Color colorDestino = positionReader.getColor(destino);
				if (colorDestino == null) {
					continue;
				}
				if (pinned == null) {
					if (color.equals(colorDestino)) {
						pinned = destino;
					} else { // if (color.oppositeColor().equals(colorDestino))
						return null;
					}
				} else {
					if (color.equals(colorDestino)) {
						return null;
					} else { //// if (color.oppositeColor().equals(colorDestino))
						Piece piece = positionReader.getPieza(destino);
						if (rookOBishop.equals(piece) || reina.equals(piece)) {
							return pinned;
						} else {
							return null;
						}
					}
				}

			}
			return null;
		}		
		
	}
}
